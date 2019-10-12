package grader.basics.project.source;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.trace.source.SourceFileComputed;


public class ABasicTextManager implements BasicTextManager{
    protected StringBuffer allSourcesText;
	protected String sourceSuffix;
	protected File sourceFolder;

    
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
        if (allSourcesText == null)
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
//        Collection<ViewableClassDescription> filteredClasses = classesManager.getViewableClassDescriptions();
//        allSourcesText = toStringBuffer(filteredClasses);
    }
    protected void addFolder(File aFolder) {
    	File[] aFiles = aFolder.listFiles();
    	for (File aFile:aFiles) {
    		if (aFile.isDirectory()) {
    			addFolder(aFile);
    		} else {
    			addSourceFile(aFile);
    		}
    	}
    	
    	
    }
    protected void addSourceFile(File aFile) {
    	try {
			String contents = new String(Files.readAllBytes(aFile.toPath()));
			String prefix = BasicTextManager.SOURCE_PREFIX + aFile.getName() + "\n";
			allSourcesText.append(prefix);			
			allSourcesText.append(contents);        
			allSourcesText.append(BasicTextManager.SOURCE_SUFFIX);
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
}
