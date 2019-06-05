package grader.basics.project;

import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.util.Option;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

//import scala.Option;
import util.pipe.InputGenerator;
import util.trace.TraceableLog;

/**
 * Based on {@link grader.project.flexible.FlexibleProject}
 */
public interface Project {
	public static final String SOURCE = "src";
	public static final String BINARY_0 = "classes";

	public static final String BINARY = "bin";
    public static final String BINARY_2 = "out";
    public static final String BINARY_3 = "build"; // net beans
    public static final String BINARY_4 = "target"; // net beans
    public static final String[] BINARIES  = {BINARY, BINARY_2, BINARY_3, BINARY_4};

//    

//
    /**
     * Attempts to start the project in the same process
     */
    public RunningProject start(String input) throws NotRunnableException;

    /**
     * Attempts to launch the project in a new process
     */
    public RunningProject launch(String input) throws NotRunnableException;
//
//    /**
//     * Attempts to start the project in the same process
//     */
//    public RunningProject start(String input, int timeout) throws NotRunnableException;
//
    /**
     * Attempts to launch the project in a new process
     */
    public RunningProject launch(InputGenerator anOutputBasedInputGenerator, String input, int timeout) throws NotRunnableException;

    public RunningProject launch(String input, int timeout) throws NotRunnableException;
//
    public RunningProject launchInteractive() throws NotRunnableException;

    /**
     * @return The {@link ClassesManager} for this project. This can be used to look at the source code.
     */
    public Option<ClassesManager> getClassesManager();

    /**
     * @return The source code folder
     */
    public File getSourceFolder();

    /**
     * @return The bin/out/target folder
     */
    public File getBuildFolder(String preferredClass) throws FileNotFoundException;

    /**
     * When the project is run in the same JVM then it should log all tracer bus events. This returns that log
     * @return The traceable log of events.
     */
    public TraceableLog getTraceableLog();

    RunningProject launch(
			InputGenerator anOutputBasedInputGenerator,
			Map<String, String> aProcessToInput, int timeout)
			throws NotRunnableException;

	RunningProject launch(String input, String[] anArgs, int timeout)
			throws NotRunnableException;

	boolean isInfinite();

	void setInfinite(boolean newVal);

	File getProjectFolder();

//	RunningProject launchInteractive(String[] args) throws NotRunnableException;
}
