package gradingTools.fileTree.nodes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gradingTools.fileTree.comparators.ComparatorFactory;

public class DirectoryNode extends FileDirectoryNode{

	private final DirectoryNode [] subDirs;
	private final FileNode [] subFiles;
	
	public DirectoryNode(String string) {
		this(new File(string));
	}
	
	public DirectoryNode(File dir) {
		this(null,dir,0);
	}
	
	public DirectoryNode(DirectoryNode parent, File dir, int level) {
		super(parent,dir,level);
		File [] subDi = dir.listFiles(File::isDirectory);
		File [] subFi = dir.listFiles(File::isFile);
		subDirs = new DirectoryNode[subDi.length];
		subFiles = new FileNode[subFi.length];
		
		incrementSize(subDi.length+subFi.length);		
		increaseSubDirs(subDi.length);
		
		int maxDirHeight=0;
		for(int i=0;i<subDi.length;i++) {
			subDirs[i]= new DirectoryNode(this,subDi[i],level+1);
			maxDirHeight = subDirs[i].getHeight() > maxDirHeight ? subDirs[i].getHeight() : maxDirHeight;
		}
		for(int i=0;i<subFi.length;i++) {
			subFiles[i]=new FileNode(this,subFi[i],level+1);
		}
		
		if(ComparatorFactory.isFileSortingEnabled()) {
			Arrays.sort(subDirs,ComparatorFactory.getFileDirectoryComparator());
			Arrays.sort(subFiles,ComparatorFactory.getFileDirectoryComparator());
		}
		
		if(maxDirHeight==0) 
			height = subFiles.length==0?0:1;
		else
			height = maxDirHeight+1;
		
		
	}
	
	public DirectoryNode(List<String> template) {
		this(template,null,0);
	}
	
	public DirectoryNode(List<String> template, DirectoryNode parent, int level) {
		//Set up
		super(template.remove(0),parent,level,true);
		List<DirectoryNode> subDi = new ArrayList<DirectoryNode>();
		List<FileNode> subFi = new ArrayList<FileNode>();
		int nextLevel = level+1;
		
		//discovery
		for(;;) {
			if(template.size()==0)
				break;
			String str = template.get(0);
			
			char [] chars = str.toCharArray();
			int charlevel = 0;
			for(char c:chars)
				if(c==' '||c=='\t') charlevel++;
				else break;
			if(charlevel<nextLevel)
				break;
			if(str.contains("|-"))
				subDi.add(new DirectoryNode(template,this,nextLevel));
			else
				subFi.add(new FileNode(template,this,nextLevel));
		}

		
		//resolution
		subDirs = new DirectoryNode[subDi.size()];
		subFiles = new FileNode[subFi.size()];
		subDi.toArray(subDirs);
		subFi.toArray(subFiles);
		
		incrementSize(subDi.size()+subFi.size());		
		increaseSubDirs(subDi.size());
		
		if(ComparatorFactory.isFileSortingEnabled()) {
			Arrays.sort(subDirs,ComparatorFactory.getFileDirectoryComparator());
			Arrays.sort(subFiles,ComparatorFactory.getFileDirectoryComparator());
		}
		
		int maxDirHeight=0;
		for(DirectoryNode dn:subDi)
			maxDirHeight = maxDirHeight>dn.getHeight()?dn.getHeight():maxDirHeight;
		if(maxDirHeight==0) 
			height = subFiles.length==0?0:1;
		else
			height = maxDirHeight+1;
		
	}


	@Override
	public String toString() {
		String retVal = super.toString();
		for(FileDirectoryNode file:subFiles)
			retVal=retVal.concat(file.toString());
		for(FileDirectoryNode directories:subDirs)
			retVal=retVal.concat(directories.toString());
		return retVal;
	}
	
	public DirectoryNode[] getSubDirectories() {
		return subDirs;
	}
	
	public FileNode[] getSubFiles() {
		return subFiles;
	}

}
