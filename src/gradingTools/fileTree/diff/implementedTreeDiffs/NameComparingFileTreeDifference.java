package gradingTools.fileTree.diff.implementedTreeDiffs;

import gradingTools.fileTree.diff.AbstractFileTreeDifference;
import gradingTools.fileTree.diff.patchCheckerTools.ADiffPatchCheckerResultInterpreter;
import gradingTools.fileTree.nodes.DirectoryNode;
import gradingTools.fileTree.nodes.FileDirectoryNode;
import gradingTools.fileTree.nodes.FileNode;

public class NameComparingFileTreeDifference extends AbstractFileTreeDifference {
	
	private boolean considerFileTypes;
	private boolean excludeMismatchedFiles;
	
	public NameComparingFileTreeDifference() {
		this(true,true);
	}
	public NameComparingFileTreeDifference(boolean considerFileTypes,boolean excludeMisMatchedFiles) {
		this.considerFileTypes=considerFileTypes;
		this.excludeMismatchedFiles=excludeMisMatchedFiles;
	}
	
	
	@Override
	protected DataHolder calculateFileSimilarities(FileNode[] subdir0, FileNode[] subdir1) {
		return compareNodes(subdir0,subdir1);
	}

	@Override
	protected DataHolder calculateDirectorySimilarities(DirectoryNode [] node0, DirectoryNode [] node1) {
		return compareNodes(node0,node1);
	}

	protected DataHolder compareNodes(FileDirectoryNode [] node0, FileDirectoryNode [] node1) {

		if(node0.length==0||node1.length==0)
			return new DataHolder();
		
		FileDirectoryNode [] larger, smaller;
		boolean fileNodes = node0[0] instanceof FileNode;
		if(node0.length>node1.length) {
			larger=node0;
			smaller=node1;
		}else {
			smaller=node0;
			larger=node1;
		}
		int max = larger.length;
		
		DataHolder [][] results = new DataHolder [max][max];
		double [][] input = new double [max][max];
		
		for(int i=0;i<max;i++) 
			for(int j=0;j<max;j++) {
				if(smaller.length<=j||(excludeMismatchedFiles&&
						(fileNodes&&!((FileNode)larger[i]).getFileType().equals(((FileNode)smaller[j]).getFileType())))) {
					results[i][j]=new DataHolder();
					input[i][j]=0;
					continue;
				}
				String file0 = smaller[j].getFile().getName();
				String file1 = larger[i].getFile().getName();
				
				if((!considerFileTypes) && fileNodes) {
					file0=file0.replace(((FileNode)smaller[j]).getFileType(),"");
					file1=file1.replace(((FileNode)larger[i]).getFileType(),"");
				}
				
				
				double percent = compareStringPercent(file0,file1);
				results[i][j]=new DataHolder(percent,percent>=requiredFileSimilarityPercent?1:0);
				input[i][j]=percent*-1;
			}
		
		return getHighestSimilarity(results, input);
	}
	
	
	protected double compareStringPercent(String one, String two) {
		int similarities = Math.max(ADiffPatchCheckerResultInterpreter.compare(one, two).getSimilarities(), 
				ADiffPatchCheckerResultInterpreter.compare(one, two).getSimilarities());
		return (double)similarities/Math.max(one.length(), two.length());
	}

}
