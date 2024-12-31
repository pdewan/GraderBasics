package valgrindpp.codegen;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.trace.Tracer;

public class Wrapper {
	List<String> imports;
	List<Function> functions;
	
	
	
//	public static final String PATH_TO_VALGRIND = "/valgrind/include/valgrind.h";
	public static final String PATH_TO_VALGRIND = "/usr/include/valgrind/valgrind.h";

	
//	public static final String TRACE_FUNCTION = 
//			"void trace(const char* format, ...) {\n"
////					+ "    printf(\"I***%ld - Thread: %lu - \",\n"
//					+ "    printf(\"%ld - Thread: %lu - \",\n"
//					+ "        time(NULL),\n"
//					+ "        pthread_self()\n"
//					+ "    );\n"
//					+ "\n"
//					+ "    va_list args;\n"
//					+ "    va_start(args, format);\n"
//					+ "    vprintf(format, args);\n"
//					+ "\n"
//					+ "    printf(\"\\n\");\n"
//					+ "}\n\n";
	
	public static final String TRACE_FUNCTION = 
			"void trace(const char* format, ...) {\n"
					+ "    printf(\"%ld - Process: %u Thread: %lu - \",\n"
					+ "        time(NULL),\n"
					+ "        getpid(),\n"
					+ "        pthread_self()\n"
					+ "    );\n"
					+ "\n"
					+ "    va_list args;\n"
					+ "    va_start(args, format);\n"
					+ "    vprintf(format, args);\n"
					+ "\n"
					+ "    printf(\"\\n\");\n"
					+ "}\n\n";
	
	public class WrapperException extends Exception {
		private static final long serialVersionUID = 1L;

		public WrapperException(String message) {
			super(message);
		}
	}
	
	public String traceFunction() {
		return TRACE_FUNCTION;
	}
	
	public Wrapper() {
		imports = new ArrayList<String>();
		functions = new ArrayList<Function>();
	}
//	public void toFile(String filename, String studentDir, String srcDir) throws Exception {

	public void toFile(String filename, String studentDir) throws Exception {
		File file = new File(Paths.get(studentDir, filename + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c").toString());
//		File file = new File(Paths.get(filename + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c").toString());
//		File file = new File(Paths.get(filename + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c").toString());

		if(file.exists()) {
			file.delete();
		}
		
		if(!file.createNewFile()) {
			throw new WrapperException("File name '" + filename + "' already exists");
		}
		
		FileWriter writer = new FileWriter(file);
		
		writer.write("#include \"" + PATH_TO_VALGRIND + "\"\n");
		for(String s: imports) {
			writer.write(s);
			writer.write("\n");
		}
		Tracer.info(this, "Written imports:" + imports);

		
		writer.write("\n");
		
		String aTraceFunction = traceFunction();
		writer.write(aTraceFunction);
		Tracer.info(this, "Written trace function:" + aTraceFunction);
		
//		writer.write(
//				"void trace(const char* format, ...) {\n"
//				+ "    printf(\"%ld - Thread: %lu - \",\n"
//				+ "        time(NULL),\n"
//				+ "        pthread_self()\n"
//				+ "    );\n"
//				+ "\n"
//				+ "    va_list args;\n"
//				+ "    va_start(args, format);\n"
//				+ "    vprintf(format, args);\n"
//				+ "\n"
//				+ "    printf(\"\\n\");\n"
//				+ "}\n\n");
		
		for(Function func: functions) {
			writer.write(func.toCString());
			writer.write("\n\n");
		}
		
		
		writer.close();
	}
}
