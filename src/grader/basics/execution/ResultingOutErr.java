package grader.basics.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import gradingTools.shared.testcases.utils.ALinesMatcher;
import gradingTools.shared.testcases.utils.LinesMatcher;
import util.trace.Tracer;

public class ResultingOutErr {
	public final String out;
	public final String err;
	protected Runner processRunner;
	protected RunningProject runningProject;
	protected List<String>  outputLinesList;
	protected String[]  outputLines;
	protected LinesMatcher linesMatcher;

	public ResultingOutErr(String anOut, String anErr) {
		out = anOut;
		err = anErr;
		runningProject = null;
		processRunner = null;
	}
	public ResultingOutErr(RunningProject aRunningProject, Runner aProcessRunner, String anOut, String anErr) {
		this(anOut, anErr);
		runningProject = aRunningProject;
		processRunner = aProcessRunner;
//		out = anOut;
//		err = anErr;
	}
	public ResultingOutErr(Runner aProcessRunner, String anOut, String anErr) {
		this (anOut, anErr);
		runningProject = null;
		processRunner = aProcessRunner;
//		out = anOut;
//		err = anErr;
	}
	public ResultingOutErr(Future aFuture, String anOut, String anErr) {
		this (anOut, anErr);
		runningProject = null;
		future = aFuture;
//		out = anOut;
//		err = anErr;
	}
	public Runner getProcessRunner() {
		return processRunner;
	}
	public void setProcessRunner(Runner processRunner) {
		this.processRunner = processRunner;
	}
	public RunningProject getRunningProject() {
		return runningProject;
	}
	public void setRunningProject(RunningProject runningProject) {
		this.runningProject = runningProject;
	}
	public Future getFuture() {
		return future;
	}
	public void setFuture(Future future) {
		this.future = future;
	}
	public String getOut() {
		return out;
	}
	public String getErr() {
		return err;
	}
	protected Future future;
	
//	public static boolean isTrace(String aString) {
//		return aString.length() > 4 && 
//				aString.charAt(1) == '*' &&
//				aString.charAt(2) == '*' &&
//				aString.charAt(3) == '*';
//	}
	
	public List<String> getOutputLinesList() {
		if (outputLinesList == null) {
			outputLinesList = new ArrayList();

			if (out != null && !out.isEmpty()) {
				String[] aRawLines = out.split("\n");
				for (String aRawLine:aRawLines) {
					if (Tracer.isTrace(aRawLine)) continue;
 					outputLinesList.add(aRawLine);
				}
			}
		}
		return outputLinesList;
	}
	
	protected static String[] emptyArray = {};
	
	public String[] getOutputLines() {
		if (outputLines == null) {
			List<String> anOutputLinesList = getOutputLinesList();
			outputLines = anOutputLinesList.toArray(emptyArray);			
		}
		return outputLines;
	}
	
	public LinesMatcher getLinesMatcher() {
		if (runningProject != null) {
			return runningProject.getLinesMatcher();
		}
		if (linesMatcher == null) {
			linesMatcher = new ALinesMatcher(getOutputLines());
		}
		return linesMatcher;
	}

}
