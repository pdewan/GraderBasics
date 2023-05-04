package byteman.tools;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class InjectionTargetFileReader {
//	static boolean isTester = false;
	public static void readFile(String aFromFileName, boolean isTester) {
//		StringBuilder aStringBuilder = new StringBuilder();
	      try {
				Scanner scan = new Scanner(new File(aFromFileName));
				while(scan.hasNext()){
					String aNextLine = scan.nextLine();
//					System.out.println("to Parse:" + aNextLine);
					ClassInjectionData data = new ClassInjectionData(aNextLine);
					if (isTester) {
						BytemanRulesGenerator.addClassBytemanRule(data);
					}
					else {

						NameDefiner.put(data.getClassName(), data.getDisplayName());
					}

					
				}
				scan.close();
//				return aStringBuilder.toString();
//				injector.inject();
	      }catch(IllegalArgumentException | IOException e) {
				e.printStackTrace();
			}
//	      return null;
		}

}
