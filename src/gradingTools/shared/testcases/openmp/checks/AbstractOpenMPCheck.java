package gradingTools.shared.testcases.openmp.checks;

import java.util.ArrayList;
import java.util.List;

import gradingTools.shared.testcases.openmp.ForHeader;
import gradingTools.shared.testcases.openmp.OpenMPForPragma;
import gradingTools.shared.testcases.openmp.OpenMPParallelPragma;
import gradingTools.shared.testcases.openmp.OpenMPPragma;
import gradingTools.shared.testcases.openmp.OpenMPUtils;

public abstract class AbstractOpenMPCheck implements OpenMPCheck {
	boolean checkStatus = false;
	protected OpenMPPragma openMPPragma;
	protected List<OpenMPPragmaAttribute> properties = new ArrayList();
//	protected List<OpenMPPragmaProperty> negativeProperties = new ArrayList();

	@Override
	public void computeCheckStatus(OpenMPPragma anOpenMPPragma) {
		openMPPragma = anOpenMPPragma;
		checkStatus = false;
		properties.clear();
		checkStatus = computeCheckStatus();

	}

	protected boolean computeIsForPragma() {
		if (openMPPragma instanceof OpenMPForPragma) {
			openMPPragma.getAttributes().add(OpenMPPragmaAttribute.IS_FOR_PRAGMA);
			return true;
		}
		return false;

	}

	protected boolean computeHasParent() {
		if (openMPPragma.getParent() != null) {
			openMPPragma.getAttributes().add(OpenMPPragmaAttribute.HAS_PARENT);
			return true;
		}
		return false;

	}
	
	protected boolean isParallelized() {
		if ((openMPPragma instanceof OpenMPParallelPragma) || computeHasParallelParent()) {
			openMPPragma.getAttributes().add(OpenMPPragmaAttribute.IS_PARALLELIZED);
			return true;
		};
		return false;
	}
	
	protected boolean computeIndicesInParallelArePrivate() {
		if (!isParallelized()) {
			return true;
		}
		List<ForHeader> aForHeaders = openMPPragma.getForHeaders();
		for (ForHeader aForHeader:aForHeaders) {
			String anInitialization = aForHeader.getInitialization();
			if (OpenMPUtils.startsWithTypeName(anInitialization)) {
				continue;
			}
			
			
		}
		return true;
	}

	protected boolean computeHasReduction() {
		if (!computeIsForPragma()) {
			return false;
		}
		if (((OpenMPForPragma) openMPPragma).getReductionOperation() != null) {
			openMPPragma.getAttributes().add(OpenMPPragmaAttribute.HAS_REDUCTION);
			return true;
		}

		return false;
	}

	protected boolean computeHasParallelParent() {
		if (!computeHasParent())
			return false;
		if (openMPPragma.getParent() instanceof OpenMPParallelPragma) {
			openMPPragma.getAttributes().add(OpenMPPragmaAttribute.IN_PARALLEL_PRAGMA);
			return true;
		}

		return false;
	}

	protected abstract boolean computeCheckStatus();

	@Override
	public boolean getCheckStatus() {
		return checkStatus;
	}
//
//	@Override
//	public List<OpenMPPragmaAttribute> getOpenMPProperties() {
//		return properties;
//	}

//	@Override
//	public List<OpenMPPragmaProperty> getNegativeOpenMPProperties() {
//		return negativeProperties;
//	}

	@Override
	public OpenMPPragma getOpenMPPragma() {
		// TODO Auto-generated method stub
		return openMPPragma;
	}

}
