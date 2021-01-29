package gradingTools.shared.testcases.utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import util.annotations.Explanation;
import util.annotations.MaxValue;
import util.trace.Tracer;
@MaxValue(10)
@Explanation("Tests that the configuration class for requirements.")
public abstract class AbstractConfigurationProvided extends PassFailJUnitTestCase {
//	public static final Class REGISTRY_INTERFACE = MapReduceConfiguration.class;


	Object testConfiguration ;
//	public static final String CONFIGURATION_FILE_NAME = CONFIGURATION_CLASS + ".csv";
	public static final String CONFIGURATION_FILE_NAME = "ClassRegistry"+ ".csv";
	Set<String> missingClasses;
	Collection<String> duplicateClasses;
	int numMissingClasses = 0;
	int numDuplicateClasses = 0;
	double numRequiredClasses = 0;
	double score = 1.0;
	

	public AbstractConfigurationProvided() {
		
	}
	
	public abstract Class referenceClass();
	
	public abstract Class referenceInterface();
	
	public double getScore() {
		return score;
	}
	
	public Object getTestConfiguration() {
		return testConfiguration;
	}

	
	Class configurationClass;
	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {
		try {
//			Class aConfigurationClass = Class.forName(CONFIGURATION_CLASS);
//			Class aConfigurationClass = BasicProjectIntrospection.findClassByName(project, CONFIGURATION_CLASS_2);
			
			configurationClass = BasicProjectIntrospection.
					findClassByExistingSupertype(project, referenceInterface());
			if (configurationClass == null) {
				System.err.println("Class path:" + System.getProperty("java.class.path"));

				System.err.println("Classes in project:"  + project.getClassesManager().get().getClassDescriptions());

				return fail ("Did not find an implementation of " + referenceInterface() + " see console input above");
			}
//			testConfiguration = (MapReduceConfiguration) BasicProjectIntrospection.createInstanceOfPredefinedSupertype(REGISTRY_INTERFACE);
			testConfiguration =  BasicProjectIntrospection.createInstanceAndProxy(configurationClass, referenceInterface() );
//			aConfigurationObject = testConfiguration;
			
//			Object aConfigurationObject = aConfigurationClass.newInstance();

			
			 File aProjectDirectory = project.getProjectFolder();
			 String aConfigurationFileName = aProjectDirectory.getCanonicalPath() + "/" + CONFIGURATION_FILE_NAME;
			 Object aConfigurationObject = aConfigurationObject = configurationClass.newInstance();

			 ConfigurationWriter.writeConfiguration(aConfigurationFileName, aConfigurationObject);

//			 File aFile = new File(aConfigurationFileName);
//			 if (!aFile.exists()) {
//				 aFile.createNewFile();
//			 }
//			 PrintWriter aPrintWriter = new PrintWriter (aFile);
//			 aPrintWriter.println ("time, " + System.currentTimeMillis());
//			 aPrintWriter.close();
			
			
			
		} 
		 
		catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail("Could not instantiate configuration class:" + e.getMessage());		
		
		} 
		catch (Throwable e) {
			e.printStackTrace();
			return fail("Exception occurred:" + e.getMessage());

		}
		Class aReferenceClass = referenceClass();
		
		if (aReferenceClass != null) {
		    Map<String, Class> aReferenceMapping = BasicProjectIntrospection.getPropertyClassMapping(null, aReferenceClass);
			Map<String, Class> anActualMapping = BasicProjectIntrospection.getPropertyClassMapping(project, configurationClass);
			numRequiredClasses = aReferenceMapping.size();
			missingClasses = BasicProjectIntrospection.diff(aReferenceMapping, anActualMapping);
			numMissingClasses = missingClasses.size();
			if (numMissingClasses > 0) {
				score = 0.0;
				return fail("To get style credit provide (possibly stub) missing classes for properties:" + missingClasses);
			}
			duplicateClasses = BasicProjectIntrospection.findDuplicateValues(anActualMapping);
			numDuplicateClasses = duplicateClasses.size();
			if (numDuplicateClasses == 0) {
				return pass();
			}
			score = (numRequiredClasses - numDuplicateClasses)/numRequiredClasses;
			return partialPass(score, "Style checks will be scaled by " + score + " because of duplicate configuration classes: " + duplicateClasses);
			
		}
		return pass();
	}
	
	public TestCaseResult computeResultBasedOnTaggedClasses(TestCaseResult aSuperResult) {
		if (getScore() == 1.0) {
			return aSuperResult;
		} else if (getScore() == 0) {
			return fail("Create " + getNumMissingClasses() + " missing classes to receive non zero score on this test");

		} else  {
			double anOriginalPercentage = aSuperResult.getPercentage();
			double aPercentage = getScore()*anOriginalPercentage;
			
			return partialPass(aPercentage, "Raw score of " + anOriginalPercentage + " scaled by " + getScore() + " because of duplicate tagged classes. See console trace of failing and passing lines");
		}
//	public static final String MAP_REDUCE_PROCESS_TEAM = "MapReduce Team";
//	public static final String MAP_REDUCE_SERVER = "MapReduce Server";
//	public static final String MAP_REDUCE_CLIENT_1 = "MapReduce Client 1";
//	public static final String MAP_REDUCE_CLIENT_2 = "MapReduce Client 2";
//
//	protected void setupProcesses(TestMapReduceConfiguration aTestMapReduceConfiguration) {
//		String aServerClassName = aTestMapReduceConfiguration.getServerTokenCounter().getName();
//		String aClientClassName = aTestMapReduceConfiguration.getClientTokenCounter().getName();
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setProcessTeams(Arrays.asList(MAP_REDUCE_PROCESS_TEAM));
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setTerminatingProcesses(MAP_REDUCE_PROCESS_TEAM, 
//				Arrays.asList(MAP_REDUCE_CLIENT_1));
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setProcesses(MAP_REDUCE_PROCESS_TEAM, 
//				Arrays.asList(MAP_REDUCE_SERVER, MAP_REDUCE_CLIENT_1, MAP_REDUCE_CLIENT_2));
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(
//				MAP_REDUCE_SERVER , aServerClassName);
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(
//				MAP_REDUCE_CLIENT_1 , aClientClassName);
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryPoint(
//				MAP_REDUCE_CLIENT_2 , aClientClassName);
////		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryTags(
////				CLIENT_1_NAME, Arrays.asList(client1TaggedTestCase.getTags()));
////		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setEntryTags(
////				CLIENT_2_NAME, Arrays.asList(client2TaggedTestCase.getTags()));
////		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setSleepTime(SERVER_NAME, 5000);
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//			setSleepTime(MAP_REDUCE_CLIENT_1, 7000);
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().
//			setSleepTime(MAP_REDUCE_CLIENT_2, 7000);
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getProcessTeams().forEach(team -> System.out.println("### " + team));
//	}

}

	public Set<String> getMissingClasses() {
		return missingClasses;
	}

	public Collection<String> getDuplicateClasses() {
		return duplicateClasses;
	}

	public int getNumMissingClasses() {
		return numMissingClasses;
	}

	public int getNumDuplicateClasses() {
		return numDuplicateClasses;
	}

	public double getNumRequiredClasses() {
		return numRequiredClasses;
	}

	public Class getConfigurationClass() {
		return configurationClass;
	}
}
