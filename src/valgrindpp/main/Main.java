package valgrindpp.main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import valgrindpp.codegen.Parser;
import valgrindpp.codegen.Wrapper;
import valgrindpp.grader.CondGrader;
import valgrindpp.grader.Grader;
import valgrindpp.grader.MutexGrader;
import valgrindpp.grader.MutexLruGrader;
import valgrindpp.grader.SimpleGrader;
import valgrindpp.grader.SleepGrader;
import valgrindpp.grader.Test;
import valgrindpp.grader.ThreadGrader;
import valgrindpp.gui.App;
import valgrindpp.helpers.CompilerHelper;
import valgrindpp.helpers.DockerHelper;

public class Main {
	public static final String WRAPPER_FILE_SUFFIX = "-wrapper";
	public static final String CONFIG_FILE = "SimpleAdderConfig";
	public static final String TRACE_FILE = "Traces";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new App());
	}
	// called by App, action performed, need to uncomment and coment to call it
	public static List<Test> testMakefileDirectory(String directory) {
		try {
			DockerHelper.deleteContainer();
			DockerHelper.createContainer(directory);
			
			Parser parser = new Parser(CONFIG_FILE, null);
			
			Wrapper wrapper = parser.parse();
			wrapper.toFile(CONFIG_FILE, directory);
			
			CompilerHelper ch = new CompilerHelper(CONFIG_FILE, directory, TRACE_FILE);
			
			ch.compileWrapper();
			ch.deleteWrapperCFile();
			// call make here
			ch.make();
			ch.deleteWrapperObjFile();
			ch.trace(new String[]{"./lru-mutex-wrapped", "-c", "2"});
			ch.makeClean();
			
			Grader grader = new MutexLruGrader(directory, TRACE_FILE);
//			ch.deleteTraces();
			return grader.grade();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Test> error = new ArrayList<Test>();
		error.add(new Test("Error: Check console", false));
		return error;
	}
	
	// called by App, action performed, need to uncomment and coment to call it
	public static List<Test> testDirectory(String directory) {
		try {
			DockerHelper.deleteContainer();
			DockerHelper.createContainer(directory);
			
			Parser parser = new Parser(CONFIG_FILE, null);
			
			Wrapper wrapper = parser.parse();
			wrapper.toFile(CONFIG_FILE, directory);
			
			CompilerHelper ch = new CompilerHelper(CONFIG_FILE, directory, TRACE_FILE);
			
			ch.compileWrapper();
			ch.deleteWrapperCFile();
			ch.compileStudentCode();
			ch.deleteWrapperObjFile();
			ch.trace();
			ch.deleteBinary();
			
			DockerHelper.stopContainer();
			
//			Grader grader = new SimpleGrader(directory, TRACE_FILE);
//			Grader grader = new MutexGrader(directory, TRACE_FILE);
//			Grader grader = new SleepGrader(directory, TRACE_FILE);
//			Grader grader = new ThreadGrader(directory, TRACE_FILE);
			Grader grader = new CondGrader(directory, TRACE_FILE);

			ch.deleteTraces();
			return grader.grade();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Test> error = new ArrayList<Test>();
		error.add(new Test("Error: Check console", false));
		return error;
	}
}
