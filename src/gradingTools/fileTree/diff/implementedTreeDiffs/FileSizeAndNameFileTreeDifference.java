package gradingTools.fileTree.diff.implementedTreeDiffs;

import gradingTools.fileTree.HungarianAlgorithmDouble;
import gradingTools.fileTree.diff.AbstractFileTreeDifference;
import gradingTools.fileTree.diff.patchCheckerTools.ADiffPatchCheckerResultInterpreter;
import gradingTools.fileTree.nodes.DirectoryNode;
import gradingTools.fileTree.nodes.FileDirectoryNode;
import gradingTools.fileTree.nodes.FileNode;

public class FileSizeAndNameFileTreeDifference extends AbstractFileTreeDifference {
	
	private double nameWeight=0.7;

	public void setNameWeight(double weight) {
		nameWeight=weight;
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
		boolean isFileNodes = node0[0] instanceof FileNode;
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
				if(smaller.length<=j||(isFileNodes&&!((FileNode)larger[i]).getFileType().equals(((FileNode)smaller[j]).getFileType()))) {
					results[i][j]=new DataHolder();
					input[i][j]=0;
					continue;
				}
				String file0 = smaller[j].getFile().getName();
				String file1 = larger[i].getFile().getName();
				int similarities = Math.max(ADiffPatchCheckerResultInterpreter.compare(file0, file1).getSimilarities(), 
											ADiffPatchCheckerResultInterpreter.compare(file1, file0).getSimilarities());
				double percent = (double)similarities/Math.max(file0.length(), file1.length());
				
				if(isFileNodes) {
					int lines0 = ((FileNode)larger[i]).getSize();
					int lines1 = ((FileNode)smaller[j]).getSize();
					lines0=lines0==0?1:lines0;
					lines1=lines1==0?1:lines1;
					double amount = (double)Math.min(lines0, lines1)/Math.max(lines0, lines1);
					percent=percent*nameWeight+amount*(1-nameWeight);
				}
				
				results[i][j]=new DataHolder(percent,percent>=requiredFileSimilarityPercent?1:0);
				input[i][j]=percent*-1;
			}
		
		if(results.length==1) {
			return results[0][0];
		}
		
		HungarianAlgorithmDouble ha = new HungarianAlgorithmDouble(input);
		
		int [][] locations=ha.findOptimalAssignment();
		DataHolder retVal= new DataHolder();
		for(int [] loc:locations)
			retVal.addDataHolderToMatches(results[loc[1]][loc[0]]);
		
		return retVal;
	}
	
	
}
