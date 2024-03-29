package byteman.tools.exampleTestCases.mergeSort;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanUnitTest;

public class MergeSortRecursionTest extends AbstractBytemanUnitTest {

//	Integer [] numbers = {7, 12, 2, 20, 19, 4, 9, 16, 10, 1, 8, 11, 3, 17, 6, 15, 18, 13, 5, 14};
//	Integer [] solution = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};

	int[] numbers = { 7, 12, 2, 20, 19, 4, 9, 16, 10, 1, 8, 11, 3, 17, 6, 15, 18, 13, 5, 14 };
	int[] solution = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };

	String[] regexes = { ".*exited sort.*19.*", ".*exited sort.*19,20.*", ".*exited sort.*2,19,20.*",
			".*exited sort.*2,7,12,19,20.*", ".*exited sort.*1,10.*", ".*exited sort.*1,10,16.*",
			".*exited sort.*1,4,9,10,16.*", ".*exited sort.*1,2,4,7,9,10,12,16,19,20.*", ".*exited sort.*6,17.*",
			".*exited sort.*3,6,17.*", ".*exited sort.*3,6,8,11,17.*", ".*exited sort.*5,14.*",
			".*exited sort.*5,13,14.*", ".*exited sort.*5,13,14,15,18.*", ".*exited sort.*3,5,6,8,11,13,14,15,17,18.*",
//		".*exited sort.*1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20.*",
	};

//	@Override
//	protected Class<?> getTarget() {
//		return getRegistry().getMergeSort();
//	}

	@Override
	protected String getMethodName() {
		return "sort";
	}

	@Override
	protected Object[] getArgs() {
		return new Object[] { numbers };
	}

	@Override
	protected String[] getRegexes() {
		return regexes;
	}

	@Override
	protected Class[] getArgTypes() {
		return new Class[] { int[].class };
	}

//	@Override
//	protected boolean testMethodReturnMatches(Object returnValue) {
//		if(!(returnValue instanceof Integer[]) || ((Integer[])returnValue).length != solution.length) {
//			Integer [] studentSolution = (Integer[])returnValue;
//			for(int i=0;i<solution.length;i++) {
//				if(studentSolution[i] != solution[i])
//					return false;
//			}
//			return true;
//		}
//		
//		return false;
//	}
//	public  Object[] toObjectArray(Object o) {
//		if(!o.getClass().isArray()) return null;
//		if(!o.getClass().getComponentType().isPrimitive()) return (Object [])o;
//		int arrlen = Array.getLength(o);
//		Object [] retval = new Object[arrlen];
//		for(int i=0;i<arrlen;i++) {
//			retval[i]=Array.get(o, i);
//		}
//		return retval;
//	}
	@Override
	protected boolean testMethodReturnMatches(Object returnValue) {
		System.out.println(returnValue.getClass());
		if (returnValue.getClass().isArray()) {
			Object[] returnArray = toObjectArray(returnValue);

			int[] studentSolution = (int[]) returnValue;
			for (int i = 0; i < returnArray.length; i++) {
				int anElement = (Integer) returnArray[i];
				if (anElement != solution[i])
					return false;
			}
		}
//
//		if(!(returnValue instanceof int[]) || ((int[])returnValue).length != solution.length) {
//			int [] studentSolution = (int[])returnValue;
//			for(int i=0;i<solution.length;i++) {
//				if(studentSolution[i] != solution[i])
//					return false;
//			}
//			return true;
//		}
//		
		return true;
	}

	@Override
	protected String getExpectedResult() {
		return Arrays.asList(this.solution).toString();
	}

	String[] tagsArray = { "MergeSort" };
	Set<String> tagsSet = new HashSet(Arrays.asList(tagsArray));

	@Override
	protected Set<String> getTags() {
		// TODO Auto-generated method stub
		return tagsSet;
	}

}
