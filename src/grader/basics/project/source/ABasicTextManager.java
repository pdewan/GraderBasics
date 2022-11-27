package grader.basics.project.source;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.trace.source.SourceFileComputed;
import valgrindpp.helpers.CompilerHelper;


public class ABasicTextManager implements BasicTextManager{
    protected StringBuffer allSourcesText;
    protected String[] allSourcesLines;
    private Map<String, StringBuffer> fileToText ;
	protected static String sourceSuffix;
	protected static String sourceSuffix2;
	protected File sourceFolder;
	protected List<File> sourceFiles = new ArrayList();
	
	protected boolean initializedSourceText = false;

    
    public ABasicTextManager(File aSourceFolder) {
    	sourceSuffix = BasicLanguageDependencyManager.getSourceFileSuffix();
    	if (BasicLanguageDependencyManager.getLanguage() == BasicLanguageDependencyManager.C_LANGUAGE) {
    		sourceSuffix2 = ".h";
    	} else {
    		sourceSuffix2 = sourceSuffix;
    	}
    	sourceFolder = aSourceFolder;
    	allSourcesText = new StringBuffer();
    }

    //@Override
    public void writeAllSourcesText(String aFileName) {
        try {
        	File sourceFile = new File(aFileName);
        	if (sourceFile.exists()) return;
            PrintWriter out = new PrintWriter(aFileName);
            String allText = getAllSourcesText().toString();
            out.print(allText);
            out.close();
            SourceFileComputed.newCase(aFileName, allText, this);
        } catch (Exception e) {
//            e.printStackTrace(); // Commented out by Josh
        }
    }
    @Override
    public StringBuffer getAllSourcesText() {
        if (!initializedSourceText)

//        if (allSourcesText == null)
            initializeAllSourcesText();
        return allSourcesText;
    }
    @Override
    public Map<String, String> getFileToText() {
    	if (fileToText == null) {
        	StringBuffer aSource = getAllSourcesText();
        	return extractFileContents(aSource.toString());

    	}
//        if (!initializedSourceText)
//
////        if (allSourcesText == null)
//            initializeAllSourcesText();
    	
        
//        return fileToText;
        return null;
    }
    @Override
    public void initializeAllSourcesText() {
    	allSourcesText.setLength(0);
//    	fileToText.clear();
    	if (sourceFolder == null) {
    		System.err.println("No source folder found in basic text manager");
    		return;
    	}
    	addFolder(sourceFolder);
    	initializedSourceText = true;
//        Collection<ViewableClassDescription> filteredClasses = classesManager.getViewableClassDescriptions();
//        allSourcesText = toStringBuffer(filteredClasses);
    }
    protected void addFolder(File aFolder) {
    	File[] aFiles = aFolder.listFiles();
    	if (aFiles == null) {
    		System.err.println ("No files in:" + aFolder);
    		return;
    	}
    	for (File aFile:aFiles) {
    		if (aFile.isDirectory()) {
    			addFolder(aFile);
    		} else {
    			addSourceFile(aFile);
    		}
    	}
    	
    	
    }
    public static Map<String, String> extractFileContents(String anAllSourcesText) {
    	String[] anAllSourcesLines = anAllSourcesText.split("\n");
    	return extractFileContents(anAllSourcesLines);
    	
    }
    public static Map<String, String> extractFileContents(String[] anAllSourcesLines) {
    	Map<String, String> aFileNameToContentsMap = new HashMap();
    	int aNextFileIndex = 0;
    	while (true) {
    		int size = fillNextFileContents(anAllSourcesLines, aNextFileIndex, aFileNameToContentsMap);
    		if (size <= 0) {
    			break;
    		}
    		aNextFileIndex += size + 2;
    		
    	}
    	return aFileNameToContentsMap;
    }
    protected static int fillNextFileContents (String[] anAllSourcesLines, int aStartIndex, Map<String, String> aFileNameToContentsMap ) {
    	int aContentsIndex = aStartIndex;
    	int aContentsStartIndex = 0;
    	int aContentsEndIndex = 0;
    	String aFileName = null;
    	StringBuffer aContentsBuffer = new StringBuffer();
    	while (true) {
    		if (aContentsIndex == anAllSourcesLines.length) {
    			break;
    		}
    		String aNextLine = anAllSourcesLines[aContentsIndex];
    		aContentsIndex++;
    		if (!aNextLine.startsWith(BasicTextManager.SOURCE_PREFIX)) {
    			continue;
    		}
    		aFileName = aNextLine.substring(BasicTextManager.SOURCE_PREFIX.length(), aNextLine.length() );
//    		aFileNameToContentsMap.put(aFileName, aContentsBuffer.toString());
    		aContentsStartIndex = aContentsIndex;
    		break;
    	}
    	while (true) {
    		if (aContentsIndex == anAllSourcesLines.length) {
    			break;
    		}
    		String aNextLine = anAllSourcesLines[aContentsIndex];
    		if (BasicTextManager.SOURCE_SUFFIX.equals(aNextLine)) {
    			aContentsEndIndex = aContentsIndex;
    			break;
    		}
    		if (aNextLine.endsWith("\r")) {
    			aNextLine = aNextLine.substring(0, aNextLine.length() -1);
    		}
    		aContentsBuffer.append(aNextLine + "\n");  
    		aContentsIndex++;
    		
    	}
    	if (aFileName == null) {
    		return -1;
    	}
    	aContentsBuffer.deleteCharAt(aContentsBuffer.length() - 1); // last new line
		aFileNameToContentsMap.put(aFileName, aContentsBuffer.toString());

    	return aContentsEndIndex - aContentsStartIndex;
    }
    public static final int MAX_SOURCE_SIZE = 10000;
    static StringBuffer stringBuffer = new StringBuffer(MAX_SOURCE_SIZE);
    public static  String toString (Map<String, String> aFileNameToContents) {
    	stringBuffer.setLength(0);
    	for (String aKey:aFileNameToContents.keySet()) {
    		String aFileName = aKey;
    		File aFile = new File(aFileName);
    		if (!isSourceFile(aFile)) {
//    		if (!aFileName.endsWith(BasicLanguageDependencyManager.getSourceFileSuffix())) {
        		continue;
        	}
    		stringBuffer.append(BasicTextManager.SOURCE_PREFIX + aKey + "\n");
    		stringBuffer.append(aFileNameToContents.get(aKey));    
    		stringBuffer.append("\n" + BasicTextManager.SOURCE_SUFFIX + "\n");
  		
    	}
    	return stringBuffer.toString();
    	
    }
    protected void addSourceFile(File aFile) {
    	if (!isSourceFile(aFile)) {
//    	if (!aFile.getName().endsWith(sourceSuffix) && 
//    			sourceSuffix!= sourceSuffix2 &&
//    			!aFile.getName().endsWith(sourceSuffix2) 
//    			) 
    	
    		return;
    	}

//    	if (!aFile.getName().endsWith(BasicLanguageDependencyManager.getSourceFileSuffix())) {
//    		return;
//    	}
    	try {
			String contents = new String(Files.readAllBytes(aFile.toPath()));
			String aRelativePath = CompilerHelper.toRelativePath(sourceFolder.getAbsolutePath(), aFile.getAbsolutePath());
//			String prefix = BasicTextManager.SOURCE_PREFIX + aFile.getName() + "\n";
			String prefix = BasicTextManager.SOURCE_PREFIX + aRelativePath + "\n";

			allSourcesText.append(prefix);			
			allSourcesText.append(contents);        
			allSourcesText.append("\n" + BasicTextManager.SOURCE_SUFFIX + "\n");
			sourceFiles.add(aFile);
//			fileToText.put(aFile.getName(), contents);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	
    }
    protected void addFileOrFolder(File aFile) {
    	if (!aFile.exists()) {
    		return;
    	}
    	if (aFile.isDirectory()) {
    		addFolder(aFile);
    	}
//    	if (aFile.getName().endsWith(sourceSuffix) || (sourceSuffix != sourceSuffix2) && aFile.getName().endsWith(sourceSuffix2)) {
    	if (isSourceFile (aFile)) {	
    		addSourceFile(aFile);
    	}
    	
    }
    
    protected static boolean isSourceFile (File aFile) {
    	String aName = aFile.getName();
    	return (aName.endsWith(sourceSuffix) ||
    			((sourceSuffix != sourceSuffix2) && aName.endsWith(sourceSuffix2))); 
    }
    @Override
    public void setAllSourcesText(StringBuffer anAllSourcesText) {
        allSourcesText = anAllSourcesText;
    }
    @Override
    public List<File> getSourceFiles() {
    	if (!initializedSourceText)

//          if (allSourcesText == null)
              initializeAllSourcesText();
		return sourceFiles;
	}
    @Override
	public void setSourceFiles(List<File> sourceFiles) {
		this.sourceFiles = sourceFiles;
	}
}
