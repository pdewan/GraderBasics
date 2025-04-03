package gradingTools.shared.testcases.valgrindTestCases;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import grader.basics.concurrency.propertyChanges.ConcurrentEventUtility;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChange;
import grader.basics.concurrency.propertyChanges.ConcurrentPropertyChangeSupport;
import grader.basics.concurrency.propertyChanges.BasicValgrindConcurrentPropertyChangeSupport;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.RunningProject;
import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.output.observer.ValgrindRepetitionBasedPropertyChangeSelector;
import grader.basics.output.observer.ValgrindTraceToPropertyChange;
import grader.basics.output.observer.ValgrindTraceToPropertyChangeFactory;
import grader.basics.output.observer.BasicPositiveOutputSelector;
import grader.basics.output.observer.BasicValgrindTraceToPropertyChange;
import grader.basics.output.observer.ObservablePrintStream;
import grader.basics.output.observer.ObservablePrintStreamFactory;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;
import grader.basics.testcase.PassFailJUnitTestCase;
import gradingTools.utils.RunningProjectUtils;
import valgrindpp.grader.ValgrindTrace;

public class ProducerConsumerOutput extends PassFailJUnitTestCase {
	ConcurrentPropertyChangeSupport concurrentPropertyChangeSupport = new BasicValgrindConcurrentPropertyChangeSupport();
	ConcurrentPropertyChange[] concurrentPrpertyChanges;
	public ConcurrentPropertyChange[] getConcurrentPrpertyChanges() {
		return concurrentPrpertyChanges;
	}

	public ConcurrentPropertyChangeSupport getConcurrentPropertyChangeSupport() {
		return concurrentPropertyChangeSupport;
	}

	public ValgrindTraceToPropertyChange getValgrindTraceToPropertyChange() {
		return valgrindTraceToPropertyChange;
	}

	ValgrindTraceToPropertyChange valgrindTraceToPropertyChange = ValgrindTraceToPropertyChangeFactory.getSingleton();

	@Override
	public TestCaseResult test(Project project, boolean autoGrade)
			throws NotAutomatableException, NotGradableException {

		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setOutputValgrindTrace(true);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setMaxOutputLines(20000); // these will not
																										// be actually
																										// output
		ObservablePrintStream anObservablePrintStream = ObservablePrintStreamFactory.getObservablePrintStream();
		valgrindTraceToPropertyChange = ValgrindTraceToPropertyChangeFactory.getSingleton();
		anObservablePrintStream.registerPropertyChangeConverter(valgrindTraceToPropertyChange);
		anObservablePrintStream.addPropertyChangeListener(concurrentPropertyChangeSupport);
		anObservablePrintStream.addPositiveSelector(new BasicPositiveOutputSelector());
		anObservablePrintStream.setPropertyOutputSelector(new ValgrindRepetitionBasedPropertyChangeSelector());
		PrintStream anOriginalOut = System.out;
		System.setOut((PrintStream) anObservablePrintStream);

		RunningProject noInputRunningProject = RunningProjectUtils.runProject(project, 1);
		String mutexOut = noInputRunningProject.await();
		System.setOut(anOriginalOut);
		concurrentPrpertyChanges = concurrentPropertyChangeSupport.getConcurrentPropertyChanges();
		if (concurrentPrpertyChanges.length == 0) {
			return fail ("No Trace Found");
		}
		return pass();
		
//		Map<Object, List<ConcurrentPropertyChange>> aByPropertyView = ConcurrentEventUtility
//				.getConcurrentEventListByProperty(anOriginalEvents);
//		Map<Object, List<ConcurrentPropertyChange>> aBySourceView = ConcurrentEventUtility
//				.getConcurrentEventListBySource(anOriginalEvents);
//		Map<Thread, List<ConcurrentPropertyChange>> aByThreadView = ConcurrentEventUtility
//				.getConcurrentPropertyChangeListByThread(anOriginalEvents);
//		Map<Object, List<ConcurrentPropertyChange>> aByNewValueView = ConcurrentEventUtility
//				.getConcurrentEventListByNewValue(anOriginalEvents);
//		Map<String, List<String>> aLinesMap = noInputRunningProject.getProcessOutputLines();
//		List<ValgrindTrace> aValgrindTraces = valgrindTraceToPropertyChange.getValgrindTraces();
//		int i = 1;
//		List<String> aMainLines = aLinesMap.get("main");

//		List<ValgrindTrace> aMainTraces = new ArrayList();
//		List<String> aMainOutputs = new ArrayList();
//		for (String aLine: aMainLines) {
//			try {
//				aMainTraces.add(new ValgrindTrace(aLine));
//			} catch (Exception e) {
//				aMainOutputs.add(aLine);
//				// TODO Auto-generated catch block
////				e.printStackTrace();
//			}
//		}

//		String anOut = noInputRunningProject.getOutput();
//
//		System.out.println("mutex_out\n " + anOut);
//		return pass();
	}

}
