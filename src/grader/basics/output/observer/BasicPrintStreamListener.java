package grader.basics.output.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BasicPrintStreamListener implements PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt);
	}

}
