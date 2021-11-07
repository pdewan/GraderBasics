package gradingTools.fileTree.diff.patchCheckerTools;

public class ADiffPatchCheckerOutput{
	
	enum DiffType {
		INSERT,
		DELETE,
		EQUAL,
		ERROR
	}
	
	private final DiffType type;
	private final String text;
	
	public ADiffPatchCheckerOutput(String pattern) {
		text = pattern.replaceAll(".*,\"", "").replaceAll("\"\\).*", "");
		if(pattern.contains("INSERT"))
			type=DiffType.INSERT;
		else if(pattern.contains("DELETE"))
			type=DiffType.DELETE;
		else if(pattern.contains("EQUAL"))
			type=DiffType.EQUAL;
		else
			type=DiffType.ERROR;
	}
	
	public String getText() {
		return text;
	}
	
	public DiffType getType() {
		return type;
	}
	
}
