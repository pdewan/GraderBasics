package fluorite.commands.generation;

import static fluorite.commands.generation.CommandGenerationUtil.XML_FILE_ENDING;
import static fluorite.commands.generation.CommandGenerationUtil.XML_START1;
import static fluorite.commands.generation.CommandGenerationUtil.XML_START2;
import static fluorite.commands.generation.CommandGenerationUtil.XML_START3;
import static fluorite.commands.generation.CommandGenerationUtil.XML_VERSION;
import static fluorite.commands.generation.CommandGenerationUtil.createCommands;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Operation;

public class CommandGenerationUtil {
	static String lastOutput;

	/*
	 * Some constants we use in the generated eclipse log, do not
	 * change these
	 */
	public static final String XML_START1 = "<Events startTimestamp=\"";
	public static final String XML_START2 = "\" logVersion=\"";
	public static final String XML_VERSION = "1.0.0.202008151525";
	public static final String XML_START3 = "\">\r\n";
	public static final String XML_FILE_ENDING = "\r\n</Events>";
	public static final String XML_FILE_ENDING_MATCH = "</Events>";
	public static void resetOutput() {
		lastOutput = null;
	}
	/**
	 * We want to create commands for inserts and deletes. If the native log contains
	 * inserts and deletes, then we just convert them to Eclipse inserts and deletes.
	 * However, we sometines have only two snapshots of a file or cell content.
	 * This method creates a list of commands from a previous and next snapshot
	 * To do so, it needs to do a diff between the two snapshots.
	 * We are using an open source program called Diff_Match_Proxy to do so.
	 * This method can be used directly without trying to understand it
	 */
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
	/**
	 * If the native log contains an insert command, then this method creates an eclipse command
	 * from the native insert command. The first argument is the offset in the file or cell
	 * where the insertion occurred and the second the text to be inserted.
	 */
	public static Insert createInsert(int anOffset, String aText) {
		return new Insert(anOffset, aText, null);
	}
	/**
	 * If the native log contains a delete command, then this method creates an eclipse command
	 * from the native delete command. The first argument is the offset in the file or cell
	 * where the insertion occurred and the second the text to be deleted.
	 */
	public static Delete createDelete(int anOffset, String aText) {
		return new Delete(anOffset, aText.length(), -1, -1, aText, null);
	}
	/**
	 * If the native log contains a command with Console input, then this method creates an eclipse command
	 * from the native console input command. The argument is the console input
	 */
	public static ConsoleInput createConsoleInput(String aText) {
		return new ConsoleInput(aText);
	}
	/**
	 * If the native log contains a run command, then this method creates an eclipse command
	 * from the native t command. The argument is the cell or file that has been run
	 */
	public static RunCommand createRunCommand(String aCellOrFileName) {
		return new RunCommand(false, false, null, aCellOrFileName, 0, false, false, false, false);
//		return new RunCommand(debug, // debug
//				terminate, // terminate
//				projectName, //projectName
//				className, exitValue, hitBreakPoint, stepEnd, stepInto, stepReturn)

	}
	/**
	 * If the native log contains a compiler error or warning message  
	 * then this method creates an eclipse command for this message.
	 * The commented args indicate the arguments to the command
	 */
	public static CompilationCommand createCompileTimeMessage(
			boolean isWarning, 
			String errorOrWarningMessage,
			int aMessageId, 
			int aLineNumber, 
			int aStart, // start offset in problem text
			int anEnd,  // end offset in problem text
			String aProblemText, // the problem text itself
			String aProblemLine, // the code line containing the problem text			
			String aCellOrFileName  // cell id
			) {
		return new CompilationCommand(isWarning, errorOrWarningMessage, aMessageId, aLineNumber, aStart, anEnd, aProblemText, aProblemLine, aCellOrFileName);
//		return new RunCommand(debug, // debug
//				terminate, // terminate
//				projectName, //projectName
//				className, exitValue, hitBreakPoint, stepEnd, stepInto, stepReturn)

	}
	/*
	 * Create exception command from a runtime message and a language.
	 * Language names are java, SML, python,
	 */
	public static ExceptionCommand createRuntimeMessage(
			String aMessage, 
			String aLanguage
			
			) {
		return new ExceptionCommand(aMessage, aLanguage);
//		
	}
	
	/**
	 * Create eclipse command from console output represented as aTexts
	 */
	public static ConsoleOutput createConsoleOutput(String aText) {
		ConsoleOutput retVal = new ConsoleOutput(aText, lastOutput);
		lastOutput = aText;
		return retVal;
	}
	public static void tryDiffs() {
//		String oldSnapshot = "hello world";
//		String newSnapshot = "goodbye and hello world wonderful";
		String oldSnapshot = "a b c d";
		String newSnapshot = "a b 3 d e";
		List<EHICommand> aCommands = createCommands(oldSnapshot, newSnapshot);
		System.out.println(aCommands);
		
//		diff_match_patch differ = new diff_match_patch();
//
//		LinkedList<Diff> diff = differ.diff_main(oldSnapshot, newSnapshot);
//		differ.diff_cleanupSemantic(diff);
//		LinkedList<diff_match_patch.Patch> patchs = differ.patch_make(oldSnapshot, diff);
//		Object[] aRetVal = differ.patch_apply(patchs,  newSnapshot);
	}
	public static String newFileName (long aTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		Date aDate = new Date(aTime);
		String aNewDateFormat = df.format(aDate);
		String aFileName = "Log" + aNewDateFormat + ".xml";
		return  aFileName;
		
	}
	public static StringBuffer toStringBuffer(
			long aStartTimestamp, List<EHICommand> aCommands) {

		StringBuffer buf = new StringBuffer();
		buf.append(XML_START1 + aStartTimestamp + XML_START2 + XML_VERSION + XML_START3);
		for (EHICommand aCommand:aCommands) {
			String aPersistedCommand = aCommand.persist();
			buf.append(aPersistedCommand);
		}
		buf.append(XML_FILE_ENDING);
		return buf;
	}
	public static void writeCommands(long aStartTimestamp, List<EHICommand> aCommands ) {
		String aFileName = newFileName(aStartTimestamp);
		StringBuffer aStringBuffer = toStringBuffer(aStartTimestamp, aCommands);
		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(aFileName), StandardCharsets.UTF_8);
		
		writer.write(aStringBuffer.toString());
		writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
