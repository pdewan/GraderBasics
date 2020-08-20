package grader.basics.execution.sml;

import grader.basics.execution.CommandGenerator;

public class SMLCommandGeneratorSelector {
	static CommandGenerator mainClassFinder = new SMLCommandGenerator();

	public static CommandGenerator getCommandGenerator() {
		return mainClassFinder;
	}

	public static void setCommandGenerator(CommandGenerator mainClassFinder) {
		SMLCommandGeneratorSelector.mainClassFinder = mainClassFinder;
	}

}
