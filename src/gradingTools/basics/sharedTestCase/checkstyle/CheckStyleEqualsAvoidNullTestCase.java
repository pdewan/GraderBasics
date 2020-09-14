package gradingTools.basics.sharedTestCase.checkstyle;

import java.util.List;

import gradingTools.basics.sharedTestCase.checkstyle.CheckStyleTestCase;



public class CheckStyleEqualsAvoidNullTestCase extends CheckStyleTestCase {
	 public CheckStyleEqualsAvoidNullTestCase() {
	        super(null, "Equal avoids null test case");
	    }
    
	@Override
	public String negativeRegexLineFilter() {
		return "(.*)String literal expressions should be on the left side(.*)";
	}



	@Override
	public String failMessageSpecifier(List<String> aFailedLines) {
		// TODO Auto-generated method stub
		return "Literal should be target rather than argument of equals(Ignorecase)";
	}
  //String literal expressions should be on the left side

}

