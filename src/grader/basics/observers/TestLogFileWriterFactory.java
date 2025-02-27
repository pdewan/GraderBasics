package grader.basics.observers;

public class TestLogFileWriterFactory {
	static AnAbstractTestLogFileWriter [] singletons = {
//			new AFineGrainedTestLogFileWriter(),
			new ATraceSourceAndTestLogWriter(),
//			new ASourceAndTestLogWriter(),
			new AStandardTestLogFileWriter()
	};

	public static AnAbstractTestLogFileWriter [] getFileWriter() {
		return singletons;
	}
	
	public static AnAbstractTestLogFileWriter getMainFileWriter() {
		return singletons[0];
	}


	public static void setFileWriter(AnAbstractTestLogFileWriter [] singleton) {
		TestLogFileWriterFactory.singletons = singleton;
	}

}
