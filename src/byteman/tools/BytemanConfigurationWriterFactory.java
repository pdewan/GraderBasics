package byteman.tools;

import grader.basics.project.Project;

public class BytemanConfigurationWriterFactory {
	static BytemanConfigurationWriter bytemanConfigurationWriter = 
			new ClassRegistryBytemanConfigurationWriter();
	
//	static BytemanConfigurationWriter bytemanConfigurationWriter = 
//			new CheckstyleConfigurationBytemanConfigurationWriter();

	public static BytemanConfigurationWriter getBytemanConfigurationWriter() {
		return bytemanConfigurationWriter;
	}

	public static void setBytemanConfigurationWriter(BytemanConfigurationWriter newVal) {
		BytemanConfigurationWriterFactory.bytemanConfigurationWriter = newVal;
	}
	
	public static void writeConfiguration(Project project) {
		bytemanConfigurationWriter.writeConfigurationIfNotWritten(project);
	}
	

}
;