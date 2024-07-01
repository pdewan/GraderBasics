package grader.basics.output.observer;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ObservablePrintStreamFactory {
	static ObservablePrintStream observablePrintStream;

	public static ObservablePrintStream getObservablePrintStream(PrintStream aDelegate) {
			try {
				observablePrintStream = new AnObservablePrintStream(aDelegate);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return observablePrintStream;
		
	}
	public static ObservablePrintStream getObservablePrintStream() {
		if (observablePrintStream == null) {
			try {
				observablePrintStream = new AnObservablePrintStream(System.out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}		
		return observablePrintStream;	
	}
	
//	public static ObservablePrintStream redirectToObservablePrintStream() {
//		ObservablePrintStream retVal = getObservablePrintStream();
//		System.setOut((PrintStream) retVal);
//		return retVal;
//
//	}
	
	

	public static void setObservablePrintStream(ObservablePrintStream newVal) {
		ObservablePrintStreamFactory.observablePrintStream = newVal;
	}
	

}
