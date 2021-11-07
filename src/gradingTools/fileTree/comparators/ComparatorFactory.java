package gradingTools.fileTree.comparators;

import java.util.Comparator;

import gradingTools.fileTree.nodes.FileDirectoryNode;

public class ComparatorFactory {
	private static Comparator<FileDirectoryNode> fileDirectoryCompare;
	public static Comparator<FileDirectoryNode> getFileDirectoryComparator(){
		if(fileDirectoryCompare==null)
			fileDirectoryCompare=new SortBySize();
		return fileDirectoryCompare;
	}
	
	public static void setFileDirectoryComparator(Comparator<FileDirectoryNode> fdc) {
		fileDirectoryCompare=fdc;
	}
	
	private static Comparator<KeyValue> keyValueComparator;
	public static Comparator<KeyValue> getKeyValueComparator(){
		if(keyValueComparator==null)
			keyValueComparator=new SortByMatches();
		return keyValueComparator;
	}
	
	private static boolean sortFiles=false;
	public static boolean isFileSortingEnabled() {
		return sortFiles;
	}
	public static void setFileSorting(boolean sF) {
		sortFiles=sF;
	}
	
}
