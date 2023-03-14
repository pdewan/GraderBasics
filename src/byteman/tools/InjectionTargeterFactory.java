package byteman.tools;

import java.io.IOException;

import grader.basics.project.Project;

public class InjectionTargeterFactory {
	static InjectionTargeter injectionTargeter;

	public static InjectionTargeter getInjectionTargeter() throws IOException {
		if (injectionTargeter == null) {
			
				injectionTargeter = new InjectionTargeter();
			
		}
		return injectionTargeter;
	}

	public static void setInjectionTargeter(InjectionTargeter injectionTargeter) {
		InjectionTargeterFactory.injectionTargeter = injectionTargeter;
	}
	
//	public static InjectionTargeter getOrCreateInitializedInjectionTargeter(Project aProject) throws IOException {
//		if (injectionTargeter == null) {
//			
//				injectionTargeter = new InjectionTargeter();
//				BytemanConfigurationWriterFactory.writeConfiguration(aProject);
//			
//		}
//		return injectionTargeter;
//	}
	

}
