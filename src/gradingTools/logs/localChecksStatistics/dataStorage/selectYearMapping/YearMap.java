package gradingTools.logs.localChecksStatistics.dataStorage.selectYearMapping;

import gradingTools.logs.localChecksStatistics.dataStorage.SuiteMapping;

public interface YearMap {

	public SuiteMapping getMapping(int assignmentNumber);
	
}
