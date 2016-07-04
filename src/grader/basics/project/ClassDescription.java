package grader.basics.project;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Based on {@link grader.project.flexible.FlexibleClassDescription}
 */
public interface ClassDescription {

    /**
     * @return The Java {@link java.lang.Class} that has been loaded
     */
    public Class<?> getJavaClass();

    /**
     * @return The source code file.
     */
    public File getSource();

    /**
     * @return The tags that the class has been annotated with
     */
    public String[] getTags();

    /**
     * @return The structure pattern that the class has been annotated with
     */
    public String getStructurePatternName();

    /**
     * @return The property names that the class has been annotated with
     */
    public String[] getPropertyNames();

    /**
     * @return The editable property that the class has been annotated with
     */
    public String[] getEditablePropertyNames();

    /**
     * @return The parsed code
     */
//    public CompilationUnit parse() throws IOException;

    /**
     * Looks for and returns all method within the class which have been tagged with the given tag
     * @param tag The tag to search for
     * @return Method with corresponding tags
     */
    public List<Method> getTaggedMethods(String tag);

}
