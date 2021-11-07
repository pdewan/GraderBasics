package gradingTools.fileTree.nodes;

import java.io.File;
import java.util.List;

import gradingTools.fileTree.ReadFile;

public class FileNode extends FileDirectoryNode{

	private final String fileType;
	
	public FileNode(DirectoryNode parent, File dir, int level) {
		super(parent,dir,level);
		height=0;
		fileType=dir.getName().replaceAll(".*\\.", "");
		int numLines=ReadFile.readLines(dir).size();
		incrementSize(numLines);
		
	}

	public FileNode(List<String> template, DirectoryNode parent, int level) {
		super(template.remove(0),parent,level,false);
		fileType=this.getName().replaceAll(".*\\.", "");
		incrementSize(0);
	}

	public String getFileType() {
		return fileType;
	}
	

	
}