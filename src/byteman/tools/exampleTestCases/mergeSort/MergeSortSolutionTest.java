package byteman.tools.exampleTestCases.mergeSort;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanUnitTest;

public class MergeSortSolutionTest extends AbstractBytemanUnitTest{

//	Integer [] numbers = {7, 12, 2, 20, 19, 4, 9, 16, 10, 1, 8, 11, 3, 17, 6, 15, 18, 13, 5, 14};
//	Integer [] solution = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	
	int [] numbers = {7, 12, 2, 20, 19, 4, 9, 16, 10, 1, 8, 11, 3, 17, 6, 15, 18, 13, 5, 14};
	int [] solution = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	String [] regexes = {};
	
	
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
		return new Object [] {numbers};
	}


	@Override
	protected String[] getRegexes() {
		return regexes;
	}


	@Override
	protected boolean testMethodReturnMatches(Object returnValue) {
		if(returnValue.getClass().equals(returnValue.getClass()) || ((Integer[])returnValue).length != solution.length) {
//			Integer [] studentSolution = (Integer[])returnValue;
			
			int [] studentSolution = (int[])returnValue;

			for(int i=0;i<solution.length;i++) {
				if(studentSolution[i] != solution[i])

//				if(studentSolution[i].intValue() != solution[i].intValue())
					return false;
			}
			return true;
		}
		
		return false;
	}


	@Override
	protected String getExpectedResult() {
		return Arrays.asList(this.solution).toString();
	}
	String[] tagsArray = {
			"MergeSort"
	};
	Set<String> tagsSet = new HashSet(Arrays.asList(tagsArray));
	@Override
	protected Set<String> getTags() {
		// TODO Auto-generated method stub
		return tagsSet;
	}
}
