package gradingTools.fileTree.diff;

import gradingTools.fileTree.diff.implementedTreeDiffs.ARegexNameComparingFileTreeDifference;

public class FileTreeDiffFactory {

	private static FileTreeDifference singleton;
	static {
		singleton = new ARegexNameComparingFileTreeDifference();
		singleton.setCompareType(CompareType.TargetToSource);
		singleton.setCheckAllSubTrees(true);
	}
	
	public static FileTreeDifference getFileTreeDifference() {
		return singleton;
	}
	
}
