package byteman.tools;

import grader.byteman.injector.target.custom.EnterExitInjectionSite;
import premain.Premain;

public class InjectedCode implements EnterExitInjectionSite{


	@Override
	public void atEnter(String clazz, String method, Object[] params) {
		StringBuilder sb = new StringBuilder();
		for(int i=1;i<params.length;i++) {
			if(params[i].getClass().isArray()) {
				sb.append(arrayPrinter(params[i]));
			}else {
				sb.append(params[i].toString());
			}
			sb.append(' ');
		}
		//Stack Trace to get calling method
		//Look at our traces
		//Confirm to our traces
		//
		Thread current = Thread.currentThread();
		StackTraceElement [] stack = current.getStackTrace();
		String caller = "?";
		for(int i=0;i<stack.length;i++) {
			if(stack[i].getClassName().equals(clazz) && i+1<stack.length) {
				caller = stack[i+1].toString();
				break;
			}
		}
		
		String clazzName = determineClass(clazz);
		
		System.out.println("GR*** Thread: "+current.getName()+" has called "+clazzName+"."+method+" from "+caller+" with params: "+sb.toString());
	}

	@Override
	public void atExit(String clazz, String method, Object returned) {
		if(returned.getClass().isArray()) {
			atExit(clazz,method,arrayPrinter(returned));
		}else {
			String clazzName = determineClass(clazz);
			System.out.println("GR*** "+Thread.currentThread().getName()+" has exited "+method+" in class "+clazzName+" and returned: "+returned);
			
		}
	}

	@Override
	public void atInitialization(String clazz, Object [] params) {
		StringBuilder sb = new StringBuilder();
		for(int i=1;i<params.length;i++) {
			sb.append(params[i].toString()+" ");
		}
		String clazzName = determineClass(clazz);
		System.out.println("GR*** "+Thread.currentThread().getName()+" has made an instance of class "+clazzName+" with params: "+sb.toString());
	
	}
	
	@Override
	public void atExit(String clazz, String method, Object[] params) {
		StringBuilder sb = new StringBuilder("arr[");
		for(Object o:params) {
			sb.append(o.toString()+",");
		}
		if(params.length>0)
			sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		String clazzName = determineClass(clazz);
		System.out.println("GR*** "+Thread.currentThread().getName()+" has exited "+method+" in class "+clazzName+"and returned: "+sb.toString());
		
	}
	
	private String determineClass(String clazz) {
		String clazzName = Premain.getDisplayNameMapping().get(clazz);
		if(clazzName==null) {
			clazzName = clazz;
		}
		return clazzName;
	}

}
