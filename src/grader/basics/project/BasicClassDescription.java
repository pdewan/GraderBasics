package grader.basics.project;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import util.annotations.EditablePropertyNames;
import util.annotations.PropertyNames;
import util.annotations.StructurePattern;
import util.annotations.Tags;

/**
 * @see ClassDescription
 */
public class BasicClassDescription implements ClassDescription {

    private Class<?> javaClass;
    protected File source;
//    private CompilationUnit compilationUnit;

    public BasicClassDescription(Class<?> javaClass, File source) {
        this.javaClass = javaClass;
        this.source = source;
    }

    @Override
    public Class getJavaClass() {
        return javaClass;
    }

    @Override
    public File getSource() {
        return source;
    }

    @Override
    public String[] getTags() {
        try {
            return javaClass.getAnnotation(Tags.class).value();
        } catch (Exception e) {
            return new String[]{};
        }
    }

    @Override
    public String getStructurePatternName() {
        try {
            return javaClass.getAnnotation(StructurePattern.class).value();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String[] getPropertyNames() {
        try {
            return javaClass.getAnnotation(PropertyNames.class).value();
        } catch (Exception e) {
            return new String[]{};
        }
    }

    @Override
    public String[] getEditablePropertyNames() {
        try {
            return javaClass.getAnnotation(EditablePropertyNames.class).value();
        } catch (Exception e) {
            return new String[]{};
        }
    }

//    @Override
//    public CompilationUnit parse() throws IOException {
//        if (compilationUnit == null)
//            compilationUnit = JavaParser.parse(source);
//        return compilationUnit;
//    }

    @Override
    public List<Method> getTaggedMethods(String tag) {
        List<Method> methods = new ArrayList<Method>();
        // Look for the tagged method

        for (Method method : javaClass.getDeclaredMethods()) {
            try {
                String[] tags = method.getAnnotation(Tags.class).value();
                for (String t : tags)
                    if (t.equalsIgnoreCase(tag))
                        methods.add(method);
            } catch (Exception e) {
                // Move along
            }
        }
        return methods;
    }

    @Override
    public String toString() {
        return javaClass.getCanonicalName();
    }
}
