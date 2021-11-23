package gradingTools.fileTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gradingTools.fileTree.diff.FileTreeDiffFactory;
import gradingTools.fileTree.nodes.DirectoryNode;

public class Template {

	private final DirectoryNode template;
	private final String name;
	
	/**
	 * the template string must be of the form: 
	 * |- Directory
	 *  |- Sub_directory1
	 *   |~ file1
	 *   |~ file2
	 *  |- Sub_directory2
	 * that is, a single space to indicate a file or directory belonging to the directory above it and the '\n' character separating each line
	 * ~ is to signify a file
	 * - is to signify a directory
	 * 
	 * @param template
	 * @param name
	 */
	public Template(String template, String name) {
		this(template.split("\n"),name);
	}
	
	/**
	 * the template string must be of the form: 
	 * |- Directory
	 *  |- Sub_directory1
	 *   |~ file1
	 *   |~ file2
	 *  |- Sub_directory2
	 * that is, a single space to indicate a file or directory belonging to the directory above it each line should be it's own index in the array
	 * such that index 1 of the example above would be the string " |- Sub_directory1"
	 * ~ is to signify a file
	 * - is to signify a directory
	 * 
	 * @param template
	 * @param name
	 */
	public Template(String [] template, String name) {
		this.name=name;
		List<String> templateList = new ArrayList<>();
		templateList.addAll(Arrays.asList(template));
		this.template= new DirectoryNode(templateList);
	}
	
	public String getName() {
		return name;
	}
	public DirectoryNode getTemplateFileTree() {
		return template;
	}
	
	public double compare(DirectoryNode file) {
		return FileTreeDiffFactory.getFileTreeDifference().getStructureDifferencePercent(file, template).getSimilarityPercent();
	}
	
}
