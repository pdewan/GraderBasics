package gradingTools.shared.testcases.openmp.checks;

import java.util.List;

import gradingTools.shared.testcases.openmp.OpenMPPragma;

public interface OpenMPCheck {
	void computeCheckStatus(OpenMPPragma anOpenMPPragma);
	boolean getCheckStatus();
//	List<OpenMPPragmaAttribute> getOpenMPProperties();
	OpenMPPragma getOpenMPPragma();
//	List<OpenMPPragmaProperty> getNegativeOpenMPProperties();
	

}
