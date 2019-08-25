package grader.basics.file;

public interface RootFolderCreator {
	public  RootFolderProxy createRootFolder(String aFolder, String[] aLazilyFetchSubFoldersOf, String[] anIgnoreFiles);
	   public static final String ZIP_SUFFIX_1 = ".zip";
	    public static final String ZIP_SUFFIX_2 = ".jar";
}