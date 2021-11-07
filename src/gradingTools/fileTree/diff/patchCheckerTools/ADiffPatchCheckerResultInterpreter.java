package gradingTools.fileTree.diff.patchCheckerTools;

import gradingTools.interpreter.checkers.ADiffPatchChecker;
import gradingTools.interpreter.checkers.CheckerResult;

public class ADiffPatchCheckerResultInterpreter {
	
	public static ADiffPatchCheckerResult compare(String source, String target) {
		
		CheckerResult cr = ADiffPatchChecker.check(source, target);
		
		String [] checkerData = cr.getNotes().split(" Diff\\(");
		int equalsAmount=0, incertions=0, deletions=0;
		
		for(int i=0;i<checkerData.length;i++) {
			ADiffPatchCheckerOutput useableData = new ADiffPatchCheckerOutput(checkerData[i]);
//			System.out.println(checkerData[i] + "\t"+useableData.getType()+"\t"+useableData.getText());
			
			switch(useableData.getType()) {
			case DELETE:
				deletions+=useableData.getText().length();
				break;
			case EQUAL:
				equalsAmount+=useableData.getText().length();
				break;
			case INSERT:
				incertions+=useableData.getText().length();
				break;
			case ERROR:
				try {
					throw new Exception("Could not determine type of checker output");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new ADiffPatchCheckerResult(source,target,equalsAmount,incertions,deletions);
	}
	
}
