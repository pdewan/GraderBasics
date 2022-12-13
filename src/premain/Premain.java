package premain;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Scanner;

import grader.byteman.injector.AnInjector;
import grader.byteman.injector.Injector;
import grader.byteman.injector.InjectorFactory;
import grader.byteman.injector.target.custom.ClassInjectionData;
import grader.byteman.injector.target.custom.FileSourceInjectionTargeter;

/**
 * Example Premain in which the injection library is used.
 */
public class Premain
{

    // The premain() will run before main if the arguments to the
    // JVM are correct. In this manner students can run the tests,
    // which will call this premain, but should never knowe this is
    // happening, and so hopefully will never need to utilize or
    // debug this premain call. 
    // Once premain() exits, the student's main() is called.
    public static void premain(String args, Instrumentation instr)
    {
        Injector injector = InjectorFactory.getInjectorSingleton();
        if (injector instanceof AnInjector) {
            ((AnInjector)injector).setInstrumentation(instr);
        }
        try {
			Scanner scan = new Scanner(new File("./injectionTarget.txt"));
			while(scan.hasNext()){
				ClassInjectionData data = new ClassInjectionData(scan.nextLine());
				try {
					injector.registerTarget(new FileSourceInjectionTargeter(data));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			scan.close();
			injector.inject();
        }catch(IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
    }

}
