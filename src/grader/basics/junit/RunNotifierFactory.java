package grader.basics.junit;

import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;

import org.junit.runner.notification.RunNotifier;

import util.models.Hashcodetable;

public class RunNotifierFactory {
//	static RunNotifier runNotifier = new RunNotifier();
	static Hashcodetable<Project, RunNotifier> projectToRunNotifier = new Hashcodetable<>();

	public static RunNotifier getRunNotifier() {
		RunNotifier aRunNotifier = projectToRunNotifier.get(CurrentProjectHolder.getOrCreateCurrentProject());
		if (aRunNotifier == null) {
//			aRunNotifier = new AGradableRunNotifier();
			aRunNotifier = new RunNotifier();
			projectToRunNotifier.put(CurrentProjectHolder.getOrCreateCurrentProject(), aRunNotifier);
		}
		return aRunNotifier;
	}

	public static void setRunNotifier(RunNotifier runNotifier) {
		projectToRunNotifier.put(CurrentProjectHolder.getOrCreateCurrentProject(), runNotifier);
	}
}
