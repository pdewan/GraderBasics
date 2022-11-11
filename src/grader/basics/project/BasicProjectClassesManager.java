package grader.basics.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.execution.RunningProject;
import grader.basics.settings.BasicGradingEnvironment;
import grader.basics.util.DirectoryUtils;
import grader.basics.util.Option;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import util.misc.Common;
import util.trace.Tracer;








//import scala.Option;

/**
 * @see ClassesManager
 */
public class BasicProjectClassesManager implements ClassesManager {
	protected Project basicProject;
    protected final File buildFolder;
    protected final File sourceFolder;
    protected ClassLoader classLoader;
//    protected ProxyClassLoader proxyClassLoader;
    protected final Set<ClassDescription> classDescriptions;
    protected String sourceFilePattern;
    protected List<String> classNamesToCompile = new ArrayList(); // contructor of subclass needs this initialized
    // removing SakaiProject to make this more modular, will make ProjectClassesManager handle this
//    SakaiProject project;
    protected void setOtherLoaders() {
//    	if (project != null) {
//        	proxyClassLoader = project.getClassLoader();
//        	}
    }
    protected void maybeCheckStyle() {
//        checkStyle(project, sourceFolder);

    }
    protected void maybeSetHasBeenCompiled(boolean newValue) {
//        project.setHasBeenCompiled(true);

    }
    protected void maybeSetCanBeCompiled(boolean newValue) {
//        project.setHasBeenCompiled(newValue);

    }
    protected void setBinaryFileSystemFolderName() {
//		project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
}

    protected void setCanBeLoaded(boolean newValue) {
//    	   project.setCanBeLoaded(true);
//         project.setCanBeLoaded(true);
    	
    }
    protected void setHasBeenLoaded(boolean newValue) {
// 	   project.setHasBeenLoaded(true);

    }
    protected boolean hasBeenCompiled() {
//    	return project.hasBeenCompiled();
    	return true;
    }
    protected void appendOutputAndErrorsToTranscriptFile(RunningProject aRunningProject) {
//    	aRunningProject.appendOutputAndErrorsToTranscriptFile(project);

    }
    protected void manageClassLoader() {
    	// may have to unload class so am doing this reset
//		project.setNewClassLoader();
//		proxyClassLoader = project.getClassLoader();
//    	project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
    }
    protected void setProject (Object aProject) {
    	
    }
    protected void initializeClassLoaders() throws IOException{
    	classLoader = this.getClass().getClassLoader();
//    	// Create the Class Loader and load the classes
//        if (BasicGradingEnvironment.get().isLoadClasses() ) {
////        	classLoader = project.getClassLoader();
////        	if (classLoader == null)
//        	setOtherLoaders();
////        	if (project != null) {
////        	proxyClassLoader = project.getClassLoader();
////        	}
//            classLoader = new URLClassLoader(new URL[]{buildFolder.toURI().toURL()});
//        }
    }
    public BasicProjectClassesManager(
    		Project aBasicProject,
    		/*SakaiProject aProject,*/ 
    		Object aProject, // do not want any dependencies on type
    		File buildFolder, File sourceFolder, String aSourceFilePattern) throws IOException,
            ClassNotFoundException {
		Tracer.info(this, "BasicProjectClassesManager created");

    	basicProject = aBasicProject;
    	setProject(aProject);
    	sourceFilePattern = aSourceFilePattern;
//        project = aProject;
        // Set the build and source folders for the project
        this.buildFolder = buildFolder;
        this.sourceFolder = sourceFolder;
        if (BasicLanguageDependencyManager.getLanguage().equals(BasicLanguageDependencyManager.JAVA_LANGUAGE)) {
        initializeClassLoaders();
        classDescriptions = new HashSet<ClassDescription>();
        tagsToClasses.clear();

        loadClasses(sourceFolder);
        maybeCheckStyle();
        } else {
        	classDescriptions = null;
        }
    }
    

    
    protected ClassDescription createClassDescription (Class<?> javaClass, File source) {
    	return new BasicClassDescription(javaClass, source);
    }
    
