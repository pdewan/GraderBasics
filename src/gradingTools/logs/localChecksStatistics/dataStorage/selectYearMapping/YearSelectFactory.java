package gradingTools.logs.localChecksStatistics.dataStorage.selectYearMapping;

public class YearSelectFactory {

	private static YearMap ym =null;
	
	public static void setYearMap(YearMap ymp) {
		ym=ymp;
	}
	
	public static YearMap getYearMap() {
		return ym;
	}
	
}
