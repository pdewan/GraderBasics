package grader.basics.execution;

import java.io.InputStream;
import java.util.Scanner;

public interface RunnerErrorOrOutStreamProcessor extends Runnable, RunnerStreamProcessor{
	 void processLine(String s);


	public Scanner getScanner();


	public InputStream getErrorOrOut();


//	

}