    protected void maybeCompileSourceFiles ( Set<File> sourceFiles ) throws IOException {
//    	if (BasicGradingEnvironment.get().isCompileMissingObjectCode()
//                || BasicGradingEnvironment.get().isForceCompile()
//                || BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
//
//            // Check if any files need to be compiled
//            ArrayList<File> aFilesToCompile = new ArrayList<File>();
//            for (File file : sourceFiles) {
//                String className = getClassName(file);
//                File classFile = getClassFile(className);
//                if (shouldCompile(file, classFile)) {
//                    classNamesToCompile.add(className);
//                    aFilesToCompile.add(file);
//                }
//            }
//            if (aFilesToCompile.size() > 0) {
//                if (GraderSettingsModelSelector.getGraderSettingsModel() != null
//                        && GraderSettingsModelSelector.getGraderSettingsModel().getNavigationSetter().getNavigationKind() != NavigationKind.AUTOMATIC
//                        && !BasicGradingEnvironment.get().isPreCompileMissingObjectCode()) {
//                    return;
//                }
//                try {
//                    System.out.println("Attempting to compile files.");
////                    project.setHasBeenCompiled(true);
//                    maybeSetHasBeenCompiled(true);
//                    aFilesToCompile = new ArrayList<>(sourceFiles); // compile all if we have to compile one because the previpusly comppiled files may be different version from ours
//                	
//    //				compile(aFilesToCompile);
//                    //				JavaClassFilesCompilerSelector.getClassFilesCompiler().compile(buildFolder, aFilesToCompile);
//                    RunningProject runningProject = BasicLanguageDependencyManager.getSourceFilesCompiler().compile(sourceFolder, buildFolder, aFilesToCompile);
//                    if (runningProject != null) {
//                        //					String outputAndErrors = runningProject.getOutputAndErrors();
////                        runningProject.appendOutputAndErrorsToTranscriptFile(project);
//                        appendOutputAndErrorsToTranscriptFile(runningProject);
//
//                    }
//                    System.out.println("Compilation attempt finished.");
//                    maybeSetCanBeCompiled(true);
////                    project.setCanBeCompiled(true);
//                    // reuse the same loader as its binary folder name has changed
////                    project.setNewClassLoader();
////					proxyClassLoader = project.getClassLoader();
////                	project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
//                	setBinaryFileSystemFolderName();
//
//                } catch (Exception e) {
//                    System.out.println("Compilation failed: " + e.toString());
//                    e.printStackTrace();
////                    project.setCanBeCompiled(false);
//                    maybeSetCanBeCompiled(false);
//                }
//            }
//        }
////        if (project != null) {
////        project.setHasBeenLoaded(true);
////        project.setCanBeLoaded(true);
////        }
    }
    protected void maybeCompileWrongVersion (String className, File file, Set<File> sourceFiles) {
//    	try {
//			System.out
//					.println("Class files are the incorrect version for the current Java version. Attempting to recompile files.");
////			List<File> recompiledFileList = new ArrayList<>();
////			recompiledFileList.add(file);
////			if (project.hasBeenCompiled() )
//			if (hasBeenCompiled())
//				return;
////			project.setHasBeenCompiled(true);
//			maybeSetHasBeenCompiled(true);
//			List<File> recompiledFileList = new ArrayList<>(sourceFiles);
////			recompiledFileList.add(file);
//			System.out.println("Recompiling files:" + recompiledFileList);
//			RunningProject runningProject = LanguageDependencyManager
//					.getSourceFilesCompiler().compile(sourceFolder,
//							buildFolder, recompiledFileList);
////			project.setCanBeCompiled(true);
//			maybeSetCanBeCompiled(true);
//			// may have to unload class so am doing this reset
//			manageClassLoader();
////			project.setNewClassLoader();
////			proxyClassLoader = project.getClassLoader();
////        	project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
//
//            classLoader = new URLClassLoader(new URL[]{buildFolder.toURI().toURL()});
//
//
//			if (runningProject != null) {
//				appendOutputAndErrorsToTranscriptFile( runningProject);
////				runningProject
////						.appendOutputAndErrorsToTranscriptFile(project);
//
//			}
//			System.out.println("Compilation attempt finished.");
//
//			Class c = null;
//			if (BasicGradingEnvironment.get().isLoadClasses()) {
////				c = classLoader.loadClass(className);
//				c = proxyClassLoader.loadClass(className);
//
//			}
//
//			if (c != null) {
//				classDescriptions
//						.add(new BasicClassDescription(c, file));
//			}
//		} catch (Exception ex) {
////			project.setCanBeCompiled(false);
//			maybeSetCanBeCompiled(false);
//
//			System.out.println("Compilation failed: " + ex.toString());
//		}
    }
    protected Class loadClass(String className) throws ClassNotFoundException {
//    	if (BasicGradingEnvironment.get().isLoadClasses() && 
//				proxyClassLoader != null) // if we are precompiling or cleaning up, this will be null
//		{
////			c = classLoader.loadClass(className);
//			return proxyClassLoader.loadClass(className);
//		}
//		if (BasicGradingEnvironment.get().isLoadClasses() && proxyClassLoader == null) {
//			return classLoader.loadClass(className);
//		}
//		return null;
    	Tracer.info(this, "Loading Class:" + className);

		if (BasicLanguageDependencyManager.isJava() && BasicGradingEnvironment.get().isLoadClasses() ) {
			Class retVal = classLoader.loadClass(className);
			if (retVal == null) {
				System.err.println("Could not load: " + className);
			}
			return retVal;
		} else {
			System.out.println("Is Java: +" + BasicLanguageDependencyManager.isJava() + " BasicGradingEnvironment.get().isLoadClasses() " + BasicGradingEnvironment.get().isLoadClasses() );
		}
		return null;
    }
    protected void loadCompiledClasses(Set<File> sourceFiles) throws ClassNotFoundException, IOException {
    	Tracer.info(this, "Loading Classes");
    	for (File file : sourceFiles) {
//    		if (file.getName().contains("Observable")) {
//    			System.out.println ("processing file:" + file.getName());
//    		}
    		if (file.getName().equals("package-info.java")) {
    			continue;
    		}
			String className = getClassName(file);
			// System.out.println(className);
			try {
//				Class c = null;
//				if (BasicGradingEnvironment.get().isLoadClasses() && 
//						proxyClassLoader != null) // if we are precompiling or cleaning up, this will be null
//				{
////					c = classLoader.loadClass(className);
//					c = proxyClassLoader.loadClass(className);
//				}
//				if (BasicGradingEnvironment.get().isLoadClasses() && proxyClassLoader == null) {
//					c = classLoader.loadClass(className);
//				}
				Class c = loadClass(className);
				// do not really need the if, but just in case we do different things
				// in the two cases
				if (c != null) {
//					classDescriptions.add(new BasicClassDescription(c, file));
					classDescriptions.add(createClassDescription(c, file));

				} else {
					
					System.err.println("Class loader returned null class for: " + className);
//					System.out.println("ValgrindTrace of steps before this error");
//					System.out.println(Tracer.getBufferedTracedMessages());
//					Tracer.clearTracedMessages();
					System.out.println("Trying to compile classes");

					maybeCompileWrongVersion(className, file, sourceFiles);
//					classDescriptions.add(createClassDescription(null, file));
					
//					System.err.println ("Could not load:" + className);
				}
//				else if (AProject.isLoadClasses()) {
//					c = classLoader.loadClass(className);
//				}
			} catch (IncompatibleClassChangeError e) {
				System.err.println("IncompatibleClassChangeError :" + file + " "+  e.getMessage());
				System.out.println("ValgrindTrace of steps before this error");
				System.out.println(Tracer.getBufferedTracedMessages());
			} catch (UnsupportedClassVersionError e) {
				maybeCompileWrongVersion(className, file, sourceFiles);
////
////				 } catch (UnsupportedClassVersionError |
////				 IncompatibleClassChangeError e) {
//				try {
//					System.out
//							.println("Class files are the incorrect version for the current Java version. Attempting to recompile files.");
////					List<File> recompiledFileList = new ArrayList<>();
////					recompiledFileList.add(file);
////					if (project.hasBeenCompiled() )
//					if (hasBeenCompiled())
//						break;
////					project.setHasBeenCompiled(true);
//					maybeSetHasBeenCompiled(true);
//					List<File> recompiledFileList = new ArrayList<>(sourceFiles);
////					recompiledFileList.add(file);
//					System.out.println("Recompiling files:" + recompiledFileList);
//					RunningProject runningProject = LanguageDependencyManager
//							.getSourceFilesCompiler().compile(sourceFolder,
//									buildFolder, recompiledFileList);
////					project.setCanBeCompiled(true);
//					maybeSetCanBeCompiled(true);
//					// may have to unload class so am doing this reset
//					manageClassLoader();
////					project.setNewClassLoader();
////					proxyClassLoader = project.getClassLoader();
////                	project.getClassLoader().setBinaryFileSystemFolderName(buildFolder.getAbsolutePath());
//
//		            classLoader = new URLClassLoader(new URL[]{buildFolder.toURI().toURL()});
//
//
//					if (runningProject != null) {
//						appendOutputAndErrorsToTranscriptFile( runningProject);
////						runningProject
////								.appendOutputAndErrorsToTranscriptFile(project);
//
//					}
//					System.out.println("Compilation attempt finished.");
//
//					Class c = null;
//					if (BasicGradingEnvironment.get().isLoadClasses()) {
////						c = classLoader.loadClass(className);
//						c = proxyClassLoader.loadClass(className);
//
//					}
//
//					if (c != null) {
//						classDescriptions
//								.add(new BasicClassDescription(c, file));
//					}
//				} catch (Exception ex) {
////					project.setCanBeCompiled(false);
//					maybeSetCanBeCompiled(false);
//
//					System.out.println("Compilation failed: " + ex.toString());
//				}
			} catch (Exception e) {
//				project.setCanBeCompiled(false);

				System.out.println("Could not load class:" + file + " " + e.getClass().getSimpleName() + " "+  e.getMessage());
//				e.printStackTrace();
			} catch (Error e) {
//				project.setCanBeCompiled(false);

				System.out.println("Could not load class:" + file + " " + e.getClass().getSimpleName() + " " + e.getMessage());

//				e.printStackTrace();
//				throw new IOException(e.getMessage());
			}/*
			 * catch (Exception e) { throw new IOException(e.getMessage()); }
			 */

		}
    }

