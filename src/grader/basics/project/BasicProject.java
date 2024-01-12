package grader.basics.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.checkstyle.CheckStyleInvoker;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.GradingMode;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.project.source.ABasicTextManager;
import grader.basics.project.source.BasicTextManager;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.trace.BinaryFolderMade;
import grader.basics.trace.BinaryFolderNotFound;
import grader.basics.trace.ProjectFolderNotFound;
import grader.basics.trace.SourceFolderAssumed;
import grader.basics.trace.SourceFolderNotFound;
import grader.basics.util.DirectoryUtils;
import grader.basics.util.Option;
import unc.symbolTable.SymbolTable;
import util.misc.Common;
import util.pipe.InputGenerator;
import util.trace.TraceableLog;
import util.trace.TraceableLogFactory;
import util.trace.Tracer;

/**
 * A "standard" project. That is, an IDE-based java project.
 */
public class BasicProject implements Project {
	public static final String EMPTY_STRING = "";
	protected boolean isInfinite;
	protected File projectFolder;
	protected File sourceFolder;
	protected Option<ClassesManager> classesManager;
	protected TraceableLog traceableLog;
	protected boolean noSrc;
	protected String sourceFilePattern = null;
	protected File buildFolder;
	protected File objectFolder;
	protected BasicTextManager textManager;
	protected String input = "";
	protected StringBuffer output;
//    protected SakaiProject project;

	/**
	 * Basic constructor
	 *
	 * @param aDirectory The location of the project
	 * @param name       The name of the project, such as "Assignment1"
	 * @throws FileNotFoundException
	 */
//    public StandardProject(File directory, String name) throws FileNotFoundException {
//        // Find the folder. We could be there or it could be in a different folder
//    	if (directory == null) return;
//        Option<File> src = DirectoryUtils.locateFolder(directory, "src");
//        if (src.isEmpty()) {
//          throw new FileNotFoundException("No src folder");
////
////        	noSrc = true;
////        	sourceFolder = directory;
////        	this.directory = sourceFolder;
//        } else {
//        sourceFolder = src.get();
//        this.directory = src.get().getParentFile();
//        }
//
//        try {
//            File sourceFolder = new File(this.directory, "src");
//            File buildFolder = getBuildFolder("main." + name);
//            classesManager = Option.apply((ClassesManager) new ProjectClassesManager(buildFolder, sourceFolder));
//        } catch (Exception e) {
//            classesManager = Option.empty();
//        }
//
//        // Create the traceable log
//        traceableLog = TraceableLogFactory.getTraceableLog();
//
//    }
//	public StandardProject(SakaiProject project, File aDirectory, String name) throws FileNotFoundException {
//		
//	}
	protected void setProject(Object aProject) {

	}

	public BasicProject(String aSourceFilePattern) throws FileNotFoundException {
		this(null, new File("."), null, aSourceFilePattern);
	}

	protected void searchForSourceAndProjectFolder() throws FileNotFoundException {

//        Option<File> src = DirectoryUtils.locateFolder(aDirectory, "src");
		Option<File> src = DirectoryUtils.locateFolder(projectFolder, Project.SOURCE);
		Tracer.info(this, "located src folder:" + src.get());

		if (src.isEmpty()) {
			Tracer.info(this, "src folder is empty:" + src);

			SourceFolderNotFound.newCase(projectFolder.getAbsolutePath(), this).getMessage();

			Set<File> sourceFiles = DirectoryUtils.getSourceFiles(projectFolder, sourceFilePattern);
			if (!sourceFiles.isEmpty()) {
				File aSourceFile = sourceFiles.iterator().next();
				System.out.println("Found a source file:" + aSourceFile);
				sourceFolder = aSourceFile.getParentFile(); // assuming no packages!
//				System.out.println("Assuming src folder is:" + sourceFolder);

				Tracer.info(this, "Assuming src folder is:" + sourceFolder);
				if (!projectFolder.equals(sourceFolder)) {
					this.projectFolder = sourceFolder.getParentFile();
				}
				SourceFolderAssumed.newCase(sourceFolder.getAbsolutePath(), this);
			} else {
				ProjectFolderNotFound.newCase(projectFolder.getAbsolutePath(), this).getMessage();
				throw new FileNotFoundException("No source files found");
			}
			noSrc = true;
//                throw new FileNotFoundException("No src folder");
//        	sourceFolder = aDirectory;
//        	this.directory = sourceFolder;
		} else {
			sourceFolder = src.get();
			this.projectFolder = src.get().getParentFile();
		}
		Tracer.info(this, "passing to text manager, folder:" + sourceFolder);
		textManager = new ABasicTextManager(sourceFolder);
	}

//    public static File getLeafBuildFolder (File aRootBuildFolder, String a) {
//    	if (aRootBuildFolder == null || !aRootBuildFolder.isDirectory()) {
//    		return null;
//    	}
//    	for (File aFile:aRootBuildFolder.listFiles()) {
//    		if (aFile.getName().startsWith(aRelativeFileName) {
//    			return aFile;
//    		}    		
//    	}
//    	
//    }
	public static File getASourceFile(File aSourceFolder) {
		return getAFileWithSuffix(aSourceFolder, BasicLanguageDependencyManager.getSourceFileSuffix());
//    	if (aSourceFolder == null || !aSourceFolder.isDirectory()) {
//    		return null;
//    	}
//    	for (File aFile:aSourceFolder.listFiles()) {
//    		if (aFile.getName().endsWith(BasicLanguageDependencyManager.getSourceFileSuffix())) {
//    			return aFile;
//    		}    		
//    	}
//    	for (File aFile:aSourceFolder.listFiles()) {
//    		if (aFile.isDirectory()) {
//    			File retVal = getASourceFile(aFile);
//    			if (retVal != null) {
//    				return retVal;
//    			}
//    		}    		   		
//    	}
//    	return null;
	}

