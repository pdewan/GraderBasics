package grader.basics.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import util.trace.Tracer;

public class BasicStaticConfigurationUtils {
	public static final String PRIVACY = "privacy";
	public static final String EXECUTION_COMMAND = "execution";

	public static final String LANGUAGE = "language";
	public static final String REQUIREMENTS = "requirements";
	public static final String ENTRY_POINT = "entryPoint";
	public static final String BUILD_FOLDER = "buildFolder";
	public static final String PERMISSIONS = "permissions";

	public static final String CLASS_PATH = "classPath";
	public static final String OE_PATH = "oePath";
	public static final String JUNIT_PATH = "junitPath";
	public static final String LOCAL_GRADER_PATH = "localGraderPath";
	public static final String OE_AND_CLASS_PATH = "oeAndClassPath";
	public static final String CLASS_PATH_SEPARATOR = ":";

	public static final String PROCESS_TEAMS = "processTeams";

	public static final String ENTRY_TAG = "entryTag";
	public static final String ENTRY_TAGS = "entryTags";
	public static final String SLEEP_TIME = "sleepTime";
	public static final String ARGS = "args";
	public static final String START_TAGS = "startTags";
	public static final String TERMINATING = "terminating";
	public static final String GENERATE_TRACE_FILES = "trace";

	public static final String JAVA = "Java";

	public static final String CLASS_PATH_VAR = toVariable(CLASS_PATH);
	public static final String CLASS_PATH_SEPARATOR_VAR = toVariable(CLASS_PATH_SEPARATOR);
	public static final String OE_PATH_VAR = toVariable(OE_PATH);
	public static final String LOCAL_GRADER_PATH_VAR = toVariable(LOCAL_GRADER_PATH);

	public static final String JUNIT_PATH_VAR = toVariable(JUNIT_PATH);
	public static final String OE_AND_CLASS_PATH_VAR = toVariable(OE_AND_CLASS_PATH );
	public static final String PERMISSIONS_VAR = toVariable(PERMISSIONS);
	public static final String BUILD_FOLDER_VAR = toVariable(BUILD_FOLDER);
	public static final String IMPLICIT_REQUIRMENTS_ROOT = "implicitRequirementsRoot";
	public static final String DEFAULT_IMPLICIT_REQUIRMENTS_ROOT = "gradingTools";
	public static final String USE_EXECEUTOR = "useExecutor";
	public static final String EXECUTOR = "executor";
	public static final String C_OBJ = "language.C.obj";	
	public static final String FORK_MAIN = "forkMain";
	private static  List<String> basicCommand;
	private static Map<String, List<String>> processToBasicCommand = new HashMap();
	private static String duplicatedClassPathSeparator;
	private static List<String> processTeams;


//	Comp533s18.execution = java, -cp, .{:}..{:}{classPath}{:}{oePath}{:}{junitPath}, {entryTags}, {args}

//	public static final String[] DEFAULT_JAVA_BASIC_COMMAND_ARRAY = {
//			"java", "-cp", ".{:}..{:}{classPath}{:}{:}{junitPath}{:}{localGraderPath}", "{entryTags}", "{args}"
//	};
	public static final String[] DEFAULT_JAVA_BASIC_COMMAND_ARRAY = {
			"java", "-cp", ".{:}..{:}{classPath}{:}{:}{junitPath}{:}{localGraderPath}", "{entryPoint}", "{args}"
	};
	public static final String[] DEFAULT_OE_BASIC_COMMAND_ARRAY = {
			"java", "-cp", ".{:}..{:}{classPath}{:}{oePath}{:}{junitPath}{:}{localGraderPath}", "{entryTags}", "{args}"
	};

