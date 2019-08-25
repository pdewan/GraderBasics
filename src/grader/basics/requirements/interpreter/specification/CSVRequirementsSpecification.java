package grader.basics.requirements.interpreter.specification;

import java.util.List;

import grader.basics.file.FileProxy;

public interface CSVRequirementsSpecification {
	String DEFAULT_REQUIREMENTS_SPREADHEET_NAME = "Requirements.csv";

	public FileProxy getSpecificationSpreadsheet() ;

	public String getFileName() ;

	public boolean isValid();

	public void setValid(boolean newValue) ;

	public List<String[]> getTable();

	int getNumberOfRequirements();

	String getArg(int aRequirementNum, int anArgNum);
	public String getType(int aFeatureNum);
	
	public String getDescription(int aRequirementNum);
	
	public Double getMaxScore(int aRequirementNum) ;

	
	public String getInput(int aRequirementNum) ;
	public String getModelOutput(int aRequirementNum) ;
	public String getChecker(int aRequirementNum);

	Integer getTimeOut(int aRequirementNum);

	boolean isExtraCredit(int aRequirementNum);

	boolean isManual(int aRequirementNum);

}
