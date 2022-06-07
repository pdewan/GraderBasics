package grader.basics.trace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class AnObservablePrintStream extends PrintStream  {
	
	PrintStream delegate;

//	public AnObservablePrintStream(String fileName) throws FileNotFoundException {
//		super(fileName);
//		// TODO Auto-generated constructor stub
//	}
	public AnObservablePrintStream(PrintStream aDelegate) throws FileNotFoundException {
		super(aDelegate);
		delegate = aDelegate;
		// TODO Auto-generated constructor stub
	}
//	void	close()
//	Closes the stream.
//	void	flush()
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
//	void	print(int i)
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
public	void	println() {
	delegate.println();
}
//	Terminates the current line by writing the line separator string.
//	void	println(boolean x)
//	Prints a boolean and then terminate the line.
//	void	println(char x)
//	Prints a character and then terminate the line.
//	void	println(char[] x)
//	Prints an array of characters and then terminate the line.
//	void	println(double x)
//	Prints a double and then terminate the line.
//	void	println(float x)
//	Prints a float and then terminate the line.
//	void	println(int x)
//	Prints an integer and then terminate the line.
//	void	println(long x)
//	Prints a long and then terminate the line.
public	void	println(Object x) {
	delegate.println(x);
}
//	Prints an Object and then terminate the line.
public	void	println(String x) {
	delegate.println(x);
}
//	Prints a String and then terminate the line.
//	protected void	setError()
//	Sets the error state of the stream to true.
//	void	write(byte[] buf, int off, int len)
//	Writes len bytes from the specified byte array starting at offset off to this stream.
//	void	write(int b)
//	Writes the specified byte to this stream
//	public ObservableOutputStream(PrintStream aDelegate) {
//		delegate = aDelegate;
//	}
//	@Override
//	public void write(int b) throws IOException {
//		delegate.write(b);
//	}
	
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
	public static void main (String[] args) {
//		Class anOutClass = System.out.getClass();
		System.out.println("before redirection");
		PrintStream redirected = AnObservablePrintStream.getInstance();
		System.out.println("after redirection");

	}

}
