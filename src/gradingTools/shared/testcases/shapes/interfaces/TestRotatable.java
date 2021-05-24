package gradingTools.shared.testcases.shapes.interfaces;

import util.annotations.Tags;

public interface TestRotatable {
//	@Tags({"rotate"})
	@Tags(ShapeTags.ROTATE)
	public void rotate(int degrees);
}
