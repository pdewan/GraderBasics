package gradingTools.fileTree.diff.implementedTreeDiffs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gradingTools.fileTree.diff.AbstractFileTreeDifference;
import gradingTools.fileTree.nodes.DirectoryNode;
import gradingTools.fileTree.nodes.FileNode;

public class FileTypeTreeDifference extends AbstractFileTreeDifference{

	@Override
	protected DataHolder calculateFileSimilarities(FileNode[] subdir0, FileNode[] subdir1) {
		List<FileNode> search = new ArrayList<FileNode>();
		Collections.addAll(search, subdir1);
		
		int counter=0;
		for(FileNode fn: subdir0) 
			for(int i=0;i<search.size();i++) 
				if(search.get(i).getFileType().equals(fn.getFileType())){
					counter++;
					search.remove(i);
					break;
				}
		return new DataHolder(counter,counter);
	}

	@Override
	protected DataHolder calculateDirectorySimilarities(DirectoryNode [] node0, DirectoryNode [] node1) {
		int min = Math.min(node0.length, node1.length);
		return new DataHolder(min,min);
	}
	
}
