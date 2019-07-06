package grader.basics.execution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.config.BasicProjectExecution;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.project.BasicProjectIntrospection;
//import framework.execution.ARunningProject;
import grader.basics.project.Project;
import grader.basics.trace.UserProcessExecutionFinished;
import grader.basics.trace.UserProcessExecutionStarted;
import grader.basics.trace.UserProcessExecutionTimedOut;
import grader.basics.util.TimedProcess;
import util.misc.Common;
import util.misc.ThreadSupport;
import util.pipe.InputGenerator;
import util.trace.Tracer;

/**
 * This runs the program in a new process.
 */
public class BasicProcessRunner implements Runner {
	public static final int PORT_RELEASE_TIME = 5000;
	protected Map<String, String> entryPoints;
	protected Map<String, RunnerErrorOrOutStreamProcessor> processToOut = new HashMap();
	protected Map<String, RunnerErrorOrOutStreamProcessor> processToErr = new HashMap();
	protected Map<String, RunnerInputStreamProcessor> processToIn = new HashMap();
	protected File folder;
	protected Project project;
	// make them global variables so while waiting someone can query them,
	// probbaly breaks least privelege
	// these are needed to allow asynchronous start of new processes
	protected Map<String, TimedProcess> nameToProcess = new HashMap();
	protected List<String> startedProcesses = new ArrayList();
	protected List<String> pendingProcesses = new ArrayList();
	protected List<String> receivedTags = new ArrayList();
	protected int timeout = 0;
//	ExecutionSpecification executionSpecification;
	protected Map<String, String> processToInput;
	protected String processTeam;
	protected List<String> processes;
	protected RunningProject runner;
	protected Process processObj;
	protected List<String> processesWithStartTags;
	protected String specifiedMainClass;
	public static final String MAIN_ENTRY_POINT = "main";
	protected BasicExecutionSpecification executionSpecification;
	
//	protected void initializeExecutionState() {
////		executionSpecification = ExecutionSpecificationSelector
////				.getExecutionSpecification();
//	}
	// At some point we need a constructor with a map of entry points, called by this constructor
	public BasicProcessRunner(Project aProject, String aSpecifiedProxyMainClass) throws NotRunnableException {
		try {
		specifiedMainClass = aSpecifiedProxyMainClass;
		project = aProject;
		initializeExecutionState();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotRunnableException();
		}
		
	}

	public BasicProcessRunner(Project aProject) throws NotRunnableException {
		this (aProject, null);

//		try {
////			initializeExecutionState();
////			executionSpecification = ExecutionSpecificationSelector
////					.getExecutionSpecification();
//			// entryPoint = getEntryPoint(aProject);
//			// entryPoint =
//			// JavaMainClassFinderSelector.getMainClassFinder().getEntryPoint(aProject);
//
//			// ideally, should gather entry points only if they are required
//			// but need the entry point to find execution folder
//			// need to make this entry points so that we can execute distributed
//			// programs
////			entryPoints = LanguageDependencyManager.getMainClassFinder()
////					.getEntryPoints(aProject);
////
////			// throw an exception if no entry point)
////			folder = aProject.getBuildFolder(entryPoints
////					.get(MainClassFinder.MAIN_ENTRY_POINT));
//			// entryPoint = folder + "\\" + entryPoint;
//			project = aProject;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new NotRunnableException();
//		}
	}

	// use it without project
	public BasicProcessRunner() throws NotRunnableException {

	}
//	public Map<String, String> getEntryPoints() {
//		
////		if (entryPoints == null) {
////			entryPoints = LanguageDependencyManager.getMainClassFinder()
////					.getEntryPoints(project);
////
////		}
////		return entryPoints;
//		
//		return null;
//	}

//	public File getFolder() {
////		return getFolder(getEntryPoints().get(MainClassFinder.MAIN_ENTRY_POINT));
//
//		return getFolder(getEntryPoints().get(getMainEntryPoint()));
//	}

