package grader.basics.junit;

import java.util.List;
import java.util.Map;

public class GradableTreeConstructionResult {
	public GradableJUnitSuite rootNode;
	public Map<String,List<GradableJUnitTest>> groupedGradables;
}
