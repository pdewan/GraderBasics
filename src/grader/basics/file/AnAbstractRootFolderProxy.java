package grader.basics.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grader.basics.trace.file.load.RootFolderProxyLoaded;
import util.misc.Common;
import util.trace.Tracer;

public abstract class AnAbstractRootFolderProxy extends AnAbstractProxy implements RootFolderProxy {
    protected Map<String, FileProxy> nameToFileProxy = new HashMap();
    protected List<FileProxy> entries = new ArrayList();
    
    protected  String subFolderName; // only children of this folder will be viisted and put in nameToFileProxy;
    protected String[] lazilyFetchSubFoldersOf; //  descendents of this folder will not be visited immediately and put in nameToFileProxy;
    protected String[] ignoreFiles; //  descendents of this folder will not be visited immediately and put in nameToFileProxy;

    protected  String subFolderNameLowerCase;
    
    
    public AnAbstractRootFolderProxy(String aSubFolderName) {
    	subFolderName = aSubFolderName;
    	if (subFolderName != null)
    		subFolderNameLowerCase = subFolderName.toLowerCase();
    	
    }
    public AnAbstractRootFolderProxy(String[] aLazilyFetchSubfoldersOf, String[] anIgnoreFiles) {
    	lazilyFetchSubFoldersOf = aLazilyFetchSubfoldersOf;
    	ignoreFiles = anIgnoreFiles;
//    	if (subFolderName != null)
//    		subFolderNameLowerCase = subFolderName.toLowerCase();
    	
    }
    protected boolean lazilyFetchDescendentsOf(String aFileName) {
    	if (lazilyFetchSubFoldersOf == null) {
    		return false;
    	}
    	for (String anExcludedSubfolder:lazilyFetchSubFoldersOf) {
    		if (aFileName.equals(anExcludedSubfolder)) {
    			return true;
    		}
    	}
    	return false;
    }
    protected boolean ignoreFile(String aFileName) {
    	if (ignoreFiles == null) {
    		return false;
    	}
    	for (String aFile:ignoreFiles) {
    		if (aFileName.equals(aFile)) {
    			return true;
    		}
    	}
    	return false;
    }
    @Override
    public void clear() {
    	super.clear();
    	Tracer.info (this, this + " clearing entries");

//    	System.out.println (this + " clearing entries");
    	entries.clear();
    	nameToFileProxy.clear();
    }
    
    protected boolean inTreeOfSubFolder(String anEntryName) {
    	return subFolderNameLowerCase == null || 
    			anEntryName.toLowerCase().contains(subFolderNameLowerCase);
    	
    }

    protected void add(FileProxy aFileProxy) {
    	
//    	System.out.println("Adding entry:" + aFileProxy);
        entries.add(aFileProxy);
        nameToFileProxy.put(aFileProxy.getAbsoluteName().toLowerCase(), aFileProxy);
        nameToFileProxy.put(aFileProxy.getAbsoluteName(), aFileProxy); // added this for Unix systems

    }

    @Override
    public List<FileProxy> getFileEntries() {
        return entries;
    }

    @Override
    public Set<String> getEntryNames() {
        return nameToFileProxy.keySet();
    }

    public List<FileProxy> getChildrenOf(String aParentName) {
        String myName = aParentName.toLowerCase();
        int parentDepth = Common.numMiddleOccurences(myName, '/');

        List<FileProxy> retVal = new ArrayList();
        for (FileProxy entry : entries) {
            String childName = entry.getAbsoluteName();
            if (!childName.startsWith(myName)) continue;
            int childDepth = Common.numMiddleOccurences(childName, '/');

            if (childDepth == parentDepth + 1) {
                retVal.add(entry);
            }
        }
//        System.out.println("Children of " + aParentName + " =" + retVal);
        return retVal;

    }

