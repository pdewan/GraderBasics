package grader.basics.file.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import grader.basics.file.AnAbstractFileProxy;
import grader.basics.file.FileProxy;
import grader.basics.file.RootFolderProxy;
import grader.basics.util.GraderFileUtils;
import util.misc.Common;

// file system translated to common interface for zip file and file system
public class AFileSystemFileProxy extends AnAbstractFileProxy implements FileProxy {
    File file;
    

	String localName;
    String absoluteName;
    String mixedCaseLocalName;
    String mixedCaseAbsoluteName;

    public AFileSystemFileProxy(RootFolderProxy aRootFolderProxy, File aFile, String aRootFolderName) {
        super(aRootFolderProxy);
        file = aFile;
        initAbsoluteNames(aFile);
        
//        mixedCaseAbsoluteName = Common.toCanonicalFileName(file.getAbsolutePath());
//        absoluteName = mixedCaseAbsoluteName.toLowerCase();

        //System.out.println(this.getClass().getName());
        mixedCaseLocalName = GraderFileUtils.toRelativeName(aRootFolderName, mixedCaseAbsoluteName);

//        mixedCaseLocalName = GraderFileUtils.toRelativeName(aRootFolderName, getMixedCaseAbsoluteName());
        localName = mixedCaseLocalName.toLowerCase();
    }
    void initAbsoluteNames(File aFile) {
    	mixedCaseAbsoluteName = Common.toCanonicalFileName(file.getAbsolutePath());
        absoluteName = mixedCaseAbsoluteName.toLowerCase();
    }

    public AFileSystemFileProxy(File aFile) {
        super(null);
        file = aFile;
        initAbsoluteNames(aFile);
//        mixedCaseAbsoluteName = Common.toCanonicalFileName(file.getAbsolutePath());
//        absoluteName = mixedCaseAbsoluteName.toLowerCase();

//        absoluteName = Common.toCanonicalFileName(file.getAbsolutePath()).toLowerCase();
    }

    public String toString() {
        return file.toString();
    }

    @Override
    public String getAbsoluteName() {
        return absoluteName;
    }

    @Override
    public String getMixedCaseAbsoluteName() {
        return mixedCaseAbsoluteName;
    }

    @Override
    public String getMixedCaseLocalName() {
        return mixedCaseLocalName;
    }

    @Override
    public long getTime() {
        return file.lastModified();
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getLocalName() {
        return localName;
    }

    @Override
    public boolean exists() {
        return file.exists();
    }
    @Override
    public File getFile() {
		return file;
	}
    
    @Override
    public boolean isDirectory() {
    	return file.isDirectory();
    }
	
}
