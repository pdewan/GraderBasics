package grader.basics.junit;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.annotations.Visible;
@StructurePattern(StructurePatternNames.LIST_PATTERN)
public class AGradableJUnitTopLevelSuite extends AGradableJUnitSuite {
	public AGradableJUnitTopLevelSuite(Class aJUnitClass) {
		super(aJUnitClass);
	}

	@Visible(false)
	public String getName() {
		return super.getName();
	}
	
	@Override
	public int numLeafNodeDescendents() {
		int retVal = 0;
		for (GradableJUnitTest aTest:children) {			
			int aNumGrandChildren = aTest.numLeafNodeDescendents();
			if (aNumGrandChildren == 0) // aleaf node
				retVal++;
			else
				retVal += aNumGrandChildren;
			
		}
		return retVal;
	}
	@Override
	public int numInternalNodeDescendents() {
		int retVal = 0;
		for (GradableJUnitTest aTest:children) {			
			int aNumGrandChildren = aTest.numLeafNodeDescendents();
			if (aNumGrandChildren > 1) // do not count an artifical parent
				retVal++;		
			
		}
		return retVal;
	}
	
//	public String getText() {
//		String retVal = getName() + "\n";
//		for (GradableJUnitTest aTest:children) {
//			retVal += aTest + "\n";
//		}
//		return retVal;
//	}
	
}
