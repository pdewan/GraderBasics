package grader.basics.trace;


public class SerializableFileInfo extends SerializableGraderInfo {
	
	String fileName;
	
	

	public SerializableFileInfo(String aMessage, String aFileName, Object aFinder) {
		super(aMessage, aFinder);
		fileName = aFileName;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
