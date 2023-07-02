package gradingTools.logs;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import fluorite.commands.CompilationCommand;
import fluorite.commands.ConsoleInput;
import fluorite.commands.ConsoleOutput;
import fluorite.commands.Delete;
import fluorite.commands.EHICommand;
import fluorite.commands.ExceptionCommand;
import fluorite.commands.Insert;
import fluorite.commands.RunCommand;
import hermes.proxy.Diff_Match_Patch_Proxy;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
public class EclipseCommandsCreationUtil {
	static String lastOutput;
	static long startTimeStamp = System.currentTimeMillis();
	static long nextTimeStamp = startTimeStamp;
	static int tenSecondPause = 1000*10;
	static int hundredMillisecondPause = 100;
	static List<EHICommand> commands = new ArrayList();
	static int maxPauseTime = 1000*5;
	public static final String XML_START1 = "<Events startTimestamp=\"";
	public static final String XML_START2 = "\" logVersion=\"";
	public static final String XML_VERSION = "1.0.0.202008151525";
	public static final String XML_START3 = "\">\r\n";
//	public static final String XML_FILE_ENDING = "\r\n</Events>"; 
	public static final String XML_FILE_ENDING = "\r\n</Events>";
	public static final String XML_FILE_ENDING_MATCH = "</Events>";
	public static long getNextPauseTime() {
		return Math.round(maxPauseTime * Math.random() );
	}
	public static List<EHICommand> createCommands(String aPreviousSnapshot, String aNewSnapshot) {
		List<Diff> aDiffs = Diff_Match_Patch_Proxy.diff(aPreviousSnapshot, aNewSnapshot);
		List<EHICommand> retVal = new ArrayList();
		int anOffset = 0;
		int anOffsetBeforePreviousChange = -1; 

		for (Diff aDiff:aDiffs) {
//			String anOperation = aDiff.operation.toString();
			Operation anOperation = aDiff.operation;
			String aText = aDiff.text;
			int aTextLength = aText.length();

			switch (anOperation) {
			case EQUAL:
				anOffsetBeforePreviousChange = -1;
				anOffset += aDiff.text.length();
				break;
			case INSERT:
				int anInsertOffset = anOffsetBeforePreviousChange == -1?
						anOffset:
						anOffsetBeforePreviousChange;	// insert at the same offset as delete, we have a replace
				EHICommand aCommand = createInsert(anInsertOffset, aText);
				retVal.add(aCommand);
				anOffsetBeforePreviousChange = anOffset;
				anOffset += aTextLength;
				break;
			case DELETE:
				int aDeleteOffset = anOffsetBeforePreviousChange == -1?
						anOffset:
							anOffsetBeforePreviousChange;	// insert at the same offset as delete, we have a replace
				aCommand = createDelete(aDeleteOffset, aText);
				retVal.add(aCommand);
				anOffsetBeforePreviousChange = anOffset;
				anOffset -= aTextLength;
			
			}
			
		}
//
//		aDiffs.diff_cleanupSemantic(diff);
//		LinkedList<diff_match_patch.Patch> patchs = differ.patch_make(currentSnapshot, diff);
//		Object[] aRetVal = differ.patch_apply(patchs, currentSnapshot);
////		Diff_Match_Patch_Proxy.diff(oldFile, currentFile)
////		diff = Normalizer.normalize(diff, Form.NFD).replaceAll("[^\\p{ASCII}]", "");
////		diff = diff.substring(1, diff.length()-1);
		return retVal;
	}
	public static Insert createInsert(int anOffset, String aText) {
		return new Insert(anOffset, aText, null);
	}
	public static Delete createDelete(int anOffset, String aText) {
		return new Delete(anOffset, aText.length(), -1, -1, aText, null);
	}
	
	public static ConsoleInput createConsoleInput(String aText) {
		return new ConsoleInput(aText);
	}
	
	public static RunCommand createRunCommand(String aCellOrFileName) {
		return new RunCommand(false, false, null, aCellOrFileName, 0, false, false, false, false);
//		return new RunCommand(debug, // debug
//				terminate, // terminate
//				projectName, //projectName
//				className, exitValue, hitBreakPoint, stepEnd, stepInto, stepReturn)

	}
	public static CompilationCommand createCompilationCommand(
			boolean isWarning, 
			String errorOrWarningMessage,
			int aMessageId, 
			int aLineNumber, 
			int aStart, // start offset in problem text
			int anEnd,  // end offset in problem text
			String aProblemText, // the problem text itself
			String aProblemLine, // the line containing the problem text			
			String aCellOrFileName  // cell id
			) {
		return new CompilationCommand(isWarning, errorOrWarningMessage, aMessageId, aLineNumber, aStart, anEnd, aProblemText, aProblemLine, aCellOrFileName);
//		return new RunCommand(debug, // debug
//				terminate, // terminate
//				projectName, //projectName
//				className, exitValue, hitBreakPoint, stepEnd, stepInto, stepReturn)

	}
	public static ExceptionCommand createExceptionCommand(
			String aMessage, 
			String aLanguage
			
			) {
		return new ExceptionCommand(aMessage, aLanguage);
//		
	}
	
	
	public static ConsoleOutput createConsoleOutput(String aText) {
		ConsoleOutput retVal = new ConsoleOutput(aText, lastOutput);
		lastOutput = aText;
		return retVal;
	}
	
	
	public static void setTimeStampOfCommands(List<EHICommand> aCommands) {
		
		long aPauseTime = getNextPauseTime(); // need to use actual pause time
		long aNewTimeStamp = nextTimeStamp + aPauseTime;
		
		for (EHICommand aCommand:aCommands) {
			aCommand.setStartTimestamp(startTimeStamp);
			aCommand.setTimestamp(aNewTimeStamp - nextTimeStamp); // i.e. pause time
		}
		nextTimeStamp = aNewTimeStamp;		
	}
	public static void setTimeStampOfCommand(EHICommand aCommand) {
		aCommand.setStartTimestamp(startTimeStamp);
		long aPauseTime = getNextPauseTime(); // need to use actual pause time
		long aNewTimeStamp = nextTimeStamp + aPauseTime;
		aCommand.setTimestamp(aNewTimeStamp - nextTimeStamp); // i.e. pause time
		nextTimeStamp = aNewTimeStamp;		
	}

}
