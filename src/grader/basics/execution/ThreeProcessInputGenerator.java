package grader.basics.execution;

import gradingTools.shared.testcases.utils.ABufferingTestInputGenerator;

import java.util.Map;
import java.util.regex.Pattern;

import com.sun.org.apache.xalan.internal.xsltc.dom.FilteredStepIterator;

import util.pipe.AnAbstractInputGenerator;
import util.trace.Tracer;

public class ThreeProcessInputGenerator extends ABufferingTestInputGenerator {
	String numThreads;
	String[] process1InputLines;
	String[] process2InputLines;
	String initiatingProcessName;
	String process1Name;
	String process2Name;
	String lastOutputInitiatingProcess;
	String lastOutputProcess1;
	public ThreeProcessInputGenerator () {

	}
	public ThreeProcessInputGenerator (String anInitiatingProcess, 
			String aLastOutputLineFromInitiatingProcess, 
			String aProcess1,  String[] aProcess1InputLines) {
//		numThreads = Integer.toString(aNumThreads);
		
		initiatingProcessName = anInitiatingProcess;
		lastOutputInitiatingProcess = aLastOutputLineFromInitiatingProcess != null?
				aLastOutputLineFromInitiatingProcess + "\n": null;
		process1Name = aProcess1;
		process1InputLines = aProcess1InputLines;
//		
	}
	public ThreeProcessInputGenerator (String anInitiatingProcess, 
			String aLastOutputLineFromInitiatingProcess, 
			String aProcess1,  String[] aProcess1InputLines, 
			String aLastOutputProcess1,
            String aProcess2, String[] aProcess2InputLines) {
		this(anInitiatingProcess, aLastOutputLineFromInitiatingProcess, aProcess1, aProcess1InputLines);
		
//		initiatingProcessName = anInitiatingProcess;
//		lastOutputInitiatingProcess = aLastOutputLineFromInitiatingProcess;
//		process1Name = aProcess1;
//		process1InputLines = aProcess1InputLines;
		lastOutputProcess1 = aLastOutputProcess1 != null? aLastOutputProcess1 + "\n":null;
		process2Name = aProcess2;
		process2InputLines = aProcess2InputLines;
	}
	
	public static String[] append(String[] anOriginalArray, String aLastItem) {
		String[] result = new String[ anOriginalArray.length + 1];
		for (int i = 0; i < anOriginalArray.length; i++) {
			result[i] = anOriginalArray[i];
		}
		result[anOriginalArray.length] = aLastItem;
		return result;
	}
	
//	@Override
//	public void newOutputLine(String aProcessName, String anOutputLine) {
//		super.newOutputLine(aProcessName, anOutputLine);
//		if (aProcessName.equals(Assignment0Suite.MAP_REDUCE_CLIENT_2)) { // can give input to server
////			notifyNewInputLine(Assignment0Suite.MAP_REDUCE_SERVER, numThreads);
////			for (int i = 0; i < inputLines.length - 1; i++) {
////				String aLine = inputLines[i];
//
//			for (String aLine:inputLines) {
//				notifyNewInputLine(Assignment0Suite.MAP_REDUCE_SERVER, aLine);
//
////			notifyNewInputLine(GetConfiguration.MAP_REDUCE_SERVER, "aaa jjj sss zzzz aaa aaa jjj zzz aaa jjj");
////			notifyNewInputLine(GetConfiguration.MAP_REDUCE_SERVER, "bbb iii ttt yyy bbb bbb iii yyy bbb iii");
//			}
////			notifyNewInputLine(Assignment0Suite.MAP_REDUCE_SERVER, "quit");
//		}
////		if (anOutputLine.contains("Processing line:" )) {
////			notifyNewInputLine(Assignment0Suite.MAP_REDUCE_SERVER, inputLines[inputLines.length -1]);
////		}
//		
//	}
	
	
	@Override
	public void newOutputLine(String aProcessName, String anOutputLine) {
		super.newOutputLine(aProcessName, anOutputLine);
		if (process1InputLines == null || process1Name == null ) {
			return;
		}
		
		Tracer.info(this, aProcessName + ":" + anOutputLine);
		if (aProcessName.equals(initiatingProcessName) 
				&& anOutputLine.equals(lastOutputInitiatingProcess)) {
			Tracer.info(this, initiatingProcessName + " lastoutput received. Providing input for " + process1Name);
			for (String anInput:process1InputLines) {
				notifyNewInputLine(process1Name, anInput);

			}
		}
		if (process2InputLines == null || process2Name == null ) {
			return;
		}
		if (aProcessName.equals(process1Name) && anOutputLine.equals(lastOutputProcess1)) {
			Tracer.info(this, process1Name + " lastoutput received. Providing input for " + process2Name);

			for (String anInput:process2InputLines) {
				notifyNewInputLine(process2Name, anInput);

			}
		}
		
		
	}
	
	
}
