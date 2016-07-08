package grader.basics.vetoers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

public class AJUnitRunVetoer implements JUnitRunVetoer {
	VetoableChangeSupport support = new VetoableChangeSupport(this);
	@Override
	public void vetoableChange(PropertyChangeEvent evt)
			throws PropertyVetoException {
		support.fireVetoableChange(evt);
		
	}
	public void addVetoableChangeListener(VetoableChangeListener listener) {
        support.addVetoableChangeListener(listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        support.removeVetoableChangeListener(listener);
    }


}
