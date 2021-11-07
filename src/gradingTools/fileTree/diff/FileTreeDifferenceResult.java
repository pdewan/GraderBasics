package gradingTools.fileTree.diff;

public class FileTreeDifferenceResult {

	private final double similarityPercent;
	private final int matchedFiles, sourceFiles, targetFiles, sourceSize, targetSize;
	
	public FileTreeDifferenceResult(double similarityPercent,int matchedFiles,int sourceFiles,int targetFiles,int sourceSize,int targetSize) {
		this.similarityPercent=similarityPercent;
		this.matchedFiles=matchedFiles;
		this.sourceFiles=sourceFiles;
		this.targetFiles=targetFiles;
		this.sourceSize=sourceSize;
		this.targetSize=targetSize;
	}

	public double getSimilarityPercent() {
		return similarityPercent;
	}

	public int getMatchedFiles() {
		return matchedFiles;
	}

	public int getSourceFiles() {
		return sourceFiles;
	}

	public int getTargetFiles() {
		return targetFiles;
	}

	public int getSourceSize() {
		return sourceSize;
	}

	public int getTargetSize() {
		return targetSize;
	}
	
}
