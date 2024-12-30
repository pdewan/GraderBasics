package grader.basics.execution.c;

import grader.basics.execution.CommandGenerator;

public class CCommandGeneratorSelector {
	static CommandGenerator mainClassFinder = new ValgrindCCommandGenerator();

	public static CommandGenerator getCommandGenerator() {
		return mainClassFinder;
	}

	public static void setCommandGenerator(CommandGenerator mainClassFinder) {
		CCommandGeneratorSelector.mainClassFinder = mainClassFinder;
	}
	 public boolean hasBinaryFolder() {
			return false;
		}

}
