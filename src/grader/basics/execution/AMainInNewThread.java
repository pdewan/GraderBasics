package grader.basics.execution;


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

		BasicProjectExecution.callMain(mainName,
				args, input);
		BasicProjectExecution.setUseMethodAndConstructorTimeOut(oldValue);

		
	}

}