    @Override
    public Set<String> getDescendentEntryNames(FileProxy aParent) {
        String parentName = aParent.getAbsoluteName();
        Set<String> allChildren = getEntryNames();
        Set<String> retVal = new HashSet();
        for (String name : allChildren) {
            if (name.startsWith(parentName) && !name.equals(parentName))
                retVal.add(name);
//            if (name.endsWith("src") || name.endsWith("src/") || name.contains("acorrect")) {
//            System.out.println("Name:" + name);
//            }
        }
        return retVal;
    }

    public FileProxy getFileEntryFromLocalName(String name) {
        return getFileEntry(getAbsoluteName() + "/" + name);
    }

    public boolean isDirectory() {
        return true;
    }

    protected void initChildrenRootData() {
        for (FileProxy entry : entries) {
        	initChildrenRootData(entry);
//            entry.initRootData();
//            String entryName = entry.getLocalName();
//            if (entryName == null) continue;
//            int index1 = entryName.indexOf('/');
//            int index2 = entryName.indexOf('\\');// use file separator
//            int index3 = entryName.indexOf(File.separator);
//            if (index1 == -1 && index2 == -1 && index3 == -1)
//                childrenNames.add(entry.getAbsoluteName());
        }
        RootFolderProxyLoaded.newCase(getAbsoluteName(), this);
    }
    @Override
    public void initChildrenRootData(FileProxy anEntry) {
    	anEntry.initRootData();
        String entryName = anEntry.getLocalName();
        if (entryName == null) return;
        int index1 = entryName.indexOf('/');
        int index2 = entryName.indexOf('\\');// use file separator
        int index3 = entryName.indexOf(File.separator);
        if (index1 == -1 && index2 == -1 && index3 == -1)
            childrenNames.add(anEntry.getAbsoluteName());
    }
    boolean debug;


    public FileProxy getFileEntryFromArg(String aName) {
    	if (aName == null) {
    		System.out.println("Null file entry returning null proxy");
    		return null;
    	}
//    	String aCanonicalName = aName.toLowerCase(); // sometimes this gives an exception
    	String aCanonicalName = aName;

//        return nameToFileProxy.get(name.toLowerCase());
    	FileProxy retVal = nameToFileProxy.get(aCanonicalName);
    	return retVal;
//    	if (retVal == null && debug) {
//    		Set<String> keys = nameToFileProxy.keySet();
//    		for (String key:keys) {
//    			System.out.println("comparing" + key + " and\n" + aCanonicalName);
//    			if (key.equals(aCanonicalName)) return nameToFileProxy.get(key);
//    		}
//    	}
//    	return retVal;
    }
    public FileProxy getFileEntry(String aName) {
    	FileProxy retVal = getFileEntryFromArg(aName);
    	if (retVal != null) 
    	
    		return retVal;
    	if (aName == null) {
    		System.err.println ("Null aName!!");
    		return null;
    	}
    	String aCanonicalName = aName.toLowerCase();
    	retVal = getFileEntryFromArg(aCanonicalName);
    	
//        return nameToFileProxy.get(name.toLowerCase());
    	if (retVal == null && debug) {
    		Set<String> keys = nameToFileProxy.keySet();
    		for (String key:keys) {
    			System.out.println("comparing" + key + " and\n" + aCanonicalName);
    			if (key.equals(aCanonicalName)) return nameToFileProxy.get(key);
    		}
    	}
    	return retVal;
    }
    
//    public FileProxy getFileEntry(String aName) {
//    	if (aName == null) {
//    		System.out.println("Null file entry returning null proxy");
//    		return null;
//    	}
//    	String aCanonicalName = aName.toLowerCase(); // sometimes this gives an exception
////        return nameToFileProxy.get(name.toLowerCase());
//    	FileProxy retVal = nameToFileProxy.get(aCanonicalName);
//    	if (retVal == null && debug) {
//    		Set<String> keys = nameToFileProxy.keySet();
//    		for (String key:keys) {
//    			System.out.println("comparing" + key + " and\n" + aCanonicalName);
//    			if (key.equals(aCanonicalName)) return nameToFileProxy.get(key);
//    		}
//    	}
//    	return retVal;
//    }

}