	public static File getAFileWithSuffix(File aSourceFolder, String aSuffix) {
		if (aSourceFolder == null || !aSourceFolder.isDirectory()) {
			return null;
		}
		for (File aFile : aSourceFolder.listFiles()) {
			if (aFile.getName().endsWith(aSuffix)) {
				return aFile;
			}
		}
		for (File aFile : aSourceFolder.listFiles()) {
			if (aFile.isDirectory()) {
				File retVal = getAFileWithSuffix(aFile, aSuffix);
				if (retVal != null) {
					return retVal;
				}
			}
		}
		return null;
	}

	public static String getRelativeFolderName(File aFile, String aRelativeFileName) {
		int anEndIndex = aRelativeFileName.indexOf(File.separator + aFile.getName());
		return aRelativeFileName.substring(0, anEndIndex);
	}

	public static String getRelativeFileNameWithoutSourceSuffix(String aRelativeFileName) {
		if (aRelativeFileName == null) {
			return null;
		}
		return aRelativeFileName.replace(BasicLanguageDependencyManager.getSourceFileSuffix(), "");

	}

	public static String getRelativeFileName(File aSourceFolder, File aSourceFile) {
		try {
			int aRelativeFileStart = aSourceFolder.getCanonicalPath().length() + 1;
			String aRelativeFileName = aSourceFile.getCanonicalPath().substring(aRelativeFileStart);
			return aRelativeFileName;

		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

	}

	public static String getClassName(File aSourceFolder, File aSourceFile) {
		try {
			String aRelativeFileName = getRelativeFileName(aSourceFolder, aSourceFile);
			if (aRelativeFileName == null) {
				return null;
			}
//			int aRelativeFileStart = aSourceFolder.getCanonicalPath().length() + 1;
//			String aRelativeFileName = aSourceFile.getCanonicalPath().substring(aRelativeFileStart);
			String aRelativeFileNameWithoutSourceSuffix = getRelativeFileNameWithoutSourceSuffix(aRelativeFileName);
			// aRelativeFileName.replace(BasicLanguageDependencyManager.getSourceFileSuffix(),
			// "");
			String aClassName = aRelativeFileNameWithoutSourceSuffix.replace(File.separator, ".");
			return aClassName;

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}

	// rewriting Josh's code
	// going back to Josh';s code
	public BasicProject(Object aProject, File aDirectory, String name, String aSourceFilePattern)
			throws FileNotFoundException {
		// Find the folder. We could be there or it could be in a different folder
//    	File anActualDirectory = aDirectory;
//    	boolean aNeedToSearchForProject = true;
		if (aDirectory == null) {
//    		String aLocation = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getGradableProjectLocation();
//            if (aLocation != null) {
//            	aNeedToSearchForProject = false;
//            } else {
//            	aLocation = ".";
//            }
//            aDirectory = new File(aLocation);
			throw new FileNotFoundException("No directory given");
		}

		if (aSourceFilePattern == null) {
			sourceFilePattern = EMPTY_STRING;
		} else {
			sourceFilePattern = aSourceFilePattern;
		}
		setProject(aProject);

		// will do this in standardproject
//    	project = aProject;
//    	BasicConfigurationManagerSelector.getConfigurationManager().createProjectConfiguration(aDirectory);
//    	BasicConfigurationManagerSelector.getConfigurationManager().setProjectDirectory(aDirectory);

		projectFolder = aDirectory;
		File[] aFiles = aDirectory.listFiles();
		if (aFiles.length == 1 && aFiles[0].getName().equals(aDirectory.getName())) {
			// zipping issue;
			projectFolder = aFiles[0];
		}

//        Option<File> src = DirectoryUtils.locateFolder(aDirectory, "src");
//        Option<File> src = DirectoryUtils.locateFolder(aDirectory, Project.SOURCE);
//
//        if (src.isEmpty()) {
//        	SourceFolderNotFound.newCase(aDirectory.getAbsolutePath(), this).getMessage();
//
//        	Set<File> sourceFiles = DirectoryUtils.getSourceFiles(aDirectory, sourceFilePattern);
//        	if (!sourceFiles.isEmpty()) {
//                    File aSourceFile = sourceFiles.iterator().next();
//                    sourceFolder = aSourceFile.getParentFile(); // assuming no packages!
//                    this.directory = sourceFolder.getParentFile();
//                    SourceFolderAssumed.newCase(sourceFolder.getAbsolutePath(), this);
//        	} else {
//                    ProjectFolderNotFound.newCase(aDirectory.getAbsolutePath(), this).getMessage();
//                    throw new FileNotFoundException("No source files found");
//        	}
//        	noSrc = true;
////                throw new FileNotFoundException("No src folder");
////        	sourceFolder = aDirectory;
////        	this.directory = sourceFolder;
//        } else {
//            sourceFolder = src.get();
//            this.directory = src.get().getParentFile();
//        }
		searchForSourceAndProjectFolder();

		try {
//            File sourceFolder = new File(this.directory, "src");
			String aBinaryFolderLocation = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
					.getBinaryFolderLocation();
			if (aBinaryFolderLocation != null) {
				Option<File> anOption = DirectoryUtils.locateFolder(projectFolder, aBinaryFolderLocation);
				if (anOption != null && !anOption.isEmpty()) {
					buildFolder = anOption.get();
					Tracer.info(this, "Found build folder:" + buildFolder);
				}
			}
			if (buildFolder == null) {
				if (!BasicLanguageDependencyManager.hasBinaryFolder()) {
					buildFolder = sourceFolder;
					Tracer.info(this, "assuming build folder is source folder:");

				} else {
					String aSourceClassName = "main." + name;
					File aSourceFile = getASourceFile(sourceFolder);
					if (aSourceFile != null) {
						String aClassName = getClassName(sourceFolder, aSourceFile);
						if (aClassName != null) {
							aSourceClassName = aClassName;
						}
					}
					buildFolder = getBuildFolder(aSourceClassName);
					Tracer.info(this, "Assuming build folder is:" + buildFolder);

				}

			}

//			String aSourceClassName = "main." + name;
//			File aSourceFile = getASourceFile(sourceFolder);
//			if (aSourceFile != null) {
//				String aClassName = getClassName(sourceFolder, aSourceFile);
//				if (aClassName != null) {
//					aSourceClassName = aClassName;
//				}
//			}
//
//			if (buildFolder == null) {
////        		 buildFolder = getBuildFolder("main." + name);
//				buildFolder = getBuildFolder(aSourceClassName);
//
//			}
			String anObjectFolderLocation = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
					.getObjectFolderLocation();
			if (anObjectFolderLocation != null) {
				Option<File> anOption = DirectoryUtils.locateFolder(projectFolder, anObjectFolderLocation);
				if (anOption != null && !anOption.isEmpty()) {
					objectFolder = anOption.get();
				}
			}
			if (objectFolder == null) {
				objectFolder = buildFolder;
			}
//			if (BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getLoadClasses()) {
//         	CurrentProjectHolder.setProject(this); // so that classesManager can find it
				Tracer.info(this, "Creating classes manager");
				classesManager = createClassesManager(buildFolder);
//			}

		} catch (Exception e) {
			e.printStackTrace();
			classesManager = Option.empty();
		}

		// Create the traceable log
		traceableLog = TraceableLogFactory.getTraceableLog();
	}

	protected Option<ClassesManager> createClassesManager(File buildFolder) throws ClassNotFoundException, IOException {
//        classesManager = Option.apply((ClassesManager) new ProjectClassesManager(project, buildFolder, sourceFolder));

		return Option.apply((ClassesManager) new BasicProjectClassesManager(this, null, buildFolder, sourceFolder,
				sourceFilePattern));

	}

	protected Option<File> out;
	protected Option<File> bin;
	protected Map<String, File> preferredClassToBuildFolder = new HashMap(); // wonder if it will ever have more than
																				// one entry

	/**
	 * Caching version of Josh's code This figures out where the build folder is,
	 * taking into account variations due to IDE
	 *
	 * @param preferredClass The name of the class that has the main method, such as
	 *                       "main.Assignment1"
	 * @return The build folder
	 * @throws FileNotFoundException
	 */
	public File getBuildFolder(String preferredClass) throws FileNotFoundException {
		File retVal = preferredClassToBuildFolder.get(preferredClass);
		if (retVal == null) {
			retVal = searchBuildFolder(preferredClass);
			if (retVal == null)
				return null;
			preferredClassToBuildFolder.put(preferredClass, retVal);
		}
		return retVal;
	}

	protected File searchBuildFolder(String preferredClass) throws FileNotFoundException {
		for (String aBinary : Project.BINARIES) {
			bin = DirectoryUtils.locateFolder(projectFolder, aBinary);
			if (bin != null && !bin.isEmpty())
				break;
		}
//        if (out == null)
//  	  out = DirectoryUtils.locateFolder(directory, Project.BINARY_2);
//
//
//      
//
//        if (bin == null)
//      bin = DirectoryUtils.locateFolder(directory,  Project.BINARY_0); // just to handle grader itself, as it has execuot.c
//      if (bin.isEmpty())
//      	bin = DirectoryUtils.locateFolder(directory,  Project.BINARY);

		// If there is no 'out' or 'bin' folder then give up
//      if (out.isEmpty() && bin.isEmpty()) {
		if (bin == null || bin.isEmpty()) {

			if (noSrc) {
				return sourceFolder;
			}
//          throw new FileNotFoundException();
			BinaryFolderNotFound.newCase(projectFolder.getAbsolutePath(), this);
			File retVal = new File(projectFolder, Project.BINARY);
			retVal.mkdirs();
//      	project.getClassLoader().setBinaryFileSystemFolderName(retVal.getAbsolutePath());
			BinaryFolderMade.newCase(retVal.getAbsolutePath(), this);
			return retVal.getAbsoluteFile();

		} else {
			// There can be more folders under it, so look around some more
			// But first check the class name to see what we are looking for
			File dir = null;
//          if (out.isDefined()) {
//              dir = out.get();
//          }
			if (bin.isDefined()) {
				dir = bin.get();
			}
			if (preferredClass == null || preferredClass.isEmpty()) {
				return dir;
			}

			if (preferredClass.contains(".")) {
				Option<File> packageDir = DirectoryUtils.locateFolder(dir, preferredClass.split("\\.")[0]);
				if (packageDir.isDefined()) {
					return packageDir.get().getParentFile();
				} else {
					return dir;
				}
			} else {
				return dir;
			}
		}
	}

	/**
	 * This figures out where the build folder is, taking into account variations
	 * due to IDE
	 *
	 * @param preferredClass The name of the class that has the main method, such as
	 *                       "main.Assignment1"
	 * @return The build folder
	 * @throws FileNotFoundException
	 */
	@Deprecated
	public File getNonCachingBuildFolder(String preferredClass) throws FileNotFoundException {
//        Option<File> out = DirectoryUtils.locateFolder(directory, "out");
		Option<File> anOut = DirectoryUtils.locateFolder(projectFolder, Project.BINARY_2);
//        if (out.isEmpty())
//        	out = DirectoryUtils.locateFolder(directory, Project.BINARY_0);

//        Option<File> bin = DirectoryUtils.locateFolder(directory, "bin");
		Option<File> aBin = DirectoryUtils.locateFolder(projectFolder, Project.BINARY_0); // just to handle grader
																							// itself, as it has
																							// execuot.c
		if (aBin.isEmpty())
//        Option<File> bin = DirectoryUtils.locateFolder(directory,  Project.BINARY);
			aBin = DirectoryUtils.locateFolder(projectFolder, Project.BINARY);

		// If there is no 'out' or 'bin' folder then give up
		if (anOut.isEmpty() && aBin.isEmpty()) {
			if (noSrc) {
				return sourceFolder;
			}
//            throw new FileNotFoundException();
			BinaryFolderNotFound.newCase(projectFolder.getAbsolutePath(), this);
			File retVal = new File(projectFolder, Project.BINARY);
			retVal.mkdirs();
//        	project.getClassLoader().setBinaryFileSystemFolderName(retVal.getAbsolutePath());
			BinaryFolderMade.newCase(retVal.getAbsolutePath(), this);
			return retVal.getAbsoluteFile();

		} else {
			// There can be more folders under it, so look around some more
			// But first check the class name to see what we are looking for
			File dir = null;
			if (anOut.isDefined()) {
				dir = anOut.get();
			}
			if (aBin.isDefined()) {
				dir = aBin.get();
			}
			if (preferredClass == null || preferredClass.isEmpty()) {
				return dir;
			}

			if (preferredClass.contains(".")) {
				Option<File> packageDir = DirectoryUtils.locateFolder(dir, preferredClass.split("\\.")[0]);
				if (packageDir.isDefined()) {
					return packageDir.get().getParentFile();
				} else {
					return dir;
				}
			} else {
				return dir;
			}
		}
	}

	@Override
	public TraceableLog getTraceableLog() {
		return traceableLog;
	}

	@Override
	public RunningProject start(String input) throws NotRunnableException {
//        return new ReflectionRunner(this).run(input);
		return null; // should not be called
	}

	@Override
	public RunningProject launch(String input) throws NotRunnableException {
		return new BasicProcessRunner(this).run(input);
	}

//    @Override
//    public RunningProject start(String input, int timeout) throws NotRunnableException {
//        return new ReflectionRunner(this).run(input, timeout);
//    }

	@Override
	public RunningProject launch(InputGenerator anOutputBasedInputGenerator, String input, int timeout)
			throws NotRunnableException {
		return new BasicProcessRunner(this).run(anOutputBasedInputGenerator, input, timeout);
	}

	@Override
	public RunningProject launch(InputGenerator anOutputBasedInputGenerator, Map<String, String> aProcessToInput,
			int timeout) throws NotRunnableException {
		return new BasicProcessRunner(this).run(anOutputBasedInputGenerator, aProcessToInput, timeout);
	}

	@Override
	public RunningProject launch(String input, int timeout) throws NotRunnableException {
		return new BasicProcessRunner(this).run(input, timeout);
	}

	@Override
	public RunningProject launch(String input, String[] anArgs, int timeout) throws NotRunnableException {
		return new BasicProcessRunner(this).run(input, anArgs, timeout);
	}

//
	@Override
	public RunningProject launchInteractive() throws NotRunnableException {
		return null; // should not be called
//    	ARunningProject retVal = new InteractiveConsoleProcessRunner(this).run("");
////    	retVal.createFeatureTranscript();
//    	return retVal;
////        return new InteractiveConsoleProcessRunner(this).run("");
	}
//    @Override
//    public RunningProject launchInteractive(String[] args) throws NotRunnableException {
//    	ARunningProject retVal = new InteractiveConsoleProcessRunner(this).run("", args);
////    	retVal.createFeatureTranscript();
//    	return retVal;
////        return new InteractiveConsoleProcessRunner(this).run("");
//    }

	@Override
	public Option<ClassesManager> getClassesManager() {
		return classesManager;
	}

	@Override
	public File getSourceFolder() {
		return sourceFolder;
	}

	public String toString() {
		return sourceFolder + " :" + sourceFilePattern;
	}

	public static void main(String[] args) {
		try {
			BasicGradingEnvironment.get().setLoadClasses(true);
//			Project aProject = new BasicProject(null, new File("."), null);
			Project anAllCorrectProject = new BasicProject(null, new File("."), null, "allcorrect");

			Class anAllCorrectClass = BasicProjectIntrospection.findClass(anAllCorrectProject, "ACartesianPoint");
			System.out.println("An all correct" + anAllCorrectClass);
			Project aWrongAngleProject = new BasicProject(null, new File("."), null, "wrongangle");

			Class aWrongAngleClass = BasicProjectIntrospection.findClass(anAllCorrectProject, "ACartesianPoint");
			System.out.println("A wrong" + aWrongAngleClass);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isInfinite() {
		return isInfinite;
	}

	@Override
	public void setInfinite(boolean newVal) {
		isInfinite = newVal;
	}

	@Override
	public File getProjectFolder() {
		return projectFolder;
	}

	@Override
	public File getBuildFolder() {
		return buildFolder;
	}

	@Override
	public File getObjectFolder() {
		return objectFolder;
	}

	@Override
	public List<File> getSourceFiles() {
		return getTextManager().getSourceFiles();
	}

	@Override
	public String getSource() {
		return getTextManager().getAllSourcesText().toString();
//		return project.
//				getClassesTextManager().getEditedAllSourcesText(project.getSourceFileName());
//    	return null;

//		return Common.toText(aRunningProject.getProject().getSourceFileName()).toString();
	}

	@Override
	public BasicTextManager getTextManager() {
		return textManager;
	}

	public String getAssignmentDataFolderName() {
		return BasicGradingEnvironment.get().getDefaultAssignmentsDataFolderName();
	}

	@Override
	public String getCurrentInput() {
		return input;
	}

	@Override
	public void setCurrentInput(String currentInput) {
		input = currentInput;
	}

	@Override
	public StringBuffer getCurrentOutput() {
		return output;
	}

	@Override
	public void clearOutput() {
		if (output != null) {
			output.setLength(0);
		}
	}

	@Override
	public void setCurrentOutput(StringBuffer currentOutput) {
		output = currentOutput;
	}

	protected boolean cannotInitializeCheckstyle = false;
	protected File checkstyleOutFolder = null;

	protected File valgrindTraceFolder = null;
	protected File valgrindConfigurationFolder = null;

//	boolean checkCheckstyle = true;
	static boolean checkCheckstyleFolder = true;

	static boolean checkEclipseFolder = false;

	static boolean checkEclipseOrCheckstyleFolder = false;

	public static boolean isCheckEclipseOrEclipseFolder() {
		return checkEclipseOrCheckstyleFolder;
	}

	public static void setCheckEclipseOrEclipseFolder(boolean checkEclipseOrEclipseFolder) {
		BasicProject.checkEclipseOrCheckstyleFolder = checkEclipseOrEclipseFolder;
	}

	public static boolean isCheckEclipseFolder() {
		return checkEclipseFolder;
	}

	public static void setCheckEclipseFolder(boolean checkEclipseFolder) {
		BasicProject.checkEclipseFolder = checkEclipseFolder;
	}

	public static boolean isCheckCheckstyleFolder() {
		return checkCheckstyleFolder;
	}

	public static void setCheckCheckstyleFolder(boolean checkCheckstyleFolder) {
		BasicProject.checkCheckstyleFolder = checkCheckstyleFolder;
	}

//	protected boolean eclipseFolderExists = true;
	public File getCheckstyleOutFolder() {
		if (checkstyleOutFolder == null) {
			File aProjectFolder = getProjectFolder();
			if (aProjectFolder == null) {
				projectFolder = new File(".");
			}
			File anEclipseFolder = new File(getProjectFolder().getAbsoluteFile() + "/Logs/Eclipse");
			File aCheckStyleAllFile = new File(
					getProjectFolder().getAbsoluteFile() + "/Logs/LocalChecks/CheckStyle_All.csv");
			File aReturnFolder = new File(getProjectFolder().getAbsoluteFile() + "/Logs/LocalChecks/");

			if (isCheckEclipseOrEclipseFolder() && (anEclipseFolder.exists() || aCheckStyleAllFile.exists())) {
				if (!aReturnFolder.exists()) {
					aReturnFolder.mkdir();
				}
				checkstyleOutFolder = aReturnFolder;
				return aReturnFolder;
			}

			// if (!anEclipseFolder.exists() ) {

			if (!anEclipseFolder.exists() && !GradingMode.getGraderRun() && isCheckEclipseFolder()) {
//				eclipseFolderExists = false;
				System.err.println("File does not exist:" + anEclipseFolder);
				System.err.println("Please run the checkstle plugin on your project");

				return null;
//			checkstyleOutFolder null;
			}
			if (!aCheckStyleAllFile.exists() && !GradingMode.getGraderRun() && isCheckCheckstyleFolder()) {
//				System.err.println("File does not exist:" + aCheckStyleAllFile);
				System.err.println("Please run the checkstle plugin on your project");

				return null;

//			return null;
			}
			if (!aCheckStyleAllFile.exists() && !GradingMode.getGraderRun()) {
				checkstyleOutFolder = new File(".");
				return checkstyleOutFolder;
			}

			checkstyleOutFolder = aCheckStyleAllFile.getParentFile();
		}
		return checkstyleOutFolder;

	}

	public File getValgrindTraceFolder() {
		if (valgrindTraceFolder == null) {
			String aTraceFolder = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
					.getValgrindTraceDirectory();
			if (aTraceFolder == null) {
				aTraceFolder = "Logs/Valgrind";
			}
			valgrindTraceFolder = new File(getProjectFolder().getAbsoluteFile() + "/" + aTraceFolder);
			if (!valgrindTraceFolder.exists()) {
				valgrindTraceFolder.mkdir();
			}
		}
		return valgrindTraceFolder;

	}

	public File getValgrindConfigurationFolder() {
		if (valgrindConfigurationFolder == null) {
			String aFolder = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
					.getValgrindConfigurationDirectory();
			if (aFolder == null) {
				aFolder = "Logs/Valgrind";
			}
			valgrindConfigurationFolder = new File(getProjectFolder().getAbsoluteFile() + "/" + aFolder);
			if (!valgrindConfigurationFolder.exists()) {
				valgrindConfigurationFolder.mkdir();
			}
		}
		return valgrindConfigurationFolder;

	}

	public static final String DEFAULT_CHECK_STYLE_FILE_PREFIX = "checks";

	public static final String DEFAULT_CHECK_STYLE_FILE_SUFFIX = ".txt";
	@Override
	public  File getCheckstyleConfigurationFile() {
		String aSpecifiedConfigurationFileName = BasicExecutionSpecificationSelector
				.getBasicExecutionSpecification().getCheckStyleConfiguration();
		String aConfigurationFileName = aSpecifiedConfigurationFileName;
		File aSpecifiedConfigurationFile = new File(aSpecifiedConfigurationFileName);
		File retVal = aSpecifiedConfigurationFile;
		if (!aSpecifiedConfigurationFile.exists()) {
//		String aConfigurationFileName = findCheckstyleConfigurationParentFolder() + "/"
//				+ BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfiguration();
			aConfigurationFileName = findCheckstyleConfigurationParentFolder() + "/"
					+ aSpecifiedConfigurationFileName;
			retVal = new File(aConfigurationFileName);
			if (!retVal.exists()) {
				System.err.println("Did not find file:" + retVal.getAbsolutePath());
				System.err.println("Download " + retVal.getName() + " and add it to directory " + retVal.getParent());
				return null;
			} 
		}
		return retVal;
	}
	public static String getCheckstyleConfigurationPrefix() {
		String retVal = EMPTY_STRING;
		String aCheckstyleConfigurationName = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.getCheckStyleConfiguration();
		File aCheckstyleConfigurationFile = new File(aCheckstyleConfigurationName);
		String aLocalConfigurationFileName = aCheckstyleConfigurationName;
		if (aCheckstyleConfigurationFile.exists()) {
			aLocalConfigurationFileName = aCheckstyleConfigurationFile.getName();
		}
		if (aCheckstyleConfigurationName != null) {
			String[] aPair = aLocalConfigurationFileName.split("\\.");
			retVal = aPair[0] + "_";
		}
		return retVal;
	}

	public String createLocalCheckStyleOutputFileName() {
		String aPrefix = getCheckstyleConfigurationPrefix();
//		return getCheckstyleConfigurationPrefix() + DEFAULT_CHECK_STYLE_FILE_PREFIX + DEFAULT_CHECK_STYLE_FILE_SUFFIX;
		return aPrefix + DEFAULT_CHECK_STYLE_FILE_PREFIX + DEFAULT_CHECK_STYLE_FILE_SUFFIX;

	}

	protected boolean checkForDefaultFolder() {
		return true;
	}

	protected String getDefaultFolder() {
		File aParentFolder = getCheckstyleOutFolder();
		if (aParentFolder == null) {
			return null;
		}
		return aParentFolder.getAbsolutePath();
	}

	public String createFullCheckStyleOutputFileName() {

		String aParentFolder = findCheckstyleOutputParentFolder();
		if (aParentFolder == null) {
			return null;
		}
		if (!GradingMode.getGraderRun() && isCheckCheckstyleFolder()) {
			String aLogFileName = aParentFolder + "/" + "CheckStyle_All.csv";
			File aLogFile = new File(aLogFileName);
			if (!aLogFile.exists()) {
				return null;
			}
		}

//		return createLocalCheckStyleFileName();
		String aLocalFileName = createLocalCheckStyleOutputFileName();
		return aParentFolder + "/" + aLocalFileName;
//		return aParentFolder + "/" + createLocalCheckStyleOutputFileName();
	}

	protected String checkStyleFileName = null;
	protected File valgrindTraceFile = null;

	public String getCheckStyleOutputFileName() {
		if (cannotInitializeCheckstyle) {
			return null;
		}
		if (checkStyleFileName == null) {
			checkStyleFileName = createFullCheckStyleOutputFileName();
			if (checkStyleFileName == null) {
				cannotInitializeCheckstyle = true;
				System.err.println(
						"Could not initialize checkstyle. Please make sure you have installed the plugin and run checkstyle on the project ");
			}
		}
		return checkStyleFileName;
	}

	////
	public static final String DEFAULT_GENERATED_CHECK_STYLE_FILE_PREFIX = "generated_configuration";

	public static final String DEFAULT_DEFAULT_CHECK_STYLE_FILE_SUFFIX = ".xml";

	public String createGeneratedCheckStyleFileName() {
		return getCheckstyleConfigurationPrefix() + DEFAULT_GENERATED_CHECK_STYLE_FILE_PREFIX
				+ DEFAULT_DEFAULT_CHECK_STYLE_FILE_SUFFIX;
	}

	protected String getCheckstyleDefaultFolder() {
		File aDefaultFolder = getCheckstyleOutFolder();
		if (aDefaultFolder == null) {
			return null;
		}
		String aDefaultFolderPath = aDefaultFolder.getAbsolutePath();
		if (checkForDefaultFolder() && aDefaultFolderPath == null) {
			return null;
		}
		return aDefaultFolderPath;
	}

	public String findCheckstyleOutputParentFolder() {
//		String aDefaultFolder = getCheckstyleOutFolder().getAbsolutePath();
//		if (checkForDefaultFolder() && aDefaultFolder == null) {
//			return null;
//		}
//		String aParentFolder = aDefaultFolder;
		String aParentFolder = getCheckstyleDefaultFolder();
		if (aParentFolder == null) {
			return null;
		}
		String aSpecifiedFolder = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.getCheckStyleOutputDirectory();
		if (aSpecifiedFolder != null) {
			aParentFolder = aSpecifiedFolder;
		}

		if (aParentFolder == null) {
			return null;
		}
		return aParentFolder;
	}

	protected File getCheckStyleConfigurationDefaultFolder() {
		return getCheckstyleOutFolder();
	}

	public String findCheckstyleConfigurationParentFolder() {
		String aDefaultFolder = getCheckStyleConfigurationDefaultFolder().getAbsolutePath();

		String aParentFolder = aDefaultFolder;
		String aSpecifiedFolder = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.getCheckStyleConfigurationDirectory();
		if (aSpecifiedFolder != null) {
			aParentFolder = aSpecifiedFolder;
		}

		if (aParentFolder == null) {
			System.err.println("Please install plugin and run checkstyle on your project");
			return null;
		}
		return aParentFolder;
	}

	public String createFullGeneratedCheckStyleFileName() {
//		File aDefaultFolder = getCheckstyleOutFolder();
//		if (checkForDefaultFolder() && aDefaultFolder == null) {
//			return null;
//		}
//		File aParentFolder = aDefaultFolder;
//		String aSpecifiedFolder = BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
//				.getCheckStyleConfigurationDirectory();
//		if (aSpecifiedFolder != null) {
//			aParentFolder = aDefaultFolder;
//		}
		String aParentFolder = findCheckstyleOutputParentFolder();
		if (aParentFolder == null) {
			return null;
		}
		return aParentFolder + "/" + createGeneratedCheckStyleFileName();

	}

	protected String generatedCheckStyleFileName = null;

	public String getGenereatedCheckStyleFileName() {
		if (cannotInitializeCheckstyle) {
			return null;
		}
		if (generatedCheckStyleFileName == null) {
			generatedCheckStyleFileName = createFullGeneratedCheckStyleFileName();
			if (generatedCheckStyleFileName == null) {
				cannotInitializeCheckstyle = true;
			}
//			generatedCheckStyleFileName = createGeneratedCheckStyleFileName();

		}
		return generatedCheckStyleFileName;
	}

	protected String checkStyleText = null;

	protected SymbolTable symbolTable;

	@Override
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	@Override
	public String getStoredCheckstyleText() {
		if (checkStyleText == null) {
			String anOutFileName = getCheckStyleOutputFileName();
			if (anOutFileName == null) {
				return null;
			}
			try {
				checkStyleText = Common.readFile(new File(anOutFileName)).toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return checkStyleText;

	}

	@Override
	public String getCheckstyleText() {
		if (checkStyleText == null) { // assume it will never change
			String anOutFileName = getCheckStyleOutputFileName();
			if (anOutFileName == null) {
				return null;
			}
			String aCheckstyleGeneratedFileName = getGenereatedCheckStyleFileName();
			if (aCheckstyleGeneratedFileName == null) {
				return null;
			}
//			String aSpecifiedConfigurationFileName = BasicExecutionSpecificationSelector
//					.getBasicExecutionSpecification().getCheckStyleConfiguration();
//			String aConfigurationFileName = aSpecifiedConfigurationFileName;
//			File aSpecifiedConfigurationFile = new File(aSpecifiedConfigurationFileName);
//			if (!aSpecifiedConfigurationFile.exists()) {
////			String aConfigurationFileName = findCheckstyleConfigurationParentFolder() + "/"
////					+ BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCheckStyleConfiguration();
//				aConfigurationFileName = findCheckstyleConfigurationParentFolder() + "/"
//						+ aSpecifiedConfigurationFileName;
//				File aFile = new File(aConfigurationFileName);
//				if (!aFile.exists()) {
//					System.err.println("Did not find file:" + aFile.getAbsolutePath());
//					System.err.println("Download " + aFile.getName() + " and add it to directory " + aFile.getParent());
//					return null;
//				}
//			}
			File aCheckstyleConfigurationFile = getCheckstyleConfigurationFile();
			if (aCheckstyleConfigurationFile == null) {
				return null;
			}
			String aConfigurationFileName = aCheckstyleConfigurationFile.getAbsolutePath();
			symbolTable = CheckStyleInvoker.runCheckstyle(this, aConfigurationFileName, anOutFileName,
					aCheckstyleGeneratedFileName);
//			CheckStyleInvoker.forkCheckstyle(this, aConfigurationFileName, anOutFileName, aCheckstyleGeneratedFileName);

			try {
				checkStyleText = Common.readFile(new File(anOutFileName)).toString();
//		        Tracer.info(this, "Checkstyle output\n" + checkStyleText);

			} catch (IOException e) {
				e.printStackTrace();
			}
//			
		}
		return checkStyleText;
	}

//    String[] aCheckStyleLines = aCheckStyleText.split(System.getProperty("line.separator"));
	protected String[] checkStyleLines;

	@Override
	public String[] getCheckstyleLines() {
		if (checkStyleLines == null) {
			String aCheckStyleText = getCheckstyleText();
			if (aCheckStyleText == null) {
				return null;
			}
			checkStyleLines = aCheckStyleText.split(System.getProperty("line.separator"));
		}
		// TODO Auto-generated method stub
		return checkStyleLines;
	}

}
