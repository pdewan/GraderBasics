package gradingTools.interpreter.checkers;

import java.io.File;
import java.io.IOException;

import grader.basics.util.DirectoryUtils;
import util.misc.Common;

public class AnExternalDiffChecker implements InterpretedChecker{
	protected String diffTool;
	
	String TEMP_DIR = "tmp";
	
	public AnExternalDiffChecker() {
		File dir = new File(TEMP_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
	}

//	@Override
//	public boolean isExpandFiles() {
//		return false;
//	}
	public int getNumArgs() {
		return 3;
	}	
	public File maybeCreateFile(String aFileName) {
		File aFile = new File(aFileName);
		if (!aFile.exists()) {
			try {
				aFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return aFile;
	}
	@Override
	public CheckerResult check(String[] anArgs) {
		try {
			String anOptions = anArgs[0];
			String anArg1Text = anArgs[1];
			String anArg2Text = anArgs[2];
			String anArg1FileName =  TEMP_DIR + "/" + "diffFile1.txt";
			String anArg2FileName = TEMP_DIR + "/" + "diffFile2.txt";
			String aResultFileName = TEMP_DIR + "/" + "diffresult.txt";
			File anArg1File = maybeCreateFile(anArg1FileName);
			File anArg2File = maybeCreateFile(anArg2FileName);
			maybeCreateFile(aResultFileName);
			Common.writeText(anArg1File, anArg1Text);
			Common.writeText(anArg2File, anArg2Text);
//			DirectoryUtils.diff("-i -w -b",  anArg1File,
//					anArg2File, aResultFileName);
			DirectoryUtils.diff(anArgs[0],  anArg1File,
					anArg2File, aResultFileName);
			StringBuffer aDiffResult = Common.toText(aResultFileName);
//			if (aDiffResult.length() > 0)
//			aDiffResult.insert(0, "Model output diff actual output:\n");
			String aNotes = aDiffResult.toString();
			boolean aResult = aNotes.isEmpty();
			return new ACheckerResult(aNotes, aResult);

		} catch (Exception e) {
			e.printStackTrace();
			return new ACheckerResult(e.getMessage(), false);
		}

	}


	

}
