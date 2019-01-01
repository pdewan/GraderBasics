package grader.basics.settings;

import java.io.File;
import java.io.IOException;

import util.trace.Tracer;

/**
 * A singleton that investigates the machine for certain things. It looks for:
 * <ul>
 *     <li>Operating System</li>
 *     <li>Text Editor (OS specific)</li>
 *     <li>File Browser (OS specific)</li>
 *     <li>Classpath (OS specific because Windows delimits with ';' rather than ':'</li>
 * </ul>
 */
public class BasicGradingEnvironment {

    private static final String[] macEditors = new String[]{
        "/Applications/Sublime Text 2.app/Contents/SharedSupport/bin/subl",
        "/usr/local/bin/edit"
    };

    private static final String[] linuxEditors = new String[]{
        "/usr/local/bin/gvim",
        "/usr/local/bin/emacs",
        "/usr/local/bin/gedit",
    };

    private static final String[] windowsEditors = new String[]{
        "C:\\Program Files\\Sublime Text 2\\sublime_text.exe",
        "C:\\Program Files (x86)\\Sublime Text 2\\sublime_text.exe",
        "C:\\Program Files\\Notepad++\\notepad++.exe",
        "C:\\Program Files (x86)\\Notepad++\\notepad++.exe",
        "notepad"
    };
    
    protected String userName;
    protected boolean nativeExecution = true;

	
	protected String osName;
	protected String editor;
    protected String diff;
    protected String browser;
    protected String classpathSeparator;
    protected String classpath, canonicalClassPath, oeClassPath, junitClassPath, localGraderClassPath;
    protected String assignmentName;
    protected String defaulAssignmentsDataFolderName;
    protected boolean forkMain;
//    ConfigurationManager configurationManager;  // maybe it does not belong here

	

	public String getDefaultAssignmentsDataFolderName() {
		return defaulAssignmentsDataFolderName;
	}

	public void setDefaultAssignmentsDataFolderName(
			String newVal) {
		this.defaulAssignmentsDataFolderName = newVal;
	}
	public boolean isNotWindows() {
		return !osName.equals("Windows");
	}
//	protected void initClassPath() {
//		classpath = findClasspath(":");
//	}

	public BasicGradingEnvironment() {
        nativeExecution = true;
        // the rest of the stuff may not be needed in basic execution
        osName = System.getProperty("os.name");
        userName = System.getProperty("user.name");
        if (osName.equals("Mac OS X")) {
            osName = "Mac";
            browser = "open";
            editor = findEditor(macEditors);
            classpathSeparator = ":";
//            classpath = findClasspathAndSetAssociatedClassPaths(":");
        } else if (osName.equals("Linux")) {
            browser = "nautilus";
            editor = findEditor(linuxEditors);
            classpathSeparator = ":";
//            classpath = findClasspathAndSetAssociatedClassPaths(":");
        } else {
            osName = "Windows";
            browser = "explorer";
            editor = findEditor(windowsEditors);
            classpathSeparator = ";";

//            classpath = findClasspathAndSetAssociatedClassPaths(";");
        }
        maybeSetClasspaths();
    }
	
	public String getClassPathSeparator() {
		return classpathSeparator;
	}
	public String getOEClassPath() {
		return oeClassPath;
	}
	public String getLocalGraderClassPath() {
		return localGraderClassPath;
	}
	public String getJUnitClassPath() {
		return junitClassPath;
	}
	
	protected void maybeSetClasspaths() {
		setClasspaths();
	}

    private static String findEditor(String[] editors) {
        for (String editor : editors) {
            if (new File(editor).exists())
                return editor;
        }
        return "";
    }
    // as the name indicates this is a badly designed method
    // this is being overriden in the subclass and seems dangerous
  protected  String findSystemClasspathAndSetAssociatedClasspaths(String separator) {
	  String retVal = System.getProperty("java.class.path");
	  classpath = retVal;  // this is done twice, once here and once below
	  oeClassPath = findOEClassPath(separator);
	  junitClassPath = findJUnitClassPath(separator);
	  localGraderClassPath = findLocalGraderClassPath(separator);

	  return retVal;
  }
  
