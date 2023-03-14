package byteman.tools;

import grader.basics.project.Project;

public interface BytemanConfigurationWriter {
	void writeConfigurationIfNotWritten(Project project);
	

}
