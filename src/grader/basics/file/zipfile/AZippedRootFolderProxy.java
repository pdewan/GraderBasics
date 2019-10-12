package grader.basics.file.zipfile;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import grader.basics.file.AnAbstractRootFolderProxy;
import grader.basics.file.RootFolderProxy;
import grader.basics.trace.file.load.RootFileSystemFolderLoaded;
import util.misc.Common;
import util.trace.Tracer;

public class AZippedRootFolderProxy extends AnAbstractRootFolderProxy implements RootFolderProxy {
    private ZipFile zipFile;
    String rootLocalName;
    String absoluteName;
    public static final String MACOS = "_MACOS";

    public AZippedRootFolderProxy(String aZipFileName, String aSubFolderName) {
        super(aSubFolderName);
        try {
            this.zipFile = new ZipFile(aZipFileName);
			System.out.println ("Opening zip file:" + zipFile);

        } catch (IOException e) {
            System.out.println(aZipFileName + ":" + e);
            return;
        }
        initRootName();
        initEntries();
        
    }
    public AZippedRootFolderProxy(String aZipFileName) {
    	this(aZipFileName, null);
    	
    }

    public boolean exists() {
        return zipFile != null && zipFile.size() > 0;
    }

    @Override
    public String getAbsoluteName() {
        return absoluteName;
    }

    @Override
    public String getLocalName() {
        return rootLocalName;
    }

    public static String rootLocalName(String anElementName) {
        int firstSlashIndex = anElementName.indexOf("/");
        if (firstSlashIndex < 0)
            return anElementName;
        else
            return anElementName.substring(0, firstSlashIndex);
    }

    void initRootName() {
        absoluteName = Common.toCanonicalFileName(zipFile.getName());
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {

            ZipEntry nextEntry = enumeration.nextElement();
            String name = nextEntry.getName();
            // why do we have this check?
            if ((name.indexOf(".") >= 0 && 
            		name.indexOf('/') == -1) 
//            		|| !name.contains(".MF")) // manifest files in jars
        		|| name.contains(".MF")) // manifest files in jars

                continue; // we got a file at the top level
            rootLocalName = rootLocalName(name);
//            Exception e= new Exception();
//            e.printStackTrace();
            Tracer.info(this, "Local name:" + rootLocalName + " of zip file" + zipFile.getName());
            return;
        }

    }
    
  

    void initEntries() {
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry nextEntry = enumeration.nextElement();
            if (nextEntry.getName().indexOf(MACOS) >= 0) // why not use contains?
                continue; // mac added stuff
            if (!inTreeOfSubFolder(nextEntry.getName()))
            	continue;
            add(new AZippedFileProxy(this, nextEntry, zipFile, rootLocalName));
        }
        initChildrenRootData();
        RootFileSystemFolderLoaded.newCase(getAbsoluteName(), this);

    }

    @Override
    public String getMixedCaseAbsoluteName() {
        return absoluteName;
    }

    @Override
    public String getMixedCaseLocalName() {
        return rootLocalName;
    }
    protected void closeZipFile() {
    	if (zipFile != null) {
    		try {
				zipFile.close();
				System.out.println ("Closing zip file:" + zipFile.getName());
				zipFile = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    @Override
    public void clear() {
    	
    	super.clear();
//    	System.out.println ("Clearing zipped root folder " );
    	closeZipFile();
//    	if (zipFile != null) {
//    		try {
//				zipFile.close();
//				System.out.println ("Closing zip file:" + zipFile);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}
    }
}
