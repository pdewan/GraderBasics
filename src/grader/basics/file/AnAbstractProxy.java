package grader.basics.file;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class AnAbstractProxy implements RootFolderProxy{
    Set<String> descendentNames;
    Set<String> childrenNames = new HashSet();
    public String toString() {
    	return getMixedCaseAbsoluteName();
    }
    public Set<String> getChildrenNames() {
        return childrenNames;
    }
    @Override
    public void clear() {
//    	System.out.println (this + " Clearing abstract proxy descendent names");
    	if (descendentNames != null)
    		descendentNames.clear();
    	childrenNames.clear();
    	
    }

    public void initChildrenRootData(FileProxy anEntry) {
    	
    }

	public void initEntries(File aFolder) {
		
	}
	public boolean isDescendentsInitialized() {
		return true;
	}
	public void setDescendentsInitialized(boolean newValue) {
		
	}

}
