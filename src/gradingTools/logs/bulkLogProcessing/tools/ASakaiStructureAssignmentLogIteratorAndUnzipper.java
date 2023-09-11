package gradingTools.logs.bulkLogProcessing.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ASakaiStructureAssignmentLogIteratorAndUnzipper implements Iterator<Pairing<String, File>>{

	private static final String replacePattern = "###";
	private static final String logs = "Logs/LocalChecks";
	public static final String fineGrainedPattern =  ".*Assignment"+replacePattern+"SuiteFineGrained\\.csv",
							   nonFineGrainedPattern=".*Assignment"+replacePattern+"Suite\\.csv";
	
	private Iterator<Pairing<String, File>> iterator;
	private final String logPattern;
	
	public ASakaiStructureAssignmentLogIteratorAndUnzipper(File logLocation, String assignmentNumber){
		this(logLocation,nonFineGrainedPattern,assignmentNumber);
	}
	
	public ASakaiStructureAssignmentLogIteratorAndUnzipper(File logLocation, String pattern, String assignmentNumber){
		logPattern=nonFineGrainedPattern.replace(replacePattern, assignmentNumber);
		Iterator<File> studentFolders = getStudentDirectories(logLocation, assignmentNumber);
		List<Pairing<String, File>> filesAndNames = new ArrayList<>();
		while(studentFolders.hasNext()) {
			File studentWork = studentFolders.next();
			String studentName = studentWork.getName().replaceAll(".*\\(", "").replaceAll("\\)", "");
			File studentLogFile = findAndPossiblyUnzipAssignmentLog(studentWork);
			if(studentLogFile != null) {
				filesAndNames.add(new Pairing<>(studentName,studentLogFile));
			}
		}
		iterator = filesAndNames.iterator();
	}
	
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Pairing<String, File> next() {
		return iterator.next();
	}
	
	private File findAndPossiblyUnzipAssignmentLog(File studentGradedFolder) {
		for(File file : studentGradedFolder.listFiles(File::isDirectory)) {
			if(file.getName().equals("Submission attachment(s)")) {
				return recursiveUnzipSearcher(file);
			}
		}
		return null;
	}
	
	private File recursiveUnzipSearcher(File directory) {
		File logsFolder = new File(directory,logs);
		if(logsFolder.exists()) {
			for(File file : logsFolder.listFiles()) {
				if(file.getName().matches(logPattern)) {
					return file;
				}
			}
		}
		File [] directories = directory.listFiles(File::isDirectory);
		if(directories.length == 0) {
			for(File lookingForZips : directory.listFiles(File::isFile)) {
				if(lookingForZips.getName().matches(".*\\.zip")) {
					File zipDeposit = new File(lookingForZips.getParentFile(), lookingForZips.getName().replaceAll("\\.zip", ""));
					extractFolder(lookingForZips.getAbsolutePath(), zipDeposit.getAbsolutePath());
					directories = directory.listFiles(File::isDirectory);
				}
			}
		}
		for(File subDir : directories) {
			File logs = recursiveUnzipSearcher(subDir);
			if(logs != null) {
				return logs;
			}
		}
		return null;
	}
	
	private Iterator<File> getStudentDirectories(File logLocation, String assignmentNumber){
		File studentDirectoryDirectory=logLocation, lastStudentDirectoryDirectory;
		do {
			lastStudentDirectoryDirectory = studentDirectoryDirectory;
			for(File file : studentDirectoryDirectory.listFiles(File::isDirectory)) {
				if(file.getName().matches(".*ssignment.*"+assignmentNumber+".*")) {
					studentDirectoryDirectory = file;
					break;
				}
			}
		}while(!studentDirectoryDirectory.getAbsolutePath().equals(lastStudentDirectoryDirectory.getAbsolutePath()));
		return Arrays.stream(studentDirectoryDirectory.listFiles(File::isDirectory)).iterator();
	}
	
	//from https://stackoverflow.com/questions/9324933/what-is-a-good-java-library-to-zip-unzip-files
	private void extractFolder(String zipFile,String extractFolder) 
	{
	    try
	    {
	        int BUFFER = 2048;
	        File file = new File(zipFile);

	        ZipFile zip = new ZipFile(file);
	        String newPath = extractFolder;

	        new File(newPath).mkdir();
	        Enumeration zipFileEntries = zip.entries();

	        // Process each entry
	        while (zipFileEntries.hasMoreElements())
	        {
	            // grab a zip file entry
	            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
	            String currentEntry = entry.getName();

	            File destFile = new File(newPath, currentEntry);
	            //destFile = new File(newPath, destFile.getName());
	            File destinationParent = destFile.getParentFile();

	            // create the parent directory structure if needed
	            destinationParent.mkdirs();

	            if (!entry.isDirectory())
	            {
	                BufferedInputStream is = new BufferedInputStream(zip
	                .getInputStream(entry));
	                int currentByte;
	                // establish buffer for writing file
	                byte data[] = new byte[BUFFER];

	                // write the current file to disk
	                FileOutputStream fos = new FileOutputStream(destFile);
	                BufferedOutputStream dest = new BufferedOutputStream(fos,
	                BUFFER);

	                // read and write until last byte is encountered
	                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
	                    dest.write(data, 0, currentByte);
	                }
	                dest.flush();
	                dest.close();
	                is.close();
	            }


	        }
	        System.out.println("UNZIPPED FILE "+zipFile);
	    }
	    catch (Exception e) 
	    {
	        System.err.println("ERROR: "+e.getMessage());
	    }

	}
}
