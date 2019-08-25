package grader.basics.file;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

// unite file system and zip file interface
public interface FileProxy extends RootFolderProxy {
    String getAbsoluteName();

    long getTime();

    long getSize();

    InputStream getInputStream();

    OutputStream getOutputStream();

    Set<String> getEntryNames();

    void initRootData();

    Set<String> getChildrenNames();

    public FileProxy getParentFolder();
    String getParentRelativeName();

	String displayTree();

	List<FileProxy> getChildren();

	String getParentFolderName();

	String getParentRelativeMixedCaseName();

	boolean isDescendentsInitialized();
	void setDescendentsInitialized(boolean newValue);

	RootFolderProxy getRootFolderProxy();

	void setRootFolderProxy(RootFolderProxy rootFolderProxy);

	File getFile();

}
