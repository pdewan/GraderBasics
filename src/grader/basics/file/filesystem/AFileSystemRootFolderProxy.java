package grader.basics.file.filesystem;

import grader.basics.file.AnAbstractRootFolderProxy;
import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.basics.trace.file.load.RootFileSystemFolderLoaded;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import util.misc.Common;

public class AFileSystemRootFolderProxy extends AnAbstractRootFolderProxy
        implements RootFolderProxy {
    protected File rootFolder;
    String rootName;
    String localName;
    
    public AFileSystemRootFolderProxy(File aRootFolder, String aSubFolderName) {
        super(aSubFolderName);
        init (aRootFolder);
    }


    public AFileSystemRootFolderProxy(String aRootFolderName, String aSubFolderName) {
        super(aSubFolderName);
        init (aRootFolderName);
//        rootFolder = new File(aRootFolderName);
//        if (!rootFolder.exists()) {
//            System.out.println("File:" + aRootFolderName + "  does not exist");
//            rootFolder = null;
//            System.exit(-1);
//            return;
//        }
//
////        rootName = aRootFolderName;
////        try {
//			rootName = Common.toCanonicalFileName(rootFolder.getAbsolutePath());
////		} catch (IOException e) {
////			rootName = aRootFolderName;
////		}
//
//        localName = Common.toCanonicalFileName(rootFolder.getName());
//        // let us see what happens if we do not do this
//        initEntries(rootFolder);
////        System.out.println("Inefficiently initializing children root data");
//        initChildrenRootData(); // I moved this out of init entries because it only needs to be called once and significantly reduces the loading time. --Josh
//        RootFileSystemFolderLoaded.newCase(getAbsoluteName(), this);

    }
    public AFileSystemRootFolderProxy(String aRootFolderName, String[] aLazilyFetchSubFolderNames, String[] anIgnoreFiles) {
        super(aLazilyFetchSubFolderNames, anIgnoreFiles);
        init (aRootFolderName);
//        rootFolder = new File(aRootFolderName);
//        if (!rootFolder.exists()) {
//            System.out.println("File:" + aRootFolderName + "  does not exist");
//            rootFolder = null;
//            System.exit(-1);
//            return;
//        }
//
////        rootName = aRootFolderName;
////        try {
//			rootName = Common.toCanonicalFileName(rootFolder.getAbsolutePath());
////		} catch (IOException e) {
////			rootName = aRootFolderName;
////		}
//
//        localName = Common.toCanonicalFileName(rootFolder.getName());
//        // let us see what happens if we do not do this
//        initEntries(rootFolder);
////        System.out.println("Inefficiently initializing children root data");
//        initChildrenRootData(); // I moved this out of init entries because it only needs to be called once and significantly reduces the loading time. --Josh
//        RootFileSystemFolderLoaded.newCase(getAbsoluteName(), this);

    }
    protected void init(String aRootFolderName) {
    	 rootFolder = new File(aRootFolderName);
    	 init (rootFolder);
//         if (!rootFolder.exists()) {
//             System.out.println("File:" + aRootFolderName + "  does not exist");
//             rootFolder = null;
//             System.exit(-1);
//             return;
//         }
//
////         rootName = aRootFolderName;
////         try {
// 			rootName = Common.toCanonicalFileName(rootFolder.getAbsolutePath());
//// 		} catch (IOException e) {
//// 			rootName = aRootFolderName;
//// 		}
//
//         localName = Common.toCanonicalFileName(rootFolder.getName());
//         // let us see what happens if we do not do this
//         initEntries(rootFolder);
////         System.out.println("Inefficiently initializing children root data");
//         initChildrenRootData(); // I moved this out of init entries because it only needs to be called once and significantly reduces the loading time. --Josh
//         RootFileSystemFolderLoaded.newCase(getAbsoluteName(), this);
    }
    protected void init(File aRootFolder) {
//   	 rootFolder = new File(aRootFolderName);
   	 rootFolder = aRootFolder;

        if (!rootFolder.exists()) {
            System.out.println("File:" + aRootFolder + "  does not exist");
            rootFolder = null;
            System.exit(-1);
            return;
        }

//        rootName = aRootFolderName;
//        try {
			rootName = Common.toCanonicalFileName(rootFolder.getAbsolutePath());
//		} catch (IOException e) {
//			rootName = aRootFolderName;
//		}

        localName = Common.toCanonicalFileName(rootFolder.getName());
        // let us see what happens if we do not do this
        initEntries(rootFolder);
//        System.out.println("Inefficiently initializing children root data");
        initChildrenRootData(); // I moved this out of init entries because it only needs to be called once and significantly reduces the loading time. --Josh
        RootFileSystemFolderLoaded.newCase(getAbsoluteName(), this);
   }

    public AFileSystemRootFolderProxy(String aRootFolderName) {
        this(aRootFolderName, (String) null);
    }
    public AFileSystemRootFolderProxy(File aRootFolder) {
        this(aRootFolder, (String) null);
    }
    
    private boolean containsOnyen(File file ){
    	int openParenPos = file.getName().lastIndexOf('(');
    	int closeParenPos = file.getName().lastIndexOf(')');
    	return openParenPos >= 0 && closeParenPos >= 0 && openParenPos + 1 < closeParenPos;
    }
    
    private File[] sortFiles(File[] files) {
    	List<File> studentDirectories = new ArrayList<File>();
    	List<File> otherFiles = new ArrayList<File>();
    	
    	for (File file : files) {
    		if (file.isDirectory() && containsOnyen(file)) {
    			studentDirectories.add(file);
    		} else {
    			otherFiles.add(file);
    		}
    	}
    	
    	Collections.sort(studentDirectories, new Comparator<File>() {

			@Override
			public int compare(File f1, File f2) {
				String onyen1 = f1.getName().substring(f1.getName().lastIndexOf('(') + 1,
						f1.getName().lastIndexOf(')'));
				String onyen2 = f2.getName().substring(f2.getName().lastIndexOf('(') + 1,
						f2.getName().lastIndexOf(')'));
				return onyen1.compareTo(onyen2);
			}
		});
    	
    	otherFiles.addAll(studentDirectories);
    	return otherFiles.toArray(new File[0]);
    	
    }
    
    
   @Override
   public  void initEntries(File aFolder) {
        File[] files = aFolder.listFiles();
        if (files == null) {
        	return;
        }
        
		if (aFolder.equals(rootFolder)) {
			files = sortFiles(files);
//			Arrays.sort(files, new Comparator<Object>() {
//
//				@Override
//				public int compare(Object o1, Object o2) {
//					if (!(o1 instanceof File && o2 instanceof File)) {
//						throw new RuntimeException("Invalid Type.  Must be of type File.");
//					}
//					File f1 = (File) o1;
//					File f2 = (File) o2;
//					if (!containsOnyen(f1) || !containsOnyen(f2)) {
//						return f1.compareTo(f2);
//					}
//					String onyen1 = f1.getName().substring(f1.getName().lastIndexOf('(') + 1,
//							f1.getName().lastIndexOf(')'));
//					String onyen2 = f2.getName().substring(f2.getName().lastIndexOf('(') + 1,
//							f2.getName().lastIndexOf(')'));
//					return onyen1.compareTo(onyen2);
//				}
//			});
		}
        
        String rootName = Common.toCanonicalFileName(rootFolder.getAbsolutePath());
        for (File aFile : files) {
        	if (aFolder.equals(rootFolder) &&  !inTreeOfSubFolder(aFile.getName()))
        		continue;
        	if (ignoreFile(aFile.getName())) {
        		continue;
        	}
        	FileProxy aFileProxy
        	= new AFileSystemFileProxy(this, aFile, rootName);
//            add(new AFileSystemFileProxy(this, aFile, rootName));
            add(aFileProxy);
            

//            if (aFile.isDirectory() ) {
            if (aFile.isDirectory() )
            		//&& !excludeDescendentsOf(aFile.getName()))
//            		!aFile.getName().contains("src") &&
//            		!aFile.getName().contains("bin")) 
            {
            
            	if (lazilyFetchDescendentsOf(aFile.getName())) {
                	aFileProxy.setDescendentsInitialized(false);

            	} else {
//                System.out.println("Recursively and inefficiently loading child of bulk folder:" + aFile + " bad things may happen");

                initEntries(aFile);
                aFileProxy.setDescendentsInitialized(true);
            	}
            } else {
            	aFileProxy.setDescendentsInitialized(true);
            }
            
            
            if (subFolderName != null && aFolder.equals(rootFolder)) // we are done, we found our subfolder
            	break;
        }
    }

    @Override
    public boolean exists() {
        return rootFolder != null && rootFolder.exists();
    }

    @Override
    public String getMixedCaseAbsoluteName() {
        return rootName;
    }

    @Override
    public String getMixedCaseLocalName() {
        return localName;
    }

    @Override
    public String getAbsoluteName() {
        return rootName;
    }

    @Override
    public String getLocalName() {
        return localName;
    }
    @Override
    public void clear() {
    	super.clear();
//    	System.out.println ("Clearing file system folder");

    }
    public String toString() {
    	return rootName;
    }
}
