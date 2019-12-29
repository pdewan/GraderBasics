package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public interface MethodCall extends SNode{

	String getMethodName();

	List<String> getMethodActuals();

}