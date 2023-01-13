package gradingTools.logs.bulkLogProcessing.selectYearMapping;

import gradingTools.logs.bulkLogProcessing.tools.dataStorage.SuiteMapping;

public class YearSelectFactory {

	private static YearMap ym =null;
	
	public static void setYearMap(YearMap ymp) {
		ym=ymp;
	}
	
	public static SuiteMapping getYearMap(int assignment) {
		return ym==null?null:ym.getMapping(assignment);
	}
	
}
