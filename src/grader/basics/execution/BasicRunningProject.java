package grader.basics.execution;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.project.Project;
import grader.basics.util.TimedProcess;
import gradingTools.shared.testcases.utils.ALinesMatcher;
import gradingTools.shared.testcases.utils.LinesMatcher;
//import grader.config.StaticConfigurationUtils;
//import grader.sakai.project.SakaiProject;
//import grader.trace.overall_transcript.OverallTranscriptSaved;
import util.pipe.InputGenerator;
import util.pipe.ProcessInputListener;
import util.trace.Tracer;

/**
 * This is a wrapper for a running project independent of the method of
 * execution. This provides support for synchronization via semaphores and
 * output manipulation.
 */
public class BasicRunningProject implements ProcessInputListener, RunningProject, Runnable {
	public static boolean echoOutput = true;
	

//	public static long RESORT_TIME = 100;
//	public static long MAX_OUTPUT_DELAY = 100;
    private long maxNotificationTime;
    private static long timeToWaitForConcurrentOutput = (long)1e10;

	private boolean hasOutput = false;
	
	protected LinkedList<AProcessOutput> pendingOutput = new LinkedList<>();
//	protected BlockingQueue<AProcessOutput> pendingOutput = new LinkedBlockingQueue<>()

	
    private Semaphore runningState = new Semaphore(1);
    protected Map<String, StringBuffer> processToErrors = new HashMap<>();
    protected Map<String, List<String>> processToErrorLines = new HashMap<>();;

    protected Map<String, StringBuffer> processToProcessedOutput = new HashMap<>();
    protected Map<String, List<String>> processToProcessedOutputLines = new HashMap<>();
    protected Map<String, LinesMatcher> processToProcessedLineMatcher;
    protected StringBuffer allProcessedOutput = new StringBuffer(5000);
    protected StringBuffer allReceivedOutputAndErrors = new StringBuffer(5000);
    protected StringBuffer allReceivedErros = new StringBuffer(5000);

    protected List<String> allProcessedOutputLines = new ArrayList(1000);
    protected List<String> allReceivedOutputAndErrorLines = new ArrayList(1000);

    
    protected Map<String, StringBuffer> processToReceivedOutput = new HashMap<>();
    protected Map<String, List<String>> processToReceivedOutputLines = new HashMap<>();
    protected Map<String, LinesMatcher> processToReceivedLineMatcher;

    // duplicates the mapping in Process Runner
    protected Map<String, RunnerInputStreamProcessor> processToIn = new HashMap<>();
    protected Map<String, TimedProcess> nameToProcess = new HashMap<>();
    protected TimedProcess currentProcess;
    protected boolean destroyed;
    protected Project project;

   

	
	protected Map<String, String> processToOutputAndErrors = new HashMap<>();

//    protected String output = ""; // why is this a string and not a string buffer?
//    protected String errorOutput = "";
//    protected String outputAndErrors = "";
    protected NotRunnableException exception;
    protected String outputFileName;
    protected StringBuffer projectOutput;
    protected Set<Thread> dependentThreads = new HashSet<>();
    protected Set<Closeable> dependentCloseables = new HashSet<>();

