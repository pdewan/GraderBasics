package byteman.tools;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class ClassInjectionData {

	private final String className, injectedCode, displayName;
	private final Set<MethodInjectionData> targetedMethods;
	private boolean includeConstructor;
	private Class<? extends EnterExitInjectionSite> helper;

	public ClassInjectionData(String toParse) {
		String[] split = toParse.split(",");
		targetedMethods = new HashSet<>();
		className = split[0];
		includeConstructor = split[1].equals("T");
		for (String method : split[2].split(" ")) {
			addMethod(method);
		}
		injectedCode = split[3];
		displayName = split[4];
		try {
			helper = (Class<? extends byteman.tools.EnterExitInjectionSite>) Class.forName(injectedCode);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Class<? extends EnterExitInjectionSite> getHelper() {
		return helper;
	}

	public String getInjectedClass() {
		return injectedCode;
	}

	public ClassInjectionData(String className, String injectedClass, Method... methods) {
		this(className, injectedClass, className, false, methods);
	}

	public ClassInjectionData(String className, String injectedClass, String displayName, Method... methods) {
		this(className, injectedClass, displayName, false, methods);
	}

	public ClassInjectionData(String className, String injectedClass, boolean incCon, Method... methods) {
		this(className, injectedClass, className, incCon, methods);
	}

	public ClassInjectionData(String className, String injectedClass, String displayName, boolean incCon,
			Method... methods) {
		this.className = className;
		this.injectedCode = injectedClass;
		this.includeConstructor = incCon;
		this.displayName = displayName;
		targetedMethods = new HashSet<>();
		for (Method method : methods) {
			addMethod(method);
		}
	}

	public boolean isIncludeConstructor() {
		return includeConstructor;
	}

	public void setIncludeConstructor(boolean includeConstructor) {
		this.includeConstructor = includeConstructor;
	}

	public boolean addMethod(String method) {
		try {
			return targetedMethods.add(new MethodInjectionData(method));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addMethod(Set<MethodInjectionData> method) {
		return targetedMethods.addAll(method);
	}

	public boolean addMethod(String method, boolean retVoid) {
		return targetedMethods.add(new MethodInjectionData(method, retVoid));
	}

	public boolean addMethod(Method method) {
		return targetedMethods.add(new MethodInjectionData(method));
	}

	public Set<MethodInjectionData> getTargetedMethods() {
		return targetedMethods;
	}

	public String getClassName() {
		return className;
	}

	public String getDisplayName() {
		return displayName;
	}
	/*
	 * RULE trace factorial init CLASS byteman.examples.RecursiveFactorial METHOD
	 * <init> AT ENTRY HELPER byteman.tools.InjectedCode IF true DO atEnter($CLASS,
	 * $METHOD, $*) ENDRULE
	 * 
	 * RULE
	 * Rule-FileSourceInjectionTargeter-0~~grader.byteman.injector.target.custom.
	 * ClassInjectionData@525b461a:factorial-exit CLASS
	 * byteman.examples.RecursiveFactorial METHOD factorial AT EXIT HELPER
	 * byteman.tools.InjectedCode IF true DO atExit($CLASS, $METHOD, $!) ENDRULE
	 */

//	public String toRuleText() {
//		StringBuilder sb = new StringBuilder();
//		int i = 0;
//		String aRuleNamePart = "Rule-" + getClassName() + "-" + "~~" + this;
//		if (isIncludeConstructor()) {
//			sb.append("RULE" + aRuleNamePart + ":constructor-enter");
//			sb.append("CLASS " + getClassName() + "\n");
//			sb.append("METHOD " + "<init>" + "\n");
//			sb.append("AT ENTRY" + "\n");
//			sb.append("HELPER " + getHelper());
//			sb.append("IF true");
//			sb.append("DO atEnter($CLASS, $METHOD, $*)");
//			sb.append("ENDRULE\n");
//
//			sb.append("RULE" + aRuleNamePart + "~~" + this + ":constructor-exit");
//			sb.append("CLASS " + getClassName() + "\n");
//			sb.append("METHOD " + "<init>" + "\n");
//			sb.append("AT EXIT" + "\n");
//			sb.append("HELPER " + getHelper() + "\n");
//			sb.append("IF true");
//			sb.append("DO atExit($CLASS, $METHOD, $*)\n");
//			sb.append("ENDRULE\n");
//
//			for (MethodInjectionData method : getTargetedMethods()) {
//				String methodName = method.getMethodName();
//				boolean returnsVoid = method.returnsVoid();
//				sb.append("RULE" + aRuleNamePart + "~~" + i + this + methodName + "-enter");
//				sb.append("CLASS " + getClassName() + "\n");
//				sb.append("METHOD " + methodName + "\n");
//				sb.append("AT ENTRY" + "\n");
//				sb.append("HELPER " + getHelper() + "\n");
//				sb.append("IF true\n");
//				sb.append("DO atEnter($CLASS, $METHOD, $*)\n");
//				sb.append("ENDRULE\n");
//
//				sb.append("RULE" + aRuleNamePart + "~~" + this + methodName + "-enter");
//				sb.append("CLASS " + getClassName() + "\n");
//				sb.append("METHOD " + methodName + "\n");
//				sb.append("AT EXIIT" + "\n");
//				sb.append("HELPER " + getHelper() + "\n");
//				sb.append("IF true");
//				sb.append("DO atExit($CLASS, $METHOD, $*)\n");
//				sb.append("ENDRULE\n");
//				i++;
//			}
//		}
//		return sb.toString();
//	}

	public String toRuleText() {
		StringBuilder sb = new StringBuilder();
		addRuleText(sb, null, true, true);
		addRuleText(sb, null, false, true);

		int i = 0;

		for (MethodInjectionData method : getTargetedMethods()) {
			String aMethodName = method.getMethodName();
			boolean returnsVoid = method.returnsVoid();

			addRuleText(sb, aMethodName, true, returnsVoid);
			addRuleText(sb, aMethodName, false, returnsVoid);

			i++;
		}

		return sb.toString();
	}

	public void addRuleText(StringBuilder sb, String aMethodName, boolean isEnter, boolean aVoid) {
		boolean isConstructor = aMethodName == null;
		if (isConstructor && !isIncludeConstructor()) {
			return;
		}
		String aRuleNamePrefix = "RULE Rule-" + getClassName() + "-" + "~~" + this;

		String aMethodNameMiddle = isConstructor ? ":constructor-" : aMethodName + "-";
		String aMethodNameSuffix = isEnter ? "enter" : "exit";
		String aFullRuleName = aRuleNamePrefix + aMethodNameMiddle + aMethodNameSuffix;
		String aNormalizedMethodName = isConstructor ? "<init>" : aMethodName;
		String anAtSuccesor = isEnter ? "ENTRY" : "EXIT";
		String aDoSuccesor = isEnter ? "atEnter" : "atExit";
		
//		String aMethodSuccessor = aVoid ? "" : ", $!";
		String aMethodSuccessor = 
				isEnter? ", $*":
					aVoid ? "":
						", $!";
				
		
		
//		String aHelperFullClassName = getHelper().getName();

		sb.append(aFullRuleName + "\n");
		sb.append("CLASS " + getClassName() + "\n");
		sb.append("METHOD " + aNormalizedMethodName + "\n");
		sb.append("AT " + anAtSuccesor + "\n");
		sb.append("HELPER " + getHelper().getName() + "\n");
		sb.append("IF TRUE\n");
		sb.append("DO " + aDoSuccesor + "($CLASS, $METHOD" + aMethodSuccessor + ")\n");
		sb.append("ENDRULE\n");
		sb.append("\n");

	}

	public String getPrintValue() {
		StringBuilder sb = new StringBuilder();
		sb.append(className);
		sb.append(',');
		sb.append(includeConstructor ? 'T' : 'F');
		sb.append(',');
		for (MethodInjectionData method : targetedMethods) {
			sb.append(method.getPrintValue());
			sb.append(' ');
		}
		if (targetedMethods.size() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(',');
		sb.append(injectedCode);
		sb.append(',');
		sb.append(displayName);
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ClassInjectionData))
			return false;
		return ((ClassInjectionData) o).className.equals(className);
	}

}
