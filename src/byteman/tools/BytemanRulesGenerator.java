package byteman.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.misc.Common;

public class BytemanRulesGenerator {
	private static Map<String, String> definedNames = new HashMap<>();

	public static Map<String, String> getDisplayNameMapping() {
		return definedNames;
	}

	public String getRuleName() {
		return "Rule-" + BytemanRulesGenerator.class.getSimpleName() + "-";
	}

	public static void addClassBytemanRule(StringBuilder aStringBuilder, ClassInjectionData injectionTarget) {
		aStringBuilder.append(injectionTarget.toRuleText());
	}

	public static void addClassBytemanRule(ClassInjectionData injectionTarget) {
		stringBuilder.append(injectionTarget.toRuleText());
	}
	static boolean rulesFileWritten = false;
	public static void maybeWriteBytemanRules() {
		maybeWriteBytemanRules("./injectionTarget.txt", "./byteman_rules.btm");
	}

	public static void maybeWriteBytemanRules(String aFromFileName, String aToFileName) {
		if (rulesFileWritten) {
			return;
		}
		String aRules = composeBytemanRules(aFromFileName);
		try {
			Common.writeText(new File(aToFileName), aRules);
			rulesFileWritten = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static StringBuilder stringBuilder = new StringBuilder();

	public static void initializeBytemanRules() {
		stringBuilder.setLength(0);
	}

	public static void addBytemanRule(ClassInjectionData aData) {
		addClassBytemanRule(stringBuilder, aData);

	}

	public static void finalizeBytemanRules() {

	}

	public static String getBytemanRules() {
		return stringBuilder.toString();
	}

	public static String composeBytemanRules(String aFromFileName) {
		InjectionTargetFileReader.readFile(aFromFileName, true);
		return getBytemanRules();

	}
//	public static String createBytemanRules(String aFromFileName) {
//			StringBuilder aStringBuilder = new StringBuilder();
//		      try {
//					Scanner scan = new Scanner(new File(aFromFileName));
//					while(scan.hasNext()){
//						String aNextLine = scan.nextLine();
////						System.out.println("to Parse:" + aNextLine);
//						ClassInjectionData data = new ClassInjectionData(aNextLine);
//						addClassBytemanRule(aStringBuilder, data);
//
////						ClassInjectionData data = new ClassInjectionData(scan.nextLine());
//						definedNames.put(data.getClassName(), data.getDisplayName());
////						
////						try {
//////							injector.registerTarget(new FileSourceInjectionTargeter(data));
//////							System.out.println("class name:" + data.getClassName());
//////							Class aLoadedClass = Class.forName(data.getClassName());
//////							System.out.println("loaded class loader:" + aLoadedClass.getClassLoader());
////						} catch (ClassNotFoundException e) {
////							e.printStackTrace();
////						}
//						
//					}
//					scan.close();
//					return aStringBuilder.toString();
////					injector.inject();
//		      }catch(IllegalArgumentException | IOException e) {
//					e.printStackTrace();
//				}
//		      return null;
//			}

	/*
	 * final public List<String> getRules(){ List<String> rules = new
	 * ArrayList<String>(); int i = 0; if(injectionTarget.isIncludeConstructor()) {
	 * rules.add(
	 * RuleConstructor.createRule(this.getRuleName()+"~~"+injectionTarget+
	 * ":constructor") .onClass(injectionTarget.getClassName()) .inMethod("<init>")
	 * .helper(this.getHelper()) .atEntry() .ifTrue()
	 * .doAction("atEnter($CLASS, $METHOD, $*)") .build() ); }
	 * 
	 * for(MethodInjectionData method:injectionTarget.getTargetedMethods()) { String
	 * methodName = method.getMethodName(); boolean returnsVoid =
	 * method.returnsVoid(); rules.add(
	 * RuleConstructor.createRule(this.getRuleName()+i+"~~"+injectionTarget+":"+
	 * methodName+"-enter") .onClass(injectionTarget.getClassName())
	 * .inMethod(methodName) .helper(this.getHelper()) .atEntry() .ifTrue()
	 * .doAction("atEnter($CLASS, $METHOD, $*)") .build() ); rules.add(
	 * RuleConstructor.createRule(this.getRuleName()+i+"~~"+injectionTarget+":"+
	 * methodName+"-exit") .onClass(injectionTarget.getClassName())
	 * .inMethod(methodName) .helper(this.getHelper()) .atExit() .ifTrue()
	 * .doAction("atExit($CLASS, $METHOD" + (returnsVoid ? "" : ", $!")+")")
	 * .build() ); i++; } return rules; }
	 */
}