	public File getFolder(String aMainClass) {
		if (folder == null) {
			try {
//				folder = project.getBuildFolder(aMainClass); // why do we need to searc for it?
				folder = project.getBuildFolder(); 


			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return folder;
	}

	// use it without project
	public BasicProcessRunner(File aFolder) throws NotRunnableException {
		folder = aFolder;

	}

	// /**
	// * This figures out what class is the "entry point", or, what class has
	// * main(args)
	// *
	// * @param project
	// * The project to run
	// * @return The class canonical name. i.e. "foo.bar.SomeClass"
	// * @throws NotRunnableException
	// */
	// private String getEntryPoint(Project project) throws NotRunnableException
	// {
	// if (project.getClassesManager().isEmpty())
	// throw new NotRunnableException();
	//
	// ClassesManager manager = project.getClassesManager().get();
	// for (ClassDescription description : manager.getClassDescriptions()) {
	// try {
	// description.getJavaClass().getMethod("main", String[].class);
	// return description.getJavaClass().getCanonicalName();
	// } catch (NoSuchMethodException e) {
	// // Move along
	// }
	// }
	// throw new NotRunnableException();
	// }

	/**
	 * This runs the project with no arguments
	 * 
	 * @param input
	 *            The input to use as standard in for the process
	 * @return A RunningProject object which you can use for synchronization and
	 *         acquiring output
	 * @throws NotRunnableException
	 */
	@Override
	public RunningProject run(String input) throws NotRunnableException {
		return run(input, -1);
	}

	/**
	 * This runs the project with no arguments with a timeout
	 * 
	 * @param input
	 *            The input to use as standard in for the process
	 * @param timeout
	 *            The timeout, in seconds. Set to -1 for no timeout
	 * @return A RunningProject object which you can use for synchronization and
	 *         acquiring output
	 * @throws NotRunnableException
	 */
	@Override
	public RunningProject run(String input, int timeout)
			throws NotRunnableException {
		return run(input, new String[] {}, timeout);
	}
	
	@Override
	public RunningProject run(InputGenerator anOutputBasedInputGenerator, String input, int timeout)
			throws NotRunnableException {
		return run(anOutputBasedInputGenerator, input, new String[] {}, timeout);
	}
	
	public RunningProject run(InputGenerator anOutputBasedInputGenerator, Map<String, String> processToInput, int timeout)
			throws NotRunnableException {
		return run(anOutputBasedInputGenerator, processToInput, new String[] {}, timeout);
	}
	public RunningProject run( Map<String, String> processToInput, int timeout)
			throws NotRunnableException {
		return run(null, processToInput, timeout);
	}
//	protected String getMainEntryPoint() {
//		return "main";
//	}
//	static final List<String> emptyStringList = new ArrayList();
//	protected List<String> getProcessTeams() {
////		return executionSpecification.getProcessTeams();
//		return emptyStringList;
//	}

	/**
	 * This runs the project providing input and arguments
	 * @param input
	 *            The input to use as standard in for the process
	 * @param args
	 *            The arguments to pass in
	 * @param timeout
	 *            The timeout, in seconds. Set to -1 for no timeout
	 * @param anOutputBasedInputGenerator
	 *            An object that provides input       
	 * 
	 * @return A RunningProject object which you can use for synchronization and
	 *         acquiring output
	 * @throws NotRunnableException
	 */
	@Override
	public RunningProject run(InputGenerator anOutputBasedInputGenerator, String input, String[] args, int timeout )
			throws NotRunnableException {
		// String[] command =
		// StaticConfigurationUtils.getExecutionCommand(folder,
		// entryPoints.get(0));
		// return run(command, input, args, timeout);
//		List<String> aProcessTeams = executionSpecification.getProcessTeams();
		List<String> aProcessTeams = getProcessTeams();

		if (aProcessTeams.isEmpty())		
			return run(anOutputBasedInputGenerator,
//				getEntryPoints().get(MainClassFinder.MAIN_ENTRY_POINT), 
					getMainEntryPoint(),
				input, args, timeout);
		else
			return runDefaultProcessTeam(aProcessTeams, input, args, timeout, anOutputBasedInputGenerator);
	}
	public RunningProject run(InputGenerator anOutputBasedInputGenerator, Map<String, String> aProcessToInput, String[] args, int timeout )
			throws NotRunnableException {
		// String[] command =
		// StaticConfigurationUtils.getExecutionCommand(folder,
		// entryPoints.get(0));
		// return run(command, input, args, timeout);
//		List<String> aProcessTeams = executionSpecification.getProcessTeams();
		List<String> aProcessTeams = getProcessTeams();

		if (aProcessTeams.isEmpty()) {
			Set<String> aProcesses = aProcessToInput.keySet();
			String anInput = "";
			for (String aProcess:aProcesses) {
				anInput += aProcessToInput.get(aProcess);
				
			}
			
			return run(anOutputBasedInputGenerator,
//				getEntryPoints().get(MainClassFinder.MAIN_ENTRY_POINT), 
					getMainEntryPoint(),
				anInput, args, timeout);
		} else
			return runDefaultProcessTeam(aProcessTeams, aProcessToInput, args, timeout, anOutputBasedInputGenerator);
	}
	/*
	 * (non-Javadoc)
	 * @see framework.execution.Runner#run(java.lang.String, java.lang.String[], int)
	 */
	@Override
	public RunningProject run(String input, String[] args, int timeout)
			throws NotRunnableException {
		return run(null, input, args, timeout);
	}
//	protected List<String> getProcesses(String firstTeam) {
////		return executionSpecification.getProcesses(firstTeam);
//		return null;
//	}
//	protected List<String> getTerminatingProcesses(String firstTeam) {
////		return executionSpecification.getTerminatingProcesses(firstTeam);
//		return null;
//	}
	public RunningProject runDefaultProcessTeam(List<String> aProcessTeams, String input, String[] args, int timeout, InputGenerator anOutputBasedInputGenerator)
			throws NotRunnableException {
	
		String firstTeam = aProcessTeams.get(0);
		// provide input to the first terminating process
//		List<String> aTerminatingProcesses = executionSpecification.getTerminatingProcesses(firstTeam);
//		List<String> aProcesses = executionSpecification.getProcesses(firstTeam);
		List<String> aTerminatingProcesses = getTerminatingProcesses(firstTeam);
		List<String> aProcesses = getProcesses(firstTeam);
		if (aTerminatingProcesses.isEmpty()) {
			throw NoTerminatingProcessSpecified.newCase(this);
		}
		Map<String, String> aProcessToInput = new HashMap();
		for (String aProcess:aProcesses)
			aProcessToInput.put(aProcess, "");
		aProcessToInput.put(aTerminatingProcesses.get(0), input);
		return run(firstTeam, timeout, anOutputBasedInputGenerator, aProcessToInput); //ignoring args, should have processToArgs in this method
		
	}
	public RunningProject runDefaultProcessTeam(List<String> aProcessTeams, Map<String, String> aProcessToInput, String[] args, int timeout, InputGenerator anOutputBasedInputGenerator)
			throws NotRunnableException {
	
		String firstTeam = aProcessTeams.get(0);
		// provide input to the first terminating process
		
		return run(firstTeam, timeout, anOutputBasedInputGenerator, aProcessToInput); //ignoring args, should have processToArgs in this method
		
	}
	
	

//	public String classWithEntryTagTarget(String anEntryTag) {
//		if (anEntryTag == null)
//			return "";
//		Class aClass = BasicProjectIntrospection.findUniqueClassByTag(project, anEntryTag);
//		if (aClass != null) {
//			String aRetVal = aClass.getName();
//		}
//		if (project instanceof ProjectWrapper) {
//			grader.project.flexible.FlexibleProject graderProject = ((ProjectWrapper) project)
//					.getProject();
//			grader.project.flexible.FlexibleClassDescription aClassDescription = graderProject
//					.getClassesManager()
//					.tagToUniqueClassDescription(anEntryTag); // looks like we should use IntrpspectUtil
//			return aClassDescription.getClassName();
//			// if (aClassDescriptions.size() == 0) {
//			// throw NoClassWithTag.newCase(this, anEntryTag);
//			// } else if (aClassDescriptions.size() > 0) {
//			// throw MultipleClassesWithTag.newCase(this, anEntryTag);
//			// }
//			// for (grader.project.ClassDescription
//			// aClassDescription:aClassDescriptions) {
//			// return aClassDescription.getClassName();
//			//
//			// }
//		}
//		return null; // this should never be executed
//	}
	protected static String[] emptyStringArray = {};
//	public String classWithEntryTagsTarget(List<String> anEntryTags) {
//		if (anEntryTags == null)
//			return "";
//		Class aClass = BasicProjectIntrospection.findClassByTags(project, anEntryTags.toArray(emptyStringArray));
//		if (aClass != null) {
//			String aRetVal = aClass.getName();
//		}
//		// we should not have to do what is below
//		if (project instanceof ProjectWrapper) {
//			grader.project.flexible.FlexibleProject graderProject = ((ProjectWrapper) project)
//					.getProject();
//			grader.project.flexible.FlexibleClassDescription aClassDescription = graderProject
//					.getClassesManager()
//					.tagsToUniqueClassDescription(anEntryTags);
//			return aClassDescription.getClassName();
//			// if (aClassDescriptions.size() == 0) {
//			// throw NoClassWithTag.newCase(this, anEntryTag);
//			// } else if (aClassDescriptions.size() > 0) {
//			// throw MultipleClassesWithTag.newCase(this, anEntryTag);
//			// }
//			// for (grader.project.ClassDescription
//			// aClassDescription:aClassDescriptions) {
//			// return aClassDescription.getClassName();
//			//
//			// }
//		}
//		return null; // this should never be executed
//	}

	
	void acquireIOLocks() {
		try {
			runner.start();
			Set<String> aProcesses = processToOut.keySet();
			for (String aProcess:aProcesses) {
				processToOut.get(aProcess).getSemaphore().acquire();
				processToErr.get(aProcess).getSemaphore().acquire();
				
			}
//			outputSemaphore.acquire(); // share once for all processes
//			errorSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void releaseTeamLocks() {
//		try {
//			outputSemaphore.release(); // share once for all processes
//			errorSemaphore.release();
//		Set<String> aProcesses = processToOut.keySet();
//		for (String aProcess:aProcesses) {
//			processToOut.get(aProcess).getSemaphore().release();;
//			processToErr.get(aProcess).getSemaphore().release();
//			
//		}
			runner.end();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
//	protected List<String> getStartTags(String aProcess) {
////		return executionSpecification
////				.getStartTags(aProcess);
//		return null;
//	}

	public RunningProject run(String aProcessTeam,
			int aTimeout, InputGenerator anOutputBasedInputGenerator, Map<String, String> aProcessToInput)
			throws NotRunnableException {
//		executionSpecification = ExecutionSpecificationSelector
//				.getExecutionSpecification();
		processTeam = aProcessTeam;
//		for (String aProcess:aProcessToInput.keySet()) {
//			processToInput.put(aProcess, new StringBuffer(aProcessToInput.get(aProcess)));
//		}
		processToInput = aProcessToInput;
		timeout = aTimeout;

//		processes = executionSpecification.getProcesses(aProcessTeam);
		processes = getProcesses(aProcessTeam);

//		runner = new ARunningProject(project, anOutputBasedInputGenerator, processes, aProcessToInput);
		runner = createRunningProject(project, anOutputBasedInputGenerator, processes, aProcessToInput);
		acquireIOLocks();
//		try {
//			runner.start();
//			outputSemaphore.acquire(); // share once for all processes
//			errorSemaphore.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		for (String aProcess : processes) {
//			List<String> startTags = executionSpecification
//					.getStartTags(aProcess);
			List<String> startTags = getStartTags(aProcess);
			if (startTags != null && !startTags.isEmpty()) {
				processesWithStartTags.add(aProcess);
				pendingProcesses.add(aProcess);
				continue;
			}
			runTeamProcess(aProcess, anOutputBasedInputGenerator);

			// List<String> basicCommand =
			// StaticConfigurationUtils.getBasicCommand(aProcess);
			// String anEntryPoint;
			// if (StaticConfigurationUtils.hasEntryPoint(basicCommand)); {
			// anEntryPoint = anExecutionSpecification.getEntryPoint(aProcess);
			// if (anEntryPoint == null)
			// throw EntryPointNotFound.newCase(this);
			// }
			// if (anEntryPoint != null && !anEntryPoint.isEmpty()) {
			// getFolder(anEntryPoint);
			// }
			// String anEntryTag = null;
			// if (StaticConfigurationUtils.hasEntryTag(basicCommand))
			// anEntryTag = anExecutionSpecification.getEntrytag(aProcess);
			// // if (anEntryTag != null ) {
			// // getFolder(anEntryTag);
			// // }
			// String aClassWithEntryTag = null;
			// if (anEntryTag != null) {
			// aClassWithEntryTag = classWithEntryTagTarget(anEntryTag);
			// if (aClassWithEntryTag == null)
			// throw TagNotFound.newCase(anEntryTag, this);
			// }
			// if (aClassWithEntryTag != null && folder == null) {
			//
			// getFolder(aClassWithEntryTag);
			// }
			// String[] anArgs =
			// anExecutionSpecification.getArgs(aProcess).toArray(new
			// String[0]);
			// int aSleepTime = anExecutionSpecification.getSleepTime(aProcess);
			// String[] command = StaticConfigurationUtils.getExecutionCommand(
			// aProcess,
			// folder,
			// anEntryPoint,
			// aClassWithEntryTag,
			// anArgs);
			// TimedProcess aTimedProcess = run(runner, command,
			// aProcessToInput.get(aProcess), anArgs, aTimeout, aProcess,
			// false); // do not wait for process to finish
			// nameToProcess.put(aProcess, aTimedProcess);
			// ThreadSupport.sleep(aSleepTime);
			// // some processes may be added dynamically on firing of events,
			// will support them later
			//

		}
		waitForDynamicProcesses();
		waitForStartedProcesses();
//		terminateTeam();
//		waitForPortsOfTerminatedProcessesToBeReleased();
//		releaseTeamLocks();
		// for (String
		// aTerminatingProcess:anExecutionSpecification.getTerminatingProcesses(aProcessTeam))
		// {
		// TimedProcess aTimedProcess = nameToProcess.get(aTerminatingProcess);
		// try {
		// aTimedProcess.wait(aTimeout);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		return runner;
		// String[] command =
		// StaticConfigurationUtils.getExecutionCommand(folder,
		// entryPoints.get(0));
		// return run(command, input, args, timeout);
		// return run (entryPoints.get(MainClassFinder.MAIN_ENTRY_POINT), input,
		// args, timeout);

	}
	
	protected void waitForPortsOfTerminatedProcessesToBeReleased() {
		ThreadSupport.sleep(PORT_RELEASE_TIME);
	}
	
	
//	protected String searchForEntryPoint (String aProcess) {
//		return null; // should have a seter for this
////		List<String> basicCommand = StaticConfigurationUtils
////				.getBasicCommand(aProcess);
////		String anEntryPoint = null;
////		if (StaticConfigurationUtils.hasEntryPoint(basicCommand))
////			
////		{
////			anEntryPoint = executionSpecification.getEntryPoint(aProcess);
////			if (anEntryPoint == null)
////				throw EntryPointNotFound.newCase(this);
////		}
////		if (anEntryPoint != null && !anEntryPoint.isEmpty()) {
////			getFolder(anEntryPoint);
////		}
////
////		return anEntryPoint;
//	}
	
	public static final String[] EMPTY_STRING_ARGS = {};
	protected String[] getArgs(String aProcess) {
		List<String> aListArgs = executionSpecification.getArgs(aProcess);
		if (aListArgs == null) {
			return EMPTY_STRING_ARGS;
		}
		else {
			return aListArgs.toArray(EMPTY_STRING_ARGS);
		}
//		return executionSpecification.getArgs(aProcess).toArray(new String[0]);
	}
//	protected String[] getArgs(String aProcess) {
////		return executionSpecification.getArgs(aProcess).toArray(
////				new String[0]);
//		return null;
//	}
	
	protected int getResourceReleaseTime(String aProcess) {
//		return executionSpecification.getSleepTime(aProcess);
		return executionSpecification.getResourceReleaseTime(aProcess);
		// why was this hardwired
//		return 1000;
	}
	
	protected String[] getCommand(String aProcess, String anEntryPoint, String anEntryTag, String[] anArgs ) {
//		return StaticConfigurationUtils.getExecutionCommand(project,
//				aProcess, folder, anEntryPoint, anEntryTag, anArgs);
			return BasicStaticConfigurationUtils.getExecutionCommand(project, aProcess, folder, anEntryPoint, anEntryTag,
					anArgs);
		
//		return null;
	}

	protected void runTeamProcess(String aProcess, InputGenerator anOutputBasedInputGenerator) {
		String anEntryPoint = searchForEntryPoint(aProcess);
		String aClassWithEntryTag = searchForEntryTag(aProcess);
//		String[] anArgs = executionSpecification.getArgs(aProcess).toArray(
//				new String[0]);
		String[] anArgs = getArgs(aProcess);
//		int aSleepTime = executionSpecification.getSleepTime(aProcess);
		int aSleepTime = getResourceReleaseTime(aProcess);

//		String[] command = StaticConfigurationUtils.getExecutionCommand(project,
//				aProcess, folder, anEntryPoint, aClassWithEntryTag, anArgs);
		String[] command = getCommand(aProcess, anEntryPoint, aClassWithEntryTag, anArgs);
		TimedProcess aTimedProcess = run(runner, anOutputBasedInputGenerator,
				command, processToInput.get(aProcess), anArgs, timeout, aProcess, false); // do
																					// not
																					// wait
																					// for
																					// process
																					// to
																					// finish
		nameToProcess.put(aProcess, aTimedProcess);
		runner.setProcess(aProcess, aTimedProcess);
		Tracer.info(this, "Sleeping for " + aSleepTime + "ms to wait for release of resources of terminated process:" + aProcess);
		ThreadSupport.sleep(aSleepTime); // should be before and not after., so ports can be released, or maybe before and after
		// some processes may be added dynamically on firing of events, will
		// support them later
	}

	synchronized void waitForDynamicProcesses() {
		while (pendingProcesses != null && !pendingProcesses.isEmpty())
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	// to be called by callback that creates a dynamic process
	synchronized void notifyDynamicProcessCreation() {
		this.notify();
	}

	synchronized void waitForStartedProcesses() {
		try {
//		for (String aTerminatingProcess : executionSpecification
//				.getTerminatingProcesses(processTeam)) {
		for (String aTerminatingProcess : getTerminatingProcesses(processTeam)) {
			TimedProcess aTimedProcess = nameToProcess.get(aTerminatingProcess);
		
				aTimedProcess.waitFor();
			
		}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			runner.terminateTeam();
			waitForPortsOfTerminatedProcessesToBeReleased();
		}

	}
	
	void terminateProcess(String aProcess) {
		try {
			processToOut.get(aProcess).getSemaphore().acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			processToErr.get(aProcess).getSemaphore().acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TimedProcess timedProcess = nameToProcess.get(aProcess);
		timedProcess.getProcess().destroy();
	}
	
	

	void terminateTeam() {
		Set<String> aProcesses = nameToProcess.keySet();
		for (String aProcess : aProcesses) {
			terminateProcess(aProcess);
		}
		terminateRunner();
	}

	void terminateRunner() {
		try {
			// Wait for the output to finish
//			acquireIOLocks();
			releaseTeamLocks();
		} catch (Exception e) {
			e.printStackTrace();
			Tracer.error(e.getMessage());
			runner.error();
			releaseTeamLocks();
//			runner.end();
		}
	}
	protected  String[] getExecutionCommand(Project aProject,
			File aBuildFolder, String anEntryPoint, String[] anArgs) {
//		return StaticConfigurationUtils.getExecutionCommand(aProject, aBuildFolder, anEntryPoint);
//		return BasicStaticConfigurationUtils.getExecutionCommand(aProject, null, aBuildFolder, anEntryPoint, anEntryPoint, anArgs);
		return BasicStaticConfigurationUtils.getExecutionCommand(aProject, null, aBuildFolder, anEntryPoint, anEntryPoint, anArgs);
//		return BasicLanguageDependencyManager.getMainClassFinder().getDefaultCommand();
//		return null;
	}
	@Override
	public RunningProject run(InputGenerator aDynamicInputProvider, String anEntryPoint, String input,
			String[] args, int timeout) throws NotRunnableException {
//		String[] command = StaticConfigurationUtils.getExecutionCommand(folder,
//		String[] command = StaticConfigurationUtils.getExecutionCommand(project, getFolder(),
//				anEntryPoint, args); // added args
//		String[] command = getExecutionCommand(project, getFolder(),
//				anEntryPoint, args); // added args
		String[] command = getExecutionCommand(project, getFolder(anEntryPoint),
				anEntryPoint, args); // added args
		return run(aDynamicInputProvider, command, input, args, timeout);

	}
	
	protected RunningProject createRunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator, String anInput) {
		return new BasicRunningProject(aProject, anOutputBasedInputGenerator, anInput);
	}
	protected RunningProject createRunningProject(Project aProject, 
			InputGenerator anOutputBasedInputGenerator, 
			List<String> aProcesses, Map<String, String> aProcessToInput) {
 
	return new BasicRunningProject(project, anOutputBasedInputGenerator, processes, aProcessToInput);
}
	protected String mainEntryPoint() {
//		return  MainClassFinder.MAIN_ENTRY_POINT;
		return null;
	}
	// move this to some util
	public RunningProject runMainClass(Class aClass, String input,
			String[] args, int timeout) throws NotRunnableException {
        String[] command = {"java",  "-cp",  System.getProperty("java.class.path",aClass.getName())};
       return run(null, command, input, args, timeout);

	}
	
	public RunningProject run(InputGenerator anOutputBasedInputGenerator, String[] command, String input,
			String[] args, int timeout) throws NotRunnableException {
//		RunningProject retVal = new RunningProject(project, anOutputBasedInputGenerator, input);
		RunningProject retVal = createRunningProject(project, anOutputBasedInputGenerator, input);
		String aMainEntryPoint = mainEntryPoint();
		String aProcessName = aMainEntryPoint == null?BasicProcessRunner.MAIN_ENTRY_POINT:aMainEntryPoint;

		TimedProcess process = run(retVal, anOutputBasedInputGenerator, command, input, args,
				timeout, 
				aProcessName,
//				mainEntryPoint(),				
//				MainClassFinder.MAIN_ENTRY_POINT, 
				true);
		return retVal;
	}

//	final Semaphore outputSemaphore = new Semaphore(1);
//	final Semaphore errorSemaphore = new Semaphore(1);
	
	protected File getPermissionFile() {
//		return JavaProjectToPermissionFile.getPermissionFile(project);
		return null;
	}
	protected String getClassPath() {
//		return GradingEnvironment.get().getClasspath();
		return System.getProperty("java.class.path");
	}
	protected String[] maybeToExecutorCommand(String[] aCommand) {
//		return ExecutorSelector.getExecutor().maybeToExecutorCommand(aCommand);
		return aCommand;

	}
	@Override
	public void terminateProcess() {
		processObj.destroy();
	}
	protected  RunnerErrorOrOutStreamProcessor createRunnerOutputStreamProcessor(InputStream aProcessErrorOut, RunningProject aRunner, /*Semaphore aSemaphore,*/ String aProcessName, Boolean anOnlyProcess) {
	  return new ABasicRunnerOutputStreamProcessor(
			 aProcessErrorOut, aRunner, /*outputSemaphore,*/
			aProcessName, anOnlyProcess);
	}
	protected RunnerErrorOrOutStreamProcessor createRunnerErrorStreamProcessor (InputStream aProcessErrorOut, 
			RunningProject aRunner, 
			/*Semaphore aSemaphore, */
			String aProcessName,
			Boolean anOnlyProcess) {
	return new ARunnerErrorStreamProcessor(
			aProcessErrorOut, aRunner, /*errorSemaphore,*/
			aProcessName, anOnlyProcess);
	}
	protected RunnerInputStreamProcessor createRunnerInputStreamProcessor(OutputStream anInput, 
			RunningProject aRunner,  String aProcessName, /*Semaphore aSemaphore,*/ Boolean anOnlyProcess) {
		return new ARunnerInputStreamProcessor(anInput, aRunner, aProcessName,  anOnlyProcess);
	}
	protected void maybeSetInputAndArgs(String input, String[] args) {
//		if (project != null && project instanceof ProjectWrapper) {
//			SakaiProject sakaiProject = ((ProjectWrapper) project).getProject();
//			sakaiProject.setCurrentInput(input); // this should go or be append for subsequent input
//			sakaiProject.setCurrentArgs(args);
//		}
	}

	@Override
	public TimedProcess run (RunningProject runner, InputGenerator anOutputBasedInputGenerator,
			String[] aCommand, String input, String[] args, int timeout,
			String aProcessName, boolean anOnlyProcess) throws NotRunnableException {
		// final RunningProject runner = new RunningProject(project);
//		if (project != null && project instanceof ProjectWrapper) {
//			SakaiProject sakaiProject = ((ProjectWrapper) project).getProject();
//			sakaiProject.setCurrentInput(input); // this should go or be append for subsequent input
//			sakaiProject.setCurrentArgs(args);
//		}
//		TimedProcess result = null;
		maybeSetInputAndArgs(input, args);
		TimedProcess process = null;
		try {
			if (anOnlyProcess)
			runner.start();


			String classPath = getClassPath();
			
			
	
			ProcessBuilder builder;
			if (aCommand.length == 0) { // this part should not execute, onlyif
										// command is null

				File aPermissionsFile = getPermissionFile();

				// Prepare to run the process
				// ProcessBuilder builder = new ProcessBuilder("java", "-cp",
				// GradingEnvironment.get().getClasspath(), entryPoint);
				if (aPermissionsFile != null) {
				builder = new ProcessBuilder("java", 
						"-cp", 
//						GradingEnvironment.get().getClasspath(), 
						getClassPath(),
						"-Djava.security.manager",
						"-Djava.security.policy==\"" + aPermissionsFile.getAbsolutePath() + "\"",						
						getEntryPoints().get(
								getMainEntryPoint()
//						MainClassFinder.MAIN_ENTRY_POINT
						));
				} else {
					builder = new ProcessBuilder("java", 
							"-cp", 
//							GradingEnvironment.get().getClasspath(), 
							getClassPath(),
//							"-Djava.security.manager",
//							"-Djava.security.policy==\"" + aPermissionsFile.getAbsolutePath() + "\"",						
							getEntryPoints().get(
									getMainEntryPoint()
//							MainClassFinder.MAIN_ENTRY_POINT
							));
				}
				
				
				Tracer.info(this,"Running process: java -cp \"" + classPath
						+ "\" "
						+ entryPoints.get(
//								MainClassFinder.MAIN_ENTRY_POINT
								getMainEntryPoint()
								));
			} else {
//				aCommand = ExecutorSelector.getExecutor().maybeToExecutorCommand(aCommand);
				aCommand = maybeToExecutorCommand(aCommand);
				if (aCommand != null) {
				builder = new ProcessBuilder(aCommand);
				Tracer.info(this,"Running command:"
						+ Common.toString(aCommand, " "));
				} else {
					return null; // this should not happen
				}
			}
		
			builder.directory(folder);
			
			if (folder != null)
				Tracer.info(this,"Running in folder: "
						+ folder.getAbsolutePath());

		
			process = new TimedProcess(builder, timeout);
			runner.setCurrentTimeProcess(process);

			 processObj = process.start();
			if (folder != null)
				UserProcessExecutionStarted.newCase(aCommand,
						folder.getAbsolutePath(),
						(entryPoints != null) ? entryPoints
								.get(

										getMainEntryPoint()
										) : 
											null,
						classPath, this);

			
			RunnerErrorOrOutStreamProcessor outRunnable = 
					createRunnerOutputStreamProcessor(
					process.getInputStream(), runner, /*outputSemaphore,*/
					aProcessName, anOnlyProcess);

			Thread outThread = new Thread(outRunnable);
			outThread.setName("Out Stream Runnable");
			processToOut.put(aProcessName, outRunnable);
			runner.setProcessOut(aProcessName, outRunnable);

			outThread.start();
			
			RunnerErrorOrOutStreamProcessor errorRunnable = createRunnerErrorStreamProcessor(
					process.getErrorStream(), runner, /*errorSemaphore,*/
					aProcessName, anOnlyProcess);
			Thread errorThread = new Thread(errorRunnable);
			errorThread.setName("Error Stream Runnable");
			processToErr.put(aProcessName, errorRunnable);
			runner.setProcessErr(aProcessName, errorRunnable);

			errorThread.start();
			

			// Write to the process
			// This can be done after the process is started, so one can create
			// a coordinator of various processes
			// using the output of one process to influence the input of another
			
			RunnerInputStreamProcessor aProcessIn = createRunnerInputStreamProcessor(process.getOutputStream(), runner, aProcessName,  anOnlyProcess);

			runner.setProcessIn(aProcessName, aProcessIn);
			processToIn.put(aProcessName, aProcessIn);
			if (anOutputBasedInputGenerator == null) {
				aProcessIn.newInput(input);
				aProcessIn.terminateInput(); // for incremental input, allow it to be given afterwards and do not close
			} else if (input != null && !input.isEmpty()) {
				aProcessIn.newInput(input); // not sure an empty input makes a difference but just in case
			}
			
			//			OutputStreamWriter processIn = new OutputStreamWriter(
//					process.getOutputStream());
//			
//			processIn.write(input);
//			processIn.flush();
//			processIn.close(); // for incremental input, allow it to be given afterwards and do not close

			if (anOnlyProcess) {
//				processIn.close(); // single team process, we need to fix this later

				// Wait for it to finish
				try {
					process.waitFor();
//					if (entryPoints != null)
					UserProcessExecutionFinished.newCase(
							folder.getAbsolutePath(),
							(entryPoints != null) ? entryPoints
									.get(
//											MainClassFinder.MAIN_ENTRY_POINT
											getMainEntryPoint()
											) : null,
							classPath, this);
				} catch (Exception e) {
					e.printStackTrace();

					outRunnable.getSemaphore().release();
					errorRunnable.getSemaphore().release();
					String entryPoint = "";
					if (entryPoints != null)
						entryPoint = entryPoints.get(
//								MainClassFinder.MAIN_ENTRY_POINT
								getMainEntryPoint()
								);
					UserProcessExecutionTimedOut.newCase(
							folder.getAbsolutePath(),
//							entryPoints.get(MainClassFinder.MAIN_ENTRY_POINT),
							entryPoint,
							classPath, this);

					System.out
							.println("*** Timed out waiting for process to finish ***");
//					result = null;
					project.setInfinite(true);
					
					// avoiding hanging processes
					// processIn.flush();
					// processIn.close();
					// process.getProcess().destroy();
				}
				// }
				if (BasicProjectExecution.isWaitForMethodConstructorsAndProcesses())
				terminateProcess();
				
//				processObj.destroy();
				// if (wait) {
				// Wait for the output to finish
				outRunnable.getSemaphore().acquire();
				errorRunnable.getSemaphore().acquire();
//				outputSemaphore.acquire();
//				errorSemaphore.acquire();
				runner.end();
			}
			// }

		} catch (Exception e) {
			e.printStackTrace();
			Tracer.error(e.getMessage());
			runner.error();
			runner.end();
		}
		// return runner;
		return process;
	}
	
	protected void initializeExecutionState() {
		executionSpecification = BasicExecutionSpecificationSelector.getBasicExecutionSpecification();
	}

	public Map<String, String> getEntryPoints() {
		if (entryPoints == null) {
			entryPoints = BasicLanguageDependencyManager.getMainClassFinder().getEntryPoints(project, specifiedMainClass);

		}
		return entryPoints;
	}

	public File getFolder() {
		return getFolder(getEntryPoints().get(BasicProcessRunner.MAIN_ENTRY_POINT));
	}
	
	/**
	 * This figures out what class is the "entry point", or, what class has
	 * main(args)
	 *
	 * @param project
	 *            The project to run
	 * @return The class canonical name. i.e. "foo.bar.SomeClass"
	 * @throws NotRunnableException
	 * should really use the method below which checks the specified class
	 */
	protected String getMainEntryPoint() {
		if (specifiedMainClass != null)
			return specifiedMainClass;
		return getMainEntryPoint(project, specifiedMainClass);
//		Map<String, String> anEntryPoints = getEntryPoints();
////		String aRetVal = getEntryPoints().get(BasicProcessRunner.MAIN_ENTRY_POINT);
//		String aRetVal = anEntryPoints.get(BasicProcessRunner.MAIN_ENTRY_POINT);
//
//		if (aRetVal == null) {
//			if (anEntryPoints.size() > 0) {
//				aRetVal = anEntryPoints.values().iterator().next();
//				if (anEntryPoints.size() > 1) {
//					System.err.println("Returning first of multiple entry points:" + anEntryPoints.values());
//				}
//			} else {
//			
////			return "main";
//			return BasicProcessRunner.MAIN_ENTRY_POINT;
//			}
//		}
//		return aRetVal;
	}
	public static String getMainEntryPoint(Project aProject, String specifiedMainClass) {
		if (specifiedMainClass == null) {
			specifiedMainClass = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getEntryPoint();
		}
		Map<String, String> anEntryPoints = BasicLanguageDependencyManager.getMainClassFinder().getEntryPoints(aProject, specifiedMainClass);
//		String aRetVal = getEntryPoints().get(BasicProcessRunner.MAIN_ENTRY_POINT);
		String aRetVal = anEntryPoints.get(BasicProcessRunner.MAIN_ENTRY_POINT);
//		String aRetVal = anEntryPoints.get(BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getEntryPoint());

		if (aRetVal == null) {
			if (anEntryPoints.size() > 0) {
				aRetVal = anEntryPoints.values().iterator().next();
				if (anEntryPoints.size() > 1) {
					System.err.println("Returning first of multiple entry points:" + anEntryPoints.values());
				}
			} else {
			
//			return "main";
			return BasicProcessRunner.MAIN_ENTRY_POINT;
			}
		}
		return aRetVal;
	}
    @Override
	public String getSpecifiedMainClass() {
		return specifiedMainClass;
	}
    @Override
	public void setSpecifiedMainClass(String specifiedMainClass) {
		this.specifiedMainClass = specifiedMainClass;
	}

	protected List<String> getProcessTeams() {
		return executionSpecification.getProcessTeams();
	}

	protected List<String> getProcesses(String firstTeam) {
		return executionSpecification.getProcesses(firstTeam);
	}

	protected List<String> getTerminatingProcesses(String firstTeam) {
		return executionSpecification.getTerminatingProcesses(firstTeam);
	}
	public String classWithEntryTagTarget(String anEntryTag) {
		if (anEntryTag == null)
			return "";
		Class aClass = BasicProjectIntrospection.findUniqueClassByTag(project, anEntryTag);
		if (aClass != null) {
			String aRetVal = aClass.getName();
			return aRetVal;
		}
//		if (project instanceof ProjectWrapper) {
//			grader.project.flexible.FlexibleProject graderProject = ((ProjectWrapper) project).getProject();
//			grader.project.flexible.FlexibleClassDescription aClassDescription = graderProject.getClassesManager()
//					.tagToUniqueClassDescription(anEntryTag); // looks like we should use IntrpspectUtil
//			return aClassDescription.getClassName();
//		}
		return null; // this should never be executed
	}
	protected String searchForEntryTag(String aProcess) {
		return searchForEntryTag(aProcess, BasicStaticConfigurationUtils.getBasicCommand());
		
	}
	
	protected String searchForEntryTag(String aProcess, List<String> basicCommand) {
//		List<String> basicCommand = StaticConfigurationUtils.getBasicCommand(aProcess);

		// if (anEntryPoint != null && !anEntryPoint.isEmpty()) {
		// getFolder(anEntryPoint);
		// }
		String anEntryTag = null;
		List<String> anEntryTags = null;
		if (BasicStaticConfigurationUtils.hasEntryTags(basicCommand)) {
			anEntryTags = executionSpecification.getEntryTags(aProcess);
//			if (anEntryTags == null || anEntryTags.isEmpty()) {
//				String anEntryPoint = executionSpecification.getEntryPoint(aProcess);
//				if (anEntryPoint != null && !anEntryPoint.isEmpty()) {
//					anEntryTags = new ArrayList();
//					anEntryTags.add(anEntryPoint);
//				}
//			}
		} else if (BasicStaticConfigurationUtils.hasEntryTag(basicCommand))
			anEntryTag = executionSpecification.getEntryTag(aProcess); // this will match entryTag also, fix at some pt

		// if (anEntryTag != null ) {
		// getFolder(anEntryTag);
		// }
		String aClassWithEntryTag = null;

		if (anEntryTag != null ) {
			aClassWithEntryTag = classWithEntryTagTarget(anEntryTag);
			if (aClassWithEntryTag == null)
				throw TagNotFound.newCase(anEntryTag, this);
		} else if (anEntryTags != null ) {
			aClassWithEntryTag = classWithEntryTagsTarget(anEntryTags);
			if (aClassWithEntryTag == null)
				throw TagNotFound.newCase(anEntryTags, this);
		}
		if (aClassWithEntryTag != null && folder == null) {

			getFolder(aClassWithEntryTag);
		}
		return aClassWithEntryTag;
	}
	public String classWithEntryTagsTarget(List<String> anEntryTags) {
		if (anEntryTags == null)
			return "";
		Class aClass = BasicProjectIntrospection.findClassByTags(project, anEntryTags.toArray(emptyStringArray));
		if (aClass != null) {
			String aRetVal = aClass.getName();
			return aRetVal; // added this
		}
		// we should not have to do what is below
//		if (project instanceof ProjectWrapper) {
//			grader.project.flexible.FlexibleProject graderProject = ((ProjectWrapper) project).getProject();
//			grader.project.flexible.FlexibleClassDescription aClassDescription = graderProject.getClassesManager()
//					.tagsToUniqueClassDescription(anEntryTags);
//			return aClassDescription.getClassName();
//		}
		return null; // this should never be executed
	}
	protected List<String> getStartTags(String aProcess) {
		return executionSpecification.getStartTags(aProcess);
	}

	// need an equivalent of this for basicprocessrunner
	protected String searchForEntryPoint(String aProcess) {
		List<String> basicCommand = BasicStaticConfigurationUtils.getBasicCommand(aProcess);
		String anEntryPoint = null;
		if (BasicStaticConfigurationUtils.hasEntryPoint(basicCommand))

		{
			anEntryPoint = executionSpecification.getEntryPoint(aProcess);
			if (anEntryPoint == null)
				throw EntryPointNotFound.newCase(this, basicCommand);
		}
		if (anEntryPoint != null && !anEntryPoint.isEmpty()) {
			getFolder(anEntryPoint);
		}
		return anEntryPoint;
	}
	
}
