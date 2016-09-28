package gradingTools.shared.testcases.shapes.interfaces;

import util.annotations.Tags;

public interface TestMovable {
	@Tags({"move"})
	public void move(int aDeltaX, int aDeltaY);
}
