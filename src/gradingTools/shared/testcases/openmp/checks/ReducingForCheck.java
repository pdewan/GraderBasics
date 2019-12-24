package gradingTools.shared.testcases.openmp.checks;

import gradingTools.shared.testcases.openmp.OpenMPForPragma;

public class ReducingForCheck extends ForInParallelCheck{
	protected boolean computeCheckStatus() {
		return  
				super.computeCheckStatus() && 
				
				computeHasReduction();
		
	}

}