    /**
     * This loads all the classes based on the source code files.
     *
     * @param sourceFolder The folder containing the source code
     * @throws ClassNotFoundException
     * @throws IOException
     */
    protected void loadClasses(File sourceFolder) throws ClassNotFoundException, IOException {
    	
        Set<File> sourceFiles = DirectoryUtils.getSourceFiles(sourceFolder, sourceFilePattern);
        maybeCompileSourceFiles(sourceFiles);
        setHasBeenLoaded(true);
        setCanBeLoaded(true);
        loadCompiledClasses(sourceFiles);
    }
    

    /**
     * Given a file, this finds the canonical class name.
     *
     * @param file The Java file
     * @return The canonical class name.
     * @throws IOException
     */
    public static String getClassName(File file) throws IOException {

        // Figure out the package
//        List<String> lines = FileUtils.readLines(file, null);
        String aText = Common.toText(file);
        String[] aLines = aText.split("\n");
        List<String> lines = Arrays.asList(aLines);
        String packageName = "";
        for (String line : lines) {
            if (line.trim().startsWith("package ")) {
                packageName = line.replace("package", "").replace(";", "").trim() + ".";
                break;
            }
        }

        // Figure out the class name and combine it with the package
//		String className = file.getName().replace(".java", "");
        String className = file.getName().replace(BasicLanguageDependencyManager.getSourceFileSuffix(), "");

        return packageName + className;
    }

//    /**
//     * Given a Java class name, this finds associated .class file.
//     *
//     * @param className The canonical name of the Java class
//     * @return The .class File.
//     */
//    private File getClassFile(String className) {
//
//        File classFolder = buildFolder;
//        String[] splitClassName = className.split("\\.");
//        for (int i = 0; i < splitClassName.length - 1; i++) {
//            String packagePart = splitClassName[i];
//            classFolder = new File(classFolder, packagePart);
//        }
//
//        String classFileName;
//        if (splitClassName.length > 0) {
////			classFileName = splitClassName[splitClassName.length - 1] + ".class";
//            classFileName = splitClassName[splitClassName.length - 1] + BasicLanguageDependencyManager.getBinaryFileSuffix();
//
//        } else {
//            classFileName = className + ".class";
//        }
//
//        return new File(classFolder, classFileName);
//    }

