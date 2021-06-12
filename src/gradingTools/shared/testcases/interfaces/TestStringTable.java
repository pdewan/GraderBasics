package gradingTools.shared.testcases.interfaces;

import util.annotations.Tags;


@Tags({"Table"})
public interface TestStringTable {
	public void put (String key, Object val);
	public Object get (String key);
}
