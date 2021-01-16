package grader.basics.observers;

public class TestLogFileWriterFactory {
	static AnAbstractTestLogFileWriter [] singletons = {
			new AFineGrainedTestLogFileWriter(),
			new AStandardTestLogFileWriter()
	};

	public static AnAbstractTestLogFileWriter [] getFileWriter() {
		return singletons;
	}

	public static void setFileWriter(AnAbstractTestLogFileWriter [] singleton) {
		TestLogFileWriterFactory.singletons = singleton;
	}

}
