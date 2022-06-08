package grader.basics.trace.output;

import java.beans.PropertyChangeEvent;

public class BasicObjectToPropertyChange implements ObjectToPropertyChange {

	@Override
	public PropertyChangeEvent toPropertyChange(Object anObject) {
		return new PropertyChangeEvent(this, "AllOutput", null, anObject);
	}

}
