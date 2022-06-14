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


public class AnObservablePrintStream extends PrintStream implements ObservablePrintStream  {
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	PrintStream delegate;
	List<Selector> positiveOutputSelectors = new ArrayList();
	List<Selector> negativeOutputSelectors = new ArrayList();
	ObjectToPropertyChange converter = new BasicObjectToPropertyChange();
	boolean redirectionFrozen = false;

//	public AnObservablePrintStream(String fileName) throws FileNotFoundException {
//		super(fileName);
//		// TODO Auto-generated constructor stub
//	}
	
	public AnObservablePrintStream(PrintStream aDelegate) throws FileNotFoundException {
		super(aDelegate);
		delegate = aDelegate;
		// TODO Auto-generated constructor stub
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
	delegate.print(i);
	maybeConvertAndAnnounceOutput(i);

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
	delegate.println();
}
//	Terminates the current line by writing the line separator string.
public	void	println(boolean x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);
}
//	Prints a boolean and then terminate the line.
public	void	println(char x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);
}
//	Prints a character and then terminate the line.
public	void	println(char[] x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);
}
//	Prints an array of characters and then terminate the line.
public	void	println(double x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);
}
//	Prints a double and then terminate the line.
public	void	println(float x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);
}
//	Prints a float and then terminate the line.
public	void	println(int x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);
}
//	Prints an integer and then terminate the line.
public	void	println(long x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);
}
//	Prints a long and then terminate the line.
public	void	println(Object x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);

}
//	Prints an Object and then terminate the line.
public	void	println(String x) {
	delegate.println(x);
	maybeConvertAndAnnounceOutput(x);
	
}
//	Prints a String and then terminate the line.
//	protected void	setError()
//	Sets the error state of the stream to true.
public void	write(byte[] buf, int off, int len) {
	delegate.write(buf, off, len);
	
}
//	Writes len bytes from the specified byte array starting at offset off to this stream.
public	void	write(int b) {
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
		positiveOutputSelectors.add(aSelector);
		
	}
	@Override
	public void addNegativeSelector(Selector aSelector) {
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
	
	protected void convertAndAnnounceOutput(Object anOutput) {
		PropertyChangeEvent aConvertedEvent = converter.toPropertyChange(anOutput);
		propertyChangeSupport.firePropertyChange(aConvertedEvent);
	}
	
	protected void maybeConvertAndAnnounceOutput(Object anOutput) {
		if (!redirectionFrozen && checkWithSelectors(anOutput)) {
			convertAndAnnounceOutput(anOutput);	
		}
		
	}
	
	@Override
	public void setRedirectionFrozen(boolean newVal) {
		redirectionFrozen = newVal;
		
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
