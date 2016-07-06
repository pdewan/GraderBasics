package grader.basics.trace;


import java.util.Date;

public class SerializableGraderInfo extends GraderInfo implements CSVSerializable {
	
	public static final String COMMA_REPLACEMENT = "COMMMA";
	public static final String NEW_LINE_REPLACEMENT = "NEWLINE";


	public SerializableGraderInfo(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
	}
	
	public static final int COLUMNS_USED = 3; 

	@Override
	public String toCSVRow() {
		long timeStamp = getTimeStamp();
		Date date = new Date(timeStamp);
		String[] parts = date.toString().split(" ");
		String time = parts[3];
		return getTimeStamp() + ","  + time + "," + this.getClass().getSimpleName();
	}
	
	public static String normalize(String aString) {
		return (aString.replaceAll(",",  COMMA_REPLACEMENT)).replaceAll("\n", NEW_LINE_REPLACEMENT);
	}
	
	public static String unNormalize(String aString) {
		return (aString.replaceAll( COMMA_REPLACEMENT, ",")).replaceAll(NEW_LINE_REPLACEMENT, "\n");
	}
	
	
	public static long timeStampFromCSVRow(String[] aRow) {
		return Long.parseLong(aRow[0]);		
	}
	
	public static String classNameFromCSVRow(String[] aRow) {
		try {
		return aRow[2];
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	public static void main (String[] args) {
		String normalized = normalize("Hell,World\nGoodye,World");
		System.out.println(normalized);
		System.out.println(unNormalize(normalized));
				
		
		

	}

}
