package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AConstDeclarationSNode extends ADeclaringAssignmentSNode implements ConstDeclarationSNode  {
	
	public AConstDeclarationSNode(int aLineNumber, String aTypeName, String lhs, String operationAndRHS) {
		super(aLineNumber, aTypeName, lhs, operationAndRHS);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return OMPSNodeUtils.CONST + " " + super.toString();
	}
	
}