	public static final List<String> DEFAULT_JAVA_BASIC_COMMAND =
			Arrays.asList(DEFAULT_JAVA_BASIC_COMMAND_ARRAY);
	public static final List<String> DEFAULT_OE_BASIC_COMMAND =
			Arrays.asList(DEFAULT_OE_BASIC_COMMAND_ARRAY);
	public static String toVariable(String aVariableName) {
		return "{" + aVariableName + "}";
	}
	public static List<String> getBasicCommand() {
		if (basicCommand == null) {
			basicCommand = DEFAULT_OE_BASIC_COMMAND;
		}
////		return getInheritedListModuleProblemProperty(EXECUTION_COMMAND);
		return basicCommand;
	}
	public static boolean hasClassPath() {
//		getBasicCommand();
		return hasClassPath(getBasicCommand());
//		if (basicCommand == null) {
//			return false;
//		}
//		for (String aCommand:basicCommand) {
//			if (aCommand.contains(CLASS_PATH_VAR) || aCommand.contains(OE_AND_CLASS_PATH_VAR)) {
//				return true;
//			}
//		}
//		return false;	
	}
	public static boolean hasClassPath(List<String> basicCommand) {
//		getBasicCommand();
		if (basicCommand == null) {
			return false;
		}
		for (String aCommand:basicCommand) {
			if (aCommand.contains(CLASS_PATH_VAR) || aCommand.contains(OE_AND_CLASS_PATH_VAR)) {
				return true;
			}
		}
		return false;	
	}
	
	public static boolean hasOEClassPath() {
		return hasOEClassPath(getBasicCommand());
//		getBasicCommand();
//		if (basicCommand == null) {
//			return false;
//		}
//		for (String aCommand:basicCommand) {
//			if (aCommand.contains(OE_PATH_VAR) || aCommand.contains(OE_AND_CLASS_PATH_VAR)) {
//				return true;
//			}
//		}
//		return false;	
	}
	public static boolean hasOEClassPath(List<String> basicCommand) {
//		getBasicCommand();
		if (basicCommand == null) {
			return false;
		}
		for (String aCommand:basicCommand) {
			if (aCommand.contains(OE_PATH_VAR) || aCommand.contains(OE_AND_CLASS_PATH_VAR)) {
				return true;
			}
		}
		return false;	
	}
	
	public static boolean hasOEOrClassPath() {
		return hasClassPath(getBasicCommand()) || hasOEClassPath(getBasicCommand());
	}
	public static boolean hasOEOrClassPath(List<String> basicCommand) {
		return hasClassPath(basicCommand) || hasOEClassPath(basicCommand);
	}
	protected static String[] emptyEntryPoints = {};
	protected static String[] potentialMainEntryPoints;
//	public static String[] getPotentialMainEntryPointNames() {
//		String retVal = getInheritedStringModuleProblemProperty(ENTRY_POINT, null);
//		if (retVal != null) {
//			GraderSettingsManager manager = GraderSettingsManagerSelector.getGraderSettingsManager();
//			retVal = manager.replaceModuleProblemVars(retVal);
//			return new String[]{retVal.replaceAll(" ", "")};
//		}
//		return emptyEntryPoints;
//	}
	public static String[] getPotentialMainEntryPointNames() {
		return potentialMainEntryPoints;
//		if (potentialMainEntryPoints == null) {
//		String retVal = getInheritedStringModuleProblemProperty(ENTRY_POINT, null);
//		if (retVal != null) {
//			GraderSettingsManager manager = GraderSettingsManagerSelector.getGraderSettingsManager();
//			retVal = manager.replaceModuleProblemVars(retVal);
//			potentialMainEntryPoints = new String[]{retVal.replaceAll(" ", "")};
//		} else {
//			potentialMainEntryPoints = emptyEntryPoints;
//		}
//		}
//		return potentialMainEntryPoints;
	}
	
	public static void setPotentialMainEntryPointNames(String[] aNames) {
		potentialMainEntryPoints = aNames;
	}
	public static List<String> getBasicCommand(String aProcessName) {	
		List<String> retVal = processToBasicCommand.get(aProcessName);
		if (retVal == null)
			retVal = getBasicCommand();
		return retVal;
		
	}
	public static void setBasicCommand(String aProcessName, List<String> aCommand) {		
		processToBasicCommand.put(aProcessName, aCommand);
	
	}
	public static boolean hasEntryPoint(List<String> aCommand) {
		return hasSubString(aCommand, ENTRY_POINT);
	}

