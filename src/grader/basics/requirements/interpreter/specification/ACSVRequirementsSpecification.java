package grader.basics.requirements.interpreter.specification;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
//import bus.uigen.Message;
import grader.basics.file.FileProxy;

public class ACSVRequirementsSpecification implements CSVRequirementsSpecification {
	public static final String TYPE_NAME = "Type";
	public static final int TYPE_COLUMN = 0;
	public static final int DESCRIPTION_COLUMN = TYPE_COLUMN+1;
	public static final int MAX_SCORE_COLUMN = DESCRIPTION_COLUMN + 1;
	public static final  int TIMEOUT_COLUMN = MAX_SCORE_COLUMN + 1;
	public static final  int INPUT_COLUMN = TIMEOUT_COLUMN + 1;
	public static final  int MODEL_OUTPUT_COLUMN = INPUT_COLUMN + 1;
	public static final int EXTRA_CREDIT_COLUMN = MODEL_OUTPUT_COLUMN + 1;
	public static final  int CHECKER_COLUMN = EXTRA_CREDIT_COLUMN  + 1;
	public static final  int START_CHECKER_ARGUMENTS_COLUMN = CHECKER_COLUMN + 1;
	
	protected int headerRow = 0;
	protected int numRequirements;
	FileProxy specificationSpreadsheet; // w
	
	List<String[]>  table;
	protected boolean valid;
//	protected FrameworkProjectRequirements projectRequirements;
  


	public ACSVRequirementsSpecification(FileProxy aSpecificationSpreadsheet) {
		specificationSpreadsheet = aSpecificationSpreadsheet;	
		init();
	}
	
//	public ACSVRequirementsSpecification(SakaiProjectDatabase aSakaiProjectDatabase) {
//		specificationSpreadsheet = aSakaiProjectDatabase.getAssignmentDataFolder().getRequirementsSpreadsheetFile();
//		init();
//	}
//	
	protected void init() {
		maybeCreateTable();
		headerRow = getHeaderRowNum(table);
		valid = headerRow >= 0;
		makeRequirements();
		

	}
	
	protected void makeRequirements() {
		if (isValid()) {
			numRequirements = table.size() - (headerRow + 1);
			
		}
		
	}
	
	protected void maybeCreateTable() {
		if (table != null)
			return;
		createTable();
		
	}
	
	protected void createTable() {
		
		try {
			InputStream input = specificationSpreadsheet.getInputStream();
			CSVReader csvReader 	=	new CSVReader(new InputStreamReader(input));
		     table = csvReader.readAll();
			csvReader.close();
			input.close();
			
		   
	    
	    
		} catch (Exception e) {
			e.printStackTrace();
		
			
		}
		
	}
	
	
	
	public int getHeaderRowNum(List<String[]> aSheet) {
		 for (int rowNum = 0; rowNum < aSheet.size(); rowNum ++) {
			 String[] aRow = aSheet.get(rowNum);
			 if (aRow[TYPE_COLUMN].equalsIgnoreCase(TYPE_NAME))
				 return rowNum;
		 }
		 return -1;
		
	}
	
	
	
	
	
	public FileProxy getSpecificationSpreadsheet() {
		return specificationSpreadsheet;
	}

	public String getFileName() {
		return specificationSpreadsheet.getMixedCaseAbsoluteName();
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean newValue) {
		this.valid = newValue;
	}

	public List<String[]> getTable() {
		return table;
	}
	@Override
	public int getNumberOfRequirements() {
		return numRequirements;
	}
	@Override
	public String getArg(int aRequirementNum, int anArgNum) {
		try {
		int aRowNum = headerRow + 1 + aRequirementNum;
		int aColumnNum = START_CHECKER_ARGUMENTS_COLUMN + anArgNum;
		return table.get(aRowNum)[aColumnNum];
		} catch (Exception e) {
			System.out.println("Requirement " + aRequirementNum + " does not have arg " + anArgNum);
			return null;
		}
	}
	public String getType(int aRequirementNum) {
		try {
		int aRowNum = headerRow + 1 + aRequirementNum;
		return table.get(aRowNum)[TYPE_COLUMN];
		} catch (Exception e) {
			System.out.println("Requirement " + aRequirementNum + " does not have type ");
			return null;
		}
	}
	
	public String getDescription(int aRequirementNum) {
		try {
		int aRowNum = headerRow + 1 + aRequirementNum;
		return table.get(aRowNum)[DESCRIPTION_COLUMN];
		} catch (Exception e) {
			System.out.println("Requirement " + aRequirementNum + " does not have description ");
			return null;
		}
	}
	@Override
	public Double getMaxScore(int aRequirementNum) {
		try {
		int aRowNum = headerRow + 1 + aRequirementNum;
		return Double.parseDouble(table.get(aRowNum)[MAX_SCORE_COLUMN]);
		} catch (Exception e) {
			System.out.println("Requirement " + aRequirementNum + " does not have max score");
			return null;
		}
	}
	
	@Override
	public Integer getTimeOut(int aRequirementNum) {
		try {
		int aRowNum = headerRow + 1 + aRequirementNum;
		String aTimeOut = table.get(aRowNum)[TIMEOUT_COLUMN];
		if (aTimeOut == null || aTimeOut.isEmpty())
			return null;
		return Integer.parseInt(aTimeOut);
		} catch (Exception e) {
			System.out.println("Requirement " + aRequirementNum + " does not have timeout");
			return null;
		}
	}
	@Override
	public boolean isExtraCredit(int aRequirementNum) {
		try {
			int aRowNum = headerRow + 1 + aRequirementNum;
			String aSpec = table.get(aRowNum)[EXTRA_CREDIT_COLUMN].replaceAll("\\s+", "");
			return !aSpec.isEmpty();
			
			} catch (Exception e) {
				System.out.println("Requirement " + aRequirementNum + " does not have extra credit");
				return false;
			}
	}
	
	
	@Override
	public boolean isManual(int aRequirementNum) {
		try {
			int aRowNum = headerRow + 1 + aRequirementNum;
			String aSpec = table.get(aRowNum)[CHECKER_COLUMN].replaceAll("\\s+", "");
			return aSpec.isEmpty();
			
			} catch (Exception e) {
				System.out.println("Requirement " + aRequirementNum + " does not have checker column");
				return false;
			}
	}

	
	public String getInput(int aRequirementNum) {
		try {
		int aRowNum = headerRow + 1 + aRequirementNum;
		return table.get(aRowNum)[INPUT_COLUMN];
		} catch (Exception e) {
			System.out.println("Requirement " + aRequirementNum + " does not have max score ");
			return null;
		}
	}
	public String getModelOutput(int aRequirementNum) {
		try {
		int aRowNum = headerRow + 1 + aRequirementNum;
		return table.get(aRowNum)[MODEL_OUTPUT_COLUMN];
		} catch (Exception e) {
			System.out.println("Requirement " + aRequirementNum + " does not have model output");
			return null;
		}
	}
	public String getChecker(int aRequirementNum) {
		try {
		int aRowNum = headerRow + 1 + aRequirementNum;
		return table.get(aRowNum)[CHECKER_COLUMN];
		} catch (Exception e) {
			System.out.println("Requirement " + aRequirementNum + " does not have a function");
			return null;
		}
	}
	

}
