package gradingTools.fileTree.diff;

import gradingTools.fileTree.nodes.DirectoryNode;

public interface FileTreeDifference {
	public FileTreeDifferenceResult getStructureDifferencePercent(DirectoryNode source, DirectoryNode target);
	public void setCompareType(CompareType ct);
	public void setDirToFileWeight(double amount);
	public void setFileSimilarityPercentThreshold(double amount);
	public void setCheckAllSubTrees(boolean bool);
}