  public void setClasspaths() {
	  classpath = findSystemClasspathAndSetAssociatedClasspaths(classpathSeparator);
  }


//    private  String findClasspath(String separator) {
//    	String systemClassPath = System.getenv("CLASSPATH");
//    	String myClassPath = System.getProperty("java.class.path");
//    	String originalClassPath = systemClassPath;
//        File oe = new File("oeall-22.jar");
////        String[] paths = new String[] { ".", "..", oe.getAbsolutePath()};
//        String[] paths = new String[] { ".", "..", myClassPath};
//
//        String classpath = "";
//        for (String path : paths)
//            classpath += (classpath.isEmpty() ? "" : separator) + path;
//        if (osName.equals("Windows"))
//        	classpath = "\""+ classpath + "\"";
//        else
//        	classpath = classpath.replaceAll(" ", "\\ ");
//        return classpath;
//    }
    public  String toOSClassPath(String aCanoicalClassPath) {
    	if (osName.equals("Windows"))
        	return "\""+ aCanoicalClassPath + "\"";
        else
        	return aCanoicalClassPath.replaceAll(" ", "\\ ");
//        return aCanoicalClassPath;
    }
    protected String findOEClassPath(String separator) {
//    	String myClassPath = System.getProperty("java.class.path");
//    	String[] paths = myClassPath.split(separator);
//    	for (String aPath:paths) {
//    		if (aPath.contains("oeall")) {
//    			return aPath;
//    		}
//    	}
//    	return null;
    	return findLibInMyClassPath(separator, "oeall");
    }
    protected String findLocalGraderClassPath(String separator) {
    	String result = findLibInMyClassPath(separator, "graderbasics");
    	if (result == null || result.isEmpty()) {
    	 result = findLibInMyClassPath(separator, "local");
    	}
    	if (result == null || result.isEmpty()) {
    		result = findLibInMyClassPath(separator, "basic");
    	}
    	if (result == null || result.isEmpty()) {
    		// just use the entire clsas path of this grader
    		result = System.getProperty("java.class.path");

    	}
    	return result;
    }
    protected String findJUnitClassPath(String separator) {
//    	String myClassPath = System.getProperty("java.class.path");
//    	String[] paths = myClassPath.split(separator);
//    	for (String aPath:paths) {
//    		if (aPath.contains("oeall")) {
//    			return aPath;
//    		}
//    	}
//    	return null;
    	return findLibInMyClassPath(separator, "junit");
    }
    protected String findLibInMyClassPath(String separator, String aLibName) {
    	String myClassPath = System.getProperty("java.class.path");
    	String[] paths = myClassPath.split(separator);
    	for (String aPath:paths) {
    		if (aPath.toLowerCase().contains(aLibName.toLowerCase())) {
    			return aPath;
    		}
    	}
    	return ""; // the lib will be replaced with nothing , good for server
    }
    // this is the code that worked and is refactored in the subclass
//    private  String findClasspath(String separator) {
//    	String systemClassPath = System.getenv("CLASSPATH");
////    	String myClassPath = System.getProperty("java.class.path");
////    	String originalClassPath = systemClassPath;
//    	canonicalOEClassPath = findOEClassPath(separator);    	
////        File oe = new File("oeall-22.jar");
////        String[] paths = new String[] { ".", "..", oe.getAbsolutePath()};
////        String[] paths = new String[] { ".", originalClassPath};
////      String[] paths = new String[] { ".", "..", originalClassPath};
//
//    	String[] paths = null;
//    	if (StaticConfigurationUtils.hasClassPath() ) {
//    		paths = new String[] { ".", "..", systemClassPath};
//    	} else if (StaticConfigurationUtils.hasOEClassPath()) {
//    		paths = new String[] { ".", "..", canonicalOEClassPath};
//    	} else if (StaticConfigurationUtils.hasOEOrClassPath()) {
//    		paths = new String[] { ".", "..", canonicalOEClassPath, systemClassPath};
//    	}
//    	
////        String[] paths = new String[] { ".", "..", originalClassPath};
//
//
//
//        String classpath = "";
//        
//        for (String path : paths)
//            classpath += (classpath.isEmpty() ? "" : separator) + path;
//        canonicalClassPath = classpath;
//        classpath = toOSClassPath(canonicalClassPath);
////        if (osName.equals("Windows"))
////        	classpath = "\""+ classpath + "\"";
////        else
////        	classpath = classpath.replaceAll(" ", "\\ ");
//        return classpath;
//    }
    

