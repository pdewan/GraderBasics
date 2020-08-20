package grader.basics.execution.lisp;

import grader.basics.execution.CommandGenerator;

public class LispCommandGeneratorSelector {
	static CommandGenerator mainClassFinder = new CLispCommandGenerator();

	public static CommandGenerator getCommandGenerator() {
		return mainClassFinder;
	}

	public static void setCommandGenerator(CommandGenerator mainClassFinder) {
		LispCommandGeneratorSelector.mainClassFinder = mainClassFinder;
	}
	 public boolean hasBinaryFolder() {
			return false;
		}

}
