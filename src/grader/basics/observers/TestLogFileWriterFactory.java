package grader.basics.observers;

public class TestLogFileWriterFactory {
	static ATestLogFileWriter singleton = new ATestLogFileWriter();

	public static ATestLogFileWriter getFileWriter() {
		return singleton;
	}

	public static void setFileWriter(ATestLogFileWriter singleton) {
		TestLogFileWriterFactory.singleton = singleton;
	}

}
