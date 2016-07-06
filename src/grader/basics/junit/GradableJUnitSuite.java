package grader.basics.junit;

import java.util.List;
import java.util.Set;


public interface GradableJUnitSuite extends GradableJUnitTest{
//	List<GradableJUnitTest> getJUnitTests();
	public int size() ;
	public void add(GradableJUnitTest anElement) ;
	public void addAll(List<GradableJUnitTest> anElement) ;
	void testAll();
	Set<Class> getPreviousFailClasses();
	Set<Class> getPreviousPassClasses();
	Set<Class> getPreviousPartialPassClasses();	

}
