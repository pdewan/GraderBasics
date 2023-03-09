package gradingTools.basics.sharedTestCase.checkstyle.predefined;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import gradingTools.basics.sharedTestCase.checkstyle.BulkierThenElseRatioCheck;
import gradingTools.basics.sharedTestCase.checkstyle.CommonPropertiesAreInheritedTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.CommonSignaturesAreInheritedRatioTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.EncapsulationTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.HiddenFieldTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.MethodAccessModifierRatioCheck;
import gradingTools.basics.sharedTestCase.checkstyle.MnemonicNameRatioTestCase;
import gradingTools.basics.sharedTestCase.checkstyle.PublicMethodsOverrideRatioCheck;
import gradingTools.basics.sharedTestCase.checkstyle.ThenElseBranchingRatioCheck;
import gradingTools.basics.sharedTestCase.checkstyle.VariableHasClassTypeRatioCheck;
import util.annotations.MaxValue;
@MaxValue(100)
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	    CommonPropertiesAreInheritedTestCase.class, 
	    CommonSignaturesAreInheritedRatioTestCase.class,
		EncapsulationTestCase.class,
////A5InterfaceAsType.class,
		HiddenFieldTestCase.class, 
		MethodAccessModifierRatioCheck.class,
		MnemonicNameRatioTestCase.class,
		PublicMethodsOverrideRatioCheck.class,
		VariableHasClassTypeRatioCheck.class,
		BulkierThenElseRatioCheck.class,
		ThenElseBranchingRatioCheck.class
		

//ProjectMatchesTemplate.class,

})
public class BasicGeneralStyleSuite {

}
