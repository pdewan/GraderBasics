package grader.basics.project.source;

public interface BasicTextManager {

	String DEFAULT_SOURCES_FILE_PREFIX = "sources";
	String DEFAULT_SOURCES_FILE_SUFFIX = ".txt";
	String DEFAULT_SOURCES_FILE_NAME = DEFAULT_SOURCES_FILE_PREFIX + DEFAULT_SOURCES_FILE_SUFFIX;
	String SOURCE_SUFFIX = "//END OF FILE\n";
	String SOURCE_PREFIX = "//START OF FILE: ";
	int MAX_FILE_NAME_LENGTH = 100;
	public StringBuffer getAllSourcesText();

    public void setAllSourcesText(StringBuffer anAllSourcesText);

    void writeAllSourcesText(String aFileName);
    public void initializeAllSourcesText();



}
