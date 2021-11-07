package gradingTools.fileTree.comparators;

import java.util.Comparator;

import gradingTools.fileTree.nodes.FileDirectoryNode;

public 	class SortBySizeLargeToSmall implements Comparator<FileDirectoryNode>{
	@Override
	public int compare(FileDirectoryNode arg0, FileDirectoryNode arg1) {
		return arg1.getSize()-arg0.getSize();
	}
}
