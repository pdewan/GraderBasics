package grader.basics.output.observer;

public class ValgrindTraceToPropertyChangeFactory {
	static ValgrindTraceToPropertyChange singleton = new BasicValgrindTraceToPropertyChange();

	public static ValgrindTraceToPropertyChange getSingleton() {
		return singleton;
	}

	public static void setSingleton(ValgrindTraceToPropertyChange newVal) {
		ValgrindTraceToPropertyChangeFactory.singleton = newVal;
	}
	
}
