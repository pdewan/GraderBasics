package gradingTools.logs.bulkLogProcessing.selectYearMapping;

import gradingTools.logs.bulkLogProcessing.tools.dataStorage.SuiteMapping;

public interface YearMap {

	public SuiteMapping getMapping(int assignmentNumber);
	
}
