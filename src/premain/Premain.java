package premain;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import byteman.tools.MethodTester;
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
	
	private static Map<String, String> definedNames = new HashMap<>();
	
    // The premain() will run before main if the arguments to the
    // JVM are correct. In this manner students can run the tests,
    // which will call this premain, but should never knowe this is
    // happening, and so hopefully will never need to utilize or
    // debug this premain call. 
    // Once premain() exits, the student's main() is called.
	
//	protected static void printInjectorClassLoader (AnInjector anInjector) {
//		  try {
//				Field aField = AnInjector.class.getDeclaredField("loader");
//				aField.setAccessible(true);
//				Object aLoader = aField.get(anInjector);
//				System.out.println("injector class loader:" + aLoader);
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//		
//	}
    public static void premain(String args, Instrumentation instr)
    {
//		System.out.println(Premain.class + ":class loader:" + Premain.class.getClassLoader());

        Injector injector = InjectorFactory.getInjectorSingleton();
        if (injector instanceof AnInjector) {
            ((AnInjector)injector).setInstrumentation(instr);
            
//            printInjectorClassLoader((AnInjector) injector);
          
            
        }
        try {
			Scanner scan = new Scanner(new File("./injectionTarget.txt"));
			while(scan.hasNext()){
				String aNextLine = scan.nextLine();
//				System.out.println("to Parse:" + aNextLine);
				ClassInjectionData data = new ClassInjectionData(aNextLine);

//				ClassInjectionData data = new ClassInjectionData(scan.nextLine());
				definedNames.put(data.getClassName(), data.getDisplayName());
				
				try {
					injector.registerTarget(new FileSourceInjectionTargeter(data));
//					System.out.println("class name:" + data.getClassName());
//					Class aLoadedClass = Class.forName(data.getClassName());
//					System.out.println("loaded class loader:" + aLoadedClass.getClassLoader());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
			scan.close();
			injector.inject();
        }catch(IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
        
        System.out.println("DN: "+definedNames);
    }

    
    public static Map<String,String> getDisplayNameMapping(){
    	return definedNames;
    }
  
}
