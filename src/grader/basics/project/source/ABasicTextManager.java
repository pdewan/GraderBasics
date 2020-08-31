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


public class ABasicTextManager implements BasicTextManager{
    protected StringBuffer allSourcesText;
	protected String sourceSuffix;
	protected File sourceFolder;
	protected List<File> sourceFiles = new ArrayList();
	
	protected boolean initializedSourceText = false;

    
    public ABasicTextManager(File aSourceFolder) {
    	sourceSuffix = BasicLanguageDependencyManager.getSourceFileSuffix();
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
    public void initializeAllSourcesText() {
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
    public static Map<String, StringBuffer> extractFileContents(String anAllSourcesText) {
    	String[] anAllSourcesLines = anAllSourcesText.split("\n");
    	return extractFileContents(anAllSourcesLines);
    	
    }
    public static Map<String, StringBuffer> extractFileContents(String[] anAllSourcesLines) {
    	Map<String, StringBuffer> aFileNameToContentsMap = new HashMap();
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
    protected static int fillNextFileContents (String[] anAllSourcesLines, int aStartIndex, Map<String, StringBuffer> aFileNameToContentsMap ) {
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
    		aFileName = aNextLine.substring(BasicTextManager.SOURCE_PREFIX.length(), aNextLine.length() -1 );
    		aFileNameToContentsMap.put(aFileName, aContentsBuffer);
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
    		aContentsBuffer.append(aNextLine + "\n");  
    		aContentsIndex++;
    		
    	}
    	return aContentsEndIndex - aContentsStartIndex;
    }
    
    protected void addSourceFile(File aFile) {
    	if (!aFile.getName().endsWith(BasicLanguageDependencyManager.getSourceFileSuffix())) {
    		return;
    	}
    	try {
			String contents = new String(Files.readAllBytes(aFile.toPath()));
			String prefix = BasicTextManager.SOURCE_PREFIX + aFile.getName() + "\n";
			allSourcesText.append(prefix);			
			allSourcesText.append(contents);        
			allSourcesText.append(BasicTextManager.SOURCE_SUFFIX + "\n");
			sourceFiles.add(aFile);
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
    	if (aFile.getName().endsWith(sourceSuffix)) {
    		addSourceFile(aFile);
    	}
    	
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
