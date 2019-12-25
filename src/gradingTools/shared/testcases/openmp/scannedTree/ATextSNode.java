package gradingTools.shared.testcases.openmp.scannedTree;

public class ATextSNode extends AnSNode implements TextSNode {
	String text;
	public ATextSNode(int lineNumber, String aText) {
		super(lineNumber);
		text = aText;
	}
	@Override
	public String getText() {
		return text;
	}
	@Override
	public void setText(String text) {
		this.text = text;
	}
}
