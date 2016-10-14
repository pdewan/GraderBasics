package gradingTools.shared.testcases.interfaces;

import util.annotations.Tags;

@Tags({"ClearableHistory"})
public interface TestHistory {
	// Can be used by tests since they do not call addElement
	public void addElement(Object element);
	// return type not used in checks
	public Object elementAt (int index); 
	public int size();
}
