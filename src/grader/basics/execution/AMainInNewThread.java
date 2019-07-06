package grader.basics.execution;

import grader.basics.config.BasicProjectExecution;

public class AMainInNewThread  implements Runnable{
	String mainName;
	String[] args;
	String input;
	
	public AMainInNewThread(String aMainName, String[] anArgs, String anInput){
		mainName = aMainName;
		args = anArgs;
		input = anInput;
	}
	@Override
	public void run() {
		Boolean oldValue = BasicProjectExecution.isUseMethodAndConstructorTimeOut();
		BasicProjectExecution.setUseMethodAndConstructorTimeOut(false);

		try {
			BasicProjectExecution.callMain(mainName,
					args, input);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BasicProjectExecution.setUseMethodAndConstructorTimeOut(oldValue);

		
	}

}
