package grader.basics.junit;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import util.models.PropertyListenerRegisterer;


public interface GradableJUnitTest extends PropertyListenerRegisterer, Serializable  {
	public static final String TEST_RUN_STARTED = "TestRunStarted";
	public static final String TEST_RUN_FINISHED = "TestRunEnded";
	public void init() ;
	public Class getJUnitClass() ;
	
	public void setDefaultScore(int aDefaultScore);
	
	public int getDefaultScore() ;
	
	public void setMaxScore (Class aJUnitClass) ;
	public void setMaxScore (double aMaxScore);
	public void setIsRestriction (Class aJUnitClass) ;
	public void setIsExtra (Class aJUnitClass) ;
	
	public void setExplanation (Class aJUnitClass) ;

	public void setGroup (Class aJUnitClass) ;
	
	public void setJUnitClass(Class aJUnitClass) ;
	public boolean isRestriction() ;
	public boolean isExtra();
	public Double getMaxScore() ;
	public String getExplanation() ;
	String getGroup();
	public void setGroup(String newVal);
	void setRestriction(boolean newVal);
	void setExtra(boolean newVal);
	void setExplanation(String newVal);
	TestCaseResult test();
	String getMessage();
	String getStatus();
	int numExecutions();
	void addPropertyChangeListenerRecursive(PropertyChangeListener arg0);
	double getFractionComplete();
	List<Double> getPercentages();
	List<String> getMessages();
	List<TestCaseResult> getTestCaseResults();
	double getScore();
	String getText();
	double getComputedMaxScore();
	int numLeafNodeDescendents();
	int numInternalNodeDescendents();
	Set<Class> getLeafClasses();
	Set<Class> getPassClasses();
	Set<Class> getPartialPassClasses();
	Set<Class> getFailClasses();
//	void setTopLevelSuite(GradableJUnitSuite newVal);
//	GradableJUnitSuite getTopLevelSuite();
//	Class[] getUntestedClasses();
//	void setWriteToConsole(boolean newVal);
//	boolean isWriteToConsole();
//	boolean isWriteToFile();
//	void setWriteToFile(boolean writeToFile);
//	boolean isWriteToServer();
	Set<Class> getUntestedClasses();

}
