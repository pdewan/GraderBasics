package grader.basics.execution;

import java.util.concurrent.Future;

public class ResultingOutErr {
	public final String out;
	public final String err;
	protected Runner processRunner;
	protected RunningProject runningProject;
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
	


}
