package gradingTools.logs.bulkLogProcessing.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogAnalyzerAssignmentLogIterator implements Iterator<File> {

	private File nextFile;

	private Iterator<File> fileIterator;
	private String logPattern;

	public LogAnalyzerAssignmentLogIterator(File logLocation, String assignmentNumber) {
		logPattern = "Assignment" + assignmentNumber + "Suite";
		List<File> logs = new ArrayList<>();
//	if (assignmentNumber.equals("4")) {
//		File assignFolder = logLocation.listFiles()[0];
		for (File student : logLocation.listFiles(File::isDirectory)) {
			File projectFolder = getProjectFolder(student);
			if (projectFolder != null) {
				File logFolder = new File(projectFolder, "Logs" + File.separator + "LocalChecks");
				if (logFolder.exists()) {
					logs.add(logFolder);
				}
			}
//			File[] logFiles = new File(projectFolder,"Logs"+File.separator+"LocalChecks").listFiles((file)->{
//				return file.getName().endsWith("Suite.csv");
//			});
//			if (logFiles != null && logFiles.length > 0) {
//				logs.add(logFiles[0]);
//			}
//		}
		}
		fileIterator = logs.iterator();
//	fileIterator=Arrays.stream(logLocation.listFiles(File::isDirectory)).iterator();
		nextFile = findNext();
	}

	public File getProjectFolder(File folder) {
		for (File file : folder.listFiles((file) -> {
			return file.isDirectory();
		})) {
			if (file.getName().equals("src")) {
				return folder;
			}
		}
		for (File file : folder.listFiles((file) -> {
			return file.isDirectory();
		})) {
			if ((file = getProjectFolder(file)) != null) {
				return file;
			}
			;
		}
		return null;
	}

	@Override
	public boolean hasNext() {
		return nextFile != null;
	}

	@Override
	public File next() {
		File returnVal = nextFile;
		nextFile = findNext();

		return returnVal;
	}

	private File findNext() {
		while (fileIterator.hasNext()) {
			File[] logFiles = fileIterator.next().listFiles();
			for (File logFile : logFiles) {
				if (logFile.length() == 0)
					continue;
				String lfs = logFile.toString();
				if (lfs.contains(logPattern))
					return logFile;
			}
		}
		return null;
	}

}
