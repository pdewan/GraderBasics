package gradingTools.interpreter.checkers;


public class AMatchChecker implements InterpretedChecker{
	
	public int getNumArgs() {
		return 2;
	}	
	
	@Override
	public CheckerResult check(String[] anArgs) {
		try {
			
			boolean aResult = anArgs[0].matches(anArgs[1]);
			String aNotes = "";
//			if (!aResult) {
//				aNotes =  "Not matched regular expression: " + anArgs[1];
//			} else 
//				aNotes =  "Matched regular expression: " + anArgs[1];
					
			return new ACheckerResult(aNotes, aResult);

		} catch (Exception e) {
			e.printStackTrace();
			return new ACheckerResult(e.getMessage(), false);
		}

	}

	public static void main (String[] args) {
		String s = "Please input an integer\nPlease input a decimal\nThe int addition:3\nThe double addition:3.500000\nThe int multiplication:2\nThe double multiplication:2.500000\n";
		s = " \n true ";
		System.out.println(s.matches("[\\s\\S]*true.*"));

		System.out.println(s.matches(".*Please.*"));
	}
	

}
