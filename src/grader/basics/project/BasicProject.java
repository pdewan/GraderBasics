package grader.basics.project;

import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.execution.BasicProcessRunner;
import grader.basics.execution.NotRunnableException;
import grader.basics.execution.RunningProject;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.trace.BinaryFolderMade;
import grader.basics.trace.BinaryFolderNotFound;
import grader.basics.trace.ProjectFolderNotFound;
import grader.basics.trace.SourceFolderAssumed;
import grader.basics.trace.SourceFolderNotFound;
import grader.basics.util.DirectoryUtils;
import grader.basics.util.Option;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import util.pipe.InputGenerator;
import util.trace.TraceableLog;
import util.trace.TraceableLogFactory;
import util.trace.Tracer;
//import scala.Option;

/**
 * A "standard" project. That is, an IDE-based java project.
 */
public class BasicProject implements Project {
    
	protected boolean isInfinite;
    protected File directory;
    protected File sourceFolder;
    protected Option<ClassesManager> classesManager;
    protected TraceableLog traceableLog;
    protected boolean noSrc;
    protected String sourceFilePattern = null;
//    protected SakaiProject project;

    /**
     * Basic constructor
     *
     * @param aDirectory The location of the project
     * @param name      The name of the project, such as "Assignment1"
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
    protected void setProject (Object aProject) {
    	
    }
    public BasicProject(String aSourceFilePattern) throws FileNotFoundException {
    	this (null,  new File("."), null, aSourceFilePattern);
    }
    // rewriting Josh's code
    // going back to Josh';s code
    public BasicProject(Object aProject, File aDirectory, String name, String aSourceFilePattern) throws FileNotFoundException {
        // Find the folder. We could be there or it could be in a different folder
    	if (aDirectory == null) {
            throw new FileNotFoundException("No directory given");
        }
    	sourceFilePattern = aSourceFilePattern;
    	setProject(aProject);
    	
    	// will do this in standardproject
//    	project = aProject;
    	BasicConfigurationManagerSelector.getConfigurationManager().createProjectConfiguration(aDirectory);
    	directory = aDirectory;
//        Option<File> src = DirectoryUtils.locateFolder(aDirectory, "src");
        Option<File> src = DirectoryUtils.locateFolder(aDirectory, Project.SOURCE);

        if (src.isEmpty()) {
        	SourceFolderNotFound.newCase(aDirectory.getAbsolutePath(), this).getMessage();

        	Set<File> sourceFiles = DirectoryUtils.getSourceFiles(aDirectory, sourceFilePattern);
        	if (!sourceFiles.isEmpty()) {
                    File aSourceFile = sourceFiles.iterator().next();
                    sourceFolder = aSourceFile.getParentFile(); // assuming no packages!
                    this.directory = sourceFolder.getParentFile();
                    SourceFolderAssumed.newCase(sourceFolder.getAbsolutePath(), this);
        	} else {
                    ProjectFolderNotFound.newCase(aDirectory.getAbsolutePath(), this).getMessage();
                    throw new FileNotFoundException("No source files found");
        	}
        	noSrc = true;
//                throw new FileNotFoundException("No src folder");
//        	sourceFolder = aDirectory;
//        	this.directory = sourceFolder;
        } else {
            sourceFolder = src.get();
            this.directory = src.get().getParentFile();
        }
        

        try {
//            File sourceFolder = new File(this.directory, "src");
            File buildFolder = getBuildFolder("main." + name);
//            if (AProject.isMakeClassDescriptions())
//            classesManager = Option.apply((ClassesManager) new ProjectClassesManager(project, buildFolder, sourceFolder));
            classesManager = createClassesManager(buildFolder);

        
        } catch (Exception e) {
        	e.printStackTrace();
            classesManager = Option.empty();
        }

        // Create the traceable log
        traceableLog = TraceableLogFactory.getTraceableLog();
    }
    protected Option<ClassesManager> createClassesManager(File buildFolder) throws ClassNotFoundException, IOException {
//        classesManager = Option.apply((ClassesManager) new ProjectClassesManager(project, buildFolder, sourceFolder));

       return Option.apply((ClassesManager) new BasicProjectClassesManager(null, buildFolder, sourceFolder, sourceFilePattern));

    }
    protected Option<File> out;
    protected Option<File> bin;
    protected Map<String, File> preferredClassToBuildFolder = new HashMap(); // wonder if it will ever have more than one entry
    /**
     * Caching version of Josh's code
     * This figures out where the build folder is, taking into account variations due to IDE
     *
     * @param preferredClass The name of the class that has the main method, such as "main.Assignment1"
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
  	   for (String aBinary:Project.BINARIES) {
  		   bin = DirectoryUtils.locateFolder(directory, aBinary);
  		   if (bin != null)
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
      	BinaryFolderNotFound.newCase(directory.getAbsolutePath(), this);
      	File retVal = new File(directory, Project.BINARY);
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
     * This figures out where the build folder is, taking into account variations due to IDE
     *
     * @param preferredClass The name of the class that has the main method, such as "main.Assignment1"
     * @return The build folder
     * @throws FileNotFoundException
     */
    public File getNonCachingBuildFolder(String preferredClass) throws FileNotFoundException {
//        Option<File> out = DirectoryUtils.locateFolder(directory, "out");
        Option<File> anOut = DirectoryUtils.locateFolder(directory, Project.BINARY_2);
//        if (out.isEmpty())
//        	out = DirectoryUtils.locateFolder(directory, Project.BINARY_0);

        

//        Option<File> bin = DirectoryUtils.locateFolder(directory, "bin");
        Option<File> aBin = DirectoryUtils.locateFolder(directory,  Project.BINARY_0); // just to handle grader itself, as it has execuot.c
        if (aBin.isEmpty())
//        Option<File> bin = DirectoryUtils.locateFolder(directory,  Project.BINARY);
        	aBin = DirectoryUtils.locateFolder(directory,  Project.BINARY);




        // If there is no 'out' or 'bin' folder then give up
        if (anOut.isEmpty() && aBin.isEmpty()) {
        	if (noSrc) {
                    return sourceFolder;
                } 
//            throw new FileNotFoundException();
        	BinaryFolderNotFound.newCase(directory.getAbsolutePath(), this);
        	File retVal = new File(directory, Project.BINARY);
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
    public RunningProject launch(InputGenerator anOutputBasedInputGenerator, String input, int timeout) throws NotRunnableException {
        return new BasicProcessRunner(this).run(anOutputBasedInputGenerator, input, timeout);
    }
    @Override
    public RunningProject launch(InputGenerator anOutputBasedInputGenerator, Map<String, String> aProcessToInput, int timeout) throws NotRunnableException {
        return new BasicProcessRunner(this).run(anOutputBasedInputGenerator, aProcessToInput, timeout);
    }
    
    @Override
    public RunningProject launch( String input, int timeout) throws NotRunnableException {
        return new BasicProcessRunner(this).run(input, timeout);
    }
    @Override
    public RunningProject launch( String input, String[] anArgs, int timeout) throws NotRunnableException {
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
    public static void main (String[] args) {
    	try {
			BasicGradingEnvironment.get().setLoadClasses(true);
//			Project aProject = new BasicProject(null, new File("."), null);
			Project anAllCorrectProject = new BasicProject(null, new File("."), null, "allcorrect");

			Class anAllCorrectClass = BasicProjectIntrospection.findClass(anAllCorrectProject, "ACartesianPoint");
			System.out.println ("An all correct" + anAllCorrectClass);
			Project aWrongAngleProject = new BasicProject(null, new File("."), null, "wrongangle");

			Class aWrongAngleClass = BasicProjectIntrospection.findClass(anAllCorrectProject, "ACartesianPoint");
			System.out.println ("A wrong" + aWrongAngleClass);
			
			
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
}
