package grader.basics.junit;

import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;
import grader.basics.vetoers.AJUnitRunVetoer;
import grader.basics.vetoers.JUnitRunVetoer;
import util.models.Hashcodetable;

public class RunVetoerFactory {
//	static RunNotifier runNotifier = new RunNotifier();
	static Hashcodetable<Project, JUnitRunVetoer > projectToRunVetoer = new Hashcodetable<>();

	public static JUnitRunVetoer getOrCreateRunVetoer() {
		JUnitRunVetoer aRunVetoer = projectToRunVetoer.get(CurrentProjectHolder.getOrCreateCurrentProject());
		if (aRunVetoer == null) {
//			aRunNotifier = new AGradableRunNotifier();
			aRunVetoer = new AJUnitRunVetoer();
			projectToRunVetoer.put(CurrentProjectHolder.getOrCreateCurrentProject(), aRunVetoer);
		}
		return aRunVetoer;
	}
	public static JUnitRunVetoer getRunVetoer() {
		return projectToRunVetoer.get(CurrentProjectHolder.getOrCreateCurrentProject());		
	}

	public static void setRunVetoer(JUnitRunVetoer newVal) {
		projectToRunVetoer.put(CurrentProjectHolder.getOrCreateCurrentProject(), newVal);
	}
}
