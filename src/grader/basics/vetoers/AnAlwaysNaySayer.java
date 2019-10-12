package grader.basics.vetoers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;


public class AnAlwaysNaySayer implements VetoableChangeListener{
	@Override
	public void vetoableChange(PropertyChangeEvent evt)
			throws PropertyVetoException {
		throw new PropertyVetoException("Tests not allowed", evt);
		
	}

}
