package grader.basics.execution;

import java.io.Closeable;
import java.util.List;
import java.util.Map;

import grader.basics.project.Project;
import grader.basics.util.TimedProcess;
import gradingTools.shared.testcases.utils.LinesMatcher;


public interface RunningProject {

	public abstract void start() throws InterruptedException;

	public abstract void end();

	public abstract void appendCumulativeOutput(String newVal);

	public abstract Map<String, StringBuffer> getProcessOutput();

	public abstract void appendProcessOutput(String aProcess, String newVal);

	public abstract void appendErrorOutput(String aProcess, String newVal);

	public abstract void appendErrorAndOutput(String aProcess, String newVal);

	public abstract void setOutput(String output);

	public abstract String getOutput();

	public abstract String getOutputAndErrors();

	public abstract void appendErrorOutput(String anErrorOutput);

	public abstract void setErrorOutput(String errorOutput);

	public abstract String getErrorOutput();

	public abstract void error();

	public static final String FEATURE_HEADER_PREFIX = "*****************************(";
	public static final String FEATURE_HEADER_SUFFIX = ")*****************************";

	public abstract void appendCumulativeOutput();

	//
	//   public static void appendToTranscriptFile(SakaiProject aProject, String aText) {
	//        try {
	//            String anOutputFileName = aProject.getOutputFileName();
	//            FileWriter fileWriter = new FileWriter(anOutputFileName, true);
	//            fileWriter.append(aText);
	//            OverallTranscriptSaved.newCase(null, null, aProject, anOutputFileName, aText, BasicRunningProject.class);
	////			if (project.getCurrentGradingFeature() != null)
	////			FeatureTranscriptSaved.newCase(null, null, project,  project.getCurrentGradingFeature()., outputFileName, transcript, this);;
	//            fileWriter.close();
	//        } catch (IOException e) {
	//            // TODO Auto-generated catch block
	//            e.printStackTrace();
	//        }
	//
	//    }
	//   
	//    public static void appendToTranscriptFile(SakaiProject aProject, String aFeatureName, String aText) {
	//    	appendToTranscriptFile(aProject, featureHeader(aFeatureName) + "\n" + aText);
	//    }
	//    public static void appendToTranscriptFile(Project aProject, String aFeatureName, String aText) {
	//    	appendToTranscriptFile(((ProjectWrapper) aProject).getProject(), featureHeader(aFeatureName) + "\n" + aText);
	//    }
	//
	//    public void appendOutputAndErrorsToTranscriptFile(SakaiProject aProject) {
	//        appendToTranscriptFile(aProject, getOutputAndErrors());
	//    }

	public abstract String await() throws NotRunnableException;

	public abstract void newInputLine(String aProcessName, String anInput);

	public abstract void terminateTeam();

	public abstract void inputTerminated(String aProcessName);

	public abstract void terminateProcess(String aProcess);

	public abstract RunnerInputStreamProcessor getProcessIn(String aProcessName);

	public abstract void setProcessIn(String aProcess,
			RunnerInputStreamProcessor processIn);

	public abstract RunnerErrorOrOutStreamProcessor getProcessOut(
			String aProcessName);

	public abstract void setProcessOut(String aProcess,
			RunnerErrorOrOutStreamProcessor newVal);

	public abstract RunnerErrorOrOutStreamProcessor getProcessErr(
			String aProcessName);

	public abstract void setProcessErr(String aProcess,
			RunnerErrorOrOutStreamProcessor newVal);

	public abstract TimedProcess getProcess(String aProcessName);

	public abstract void setProcess(String aProcessName,
			TimedProcess aTimedProcess);

	//    public SakaiProject getProject() {
	//        return project;
	//    }
	public abstract boolean isDestroyed();

	public abstract void setDestroyed(boolean destroyed);

	public abstract void addDependentThread(Thread aThread);

	public abstract void addDependentCloseable(Closeable aCloseable);

	public abstract void destroy();

	public abstract TimedProcess getCurrentTimedProcess();

	public abstract void setCurrentTimeProcess(TimedProcess currentProcess);

	Map<String, List<String>> getProcessOutputLines();

	Map<String, List<String>> getProcessErrorLines();

	Map<String, StringBuffer> getProcessError();

	Map<String, LinesMatcher> getProcessLinesMatcher();
	LinesMatcher getLinesMatcher();

	Project getBasicProject();

	

}