package grader.basics.execution.prolog;

import grader.basics.execution.CommandGenerator;

public class PrologCommandGeneratorSelector {
	static CommandGenerator mainClassFinder = new SwiplCommandGenerator();

	public static CommandGenerator getCommandGenerator() {
		return mainClassFinder;
	}

	public static void setCommandGenerator(CommandGenerator mainClassFinder) {
		PrologCommandGeneratorSelector.mainClassFinder = mainClassFinder;
	}

}
