package gradingTools.fileTree.comparators;

import java.util.Comparator;

import gradingTools.fileTree.nodes.FileDirectoryNode;

public 	class SortBySize implements Comparator<FileDirectoryNode>{
	@Override
	public int compare(FileDirectoryNode arg0, FileDirectoryNode arg1) {
		return arg0.getSize()-arg1.getSize();
	}
}
