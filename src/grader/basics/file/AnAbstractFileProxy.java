package grader.basics.file;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grader.basics.util.GraderFileUtils;
import util.misc.Common;

public abstract class AnAbstractFileProxy extends AnAbstractProxy implements FileProxy {
    protected RootFolderProxy rootFolderProxy;
   

	List<FileProxy> fileEntries;
    List<FileProxy> childrenProxies;
    boolean isFolder;
    boolean isDescendentsInitialized;

  
	public AnAbstractFileProxy(RootFolderProxy aRootFolderProxy) {
        rootFolderProxy = aRootFolderProxy;
    }

    @Override
    public boolean isDirectory() {
        return getChildrenNames().size() > 0;
    }

    public FileProxy getParentFolder() {
//        String parentName = Common.getParentFileName(getAbsoluteName());
//        return this.getFileEntry(parentName);
    	return this.getFileEntry(getParentFolderName());
    }
    @Override
    public String getParentFolderName() {
        return Common.getParentFileName(getAbsoluteName());
//        return this.getFileEntry(parentName);
    }
    
    public void clear() {
    	super.clear();
    	if (descendentNames != null)
    	descendentNames.clear();
    	if (fileEntries != null)
    	fileEntries.clear();   	
    }

    public void initRootData() {
        String myName = getAbsoluteName();
        int myDepth = Common.numOccurences(myName, '/');
        descendentNames = rootFolderProxy.getDescendentEntryNames(this);
        isFolder = descendentNames.size() > 0;
        fileEntries = new ArrayList();
        for (String entryName : descendentNames) {
            int childDepth = Common.numOccurences(entryName, '/');

            if (childDepth == myDepth + 1) {
                childrenNames.add(entryName);
            }
            fileEntries.add(rootFolderProxy.getFileEntry(entryName));
        }
    }

    public List<FileProxy> getFileEntries() {
        return fileEntries;
    }

    public FileProxy getFileEntry(String name) {
        return rootFolderProxy.getFileEntry(name);
    }

    public FileProxy getFileEntryFromLocalName(String name) {
        return rootFolderProxy.getFileEntry(getAbsoluteName() + "/" + name);
    }

    public Set<String> getEntryNames() {
        return descendentNames;
    }

    public Set<String> getDescendentEntryNames(FileProxy aParent) {
    	if (aParent == null)
    		return new HashSet();
        return rootFolderProxy.getDescendentEntryNames(aParent);
    }

    @Override
    public List<FileProxy> getChildrenOf(String aParentName) {
        return rootFolderProxy.getChildrenOf(aParentName);
    }
    @Override
   	public String getParentRelativeName() {
            if (getParentFolder() == null) {
       		return getLocalName();
            } else {
                //System.out.println(this.getClass().getName());
       		return GraderFileUtils.toRelativeName(getParentFolder().getLocalName(), getLocalName());
            }
   	}
    @Override
   	public String getParentRelativeMixedCaseName() {
            if (getParentFolder() == null) {
                return getMixedCaseLocalName();
            } else {
                //System.out.println(this.getClass().getName());
                return GraderFileUtils.toRelativeName(getParentFolder().getMixedCaseLocalName(), getMixedCaseLocalName());
            }	
   	}
    @Override
    public List<FileProxy> getChildren() {
    	if (childrenProxies == null) {
    		childrenProxies = getChildrenOf(getAbsoluteName());
    	}
//    	return getChildrenOf(getAbsoluteName());
    	return childrenProxies;

    }
    
    @Override
    public String displayTree() {
    	String retVal = getParentRelativeName();
    	if (!isDirectory()) return retVal;
//    	String localName = getLocalName();
//    	String absoluteName = getAbsoluteName();
    	List<FileProxy> children = getChildren();
    	if (children.size() == 0) return retVal;
    	retVal += "( ";
    	
    	for (int i = 0; i < children.size(); i++) {
    		String childRepresentation = children.get(i).displayTree();
    		if (i == 0)
    			retVal += childRepresentation;
    		else
    			retVal += ", " + childRepresentation;
    	}
    	retVal += ")";

    	return retVal;
    }
    public boolean isDescendentsInitialized() {
  		return isDescendentsInitialized;
  	}

  	public void setDescendentsInitialized(boolean isDescendentsInitialized) {
  		this.isDescendentsInitialized = isDescendentsInitialized;
  	}
  	@Override
  	 public RootFolderProxy getRootFolderProxy() {
 		return rootFolderProxy;
 	}
  	@Override

 	public void setRootFolderProxy(RootFolderProxy rootFolderProxy) {
 		this.rootFolderProxy = rootFolderProxy;
 	}

}
