package grader.basics.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.settings.BasicGradingEnvironment;
//import scala.Option;
import util.misc.Common;
import util.trace.Tracer;

/**
 * A set of utilities to assist with the recursive nature of directories.
 */
public class DirectoryUtils {

    /**
     * Finds the first file in the given folder that matches the provided filter
     * @param folder The folder to look in
     * @param filter The filter to apply
     * @return The first found file wrapped in an {@link Option} in case none was found.
     */
    public static Option<File> find(File folder, FileFilter filter) {
        File[] files = folder.listFiles(filter);
        if (files == null || files.length == 0) {
            return Option.empty();
        } else {
            return Option.apply(files[0]);
        }
    }

    /**
     * Looks for a folder with the given name recursively in a provided directory.
     * @param currentDir The directory to start looking in
     * @param folderName The name of the desired directory
     * @return The located directory wrapped in an {@link Option} in case it wasn't found
     */
    public static Option<File> locateFolder(File currentDir, final String folderName) {
        // Don't accept files (they don't make sense) or Mac meta folders
    	try {
    		if (currentDir == null) {
//    			System.out.println("Null current dir in locate folder");
    			return Option.empty();
    		}
        if (!currentDir.isDirectory() || currentDir.getName().equals("__MACOSX")) {
            return Option.empty();
        } else {
            Option<File> folder = find(currentDir, new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().equals(folderName);
                }
            });
            if (folder.isDefined()) {
                return folder;
            } else {
                for (File file : currentDir.listFiles()) {
                    Option<File> possible = locateFolder(file, folderName);
                    if (possible.isDefined()) {
                        return possible;
                    }
                }
                return Option.empty();
            }
        }
    	} catch (Exception e) {
    		System.out.println ("Exception:" + e.getMessage());
    		System.out.println ("Current file " + currentDir + " folder name " + folderName);
    		 return Option.empty();
    	}
    }

    /**
     * Looks for all files matching the provided filter recursively.
     * @param dir The directory to start looking from
     * @param filter The filter to apply
     * @return The set all of all files that matched the filter
     */
    public static Set<File> getFiles(File dir, final FileFilter filter) {
        // Get files in this folder
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory() && filter.accept(pathname);
            }
        });
        
        Set<File> allFiles = new HashSet<>(files.length);
        //if (files != null) {
            allFiles.addAll(Arrays.asList(files));
        //}

        // Get files in sub directories
        File[] directories = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        
        //if (directories != null) {
            for (File directory : directories) {
                allFiles.addAll(getFiles(directory, filter));
            }
        //}

        return allFiles;
    }

	public static Set<File> getSourceFiles(File sourceFolder, String aSourceFilePattern) {
		if (!sourceFolder.exists()) {
			System.err.println(sourceFolder + " does not exist");
			System.exit(0);
		}
	Set<File> aSourceFiles = getFiles(sourceFolder, new FileFilter() {
		@Override
		public boolean accept(File pathname) {
//			return pathname.getName().endsWith(".java");
			
			try {
				return ((aSourceFilePattern == null) || pathname.getCanonicalPath().contains(aSourceFilePattern)) &&					
						!pathname.getName().startsWith(".") && !pathname.getName().equals("package-info.java") &&
						pathname.getName().endsWith(BasicLanguageDependencyManager.getSourceFileSuffix());
			} catch (IOException e) {				
				e.printStackTrace();
				return false; // should never happen
			}

		}
	});
	return aSourceFiles;
	}
	
	public static boolean hasSuffix (String name, List<String> ignoreSuffixes) {
		for (String suffix:ignoreSuffixes) {
			if (name.endsWith(suffix)) {
                            return true;
                        }
			
		}
		return false; 
		
		
	}
	
	public static boolean compare (File correctDir, File testDir) {
		List<String> suffixes = new ArrayList<>();
		return compare(correctDir, testDir, suffixes);
		
	}
	
	public static String handleSpacesInExecutale(String aCommand) {
//		return aCommand.replaceAll("Program Files", "\"Program Files\""); // hack for now
		return "\"" + aCommand + "\"";
	}
	
	public static String diffWithResult( String anOptions, File correctChild, File testChild, String anOutputFileName) {
		String diffTool = BasicGradingEnvironment.get().getDiff();
		if ((diffTool == null || diffTool.isEmpty())) {
			System.out.println("cannot diff as no difftool provided");
			return "";
		}
//			diffTool = handleSpacesInExecutale(diffTool);
//		Path aPath = Paths.get(diffTool);
		try {
//			String aCanonicalPath = aPath.toFile().getCanonicalPath();
			String aCanonicalPath = diffTool;
			String aCorrectChildName =  "\"" + correctChild.getAbsolutePath() + "\"";
			String aTestChildName = "\"" + testChild.getAbsolutePath() + "\"";
			
//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, correctChild.getAbsolutePath(), testChild.getAbsolutePath(), ">", testChild.getAbsolutePath()+"diff" );
//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, aCorrectChildName, aTestChildName);
//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, aCorrectChildName, aTestChildName,  ">", anOutputFileName);
			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, anOptions, aCorrectChildName, aTestChildName);


//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath );
			Process aProcess;
			aProcessBuilder.redirectError(Redirect.INHERIT);
			if (anOutputFileName == null) {
//				aProcessBuilder.redirectOutput(Redirect.INHERIT);
				aProcess = aProcessBuilder.start();
				aProcess.waitFor();
				BufferedReader br = new BufferedReader(new InputStreamReader(aProcess.getInputStream()));

				StringBuilder builder = new StringBuilder();
				String line = null;
				while ( (line = br.readLine()) != null) {
				   builder.append(line);
				   builder.append(System.getProperty("line.separator"));
				}
				String result = builder.toString();
				return result;
				

			} else {

			File anOutputFile = new File(anOutputFileName);
			if (!anOutputFile.exists())
				anOutputFile.createNewFile();
			aProcessBuilder.redirectOutput(Redirect.to(anOutputFile));
			 aProcess = aProcessBuilder.start();
			aProcess.waitFor();
			return Common.toText(anOutputFile);
			}

//			Process aProcess = aProcessBuilder.start();
//			aProcess.waitFor();
//			String diffText = Common.toText(new File(testChild.getAbsolutePath()+"diff"));

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
//		String command = diffTool + " " + correctChild.getAbsolutePath() + " " + testChild.getAbsolutePath() + " > " + testChild.getAbsolutePath()+"diff";
////		try {
////			Runtime.getRuntime().exec(command);
//			OEMisc.runWithProcessExecer(diffTool);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		
	}
	public static void diff( String anOptions, File anOriginalFile, File aModifiedFile, String anOutputFileName) {
		String diffTool = BasicGradingEnvironment.get().getDiff();
		if ((diffTool == null || diffTool.isEmpty())) {
			System.out.println("cannot diff as no difftool provided");
			return;
		}
		String[] anOptionsList = anOptions.replace("\"", "").split("\\s+");
//			diffTool = handleSpacesInExecutale(diffTool);
//		Path aPath = Paths.get(diffTool);
		try {
//			String aCanonicalPath = aPath.toFile().getCanonicalPath();
			String aCanonicalPath = diffTool;
			String anOriginalFileName =  "\"" + anOriginalFile.getAbsolutePath() + "\"";
			String aModifiedFileName = "\"" + aModifiedFile.getAbsolutePath() + "\"";
			String[] anArgs = new String[anOptionsList.length + 3];
			for (int i=0; i < anOptionsList.length; i++) {
				anArgs[i + 1] = anOptionsList[i];
			}
			anArgs[0] = aCanonicalPath;
			anArgs[anOptionsList.length + 1] = anOriginalFileName;
			anArgs[anOptionsList.length + 2] = aModifiedFileName;
			
			
			
//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, correctChild.getAbsolutePath(), testChild.getAbsolutePath(), ">", testChild.getAbsolutePath()+"diff" );
//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, aCorrectChildName, aTestChildName);
//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, aCorrectChildName, aTestChildName,  ">", anOutputFileName);
//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, anOptions, anOriginalFileName, aModifiedFileName);
			ProcessBuilder aProcessBuilder = new ProcessBuilder(anArgs);


//			ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath );
			aProcessBuilder.redirectError(Redirect.INHERIT);
			if (anOutputFileName == null) {
				aProcessBuilder.redirectOutput(Redirect.INHERIT);
				

			} else {

			File anOutputFile = new File(anOutputFileName);
			if (!anOutputFile.exists())
				anOutputFile.createNewFile();
			aProcessBuilder.redirectOutput(Redirect.to(anOutputFile));
			
			}

			Process aProcess = aProcessBuilder.start();
			aProcess.waitFor();
//			String diffText = Common.toText(new File(testChild.getAbsolutePath()+"diff"));

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
//		String command = diffTool + " " + correctChild.getAbsolutePath() + " " + testChild.getAbsolutePath() + " > " + testChild.getAbsolutePath()+"diff";
////		try {
////			Runtime.getRuntime().exec(command);
//			OEMisc.runWithProcessExecer(diffTool);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		
	}
	
	public static boolean compare (File correctDir, File testDir, List<String> ignoreSuffixes) {
		boolean retVal = true;		
		if (!correctDir.isDirectory() || ! testDir.isDirectory()) {
			Tracer.error("test or corect dir not really directories");
			return false;
		}
		File[] correctChildren = correctDir.listFiles();
		File[] testChildren = testDir.listFiles();
		if (correctChildren.length != testChildren.length) {
			Tracer.error("correct and test dir not same size:" + correctDir.getAbsolutePath() + "(" + correctChildren.length + "," + testChildren.length + ")");
			Tracer.info(DirectoryUtils.class, "Correct:" +  Common.toString(correctChildren) + " Test:" + Common.toString(testChildren));
			System.out.println( "Correct:" +  Common.toString(correctChildren) + " Test:" + Common.toString(testChildren));

			retVal = false;
//			return false;
			
		}
		for (File correctChild:correctChildren) {
			File testChild = new File (testDir,  correctChild.getName());
			if (!testChild.exists()) {
				Tracer.error("test file does not exist:" + testChild.getName());
				retVal = false;
//				return false;
                                continue;
			}
			if (hasSuffix(correctChild.getName(), ignoreSuffixes)) {
                            continue;
                        }
			if (correctChild.isDirectory()) {
				if (!testChild.isDirectory()) {
					Tracer.error("Test file is not a directory:" + testChild.getName());
					retVal = false;
//					return false;
				}
					if (!compare(correctChild, testChild, ignoreSuffixes)) {
						retVal = false;
						continue;
//						return false;
					} else {
                                            continue;
                                }
				
			}
//			if (hasSuffix(correctChild.getName(), ignoreSuffixes))
//				continue;
//			String diffTool = GradingEnvironment.get().getDiff();
//			if (!(diffTool == null || diffTool.isEmpty())) {
////				diffTool = handleSpacesInExecutale(diffTool);
////			Path aPath = Paths.get(diffTool);
//			try {
////				String aCanonicalPath = aPath.toFile().getCanonicalPath();
//				String aCanonicalPath = diffTool;
//				String aCorrectChildName =  "\"" + correctChild.getAbsolutePath() + "\"";
//				String aTestChildName = "\"" + testChild.getAbsolutePath() + "\"";
//				String anOutputFileName =  testChild.getAbsolutePath() + "diff";
//				File anOutputFile = new File(anOutputFileName);
//				if (!anOutputFile.exists())
//					anOutputFile.createNewFile();
////				ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, correctChild.getAbsolutePath(), testChild.getAbsolutePath(), ">", testChild.getAbsolutePath()+"diff" );
////				ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, aCorrectChildName, aTestChildName);
////				ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, aCorrectChildName, aTestChildName,  ">", anOutputFileName);
//				ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath, aCorrectChildName, aTestChildName);
//
//
////				ProcessBuilder aProcessBuilder = new ProcessBuilder(aCanonicalPath );
//
//				aProcessBuilder.redirectError(Redirect.INHERIT);
//				
//				aProcessBuilder.redirectOutput(Redirect.appendTo(anOutputFile));
//
//				Process aProcess = aProcessBuilder.start();
//				aProcess.waitFor();
////				String diffText = Common.toText(new File(testChild.getAbsolutePath()+"diff"));
//
//			} catch (IOException | InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////			String command = diffTool + " " + correctChild.getAbsolutePath() + " " + testChild.getAbsolutePath() + " > " + testChild.getAbsolutePath()+"diff";
//////			try {
//////				Runtime.getRuntime().exec(command);
////				OEMisc.runWithProcessExecer(diffTool);
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			}

			String correctText = Common.toText(correctChild).replaceAll("\r\n", "\n");
			String testText = Common.toText(testChild).replaceAll("\r\n", "\n");
			if (!correctText.equals(testText)) {
				Tracer.error("Not equal to test file:" + correctChild.getAbsolutePath());
				Tracer.info(DirectoryUtils.class, "-----------------Correct Text-----------------\n" + correctText);
				Tracer.info(DirectoryUtils.class,"-----------------Test Text-----------------\n" + testText);
//				System.out.println( "-----------------Correct Text-----------------\n" + correctText);
//				System.out.println("-----------------Test Text-----------------\n" + testText);
				diff("-w", correctChild, testChild, null);
				retVal = false;
//				return false;
			} else {
				Tracer.info(DirectoryUtils.class, "Equal to test file:" + correctChild.getAbsolutePath());
			}
			
			
		}		
		return retVal;
//		return true;
	}
	
	public static void main (String[] args) {
		File correctDir = new File ("Test Data/Correct 110 F13 Results/Assignment3");
		File testDir = new File ("Test Data/Test 110 F13 Assignments/Assignment3");
		String[] ignoreSuffixesArray = {".zip", ".ini", ".json"};
		List<String> ignoreSuffixesList = Arrays.asList(ignoreSuffixesArray);
		compare (correctDir, testDir, ignoreSuffixesList);
		
	}
}