    /**
     * Given a .java and .class file, returns whether the .java file needs to be
     * compiled or recompiled
     *
     * @param javaFile The Java file
     * @param classFile The class file
     * @return boolean true if should compile/recompile false otherwise
     */
    private boolean shouldCompile(File javaFile, File classFile) {
//		System.out.println("Class time:" + classFile.lastModified() + " source time:" + javaFile.lastModified());
    	String javaName = javaFile.getName();
    	String className = classFile.getName();
        return !hasBeenCompiled()
//        		!project.hasBeenCompiled() 
        		&& !classFile.getName().startsWith("_") &&
        		!javaFile.getName().startsWith("._") &&
        		( BasicGradingEnvironment.get().isForceCompile()
                || !classFile.exists()
                || classFile.lastModified() < javaFile.lastModified());
//				(classFile.lastModified() - javaFile.lastModified()) < 1000;

    }

//	/**
//	 * Given an ArrayList of .javaFiles, returns whether the .java file needs to
//	 * be compiled or recompiled
//	 * 
//	 * @param javaFiles
//	 *            ArrayList of .java files
//	 * @throws IOException
//	 */
//	private void compile(ArrayList<File> javaFiles) throws IOException, IllegalStateException {
//
//		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//		if (compiler != null) {
//			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
//
//			List<String> optionList = new ArrayList<String>();
//			// set the output directory for the compiler
//			String buildFolderPath = buildFolder.getCanonicalPath();
//			optionList.addAll(Arrays.asList("-d", buildFolderPath));
//			System.out.println(buildFolderPath);
//
//			Iterable<? extends JavaFileObject> compilationUnits = fileManager
//					.getJavaFileObjectsFromFiles(javaFiles);
//			compiler.getTask(null, fileManager, null, optionList, null, compilationUnits).call();
//			for (File javaFile:javaFiles) {
//				SourceFileCompiled.newCase(javaFile.getAbsolutePath(), this);
//				
//			}
//		} else {
////			throw new RuntimeException("Compiler not accessible");
//			throw CompilerNotFound.newCase(this);
//		}
//	}
    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Looks for a class description given a class name (simple or canonical)
     *
     * @param className The name of the class to find
     * @return The class description wrapped in a {@link Option} in case none
     * was found.
     */
    @Override
    public Set<ClassDescription> findByClassOrInterfaceName(String className) {
    	Tracer.info(this, "finding type by name:" + className);

        Set<ClassDescription> classes = new HashSet<>();

        // First search the simple names
        for (ClassDescription description : classDescriptions) {
            if (
            		className == null || // return all classes
//            		description.getJavaClass().getSimpleName().equalsIgnoreCase(className) || 
            		description.getJavaClass().getCanonicalName().equalsIgnoreCase(className)) {
               classes.add(description);
            }
        }
        // if any full name matched, do not check simple name, which creates more ambiguityt
        if (!classes.isEmpty()) {
        	return classes;
        }
     // Next search the simple names
        for (ClassDescription description : classDescriptions) {
            if (description.getJavaClass().getSimpleName().equalsIgnoreCase(className)) {
                classes.add(description);
            }
        }

//        // Next search the canonical names
//        for (ClassDescription description : classDescriptions) {
//            if (description.getJavaClass().getCanonicalName().equalsIgnoreCase(className)) {
//                classes.add(description);
//            }
//        }
        return classes;
    }
    
