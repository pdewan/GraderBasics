package gradingTools.fileTree.diff.patchCheckerTools;

public class ADiffPatchCheckerResult {

	private final String source, target;
	private final int similarities, incertions, deletions;
	
	public ADiffPatchCheckerResult(String source, String target, int similarities, int incertions, int deletions) {
		this.similarities=similarities;
		this.target=target;
		this.source=source;
		this.incertions=incertions;
		this.deletions=deletions;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public int getSimilarities() {
		return similarities;
	}
	
	public int getIncertions() {
		return incertions;
	}
	
	public int getDeletions() {
		return deletions;
	}
	
	@Override
	public String toString() {
		return "\""+source+"\" and \""+target+"\" have " + similarities+ " characters in common, "+incertions+" incertions, and "+ deletions+" deletions";
	}
	
	
}
