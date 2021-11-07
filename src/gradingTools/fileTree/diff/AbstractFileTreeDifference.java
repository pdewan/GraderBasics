package gradingTools.fileTree.diff;

import gradingTools.fileTree.HungarianAlgorithmDouble;
import gradingTools.fileTree.nodes.DirectoryNode;
import gradingTools.fileTree.nodes.FileNode;

public abstract class AbstractFileTreeDifference implements FileTreeDifference{

	private CompareType comparing = CompareType.LargestOnly;
	private double dirToFileWeight = 1;
	protected double requiredFileSimilarityPercent=0.75;
	private boolean testAllViableTrees=false;
	
	
	public FileTreeDifferenceResult getStructureDifferencePercent(DirectoryNode source, DirectoryNode target) {
		DataHolder matches= testAllViableTrees ? findHighestSubTree(source,target):matchingTypes(source,target); 
		double totalPossible;
		switch(comparing) {
		case LargestOnly:
			totalPossible=Math.max(source.getNumSubFiles()+source.getNumSubDirs()*dirToFileWeight,
								   target.getNumSubFiles()+target.getNumSubDirs()*dirToFileWeight);
			break;
		case LargestOnlyFlat:
			totalPossible=Math.max(source.getSize(), target.getSize());
			break;
		case SourceToTarget:
			totalPossible=source.getNumSubFiles()+source.getNumSubDirs()*dirToFileWeight;
			break;
		case TargetToSource:
			totalPossible=target.getNumSubFiles()+target.getNumSubDirs()*dirToFileWeight;
			break;
		default:
			totalPossible=0;
		}

		return new FileTreeDifferenceResult(matches.getRoughMatches()/totalPossible,matches.getFileMatches(),
											source.getNumSubFiles(),target.getNumSubFiles(),
											source.getSize(),target.getSize());
	}
	
	private DataHolder findHighestSubTree(DirectoryNode source, DirectoryNode target) {
		DirectoryNode base,compare;
		if(source.getHeight()>target.getHeight()) {
			base =source;
			compare=target;
		}else {
			base=target;
			compare=source;
		}
		return recursiveBaseCompare(base,compare);
	}
	
	private DataHolder recursiveBaseCompare(DirectoryNode base, DirectoryNode target) {
		DataHolder retval = matchingTypes(base,target);
		
		DirectoryNode [] subDirs = base.getSubDirectories();
		for(DirectoryNode subdir:subDirs) 
			if(subdir.getHeight()>=target.getHeight()) {
				DataHolder value = recursiveBaseCompare(subdir,target);
				retval = retval.getRoughMatches() < value.getRoughMatches() ? value:retval;
			}
		
		return retval;
	}
	
	public void setCheckAllSubTrees(boolean bool) {
		testAllViableTrees=bool;
	}
	
	@Override
	public void setFileSimilarityPercentThreshold(double amount) {
		this.requiredFileSimilarityPercent=amount;
	}
	
	@Override
	public void setCompareType(CompareType ct) {
		comparing=ct;
	}
	
	@Override
	public void setDirToFileWeight(double amount) {
		dirToFileWeight=amount;
	}
	
	private DataHolder matchingTypes(DirectoryNode node0, DirectoryNode node1) {
		//Similarities from directory data (i.e. name) does not look into directories
		DataHolder dirNum = calculateDirectorySimilarities(node0.getSubDirectories(), node1.getSubDirectories());
		DataHolder retVal = new DataHolder(dirNum.getRoughMatches()*dirToFileWeight,dirNum.getFileMatches());
		
		//Similarities from file data (i.e. name)
		retVal.addDataHolderToMatches(calculateFileSimilarities(node0.getSubFiles(), node1.getSubFiles()));
		
		//Base case and recursive step looks into directory content
		if(!(Math.min(node0.getNumSubDirs(), node1.getNumSubDirs())==0))
			retVal.addDataHolderToMatches(mostSimilarDirectories(node0.getSubDirectories(),node1.getSubDirectories()));
		return retVal;
	}
	
	private DataHolder mostSimilarDirectories(DirectoryNode[] nodes0, DirectoryNode[] nodes1) {
		//Useful to avoid index out of bounds
		DirectoryNode [] base, compare;
		if(nodes0.length<nodes1.length) {
			base=nodes0;
			compare=nodes1;
		}else {
			base=nodes1;
			compare=nodes0;
		}
		
		//Extremely large time complexity can be improved
		int max = compare.length;
		DataHolder [][] results = new DataHolder [max][max];
		double [][] algorithmInput = new double [max][max];
		for(int i=0;i<base.length;i++) 
			for(int j=0;j<compare.length;j++) {
				results[i][j]=matchingTypes(base[i],compare[j]);
				algorithmInput[i][j]=-1*results[i][j].getRoughMatches();
			}
		
		return getHighestSimilarity(results,algorithmInput);
	}
	
	protected abstract DataHolder calculateFileSimilarities(FileNode [] subdir0, FileNode [] subdir1);
	protected abstract DataHolder calculateDirectorySimilarities(DirectoryNode [] node0, DirectoryNode [] node1);
	
	protected DataHolder getHighestSimilarity(DataHolder [][] results, double [][] algorithmInput) {
		HungarianAlgorithmDouble ha = new HungarianAlgorithmDouble(algorithmInput);
		int [][] locations=ha.findOptimalAssignment();
		DataHolder retVal=new DataHolder();
		for(int [] loc:locations)
			retVal.addDataHolderToMatches(results[loc[1]][loc[0]]);
		return retVal;
	}

	protected double getPercentSimilarity(double var0, double var1) {
		double max,min;
		if(var0>var1) {
			max=var0;
			min=var1;
		}else {
			max=var1;
			min=var0;
		}
		return (1-(max-min)/max);
	}

	//Useful for handling information passed from all the branches
	protected class DataHolder {
		
		private double roughMatches;
		private int fileMatches;
		
		public DataHolder() {
			this(0,0);
		}
		
		public DataHolder(double roughMatches, int fileMatches) {
			this.roughMatches=roughMatches;
			this.fileMatches=fileMatches;
		}

		public double getRoughMatches() {
			return roughMatches;
		}

		public int getFileMatches() {
			return fileMatches;
		}
		
		public void addToRoughMatches(double amount) {
			roughMatches+=amount;
		}
		
		public void addToFileMatches(int amount) {
			fileMatches+=amount;
		}
		
		public void addDataHolderToMatches(DataHolder dh) {
			if(dh==null) return;
			roughMatches+=dh.getRoughMatches();
			fileMatches+=dh.getFileMatches();
		}
		
	}
	
}
