package byteman.tools.exampleTestCases.mvcSummer;

import gradingTools.fileTree.Template;
import gradingTools.shared.testcases.packageStructure.AbstractFolderMatchesTemplateTest;

public class MVCSummerStructure extends AbstractFolderMatchesTemplateTest{

	private final static String [] stringTemplate= {
			"|- .*",
			" |- controller",
			"  |~ .*Controller.*.java",
			"  |~ .*Controller.*.java",
			" |- mains",
			"  |~ .*Main.*.java",
			"  |~ .*Main.*.java",
			" |- model",
			"  |~ .*Model.*.java",
			"  |~ .*Model.*.java",
			"  |~ .*Model.*.java",
			"  |~ .*Model.*.java",
			" |- view",
			"  |~ .*View.*.java",
			"  |~ .*View.*.java",
			"  |~ .*View.*.java",
		};

	@Override
	protected Template getTemplate() {
		return new Template(stringTemplate, "MVC Example");
	}

	@Override
	protected double getAcceptabilityCutoff() {
		return 0.95;
	}
	
}
