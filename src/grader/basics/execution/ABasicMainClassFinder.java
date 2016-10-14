package grader.basics.execution;


import grader.basics.project.BasicProjectIntrospection;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
//import grader.project.Project;
//import framework.project.ClassDescription;
//import framework.project.ClassesManager;
//import framework.project.Project;

public class ABasicMainClassFinder implements MainClassFinder {
    public static final String DEFAULT_MAIN_PACKAGE_NAME = "main";
    
    protected boolean isEntryPoint (String aCandidate,
    		grader.basics.project.Project aProject
//    		framework.project.ClassesManager manager
    		) {
    	if (aCandidate == null)
    		return false;
    	Class aCandidateClass = BasicProjectIntrospection.findClass(aProject, aCandidate);
    	if (aCandidateClass == null) {
    		return false;
    	}
    	try {
    	Method method = aCandidateClass.getMethod("main", String[].class);
		return method != null;

    	} catch (NoSuchMethodException e) {
    		return false;
    	}
		
//    	for (framework.project.ClassDescription description : manager.getClassDescriptions()) {
//			try {
//				if (!description.getJavaClass().getCanonicalName().equals(aCandidate))
//					continue;
//				Method method = description.getJavaClass().getMethod("main", String[].class);
//				
//				return method != null;
//				
////				return description.getJavaClass().getCanonicalName();
////				return description.getJavaClass().getCanonicalName();
//
//			} catch (NoSuchMethodException e) {
//				return false;
//			}
//		}
//    	return false;
    	
    }
    
//    protected List<String> getEntryPoints(ProxyClassLoader aLoader, Project project) throws NotRunnableException {
//		if (project.getClassesManager() == null)
//			throw new NotRunnableException();
//		List<String> entryPoints = new ArrayList();
//	
//
//		ClassesManager manager = project.getClassesManager();
////		String aCandidate = StaticConfigurationUtils.getEntryPoint();
////		if (isEntryPoint(aCandidate, manager)) {
////			entryPoints.add(aCandidate);
////			return entryPoints;
////		}
//		for (ClassDescription description : manager.getClassDescriptions()) {
//			try {
//				description.getJavaClass().getMethod("main", String[].class);
//				entryPoints.add(description.getJavaClass().getCanonicalName());
//				return entryPoints;
////				return description.getJavaClass().getCanonicalName();
////				return description.getJavaClass().getCanonicalName();
//
//			} catch (NoSuchMethodException e) {
//				// Move along
//			}
//		}
//		throw new NotRunnableException();
//	}
    protected void setEntryPoints(grader.basics.project.Project project, Map<String, String> anEntryPoints) {
//      ProjectWrapper projectWrapper = (ProjectWrapper) project;
//      projectWrapper.getProject().setEntryPoints(anEntryPoints);


    }
    /**
     * This figures out what class is the "entry point", or, what class has main(args)
     * @param project The project to run
     * @return The class canonical name. i.e. "foo.bar.SomeClass"
     * @throws grader.basics.execution.NotRunnableException
     * @see grader.basics.execution.ABasicMainClassFinder which repeats this code (sigh)
     * Both need to be kept consistent
     */
    public Map<String, String> getEntryPoints(grader.basics.project.Project project, String aSpecifiedMainClass) throws NotRunnableException {
    	return getEntryPoints(project, new String[] {aSpecifiedMainClass});
 //        if (project.getClassesManager().isEmpty())
//            throw new NotRunnableException();
////        ProjectWrapper projectWrapper = (ProjectWrapper) project;
//		Map<String, String> entryPoints = new HashMap();
//
////        framework.project.ClassesManager manager = project.getClassesManager().get();
//        
////        String aCandidate = StaticConfigurationUtils.getEntryPoint();
//        String aCandidate = aSpecifiedMainClass;
//        if (aCandidate != null) {
////        	if (isEntryPoint(aCandidate, manager))  {
//            if (isEntryPoint(aCandidate, project))  {
//
//        		entryPoints.put(BasicProcessRunner.MAIN_ENTRY_POINT, aCandidate);
//        		return entryPoints;
//        	}
//        }
////		if (isEntryPoint(aCandidate, manager)) {
//		if (isEntryPoint(aCandidate, project)) {
//
//			entryPoints.put(BasicProcessRunner.MAIN_ENTRY_POINT, aCandidate);
//			return entryPoints;
//		}
//        grader.basics.project.ClassesManager manager = project.getClassesManager().get();
//
//        for (grader.basics.project.ClassDescription description : manager.getClassDescriptions()) {
//            try {
//                description.getJavaClass().getMethod("main", String[].class);
//                entryPoints.put(BasicProcessRunner.MAIN_ENTRY_POINT, description.getJavaClass().getCanonicalName());
////                projectWrapper.getProject().setEntryPoints(entryPoints);
//                setEntryPoints(project, entryPoints);
//                return entryPoints;
////                return description.getJavaClass().getCanonicalName();
//            } catch (NoSuchMethodException e) {
//            }
//        }
//        throw new NotRunnableException();
    }
    /**
     * This figures out what class is the "entry point", or, what class has main(args)
     * @param project The project to run
     * @return The class canonical name. i.e. "foo.bar.SomeClass"
     * @throws grader.basics.execution.NotRunnableException
     * @see grader.basics.execution.ABasicMainClassFinder which repeats this code (sigh)
     * Both need to be kept consistent
     */
    @Override
    public Map<String, String> getEntryPoints(grader.basics.project.Project project, String[] aSpecifiedMainClasses) throws NotRunnableException {
        if (project.getClassesManager().isEmpty())
            throw new NotRunnableException();
//        ProjectWrapper projectWrapper = (ProjectWrapper) project;
		Map<String, String> entryPoints = new HashMap();

//        framework.project.ClassesManager manager = project.getClassesManager().get();
        
//        String aCandidate = StaticConfigurationUtils.getEntryPoint();
        for (String aCandidate:aSpecifiedMainClasses) {
            if (isEntryPoint(aCandidate, project))  {

        		entryPoints.put(BasicProcessRunner.MAIN_ENTRY_POINT, aCandidate);
        		return entryPoints;
        	}
        }
////		if (isEntryPoint(aCandidate, manager)) {
//		if (isEntryPoint(aCandidate, project)) {
//
//			entryPoints.put(BasicProcessRunner.MAIN_ENTRY_POINT, aCandidate);
//			return entryPoints;
//		}
        
        grader.basics.project.ClassesManager manager = project.getClassesManager().get();

        for (grader.basics.project.ClassDescription description : manager.getClassDescriptions()) {
            try {
                description.getJavaClass().getMethod("main", String[].class);
                entryPoints.put(BasicProcessRunner.MAIN_ENTRY_POINT, description.getJavaClass().getCanonicalName());
//                projectWrapper.getProject().setEntryPoints(entryPoints);
                setEntryPoints(project, entryPoints);
                return entryPoints;
//                return description.getJavaClass().getCanonicalName();
            } catch (NoSuchMethodException e) {
            }
        }
        throw new NotRunnableException();
    }
    
//    public Class nonPackagedMainClass ( ProxyClassLoader aProxyClassLoader, Project aProject) {
//    	try {
//			return  aProxyClassLoader.loadClass(getEntryPoints(aProxyClassLoader, aProject).get(0));
//		} catch (ClassNotFoundException e1) {
//			
//			e1.printStackTrace();
//			return null;
//		} catch (NotRunnableException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			return null;
//		}
//    }
//    
//
//    public Class mainClass(RootCodeFolder aRootCodeFolder, ProxyClassLoader aProxyClassLoader, String expectedName, Project aProject) {
//        
//    	
//    	String binaryFolderName = aRootCodeFolder.getBinaryProjectFolderName();
//        String mainFolderName = binaryFolderName + "/" + DEFAULT_MAIN_PACKAGE_NAME;
//        List<FileProxy> mainBinaryChildren = aRootCodeFolder.getRootFolder().getChildrenOf(
//                mainFolderName);
//        if (mainBinaryChildren.size() != 1) {
//        	return  nonPackagedMainClass(aProxyClassLoader, aProject);
//        }
//
//        else if (mainBinaryChildren.size() == 1) {
//            String mainClassAbsoluteFileName = mainBinaryChildren.get(0).getMixedCaseAbsoluteName();
//            String classFileName = Common.absoluteNameToLocalName(mainClassAbsoluteFileName);
//            int dotIndex = classFileName.indexOf('.');
//            String className = classFileName.substring(0, dotIndex);
//
//            String mainClassFileName = DEFAULT_MAIN_PACKAGE_NAME + "." + className;
//            try {
//                return aProxyClassLoader.loadClass(mainClassFileName);
//            } catch (ClassNotFoundException e) {
//            	// not sure of this makes sense
//            	return  nonPackagedMainClass(aProxyClassLoader, aProject);
////                try {
////					return  aProxyClassLoader.loadClass(getEntryPoint(aProject));
////				} catch (ClassNotFoundException e1) {
////					
////					e1.printStackTrace();
////					return null;
////				} catch (NotRunnableException e1) {
////					// TODO Auto-generated catch block
////					e1.printStackTrace();
////					return null;
////				}
//            }
//        }
//
//        return null;
//    }

}
