package gradingTools.fileTree.diff.implementedTreeDiffs;

import gradingTools.fileTree.diff.AbstractFileTreeDifference;
import gradingTools.fileTree.nodes.DirectoryNode;
import gradingTools.fileTree.nodes.FileNode;

public class BasicStructureFileTreeDifference extends AbstractFileTreeDifference{

	@Override
	protected DataHolder calculateFileSimilarities(FileNode[] subdir0, FileNode[] subdir1) {
		int min = Math.min(subdir0.length, subdir1.length);
		return new DataHolder(min,min);
	}

	@Override
	protected DataHolder calculateDirectorySimilarities(DirectoryNode [] node0, DirectoryNode [] node1) {
		int min = Math.min(node0.length, node1.length);
		return new DataHolder(min,min);
	}

}
