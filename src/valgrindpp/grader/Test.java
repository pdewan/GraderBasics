package valgrindpp.grader;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;

public class Test {
	public String name;
	public boolean passed;
	
	public Test(String name, boolean passed) {
		this.name = name;
		this.passed = passed;
	}
	
	public Component view() {
		JLabel view = new JLabel((passed ? "PASSED - " : "FAILED - ") + name);
		view.setForeground(passed ? Color.GREEN : Color.RED);
		return view;
	}
}
