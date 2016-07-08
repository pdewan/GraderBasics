package grader.basics.vetoers;

import java.beans.VetoableChangeListener;

public interface VetoListenerRegisterer {
	public void addVetoableChangeListener(VetoableChangeListener listener) ;
    public void removeVetoableChangeListener(VetoableChangeListener listener) ;
}
