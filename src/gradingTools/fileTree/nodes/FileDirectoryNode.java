package gradingTools.fileTree.nodes;

import java.io.File;

public abstract class FileDirectoryNode {

	private final File file;
	private final int level;
	protected int height;
	private final FileDirectoryNode parent;
	private final String name;
	private int size=0, numSubDirs=0;
	private final boolean isDir;
	private String replaceFileName=null;

	
	public FileDirectoryNode(FileDirectoryNode parent,File dir, int level) {
		if(!dir.exists())
			throw new IllegalArgumentException("Provided directory does not exist: "+dir.getAbsolutePath());
		this.parent=parent;
		this.file=dir;
		this.level=level;
		name=dir.getName();
		isDir=dir.isDirectory();
	}
	
	public FileDirectoryNode(String name, FileDirectoryNode parent, int level, boolean isDir) {
		if(isDir)
			this.name=name.replaceFirst("[ \t]*\\|-[ \t]*", "").replaceAll("[ \t\r\n]*$", "");
		else
			this.name=name.replaceFirst("[ \t]*\\|~[ \t]*", "").replaceAll("[ \t\r\n]*$", "");
		this.file=null;
		this.level=level;
		this.isDir=isDir;
		this.parent=parent;
	}
	
	
	public int getSize() {
		return size;
	}
	
	protected void incrementSize(int amount) {
		size+=amount;
		if(parent!=null && isDir) 
			parent.incrementSize(amount);
	}
	
	protected void increaseSubDirs(int amount) {
		numSubDirs+=amount;
		if(parent!=null)
			parent.increaseSubDirs(amount);
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getNumSubDirs() {
		return numSubDirs;
	}
	
	public int getNumSubFiles() {
		return size-numSubDirs;
	}
	
	public File getFile() {
		return file;
	}
	
	public boolean isDirectory() {
		return isDir;
	}
	
	public void setName(String name) {
		replaceFileName=name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toStringNonRec() {
		StringBuilder retVal=new StringBuilder();
		for(int i=0;i<level;i++) 
			retVal.append(" ");
		
		if(replaceFileName==null)
			retVal=retVal.append((isDir?"|- ":"|~ ")+name);
		else
			retVal=retVal.append((isDir?"|- ":"|~ ")+replaceFileName);
		return retVal.toString();
	}
	
	@Override
	public String toString() {
		String retVal=parent==null?"":"\n";
		for(int i=0;i<level;i++) 
			retVal=retVal.concat(" ");
		
		if(replaceFileName==null)
			retVal=retVal.concat((isDir?"|- ":"|~ ")+name+" ("+size+")");
		else
			retVal=retVal.concat((isDir?"|- ":"|~ ")+replaceFileName+" ("+size+")");
		
		return retVal;
	}
	
}
