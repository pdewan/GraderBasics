package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import grader.basics.concurrency.propertyChanges.Selector;
import grader.basics.config.BasicExecutionSpecificationSelector;
import util.misc.ThreadSupport;


public class AnObservablePrintStream extends PrintStream implements ObservablePrintStream  {
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	PrintStream delegate;
	List<Selector> positiveOutputSelectors = new ArrayList();
	List<Selector> negativeOutputSelectors = new ArrayList();
	ObjectToPropertyChange converter = new BasicObjectToPropertyChange();
	boolean redirectionFrozen = false;
	boolean sleepFrozen = true;
	PropertyOutputSelector propertyOutputSelector = new BasicPropertyOutputSelector();
//	boolean hidePrints = false;

//	public AnObservablePrintStream(String fileName) throws FileNotFoundException {
//		super(fileName);
//		// TODO Auto-generated constructor stub
//	}
	
	
	
	public AnObservablePrintStream(PrintStream aDelegate) throws FileNotFoundException {
		super(aDelegate);
		delegate = aDelegate;
//		hidePrints = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getHideRedirectedOutput();
		
		// TODO Auto-generated constructor stub
	}
	protected boolean hidePrints() {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getHideRedirectedOutput();
	}
public	void	close() {
	delegate.close();
}
//	Closes the stream.
public	void	flush() {
	delegate.flush();
}
//	Flushes the stream.
//	PrintStream	format(Locale l, String format, Object... args)
//	Writes a formatted string to this output stream using the specified format string and arguments.
//	PrintStream	format(String format, Object... args)
//	Writes a formatted string to this output stream using the specified format string and arguments.
//	void	print(boolean b)
//	Prints a boolean value.
//	void	print(char c)
//	Prints a character.
//	void	print(char[] s)
//	Prints an array of characters.
//	void	print(double d)
//	Prints a double-precision floating-point number.
//	void	print(float f)
//	Prints a floating-point number.
public	void	print(int i) {
	if (hidePrints()) return;	
//	delegate.print(i);
//	maybeConvertAndAnnounceOutput(i);
	if (maybeConvertAndAnnounceOutput(i)) {
		delegate.println(i);
	}

}
//	Prints an integer.
//	void	print(long l)
//	Prints a long integer.
//	void	print(Object obj)
//	Prints an object.
//	void	print(String s)
//	Prints a string.
//	PrintStream	printf(Locale l, String format, Object... args)
//	A convenience method to write a formatted string to this output stream using the specified format string and arguments.
//	PrintStream	printf(String format, Object... args)
//	A convenience method to write a formatted string to this output stream using the specified format string and arguments.
public	void println() {
	if (hidePrints()) return;
	delegate.println();
	maybeSleep();
}
//	Terminates the current line by writing the line separator string.
public	void	println(boolean x) {
	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		delegate.println(x);
	}
	maybeSleep();
}
//	Prints a boolean and then terminate the line.
public	void	println(char x) {
//	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		if (hidePrints()) return;
		delegate.println(x);
	}
	maybeSleep();
}
//	Prints a character and then terminate the line.
public	void	println(char[] x) {
//	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		if (hidePrints()) return;

		delegate.println(x);
	}
	maybeSleep();
}
//	Prints an array of characters and then terminate the line.
public	void	println(double x) {
//	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		if (hidePrints()) return;

		delegate.println(x);
	}
	maybeSleep();
}
//	Prints a double and then terminate the line.
public	void	println(float x) {
	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		if (hidePrints()) return;

		delegate.println(x);
	}
	maybeSleep();
}
//	Prints a float and then terminate the line.
public	void	println(int x) {
	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		if (hidePrints()) return;

//		delegate.println(x);
	}
	maybeSleep();
}
//	Prints an integer and then terminate the line.
public	void	println(long x) {
//	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		if (hidePrints()) return;

		delegate.println(x);
	}
	maybeSleep();
}
//	Prints a long and then terminate the line.
public	void	println(Object x) {
//	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		if (hidePrints()) return;

		delegate.println(x);
	}
	maybeSleep();


}
//	Prints an Object and then terminate the line.
public	void println(String x) {
//	if (hidePrints()) return;
//	delegate.println(x);
//	maybeConvertAndAnnounceOutput(x);
	if (maybeConvertAndAnnounceOutput(x)) {
		if (hidePrints()) return;

		delegate.println(x);
	}
	maybeSleep();
}
//	Prints a String and then terminate the line.
//	protected void	setError()
//	Sets the error state of the stream to true.
public void	write(byte[] buf, int off, int len) {
	if (hidePrints()) return;

	delegate.write(buf, off, len);
	
}
//	Writes len bytes from the specified byte array starting at offset off to this stream.
public	void	write(int b) {
	if (hidePrints()) return;

	delegate.write(b);
}
//	Writes the specified byte to this stream

	
	static PrintStream instance;
	public static PrintStream getInstance() {
		if (instance == null) {
			try {
				instance = new AnObservablePrintStream(System.out);
				System.setOut(instance);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.addPropertyChangeListener(aListener);
	}
	@Override
	public void removePropertyChangeListener(PropertyChangeListener aListener) {
		propertyChangeSupport.removePropertyChangeListener(aListener);
	}
	@Override
	public void addPositiveSelector(Selector aSelector) {
		if (positiveOutputSelectors.contains(aSelector)) {
			return;
		}
		positiveOutputSelectors.add(aSelector);
		
	}
	@Override
	public void addNegativeSelector(Selector aSelector) {
		if (negativeOutputSelectors.contains(aSelector)) {
			return;
		}
		negativeOutputSelectors.add(aSelector);
	}
	@Override
	public void registerPropertyChangeConverter(ObjectToPropertyChange aConverter) {
		converter = aConverter;
	}
	protected boolean checkWithPositiveSelectors(Object anOutput) {
		for (Selector aSelector:positiveOutputSelectors) {
			if (aSelector.selects(anOutput)) {
				return true;
			}
		}
		return false;
	}
	protected boolean checkWithNegativeSelectors(Object anOutput) {
		for (Selector aSelector:negativeOutputSelectors) {
			if (aSelector.selects(anOutput)) {
				return false;
			}
		}
		return true;
	}
	protected boolean checkWithSelectors(Object anOutput) {
		return checkWithPositiveSelectors(anOutput) && checkWithNegativeSelectors(anOutput);
	}
	
	protected boolean convertAndAnnounceOutput(Object anOutput) {
		PropertyChangeEvent aConvertedEvent = converter.toPropertyChange(anOutput);
		boolean retVal = propertyOutputSelector.selectOutput(anOutput, aConvertedEvent);
		if (aConvertedEvent != null && retVal) {
			propertyChangeSupport.firePropertyChange(aConvertedEvent); 
			
		} 
		return retVal;
//		propertyChangeSupport.firePropertyChange(aConvertedEvent);
	}
	
	
	
	protected boolean isTrace(Object anOutput) {
		return anOutput.toString().startsWith("I***");
	}
	protected boolean maybeConvertAndAnnounceOutput(Object anOutput) {
		//if (!redirectionFrozen && checkWithSelectors(anOutput)) {
		if (
				!redirectionFrozen && 
				!isTrace(anOutput) &&
				checkWithSelectors(anOutput)) {
			return convertAndAnnounceOutput(anOutput);	
		}
		return true;
		
	}
	protected void maybeSleep() {
		if (!sleepFrozen) {
			ThreadSupport.sleep(1);
		}		
	}
	
	@Override
	public void setRedirectionFrozen(boolean newVal) {
		redirectionFrozen = newVal;
		
	}
	public boolean isRedirectionFrozen() {
		return redirectionFrozen;
	}
	public boolean isSleepFrozen() {
		return sleepFrozen;
	}
	public void setSleepFrozen(boolean sleepFrozen) {
		this.sleepFrozen = sleepFrozen;
	}
	@Override
	public PropertyOutputSelector getPropertyOutputSelector() {
		return propertyOutputSelector;
	}
	@Override
	public void setPropertyOutputSelector(PropertyOutputSelector propertyOutputSelector) {
		this.propertyOutputSelector = propertyOutputSelector;
	}
	public static void main (String[] args) {
//		Class anOutClass = System.out.getClass();
		System.out.println("before redirection");
//		PrintStream redirected = AnObservablePrintStream.getInstance();P
		ObservablePrintStream aRedirectedStream = ObservablePrintStreamFactory.getObservablePrintStream();
		aRedirectedStream.addPropertyChangeListener(new BasicPrintStreamListener());
		aRedirectedStream.addPositiveSelector(new BasicPositiveOutputSelector());
		aRedirectedStream.addNegativeSelector(new BasicNegativeOutputSelector());

		System.setOut((PrintStream) aRedirectedStream);
		System.out.println("after redirection");

	}
}
