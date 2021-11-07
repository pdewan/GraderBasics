package gradingTools.fileTree.comparators;

import gradingTools.fileTree.nodes.DirectoryNode;

public class KeyValue {

	private final int value;
	private final DirectoryNode base;
	private final DirectoryNode compare;
	
	public KeyValue(DirectoryNode base,DirectoryNode compare, int value) {
		this.base=base;
		this.compare=compare;
		this.value=value;
	}
	
	public DirectoryNode getBase() {
		return base;
	}
	
	public DirectoryNode getCompare() {
		return compare;
	}
	
	public int getValue() {
		return value;
	}

}
