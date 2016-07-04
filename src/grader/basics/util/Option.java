package grader.basics.util;

public class Option<T> {
	T actualValue;
	boolean empty = true;
	public Option (T anActualValue) {
		actualValue = anActualValue;
		empty = false;
	}
	public Option() {
		
	}
	public boolean isEmpty() {
		return empty;
	}
	public boolean isDefined() {
		return !empty;
	}
	public static<T> Option apply (T anActualValue) {
		return new Option(anActualValue);
	}
	public T get() {
		return actualValue;
	}
	
	public static Option empty() {
		return new Option();
	}
	

}
