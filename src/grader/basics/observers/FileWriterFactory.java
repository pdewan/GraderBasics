package grader.basics.observers;

public class FileWriterFactory {
	static AFileWriter singleton = new AFileWriter();

	public static AFileWriter getFileWriter() {
		return singleton;
	}

	public static void setFileWriter(AFileWriter singleton) {
		FileWriterFactory.singleton = singleton;
	}

}
