package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public interface MethodSNode extends SNode{

	String getMethodName();


	String getMethodType();


	List<MethodCall> getCalls();

//	String[] getMethodParameterNames();
//
//
//	String[] getMethodParameterTypes();

}