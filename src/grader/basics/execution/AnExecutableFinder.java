package grader.basics.execution;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import grader.basics.config.BasicStaticConfigurationUtils;

import java.util.List;
//import framework.project.ClassDescription;
//import framework.project.ClassesManager;
//import framework.project.Project;

public class AnExecutableFinder implements MainClassFinder {
	public static final String EXECUTABLE_SUFFIX = ".exe";
	public static final String[] DEFAULT_EXECUTABLE_COMMAND_ARRAY = {
			"{buildFolder}\\{entryPoint}", "{args}"
	};
	public static final List<String> DEFAULT_EXECUTABLE_COMMAND_LIST = Arrays.asList(DEFAULT_EXECUTABLE_COMMAND_ARRAY);
    public AnExecutableFinder() {
    	
    }
    

    public Map<String, String> getEntryPoints(grader.basics.project.Project frameworkProject, String aSpecifiedMainClass) throws NotRunnableException {
        try {
//    	File binaryFolder = frameworkProject.getBuildFolder("");
    	File binaryFolder = frameworkProject.getBuildFolder(); // to be consistent with other calls

    	File[] binaryChildren =  binaryFolder.listFiles();
//        List<FileProxy> binaryChildren =  binaryFolder.getC
    	Map<String, String> retVal = new HashMap();
        for ( File child:binaryChildren) {
        	if (child.getName().endsWith(EXECUTABLE_SUFFIX)) {
        		retVal.put(BasicProcessRunner.MAIN_ENTRY_POINT, child.getName());
        		return retVal;
//        		return child.getName();
        	}
        	
        }
        return retVal;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    	
//    	if (frameworkProject instanceof ProjectWrapper) {
//    		ProjectWrapper projectWrapper = (ProjectWrapper) frameworkProject;
//    		SakaiProject sakaiProject = projectWrapper.getProject();
//    		
//    		RootCodeFolder aRootCodeFolder = sakaiProject.getRootCodeFolder();
//    		RootFolderProxy binaryFolder = aRootCodeFolder.getBinaryFolder();
////    		String binaryFolderName = aRootCodeFolder.getBinaryProjectFolderName();
//            List<FileProxy> binaryChildren =  aRootCodeFolder.getRootFolder().getChildrenOf(
//            		binaryFolder.getAbsoluteName());
////            List<FileProxy> binaryChildren =  binaryFolder.getC
//            for ( FileProxy child:binaryChildren) {
//            	if (child.getMixedCaseLocalName().endsWith(EXECUTABLE_SUFFIX)) {
//            		String localName = child.getParentRelativeMixedCaseName();
//            		return localName;
//            	}
//            	
//            }
//            
//    	}
    }
    public Map<String, String> getEntryPoints(grader.basics.project.Project frameworkProject, String[] aSpecifiedMainClass) throws NotRunnableException {
        try {
//    	File binaryFolder = frameworkProject.getBuildFolder("");
    	File binaryFolder = frameworkProject.getBuildFolder();

    	File[] binaryChildren =  binaryFolder.listFiles();
//        List<FileProxy> binaryChildren =  binaryFolder.getC
    	Map<String, String> retVal = new HashMap();
        for ( File child:binaryChildren) {
        	if (child.getName().endsWith(EXECUTABLE_SUFFIX)) {
        		retVal.put(BasicProcessRunner.MAIN_ENTRY_POINT, child.getName());
        		return retVal;
//        		return child.getName();
        	}
        	
        }
        return retVal;
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    	
//    	if (frameworkProject instanceof ProjectWrapper) {
//    		ProjectWrapper projectWrapper = (ProjectWrapper) frameworkProject;
//    		SakaiProject sakaiProject = projectWrapper.getProject();
//    		
//    		RootCodeFolder aRootCodeFolder = sakaiProject.getRootCodeFolder();
//    		RootFolderProxy binaryFolder = aRootCodeFolder.getBinaryFolder();
////    		String binaryFolderName = aRootCodeFolder.getBinaryProjectFolderName();
//            List<FileProxy> binaryChildren =  aRootCodeFolder.getRootFolder().getChildrenOf(
//            		binaryFolder.getAbsoluteName());
////            List<FileProxy> binaryChildren =  binaryFolder.getC
//            for ( FileProxy child:binaryChildren) {
//            	if (child.getMixedCaseLocalName().endsWith(EXECUTABLE_SUFFIX)) {
//            		String localName = child.getParentRelativeMixedCaseName();
//            		return localName;
//            	}
//            	
//            }
//            
//    	}
    }
    protected List<String> defaultCommand;
	@Override
	public List<String> getDefaultCommand() {
		if (defaultCommand == null) {
			return DEFAULT_EXECUTABLE_COMMAND_LIST;
		} else {
			return defaultCommand;
		}
	}

	@Override
	public void setDefaultCommand(List<String> aCommand) {
		defaultCommand = aCommand;
	}


//	@Override
//	public Class mainClass(RootCodeFolder aRootCodeFolder,
//			ProxyClassLoader aProxyClassLoader, String expectedName,
//			Project aProject) {
//		return null;
//	}
    
    

}
