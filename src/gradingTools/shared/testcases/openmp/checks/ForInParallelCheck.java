package gradingTools.shared.testcases.openmp.checks;

import java.util.ArrayList;
import java.util.List;

import gradingTools.shared.testcases.openmp.OpenMPForPragma;
import gradingTools.shared.testcases.openmp.OpenMPParallelPragma;
import gradingTools.shared.testcases.openmp.OpenMPPragma;

public class ForInParallelCheck extends AbstractOpenMPCheck implements OpenMPCheck{
	
	public void computeCheckStatus(OpenMPPragma anOpenMPPragma) {
		super.computeCheckStatus(anOpenMPPragma);
	}
	protected boolean computeCheckStatus() {
		return computeIsForPragma() && computeHasParallelParent();		 
	}	

}
