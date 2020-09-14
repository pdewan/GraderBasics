package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import grader.basics.junit.TestCaseResult;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;


public class CheckStylePackageDeclarationTestCase extends CheckStyleTestCase {
    public CheckStylePackageDeclarationTestCase() {
        super(null, "Package declaration test case");
    }
    
 
@Override
public String negativeRegexLineFilter() {
	return "(.*)Missing package declaration(.*)";
}

protected  TestCaseResult computeResult (Project aProject, String[] aCheckStyleLines, List<String> aFailedLines, List<String> aSucceededLines, boolean autoGrade) {

	return classFractionResult(aProject, aCheckStyleLines, aFailedLines, autoGrade);
	
}


@Override
public String failMessageSpecifier(List<String> aFailedLines) {
	// TODO Auto-generated method stub
	return "Missing package declaration";
}
}