	public static boolean hasSubString(List<String> aCommand, String aSubString) {
		for (String aCommmandComponent : aCommand) {
			if (aCommmandComponent.contains(aSubString)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasEntryTag(List<String> aProcessCommand) {
		return hasSubString(aProcessCommand, ENTRY_TAG);
	}

	public static boolean hasEntryTags(List<String> aProcessCommand) {
		return hasSubString(aProcessCommand, ENTRY_TAGS);
	}

	public static boolean haArgs(String aProcessCommand) {
		return aProcessCommand.contains(ARGS);
	}
	
//	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String FILE_SEPARATOR = "/";


	public static String quotePath(String path) {
		if (!path.contains(" ")) return path;
	    boolean startSlash = path.startsWith("\\") || path.startsWith("/");
	    boolean endSlash = path.endsWith("\\") || path.endsWith("/");
	    String[] split = path.split("[\\\\/]+");

	    StringBuilder quotPath = new StringBuilder(path.length());

	    if (startSlash) {
	        quotPath.append(FILE_SEPARATOR);
	    }
	    
	    for(int i = 0; i < split.length; i ++) {
	    	String s = split[i];
	        if (s.contains(" ")) {
//	            s = "\"" + s + "\"";
	            s = "\\\"" + s + "\\\"";

	        }
	        quotPath.append(s);
	        if (i+1 < split.length) {
	            quotPath.append(FILE_SEPARATOR);
	        }
	    }
	    
	    if (endSlash) {
	        quotPath.append(FILE_SEPARATOR);
	    }
	    
	    return quotPath.toString();
	}
	
	public static int getClassPathFlagIndex(List<String> aBasicCommand) {
		int aCpIndex = aBasicCommand.indexOf("-cp");
		if (aCpIndex < 0) 
			aCpIndex = aBasicCommand.indexOf("-classpath");
		return aCpIndex;
	}
	public static String getExecutionCommandRawClassPath() {
		return getExecutionCommandRawClassPath(getBasicCommand());
//		List<String> aBasicCommand = getBasicCommand();
//		int aCpIndex = getClassPathFlagIndex(aBasicCommand);
//		if (aCpIndex < 0)
//			return null;
//		if (aCpIndex + 1 >= aBasicCommand.size())
//			return null;
//		return getReplacedRawClassPath(aBasicCommand.get(aCpIndex + 1));
		
	}
	public static String getExecutionCommandRawClassPath(List<String> aBasicCommand) {
//		List<String> aBasicCommand = getBasicCommand();
		int aCpIndex = getClassPathFlagIndex(aBasicCommand);
		if (aCpIndex < 0)
			return null;
		if (aCpIndex + 1 >= aBasicCommand.size())
			return null;
		return getReplacedRawClassPath(aBasicCommand.get(aCpIndex + 1));
		
	}
	
	public static String getReplacedRawClassPath (String command) {
		// do we really need all of these ifs, more efficient without them? - debugging will be easier
					// all of these will be in the same command
					if (command.contains(CLASS_PATH_VAR)) {

						command = command.replace(CLASS_PATH_VAR,
								BasicGradingEnvironment.get().getClassPath());
					}

					if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
						command = command.replace(CLASS_PATH_SEPARATOR_VAR,
								BasicGradingEnvironment.get().getClassPathSeparator());
					}
					 
					if (command.contains(LOCAL_GRADER_PATH_VAR)) {

						command = command.replace(LOCAL_GRADER_PATH_VAR,
						// BasicGradingEnvironment.get().getClasspath());
								BasicGradingEnvironment.get().getLocalGraderClassPath());

					} 
					if (command.contains(OE_PATH_VAR)) {
						if (command.toLowerCase().contains("local")) { // we already have oeall
							command = command.replace(OE_PATH_VAR,
									// BasicGradingEnvironment.get().getClasspath());
									"");
						} else {
						command = command.replace(OE_PATH_VAR,
						// BasicGradingEnvironment.get().getClasspath());
								BasicGradingEnvironment.get().getOEClassPath());
						}

					}

					if (command.contains(JUNIT_PATH_VAR)) {
						command = command.replace(JUNIT_PATH_VAR,
								BasicGradingEnvironment.get().getJUnitClassPath());
						// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
						// command = command.replace(OE_AND_CLASS_PATH_VAR,
						// BasicGradingEnvironment.get().getClassPath());
					} 
					String aClassPathSeparator = BasicGradingEnvironment.get().getClassPathSeparator();
					if (duplicatedClassPathSeparator == null) {
						//just avoding new String creation
						duplicatedClassPathSeparator = aClassPathSeparator + aClassPathSeparator;
					}
					// certain libraries may not exist, specially in the server, see what happens without them
					command = command.replaceAll(duplicatedClassPathSeparator, aClassPathSeparator);
					return command;
					// javac wants no quotes!
//					String anOSPath = BasicGradingEnvironment.get().toOSClassPath(command);
//					return anOSPath;
	}
	public static void replaceClassPathVars (List<String> basicCommand) {
		int aCpIndex = getClassPathFlagIndex(basicCommand);
		if (aCpIndex < 0)
			return ;
		
		if (aCpIndex + 1 >= basicCommand.size()) {
			Tracer.warning("Nothing follows classpath flag");
			return ;
		}
		String aReplacement = getReplacedRawClassPath(basicCommand.get(aCpIndex + 1));
		String anOSPath = BasicGradingEnvironment.get().toOSClassPath(aReplacement);
		basicCommand.set(aCpIndex + 1, anOSPath);

		
//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			
////			// do we really need all of these ifs, more efficient without them? - debugging will be easier
////			// all of these will be in the same command
////			if (command.contains(CLASS_PATH_VAR)) {
////
////				command = command.replace(CLASS_PATH_VAR,
////						BasicGradingEnvironment.get().getClassPath());
////			}
////
////			if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
////				command = command.replace(CLASS_PATH_SEPARATOR_VAR,
////						BasicGradingEnvironment.get().getClassPathSeparator());
////			}
////			if (command.contains(OE_PATH_VAR)) {
////
////				command = command.replace(OE_PATH_VAR,
////				// BasicGradingEnvironment.get().getClasspath());
////						BasicGradingEnvironment.get().getOEClassPath());
////
////			} 
////			if (command.contains(JUNIT_PATH_VAR)) {
////				command = command.replace(JUNIT_PATH_VAR,
////						BasicGradingEnvironment.get().getJUnitClassPath());
////				// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
////				// command = command.replace(OE_AND_CLASS_PATH_VAR,
////				// BasicGradingEnvironment.get().getClassPath());
////			} 
//			command = getReplacedClassPath(command);
//			basicCommand.set(aCommandIndex, command);
//		}
	}
	
//	public static void replacePermissionVariables(List<String> basicCommand, Project aProject) {
//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			if (doPermissions && command.contains(PERMISSIONS_VAR)) {
//
//				String aPolicyFilePath = JavaProjectToPermissionFile
//						.getPermissionFile(aProject).getAbsolutePath();
//				try {
//					aPolicyFilePath = JavaProjectToPermissionFile
//							.getPermissionFile(aProject).getCanonicalPath();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				aPolicyFilePath = aPolicyFilePath.replace("\\", "/");
//
//				aPolicyFilePath = quotePath(aPolicyFilePath);
//
//				command = command.replace(PERMISSIONS_VAR, aPolicyFilePath);
//				basicCommand.set(aCommandIndex, command);
//
//			}
//		}
//	}
	
	public static void replaceEntryPoint(List<String> basicCommand,  String anEntryPoint,
			String anEntryTagTarget) {
		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {

			String command = basicCommand.get(aCommandIndex);
			if (anEntryPoint != null) {
				command = command
						.replace(toVariable(ENTRY_POINT), anEntryPoint);
				
			}
			if (anEntryTagTarget != null) {
				command = command.replace(toVariable(ENTRY_TAGS),
						anEntryTagTarget);
				command = command.replace(toVariable(ENTRY_TAG),
						anEntryTagTarget); // will match tags also

			}
			// check if entryTagTarget is empty and replace it with entry point
			basicCommand.set(aCommandIndex, command);
		}
	}
	public static void replaceBuildFolder(List<String> basicCommand,  File aBuildFolder) {
		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {

			String command = basicCommand.get(aCommandIndex);
			// we should always have a build folder I suppose
			// this is meant for C like programs
			if (command.contains(BUILD_FOLDER_VAR)) {
			command = command.replace(BUILD_FOLDER_VAR,
					aBuildFolder.getAbsolutePath());
			basicCommand.set(aCommandIndex, command);

			}
		}
	}
	public static void replaceArgs(List<String> basicCommand,  String[] anArgs) {
		int argsIndex = basicCommand.indexOf(toVariable(ARGS));
		if (argsIndex >= 0) {
			basicCommand.remove(argsIndex);
			for (int i = 0; i < anArgs.length; i++) {
				basicCommand.add(argsIndex + i, anArgs[i]);
			}

		}
	}
	public static String[] getExecutionCommand(Project aProject,
			String aProcessName, File aBuildFolder, String anEntryPoint,
			String anEntryTagTarget, String[] anArgs) {

		List<String> basicCommand = null;
		if (aProcessName == null || aProcessName.isEmpty()) {
		
			basicCommand = getBasicCommand();
		} else {
		
			basicCommand = getBasicCommand(aProcessName);

		}
		return getExecutionCommand(basicCommand, aProject, aProcessName, aBuildFolder, anEntryPoint, anEntryTagTarget, anArgs);
////		List<String> retVal = new ArrayList(basicCommand.size());
//		List<String> retVal = new ArrayList(basicCommand.size() + 5); // to accommodate args
//		retVal.addAll(basicCommand);
//		replaceClassPathVars(retVal);
////		replacePermissionVariables(retVal, aProject);
//		replaceEntryPoint(retVal, anEntryPoint, anEntryTagTarget);
//		replaceBuildFolder(retVal, aBuildFolder);
//		replaceArgs(retVal, anArgs);
//		return retVal.toArray(new String[0]);


//		
//		
//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			if (command.contains(CLASS_PATH_VAR)) {
//
//				command = command.replace(CLASS_PATH_VAR,
//						BasicGradingEnvironment.get().getClassPath());
//
//			} else if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
//				command = command.replace(CLASS_PATH_SEPARATOR_VAR,
//						BasicGradingEnvironment.get().getClassPathSeparator());
//			} else if (command.contains(OE_PATH_VAR)) {
//
//				command = command.replace(OE_PATH_VAR,
//				// BasicGradingEnvironment.get().getClasspath());
//						BasicGradingEnvironment.get().getOEClassPath());
//
//			} else if (command.contains(JUNIT_PATH_VAR)) {
//				command = command.replace(JUNIT_PATH_VAR,
//						BasicGradingEnvironment.get().getJUnitClassPath());
//				// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
//				// command = command.replace(OE_AND_CLASS_PATH_VAR,
//				// BasicGradingEnvironment.get().getClassPath());
//			} else if (doPermissions && command.contains(PERMISSIONS_VAR)) {
//
//				String aPolicyFilePath = JavaProjectToPermissionFile
//						.getPermissionFile(aProject).getAbsolutePath();
//				try {
//					aPolicyFilePath = JavaProjectToPermissionFile
//							.getPermissionFile(aProject).getCanonicalPath();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				aPolicyFilePath = aPolicyFilePath.replace("\\", "/");
//
//				aPolicyFilePath = quotePath(aPolicyFilePath);
//
//				command = command.replace(PERMISSIONS_VAR, aPolicyFilePath
//
//				);
//			}
//
//			if (anEntryPoint != null) {
//				command = command
//						.replace(toVariable(ENTRY_POINT), anEntryPoint);
//			}
//			if (anEntryTagTarget != null) {
//				command = command.replace(toVariable(ENTRY_TAGS),
//						anEntryTagTarget);
//				command = command.replace(toVariable(ENTRY_TAG),
//						anEntryTagTarget); // will match tags also
//
//			}
//
//			command = command.replace(toVariable(BUILD_FOLDER),
//					aBuildFolder.getAbsolutePath());
//
//			retVal.add(command);
//		}
//		int argsIndex = retVal.indexOf(toVariable(ARGS));
//		if (argsIndex >= 0) {
//			retVal.remove(argsIndex);
//			for (int i = 0; i < anArgs.length; i++) {
//				retVal.add(argsIndex + i, anArgs[i]);
//			}
//
//		}
//		return retVal.toArray(new String[0]);

	}
	public static String[] getExecutionCommand(List<String> basicCommand, Project aProject,
			String aProcessName, File aBuildFolder, String anEntryPoint,
			String anEntryTagTarget, String[] anArgs) {

//		List<String> basicCommand = null;
//		if (aProcessName == null || aProcessName.isEmpty()) {
//		
//			basicCommand = getBasicCommand();
//		} else {
//		
//			basicCommand = getBasicCommand(aProcessName);
//
//		}
//		List<String> retVal = new ArrayList(basicCommand.size());
		List<String> retVal = new ArrayList(basicCommand.size() + 5); // to accommodate args
		retVal.addAll(basicCommand);
		replaceClassPathVars(retVal);
//		replacePermissionVariables(retVal, aProject);
		replaceEntryPoint(retVal, anEntryPoint, anEntryTagTarget);
		replaceBuildFolder(retVal, aBuildFolder);
		replaceArgs(retVal, anArgs);
		return retVal.toArray(new String[0]);


//		
//		
//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			if (command.contains(CLASS_PATH_VAR)) {
//
//				command = command.replace(CLASS_PATH_VAR,
//						BasicGradingEnvironment.get().getClassPath());
//
//			} else if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
//				command = command.replace(CLASS_PATH_SEPARATOR_VAR,
//						BasicGradingEnvironment.get().getClassPathSeparator());
//			} else if (command.contains(OE_PATH_VAR)) {
//
//				command = command.replace(OE_PATH_VAR,
//				// BasicGradingEnvironment.get().getClasspath());
//						BasicGradingEnvironment.get().getOEClassPath());
//
//			} else if (command.contains(JUNIT_PATH_VAR)) {
//				command = command.replace(JUNIT_PATH_VAR,
//						BasicGradingEnvironment.get().getJUnitClassPath());
//				// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
//				// command = command.replace(OE_AND_CLASS_PATH_VAR,
//				// BasicGradingEnvironment.get().getClassPath());
//			} else if (doPermissions && command.contains(PERMISSIONS_VAR)) {
//
//				String aPolicyFilePath = JavaProjectToPermissionFile
//						.getPermissionFile(aProject).getAbsolutePath();
//				try {
//					aPolicyFilePath = JavaProjectToPermissionFile
//							.getPermissionFile(aProject).getCanonicalPath();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				aPolicyFilePath = aPolicyFilePath.replace("\\", "/");
//
//				aPolicyFilePath = quotePath(aPolicyFilePath);
//
//				command = command.replace(PERMISSIONS_VAR, aPolicyFilePath
//
//				);
//			}
//
//			if (anEntryPoint != null) {
//				command = command
//						.replace(toVariable(ENTRY_POINT), anEntryPoint);
//			}
//			if (anEntryTagTarget != null) {
//				command = command.replace(toVariable(ENTRY_TAGS),
//						anEntryTagTarget);
//				command = command.replace(toVariable(ENTRY_TAG),
//						anEntryTagTarget); // will match tags also
//
//			}
//
//			command = command.replace(toVariable(BUILD_FOLDER),
//					aBuildFolder.getAbsolutePath());
//
//			retVal.add(command);
//		}
//		int argsIndex = retVal.indexOf(toVariable(ARGS));
//		if (argsIndex >= 0) {
//			retVal.remove(argsIndex);
//			for (int i = 0; i < anArgs.length; i++) {
//				retVal.add(argsIndex + i, anArgs[i]);
//			}
//
//		}
//		return retVal.toArray(new String[0]);

	}
	public static List<String> getProcessTeams() {
		return processTeams;
	}
	public static void setProcessTeams(List<String> newVal) {
		 processTeams = newVal;;
	}
	
}
