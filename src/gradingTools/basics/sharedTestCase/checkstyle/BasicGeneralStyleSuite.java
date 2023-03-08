package gradingTools.basics.sharedTestCase.checkstyle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import util.annotations.MaxValue;
@MaxValue(100)
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
//	    CommonPropertiesAreInheritedTestCase.class, 
//	    CommonSignaturesAreInheritedRatioTestCase.class,
//		EncapsulationTestCase.class,
////A5InterfaceAsType.class,
//		HiddenFieldTestCase.class, 
//		MethodAccessModifierRatioCheck.class,
//		MnemonicNameRatioTestCase.class,
//		PublicMethodsOverrideRatioCheck.class,
//		VariableHasClassTypeRatioCheck.class,
//	BulkierThenElseRatioCheck.class,
	ThenElseBranchingRatioCheck.class
		

//ProjectMatchesTemplate.class,

})
public class BasicGeneralStyleSuite {

}