    @Override
    public Set<ClassDescription> findByClassOrInterfaceNameMatch(String className) {
    	Tracer.info(this, "finding type by name match:" + className);

        Set<ClassDescription> classes = new HashSet<>();
        if (className == null) return classes;

//        // First search the simple names
//        for (ClassDescription description : classDescriptions) {
//            if (description.getJavaClass().getSimpleName().matches(className)) {
//               classes.add(description);
//            }
//        }
//        if (!classes.isEmpty())
//        	return classes;
        //  search the canonical names
        for (ClassDescription description : classDescriptions) {
            if (!className.contains("[") && 
            		description.getJavaClass().getCanonicalName().
//            		matches(BasicProjectIntrospection.toRegex(className))) {
        		matches(className)) {

                classes.add(description);
            }
        }
        return classes;
    }
    @Override
    public Set<ClassDescription> findClassesAndInterfaces (String aName, String aTag, String aNameMatch, String aTagMatch) {
    	return findClassAndInterfaces(aName, new String[] { aTag}, aNameMatch, aTagMatch);
    }
    @Override
    public Set<ClassDescription> findClassAndInterfaces (String aName, String[] aTag, String aNameMatch, String aTagMatch) {

    	if (aName == null && (aTag == null || aTag.length == 0) && aNameMatch == null && aTagMatch == null) {
    		return findByClassOrInterfaceName(null); // return all classes
    	}
    	Set<ClassDescription> result = new HashSet();
    	if (aTag != null && !(aTag.length == 0))
    		result = findByTag(aTag); 
    	// added this recently
    	if (!result.isEmpty())
    		return result;
    	if (aTag != null && aTag.length > 0 && result.isEmpty())
    		result = findByPattern(aTag[0]); 
    	if (!result.isEmpty())
    		return result;
    	if (aName != null)
    		result = findByClassOrInterfaceName(aName);
    	if (!result.isEmpty())
    		return result;
    	 
//    	if (aTag != null)
//    		result = findClassByTag(aTag);  
//    	if (aTag != null && result.isEmpty())
//    		result = findClassByPattern(aTag); 
//    	if (!result.isEmpty())
//    		return result;
    	if (aNameMatch != null) {
    		result = findByClassOrInterfaceNameMatch(aNameMatch);  		
    	}
    	if (!result.isEmpty())
    		return result;
    	if (aTagMatch != null) {
    		result = findByTagMatch(aTagMatch);
    	}
    	return result;
    }
    @Override
    public Set<ClassDescription> findByTag(String aTag) {
    	return findByTag (new String[] {aTag});
    }
    protected Map<String, Set<ClassDescription>> tagsToClasses = new HashMap();
    /**
     * Looks for all class descriptions with a particular tag
     *
     * @param tag The tag to search for
     * @return The set of matching class descriptions
     */
    @Override
    public Set<ClassDescription> findByTag(String[] aTags) {
    	if (aTags.length == 0) {
    		return null;
    	}
//    	String normalizedTag = tag.replaceAll("\\s","");
    	BasicProjectIntrospection.normalizeTags(aTags); // using array instead
    	List<String> aSpecificationList = Arrays.asList(aTags);
    	if (aTags.length > 1) {
    		aSpecificationList = new ArrayList(aSpecificationList);
    		java.util.Collections.sort(aSpecificationList);    		
    	}
    	String aTagKey = aSpecificationList.toString();
    	Set<ClassDescription> classes = null;
    	classes = tagsToClasses.get(aTagKey);
    	if (classes != null) {
    		return classes;
    	}
    	
    	classes = new HashSet<>();
//        System.out.println ("Class descriptions:" +classDescriptions);
        for (ClassDescription description : classDescriptions) {
//        	if (description.getJavaClass().isInterface())
//        		continue;
        	String[] anActualTags = description.getTags();
//        	System.out.println ("Testing description:" + description);
//        	if (description.getJavaClass().getName().contains("ObservableBridge")) {
//        		System.out.println ("found class");
//        	}
        	if (BasicProjectIntrospection.matchesTags(aSpecificationList, anActualTags )) {
        		 classes.add(description);
        	}
//            for (String t : description.getTags()) {
//            
//            	String aNormalizedActualTag = t.replaceAll("\\s","");
////                if (t.equalsIgnoreCase(tag)) {
//
//                if (aNormalizedActualTag.equalsIgnoreCase(normalizedTag)) {
//                    classes.add(description);
//                }
//            }
        }
        tagsToClasses.put(aTagKey, classes);
        return classes;
    }
    
