package grader.basics.file;

import grader.basics.file.filesystem.AFileSystemRootFolderProxy;
import grader.basics.file.zipfile.AZippedRootFolderProxy;

public class ARootFolderCreator implements RootFolderCreator {
    public  RootFolderProxy createRootFolder(String aFolder, String[] aLazilyFetchSubFoldersOf, String[] anIgnoreFiles) {
//        boolean isZipperFolder = aFolder.endsWith(AFlexibleProject.ZIP_SUFFIX_1) || aFolder.endsWith(AFlexibleProject.ZIP_SUFFIX_2);
        boolean isZipperFolder = aFolder.endsWith(ZIP_SUFFIX_1) || aFolder.endsWith(ZIP_SUFFIX_2);

        if (isZipperFolder) {
        	System.out.println ("Creating zipped folder:" + aFolder + " with sffx2 " + ZIP_SUFFIX_2);
            return new AZippedRootFolderProxy(aFolder);
        } else {
            return new AFileSystemRootFolderProxy(aFolder, aLazilyFetchSubFoldersOf, anIgnoreFiles );
        }
    }

}
