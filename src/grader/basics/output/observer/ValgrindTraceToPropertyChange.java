package grader.basics.output.observer;

import java.util.List;

import valgrindpp.grader.ValgrindTrace;

public interface ValgrindTraceToPropertyChange extends ObjectToPropertyChange {

	List<ValgrindTrace> getValgrindTraces();

}