    protected InputGenerator outputBasedInputGenerator; // actuall all we need is an output consumer
    protected StringBuffer input = new StringBuffer();
    protected Map<String, StringBuffer> processToInput = new HashMap<>();
    protected Map<String, RunnerErrorOrOutStreamProcessor> processToOut = new HashMap<>();
    protected Map<String, RunnerErrorOrOutStreamProcessor> processToErr = new HashMap<>();
    protected List<String> processes;
    int numProcesses;
    public BasicRunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator, List<String> aProcesses, Map<String, String> aProcessToInput) {
        project = aProject;
    	exception = null;
//        output = null;
        processes = aProcesses;
    	numProcesses =  processes == null? 1:processes.size();

        maybeProcessProjectWrappper(aProject);

        outputBasedInputGenerator = anOutputBasedInputGenerator;
        if (outputBasedInputGenerator != null) {
            outputBasedInputGenerator.addProcessInputListener(this); // maybe this should be in another class
        }
        if (aProcessToInput != null) {
            for (String aProcess : aProcessToInput.keySet()) {
                String anInput = aProcessToInput.get(aProcess);
                // if (anInput != null)
                processToInput.put(aProcess, new StringBuffer(anInput));
//				processToOutput.put(aProcess, new StringBuffer());
                input.append(anInput);
            }
        }
        if (aProcesses != null) {
            for (int i = 0; i < aProcesses.size(); i++) {
                String aProcess = aProcesses.get(i);
//                if (StaticConfigurationUtils.getTrace()) {
//                    LocalGlobalTranscriptManager aTranscriptManager = new ALocalGlobalTranscriptManager();
//                    processToTranscriptManager.put(aProcess, aTranscriptManager);
//                    aTranscriptManager.setIndexAndLogDirectory(i, project.getStudentAssignment().getFeedbackFolder().getAbsoluteName());
//                    aTranscriptManager.setProcessName(aProcess);
//                }
                maybeProcessTrace(aProcess, i);
                if (outputBasedInputGenerator != null) {
                    outputBasedInputGenerator.addProcessName(aProcess);

                }
            }
            if (outputBasedInputGenerator != null) {
                outputBasedInputGenerator.processNamesAdded();
            }

        }
        maxNotificationTime = System.nanoTime();
        outputSorter = new Thread(this);
        outputSorter.setName("Output Sorter");
        outputSorter.start();
    }
    Thread outputSorter;
    @Override
    public Project getBasicProject() {
    	return project;
    }
    
    protected void maybeProcessProjectWrappper(Project aProject) {
//    	if (aProject != null && aProject instanceof ProjectWrapper) {
//            projectWrapper = (ProjectWrapper) aProject;
//            project = projectWrapper.getProject();
//            outputFileName = project.getOutputFileName();
//            projectOutput = project.getCurrentOutput();
////			input.append(project.getCurrentInput());
//        }
    }
    
    protected void maybeProcessTrace(String aProcess, int aProcessNumber) {
//    	 if (StaticConfigurationUtils.getTrace()) {
//             LocalGlobalTranscriptManager aTranscriptManager = new ALocalGlobalTranscriptManager();
//             processToTranscriptManager.put(aProcess, aTranscriptManager);
//             aTranscriptManager.setIndexAndLogDirectory(i, project.getStudentAssignment().getFeedbackFolder().getAbsoluteName());
//             aTranscriptManager.setProcessName(aProcess);
//         }
    }
    
    
    protected void maybeWaitForResort() throws InterruptedException {
    	if (BasicExecutionSpecificationSelector.getBasicExecutionSpecification().isWaitForResort()) {
    		Tracer.info(this, System.currentTimeMillis() + ":Waiting for output resort");
    		// should this be sleep?
    		wait(BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getResortTime());
    		
    	}
    }
    protected void resortingOutputProcessing() {
    	try {
			if (pendingOutput.isEmpty()) {
//				System.out.println("waitimng for received output" );
//	    		Tracer.info(this, "Waiting for received output");

				wait();
//				System.out.println("end wait 1");
			} else {
				Tracer.info(this, System.currentTimeMillis() + ": waiting for resort time");
				wait(BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getResortTime());
			}
			long aCurrentTime = System.nanoTime();
			Tracer.info(this, aCurrentTime + ":Finished waiting for input");
			List<AProcessOutput> copy = new LinkedList<>(pendingOutput);
			Collections.sort(pendingOutput);
			
			if (!pendingOutput.equals(copy)) {
				System.out.println("***** Input reordered");
			}

			while (!pendingOutput.isEmpty()) {
				AProcessOutput aProcessOutput = pendingOutput.peek();
				long aTime = aProcessOutput.time;
				if (aCurrentTime - aTime > timeToWaitForConcurrentOutput) {
					if (hasOutput && aTime < maxNotificationTime) {
						long diff = (maxNotificationTime - aTime);
						timeToWaitForConcurrentOutput = Math.max(diff, timeToWaitForConcurrentOutput);
						System.err.println("+++" + diff + " " + timeToWaitForConcurrentOutput);
					}
					hasOutput = true;
					maxNotificationTime = Math.max(maxNotificationTime, aTime);
					pendingOutput.removeFirst();
					if (isEchoOutput())
						Tracer.info(this, System.currentTimeMillis() + ":Processing line from " + aProcessOutput.process + ": " + aProcessOutput.output);
					doAppendProcessProcessedOutput(aProcessOutput.process, aProcessOutput.output + "\n");
				} else {
//					System.out.printf("***** Times:\nCur  %15d\nPend %15d\nDiff %15d\n", aCurrentTime, aTime, aCurrentTime - aTime);
//					System.out.println("***** Pending: \n" + pendingOutput);
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    boolean sorterEchoOutput = false;
    int numEndedProcesses;

    protected void normalOutputProcessing() {
    	numEndedProcesses = 0;
    	try {
			while (pendingOutput.isEmpty()) {
//				System.out.println("waitimng for received output" );
//	    		Tracer.info(this, "Waiting for received output");

				wait();
//				System.out.println("end wait 1");
			}
			

			while (!pendingOutput.isEmpty()) {
				AProcessOutput aProcessOutput = pendingOutput.removeFirst();
				if (aProcessOutput.output == RunnerErrorOrOutStreamProcessor.END_OF_OUPUT) {
					Tracer.info(this, aProcessOutput.process + " ended output");
					numEndedProcesses++;
//					if (aNumEndedProcesses == aNumProcesses) {
//						break;
//					}
					continue;
				}

				if (isEchoOutput()) {
					Tracer.info(this, System.currentTimeMillis() + ": Accumulated received output");

//						Tracer.info(this, System.currentTimeMillis() + "Sorter processing line from " + aProcessOutput.process + ": " + aProcessOutput.output);
				}
				doAppendProcessProcessedOutput(aProcessOutput.process, aProcessOutput.output + "\n");

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
//	boolean noResort;

    @Override
    public void run() {

    	while (true) {
    		synchronized(this) {
    			// not caching the resorting as we may want to change it dynamically
    	    	if (BasicExecutionSpecificationSelector.getBasicExecutionSpecification().isWaitForResort()) {
    	    		resortingOutputProcessing();
    	    	} else {
    	    		normalOutputProcessing();
    	    		if (numProcesses == numEndedProcesses) {
    	    			break;
    	    		}
    	    	}


    		}
    	}
    	Tracer.info (this, "Sorter exiting");
    }

    

    public BasicRunningProject(Project aProject, InputGenerator anOutputBasedInputGenerator, String anInput) {
        this(aProject, anOutputBasedInputGenerator, null, (Map) null);
//		exception = null;
//		output = null;
//		if (aProject != null && aProject instanceof ProjectWrapper) {
//			projectWrapper = (ProjectWrapper) aProject;
//			project = projectWrapper.getProject();
//			outputFileName = project.getOutputFileName();
//			projectOutput = project.getCurrentOutput();
////			input.append(project.getCurrentInput());
//		}
//		outputBasedInputGeneraor = anOutputBasedInputGenerator;
//		if (outputBasedInputGeneraor != null) {
//			outputBasedInputGeneraor.addProcessInputListener(this); // maybe this should be in another class

        input.setLength(0);
        input.append(anInput);

    }

    @Override
	public void start() throws InterruptedException {
        runningState.acquire();
    }

    @Override
	public void end() {
        runningState.release();
    }
    /**
     * The difference between appendProcessOutput and this method is that this one
     * has a \n in it
     */
    @Override
	public synchronized void  appendCumulativeOutput(String newVal) {
    	allReceivedOutputAndErrors.append(newVal);
    	
//    	allReceivedOutputAndErrorLines.add(newVal);
//        if (this.output == null && newVal != null) {
//            this.output = "";
//        }
//        this.output += newVal;
//        Tracer.info(this, System.currentTimeMillis() + ": Appended output:" + newVal);
//        System.out.println("Output:" + output);
//
//
//        outputAndErrors += newVal;

    }

    @Override
	public Map<String, StringBuffer> getProcessOutput() {
        return processToProcessedOutput;
    }
    
    @Override
	public Map<String, List<String>> getProcessOutputLines() {
		
        return processToProcessedOutputLines;
    }
    public static final String ALL_PROCESSES = "All";
	public LinesMatcher getLinesMatcher() {
		Map<String, LinesMatcher> aProcessLinesMatcher = getProcessLinesMatcher();
		if (aProcessLinesMatcher == null) {
			return null;
		}
		return aProcessLinesMatcher.get(BasicProcessRunner.MAIN_ENTRY_POINT);
	}

    @Override
	public Map<String, LinesMatcher> getProcessLinesMatcher() {
    	if (processToProcessedLineMatcher == null) {
//    		List<String> anAllLines = new ArrayList();
    		processToProcessedLineMatcher = new HashMap<>();
    		Set<String> aKeys = processToProcessedOutputLines.keySet();
    		if (aKeys.size() == 0) {
//    			String[] anOutputLines = output.split("\n");
    			String[] anOutputLines = new String[allProcessedOutputLines.size()];
//    			String[] anOutputLines = allProcessedOutputLines.toArray();
    			anOutputLines = allProcessedOutputLines.toArray(anOutputLines);
    			LinesMatcher aLineMatcher =  new ALinesMatcher(anOutputLines);
    			processToProcessedLineMatcher.put(BasicProcessRunner.MAIN_ENTRY_POINT, aLineMatcher);
//    			processToProcessedLineMatcher.put("main", aLineMatcher);

    			return processToProcessedLineMatcher;
    		}
    		for (String aKey:aKeys) {
//    			System.out.println("line matcher for key:" + aKey);
    			List<String> aList = processToProcessedOutputLines.get(aKey);
//    			anAllLines.addAll(aList);
    			String[] anArray = new String[aList.size()];
    			anArray = aList.toArray(anArray);
    			LinesMatcher aLineMatcher = new ALinesMatcher(anArray);
    			processToProcessedLineMatcher.put(aKey, aLineMatcher);
    		}
    		String[] anAllArray = new String[allProcessedOutputLines.size()];
    		anAllArray = allProcessedOutputLines.toArray(anAllArray);

			LinesMatcher anAllLineMatcher = new ALinesMatcher(anAllArray);

    		processToProcessedLineMatcher.put(ALL_PROCESSES, anAllLineMatcher);
    	}		
        return processToProcessedLineMatcher;
    }
    @Override
   	public Map<String, List<String>> getProcessErrorLines() {
   		
           return processToErrorLines;
       }
    
    @Override
   	public Map<String, StringBuffer> getProcessError() {
   		
           return processToErrors;
       }

	protected synchronized void doAppendProcessProcessedOutput(String aProcess, String newVal) {
    	// delete me
//    	System.out.println("+++++ " + Thread.currentThread().getId() + " " + java.lang.management.ManagementFactory.getRuntimeMXBean().getName() + " - " + aProcess + " - " + newVal);
    	
//    	System.out.println("=== BEGIN STACK DUMP ===");
//    	for(Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
//    		System.out.println("*** " + entry.getKey() + " ***");
//    		for(StackTraceElement element : entry.getValue()) {
//    			System.out.println("  " + element);
//    		}
//    	}
    	//		if (aProcess == null) { // it will never be null
//			return;
//		}
//    	System.out.println ("New output " + newVal + " aprocess " + aProcess);
        // wonder why did not have this before
        if (newVal == null) {
            return;
        }
//        System.out.println(("Current thread:" + Thread.currentThread()));
        List<String> aProcessOutputLines = processToProcessedOutputLines.get(aProcess);
        StringBuffer aProcessOutput = processToProcessedOutput.get(aProcess);
//        System.out.println(" process output of " + aProcess + " aProcessOutput " + aProcessOutput);
//		boolean newProcess = processToTranscriptManager.get(aProcess) == null;
//		if (processOutput == null && newVal != null) {
        if (aProcessOutput == null) {
            aProcessOutput = new StringBuffer();
            aProcessOutputLines = new ArrayList();
//            System.out.println(" process output of " + aProcess + " aProcessOutput " + aProcessOutput);

            processToProcessedOutput.put(aProcess, aProcessOutput);
            processToProcessedOutputLines.put(aProcess, aProcessOutputLines);
//            System.out.println(" process output lines of " + aProcess + " aProcessOutputLines " + aProcessOutputLines);


        }
//        String anAnnotatedLine = aProcess + ":" + newVal;
        
        String anAnnotatedLine = null;
        if (aProcess == BasicProcessRunner.MAIN_ENTRY_POINT ) {
        	anAnnotatedLine = newVal;

        } else {
        	anAnnotatedLine = aProcess + ":" + newVal;

        }

        allProcessedOutput.append(anAnnotatedLine);
        aProcessOutput.append(newVal);
//        System.out.println(" new process output  " + aProcessOutput);

        aProcessOutputLines.add(newVal);
        allProcessedOutputLines.add(anAnnotatedLine);
//        System.out.println(" new process output lines " + aProcessOutputLines);

        
//		processToOutput.put(aProcess, aProcessOutput);
        if (outputBasedInputGenerator != null) {
        	
            outputBasedInputGenerator.newOutputLine(aProcess, newVal);
//			if (newProcess) {
//				outputBasedInputGenerator.addProcessName(aProcess);
//			}
        }

//		if (newProcess) {
//			LocalGlobalTranscriptManager aTranscriptManager = new ALocalGlobalTranscriptManager();
//			processToTranscriptManager.put(aProcess, aTranscriptManager );
//			aTranscriptManager.setProcessName(aProcess);
//			
//		}
        appendErrorAndOutput(aProcess, newVal);

    }
	
	public static final String PROCESS_SEPARARTOR = "#@!";
	public static final Pattern timePattern = Pattern.compile("@([0-9]+) ");
	
    @Override
    /**
     * A newVal does not have a \n in it. All processedoutput is resorted
     */
	public void appendProcessOutput(String aProcess, String newVal) {
    	String aProcessName = aProcess == null?BasicProcessRunner.MAIN_ENTRY_POINT:aProcess;
//		System.out.println("Received output from: " + aProcessName + ": " + newVal);
    	System.out.println(newVal);
    	if (BasicRunningProject.isEchoOutput())
    		Tracer.info(this, System.currentTimeMillis() + ": Received output from " + aProcessName + ": " + newVal);
    	allReceivedOutputAndErrorLines.add(newVal); // we do not resort errors, so mix them with received output

    	
    	//    	doAppendProcessOutput(aProcess, newVal);
    	Matcher timeMatcher = timePattern.matcher(newVal);
//    	System.out.println("+++** " + newVal);
//		int anAtIndex = newVal.indexOf('@');
		long aTime = System.nanoTime();
    	if (timeMatcher.find()) {
    		aTime = Long.parseLong(timeMatcher.group(1));
//    		System.out.println("+++** " + aTime);
    	}
//		if (anAtIndex != -1) {
//			int aSpaceIndex = newVal.indexOf(' ', anAtIndex);
//			if (aSpaceIndex > 0) {
//				String aTimeString = newVal.substring(anAtIndex + 1, aSpaceIndex);
//				aTime = Long.parseLong(aTimeString);
//			}
//		}
    	synchronized(this) {
//    		System.out.println("Notifying processor");
//			pendingOutput.addLast(new AProcessOutput(aTime, aProcess, newVal));
			pendingOutput.addLast(new AProcessOutput(aTime, aProcessName, newVal));

			notify();
	    }

	}

    @Override
    /**
     * we are not resorting errors!
     */
	public void appendErrorOutput(String aProcess, String newVal) {
        StringBuffer processErrors = processToErrors.get(aProcess);
        List<String> processErrorLines = processToErrorLines.get(aProcess);
        if (processErrors == null ) {
//        if (processErrors == null && newVal != null) {
            processErrors = new StringBuffer();
            processErrorLines = new ArrayList();
            processToErrors.put(aProcess, processErrors);
            processToErrorLines.put(aProcess, processErrorLines);
        }
        processErrors.append(newVal);
        processErrorLines.add(newVal);

        processToErrors.put(aProcess, processErrors);
        appendErrorAndOutput(aProcess, newVal);
    }

    @Override
	public void appendErrorAndOutput(String aProcess, String newVal) {
        String processOutputAndErrors = processToOutputAndErrors.get(aProcess);
        processOutputAndErrors += newVal;

        processToOutputAndErrors.put(aProcess, processOutputAndErrors);

    }

    @Override
	public void setOutput(String output) {
//        this.output = output;
    	allProcessedOutput.setLength(0);
    	allProcessedOutput.append(output);
    }

    @Override
	public String getOutput() {
//        return output;
    	return allProcessedOutput.toString();
    }

    @Override
	public String getOutputAndErrors() {
//        return outputAndErrors;
    	return allReceivedOutputAndErrors.toString();
    }

    @Override
	public void appendErrorOutput(String anErrorOutput) {
    	allReceivedOutputAndErrors.append(anErrorOutput);
    	allReceivedErros.append(anErrorOutput);
//        if (this.errorOutput == null && anErrorOutput != null) {
//            this.errorOutput = "";
//        }
//        this.errorOutput += anErrorOutput;
//        outputAndErrors += anErrorOutput;

    }

    @Override
	public void setErrorOutput(String errorOutput) {
//        this.errorOutput = errorOutput;
    	allReceivedErros.setLength(0);
    	allReceivedErros.append(errorOutput);
    }

    @Override
	public String getErrorOutput() {
//        return errorOutput;
    	return allReceivedOutputAndErrors.toString();
    }

    @Override
	public void error() {
        this.exception = new NotRunnableException();
        exception.announce();
    }

    public static String featureHeader(String aFeatureName) {
        return FEATURE_HEADER_PREFIX + aFeatureName + FEATURE_HEADER_SUFFIX;
    }

    public static String extractFeatureTranscript(String aFeatureName, String allOutput) {
    	if (aFeatureName.isEmpty()) {
    		return "";
    	}
        int startIndex = allOutput.indexOf(featureHeader(aFeatureName));
        if (startIndex == -1) {
            return "";
        }
        int endIndex;
        int prevIndex = startIndex;
        int nextIndex;
        while (true) {
            nextIndex = allOutput.indexOf(aFeatureName, prevIndex + 1);
            if (nextIndex < 0) {
                endIndex = allOutput.indexOf(FEATURE_HEADER_PREFIX, prevIndex + 1);
                if (endIndex == -1) {
                    endIndex = allOutput.length();
                }
                break;
            } else {
                prevIndex = nextIndex;
            }

        }
        return allOutput.substring(startIndex, endIndex);
    }

    protected StringBuffer transcript = new StringBuffer(); // reusing the buffer
    

   @Override
public void appendCumulativeOutput() {
//        if (projectOutput == null) {
//            return;
//        }
//        String transcript = createFeatureTranscript();
//        projectOutput.append(transcript);
//        if (outputFileName == null) {
//            return;
//        }
//        appendToTranscriptFile(project, transcript);
////		try {
////			FileWriter fileWriter = new FileWriter(outputFileName, true);
////			fileWriter.append(transcript);
////			OverallTranscriptSaved.newCase(null, null, project,  outputFileName, transcript, this);
//////			if (project.getCurrentGradingFeature() != null)
//////			FeatureTranscriptSaved.newCase(null, null, project,  project.getCurrentGradingFeature()., outputFileName, transcript, this);;
////			fileWriter.close();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//
    }
    
    protected void maybeSetCurrentProjectIO() {    
    			String anInput = input.toString();
    			project.setCurrentInput(anInput);
    			project.setCurrentOutput(allProcessedOutput);   		
    	
    }
    
    public static final int PROCESS_TEAM_OUTPUT_OUTPUT_SLEEP_TIME = 5000;
    public static final int PROCESS_OUTPUT_SLEEP_TIME = 2000;

//    protected static Integer processOutputSleepTime = PROCESS_OUTPUT_SLEEP_TIME;
    public static Integer getProcessOutputSleepTime() {
    	return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getProcessOutputSleepTime();
//		return processOutputSleepTime;
	}

	public static void setProcessOutputSleepTime(Integer processOutputSleepTime) {
//		BasicRunningProject.processOutputSleepTime = processOutputSleepTime;
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setProcessOutputWaitTime(processOutputSleepTime);

	}

//	protected static Integer processTeamOutputSleepTime = PROCESS_TEAM_OUTPUT_OUTPUT_SLEEP_TIME;

    
	public static Integer getProcessTeamOutputSleepTime() {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getProcessTeamOutputSleepTime();
//		return processTeamOutputSleepTime;
	}

	public static void setProcessTeamOutputSleepTime(Integer newVal) {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setProcessTeamOutputWaitTime(newVal);
//		BasicRunningProject.processTeamOutputSleepTime = processTeamOutputSleepTime;
	}
	
	public static int getOutputSleepTime() {
		List<String> aProcessTeams = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getProcessTeams();
		if (aProcessTeams != null && !aProcessTeams.isEmpty()) {
			return getProcessTeamOutputSleepTime() ;
		};
		return getProcessOutputSleepTime();
	}

	@Override
	public String await() throws NotRunnableException {
		 if (exception != null) {
	            throw exception;
	        }
	        try {
	            Tracer.info(this, Thread.currentThread() + " Acquring running state");

	            runningState.acquire();
	        } catch (InterruptedException e) {
	            throw new NotRunnableException();
	        }
		try {
            Tracer.info(this, Thread.currentThread() + " Waiting for output sorter to exit");
        	int anOutputSleepTime = getOutputSleepTime();
        	outputSorter.join(anOutputSleepTime);;

//            if (numProcesses == 1) {
//        	outputSorter.join(millis);;
//            }
//            else {
//        	int anOutputSleepTime = getOutputSleepTime();
//            Tracer.info(this, Thread.currentThread() + " sleeping for ms:" + anOutputSleepTime + " waiting for pending output from threads ");
//
//			Thread.sleep(getOutputSleepTime()); // wait for output to be received
//            Tracer.info(this, Thread.currentThread() + " sleeping for ms:" + anOutputSleepTime + " wait ended ");
//        	Tracer.info(this, Thread.currentThread() + " wait ended, returning output ");

//            }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tracer.info(this, "Appending accumulated output");
//        if (exception != null) {
//            throw exception;
//        }
//        try {
//            runningState.acquire();
//        } catch (InterruptedException e) {
//            throw new NotRunnableException();
//        }
        appendCumulativeOutput();
        maybeSetCurrentProjectIO();
//        try {
//            Tracer.info(this, Thread.currentThread() + " waiting for output sorter to exit");
//        	int anOutputSleepTime = getOutputSleepTime();
//        	outputSorter.join(anOutputSleepTime);;
//
////            if (numProcesses == 1) {
////        	outputSorter.join(millis);;
////            }
////            else {
////        	int anOutputSleepTime = getOutputSleepTime();
////            Tracer.info(this, Thread.currentThread() + " sleeping for ms:" + anOutputSleepTime + " waiting for pending output from threads ");
////
////			Thread.sleep(getOutputSleepTime()); // wait for output to be received
////            Tracer.info(this, Thread.currentThread() + " sleeping for ms:" + anOutputSleepTime + " wait ended ");
//        	Tracer.info(this, Thread.currentThread() + " wait ended, returning output ");
//
////            }
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        if (project != null) {
//            project.setCurrentOutput(new StringBuffer(output));
//            project.setCurrentInput(input.toString());
//        }
//        System.out.println("returning output:" + output);
//        return output;
        return allProcessedOutput.toString();
    }
    
    protected void maybeAppendToProjectInput(String anInput) {
//        project.appendCurrentInput(anInput);// this should go, 

    }
    
    protected void maybeTraceInput (String anInput, String aProcessName) {
//    	if (Tracer.isInfo(anInput)) {
//            return;
//        }
//        if (!StaticConfigurationUtils.getTrace()) {
//            return;
//        }
//
//        ConsoleInput consoleInput = ConsoleInput.newCase(anInput, this);
//        String infoString = Tracer.toInfo(consoleInput, consoleInput.getMessage());
//        if (infoString != null) {
//            appendProcessOutput(aProcessName, infoString);
//        }
    }

    
	@Override
    public void newInputLine(String aProcessName, String anInput) {
    	Tracer.info(this, "New input " + anInput + "for " + aProcessName );
    	if (aProcessName != null && processToIn.get(aProcessName) != null) {
        processToIn.get(aProcessName).newInput(anInput + "\n");
    	} else {
    		System.err.println("process name = " + aProcessName + " processToIn " + processToIn);
    	}
    	maybeAppendToProjectInput(anInput);
//        project.appendCurrentInput(anInput);// this should go, 
        if (aProcessName != null && processToInput != null) {
            StringBuffer aProcessStringBuffer = processToInput.get(aProcessName);
            if (aProcessStringBuffer != null) {
                aProcessStringBuffer.append(anInput);
            }
        }
//        input.append(anInput);
       input.append(anInput + "\n"); // need to put new line to it
       maybeTraceInput(anInput, aProcessName);
//        // why would this be info ever?
//        if (Tracer.isInfo(anInput)) {
//            return;
//        }
//        if (!StaticConfigurationUtils.getTrace()) {
//            return;
//        }
//
//        ConsoleInput consoleInput = ConsoleInput.newCase(anInput, this);
//        String infoString = Tracer.toInfo(consoleInput, consoleInput.getMessage());
//        if (infoString != null) {
//            appendProcessOutput(aProcessName, infoString);
//        }

    }

    @Override
	public void terminateTeam() {
        Set<String> aProcesses = nameToProcess.keySet();
        for (String aProcess : aProcesses) {
            terminateProcess(aProcess);
//			try {
//				processToOut.get(aProcess).getSemaphore().acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				processToErr.get(aProcess).getSemaphore().acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			TimedProcess timedProcess = nameToProcess.get(aProcess);
//			timedProcess.getProcess().destroy();
        }
        terminateRunner();
		// try {
        // // Wait for the output to finish
        // outputSemaphore.acquire();
        // errorSemaphore.acquire();
        // runner.end();
        // } catch (Exception e) {
        // e.printStackTrace();
        // Tracer.error(e.getMessage());
        // runner.error();
        // runner.end();
        // }
    }

    void terminateRunner() {
        try {
            // Wait for the output to finish
//			acquireIOLocks();
            releaseTeamLocks();
        } catch (Exception e) {
            e.printStackTrace();
            Tracer.error(e.getMessage());
            error();
            releaseTeamLocks();
//			runner.end();
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
        end();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }

	@Override
    public void inputTerminated(String aProcessName) {
//		Tracer.info(this, "Terminating input");
        terminateProcess(aProcessName);
//		processToIn.get(aProcessName).terminateInput();

    }

    @Override
	public void terminateProcess(String aProcess) {
        Tracer.info(this, System.currentTimeMillis() +":Terminating:" + aProcess);
//
        try {
            processToOut.get(aProcess).getSemaphore().acquire(); // this is deadlocking, need to debug
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

    @Override
	public RunnerInputStreamProcessor getProcessIn(String aProcessName) {
        return processToIn.get(aProcessName);
    }

    // the mapping could be passed to this object rather than the individual processIn's

    @Override
	public void setProcessIn(String aProcess, RunnerInputStreamProcessor processIn) {
        processToIn.put(aProcess, processIn);
    }

    @Override
	public RunnerErrorOrOutStreamProcessor getProcessOut(String aProcessName) {
        return processToOut.get(aProcessName);
    }
    
    

    // the mapping could be passed to this object rather than the individual processIn's

    @Override
	public void setProcessOut(String aProcess, RunnerErrorOrOutStreamProcessor newVal) {
        processToOut.put(aProcess, newVal);
    }

    @Override
	public RunnerErrorOrOutStreamProcessor getProcessErr(String aProcessName) {
        return processToErr.get(aProcessName);
    }

    // the mapping could be passed to this object rather than the individual processIn's

    @Override
	public void setProcessErr(String aProcess, RunnerErrorOrOutStreamProcessor newVal) {
        processToErr.put(aProcess, newVal);
    }

    @Override
	public TimedProcess getProcess(String aProcessName) {
        return nameToProcess.get(aProcessName);
    }

    @Override
	public void setProcess(String aProcessName, TimedProcess aTimedProcess) {
        nameToProcess.put(aProcessName, aTimedProcess);
    }

    @Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	@Override
	public void addDependentThread(Thread aThread) {
		dependentThreads.add(aThread);
	}
	
	@Override
	public void addDependentCloseable(Closeable aCloseable) {
		dependentCloseables.add(aCloseable);
	}

    @Override
	public void destroy() {
    	setDestroyed(true);
    	currentProcess.destroy();
    	for (Thread dependentThread:dependentThreads) {
    		dependentThread.interrupt();
    	}
    	for (Closeable closeable:dependentCloseables) {
    		try {
				closeable.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	end();
    }
    @Override
	public TimedProcess getCurrentTimedProcess() {
		return currentProcess;
	}

	@Override
	public void setCurrentTimeProcess(TimedProcess currentProcess) {
		this.currentProcess = currentProcess;
	}


	/**
	 * 
	 * @return time in nanoseconds
	 */
    public static long getTimeToWaitForConcurrentOutput() {
		return timeToWaitForConcurrentOutput;
	}

    /**
     * 
     * @param timeToWaitForConcurrentOutput time in nanoseconds
     */
	public static void setTimeToWaitForConcurrentOutput(long timeToWaitForConcurrentOutput) {
		BasicRunningProject.timeToWaitForConcurrentOutput = timeToWaitForConcurrentOutput;
	}
	public static boolean isEchoOutput() {
		return echoOutput;
	}

	public static void setEchoOutput(boolean echoOutput) {
		BasicRunningProject.echoOutput = echoOutput;
	}
}