    public String getClassPath() {
        return classpath;
    }
    public String getCanonicalClasspath() {
        return canonicalClassPath;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getEditor() {
        return editor;
    }
    
    public String getDiff() {
        return diff;
    }
    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getOsName() {
        return osName;
    }
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

    /**
     * Opens a directory in the file browser
     * @param file The directory
     */
    public void open(File file) {
        try {
            new ProcessBuilder(browser, file.getAbsolutePath()).start();
        } catch (IOException e) {
            Tracer.info(this,"Can't open file");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    private void editSubFiles(File folder) {
    	File[] subFiles = folder.listFiles();
    	for (File subFile: subFiles) {
    		if (subFile.isDirectory()) {
    			editSubFiles(subFile);
    		} else {
    			edit(subFile);
    		}
    	}
    }

    /**
     * Edits a directory or file in the text editor
     * @param file The directory or file
     */
    public void edit(File file) {

    	if (file.isDirectory() && osName.equals("Linux")) {
    		editSubFiles(file);
    		return;
    	}
    	
        try {
            new ProcessBuilder(editor, file.getAbsolutePath()).start();
        } catch (IOException e) {
            Tracer.info(this,"Can't edit file/folder");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }
    protected boolean loadClasses = true;

    protected boolean compileMissingObjectCode = false;
//    protected boolean unzipFiles = false;
    protected boolean unzipFiles = true;

 
    protected boolean forceCompile = false; //compile whether that is needed or not
    protected boolean checkStyle = false; 
    protected boolean preCompileMissingObjectCode = false;
    public  boolean isLoadClasses() {
        return loadClasses;
    }

    public  void setLoadClasses(boolean makeClassDescriptions) {
        loadClasses = makeClassDescriptions;
    }

    public  boolean isCompileMissingObjectCode() {
        return compileMissingObjectCode;
    }

    public void setCompileMissingObjectCode(boolean newVal) {
        compileMissingObjectCode = newVal;
    }

    public boolean isForceCompile() {
        return forceCompile;
    }

    public void setForceCompile(boolean forceCompile) {
        this.forceCompile = forceCompile;
    }

    public  boolean isPreCompileMissingObjectCode() {
        return preCompileMissingObjectCode;
    }

    public  void setPrecompileMissingObjectCode(
            boolean preCompileMissingObjectCode) {
        this.preCompileMissingObjectCode = preCompileMissingObjectCode;
    }
    public  boolean isUnzipFiles() {
		return unzipFiles;
	}

	public  void setUnzipFiles(boolean unzipFiles) {
		this.unzipFiles = unzipFiles;
	}
	
	 public  boolean isCheckStyle() {
			return checkStyle;
		}

	    public  void setCheckStyle(boolean checkStyle) {
			this.checkStyle = checkStyle;
		}

    // Singleton methods
    private static BasicGradingEnvironment singleton = null;

    public static BasicGradingEnvironment get() {
        if (singleton == null)
            singleton = new BasicGradingEnvironment();
        return singleton;
    }
    public static void set(BasicGradingEnvironment anEnvironment) {
    	singleton = anEnvironment;
    }
    public boolean isNativeExecution() {
		return nativeExecution;
	}
    
    public boolean isForkMain() {
    	return forkMain;
    }
    
    public void setForkMain(boolean newValue) {
		forkMain = newValue;
	}

	public void setNativeExecution(boolean basicExecution) {
		this.nativeExecution = basicExecution;
	}
//    public ConfigurationManager getConfigurationManager() {
//		return configurationManager;
//	}
//
//	public void setConfigurationManager(ConfigurationManager configurationManager) {
//		this.configurationManager = configurationManager;
//	}

}