    protected Map<String, Set<ClassDescription>> supertypesToClasses = new HashMap();
    /**
     * Looks for all class descriptions with a particular tag
     *
     * @param tag The tag to search for
     * @return The set of matching class descriptions
     */
    @Override
    public Set<ClassDescription> findBySupertypes(Class[] aSupertypes) {
    	if (aSupertypes.length == 0) {
    		return null;
    	}
    	List<String> anInterfaceNames = new ArrayList<>(aSupertypes.length);
    	for (Class anInterface:aSupertypes) {
    		anInterfaceNames.add(anInterface.getCanonicalName());
    	}
    	java.util.Collections.sort(anInterfaceNames);    		
    	String aKey = anInterfaceNames.toString();
    	Set<ClassDescription> classes = null;
    	classes = supertypesToClasses.get(aKey);
    	if (classes != null) {
    		return classes;
    	}
    	
    	classes = new HashSet<>();
//        System.out.println ("Class descriptions:" +classDescriptions);
        for (ClassDescription description : classDescriptions) {
        	Class aCandidate = description.getJavaClass();
        	boolean aMatch = true;
        	for (Class anInterface:aSupertypes) {
        		if (anInterface == aCandidate  || !anInterface.isAssignableFrom(aCandidate)) {
        			aMatch = false;
        			break;
        		}        		
        	}
        	if (aMatch) {
        		classes.add(description);
        	}


        }
        supertypesToClasses.put(aKey, classes);
        return classes;
    }
    
    
    @Override
    public Set<ClassDescription> findByPattern(String tag) {
    	Tracer.info(this, "Finding type by pattern:" + tag);
        Set<ClassDescription> classes = new HashSet<>();
        for (ClassDescription description : classDescriptions) {
//        	if (description.getJavaClass().isInterface())
//        		continue;
        	String aPattern = description.getStructurePatternName();
            
                if (tag == null || tag.equalsIgnoreCase(aPattern)) {
                    classes.add(description);
                }
            
        }
        return classes;
    }
    
    /**
     * Looks for all class descriptions with a particular tag
     *
     * @param tag The tag to search for
     * @return The set of matching class descriptions
     */
    @Override
    public Set<ClassDescription> findByTagMatch(String regex) {
    
    	Tracer.info(this, "finding type by tag match:" + regex);

        Set<ClassDescription> classes = new HashSet<>();
        if (regex.contains("["))
    		return classes;
        for (ClassDescription description : classDescriptions) {
            for (String t : description.getTags()) {
                if (t.matches(regex)) {
                    classes.add(description);
                }
            }
        }
        return classes;
    }

    /**
     * @return All class descriptions
     */
    @Override
    public Set<ClassDescription> getClassDescriptions() {
        return classDescriptions;
    }
//	@Override
//	public boolean isHasCompileErrors() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	@Override
//	public void setHasCompileErrors(boolean hasCompileErrors) {
//		// TODO Auto-generated method stub
//		
//	}

//    @Override
//    public List<String> getClassNamesToCompile() {
//        return classNamesToCompile;
//        
//    }
//
//    @Override
//    public void setClassNamesToCompile(List<String> classNamesToCompile) {
//        this.classNamesToCompile = classNamesToCompile;
//    }
    
   
}
