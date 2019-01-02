package grader.basics.execution;

import util.trace.TraceableError;

public class TagNotFound extends TraceableError{
	
	Object tag;

	public TagNotFound(String aMessage, Object aTag, Object aFinder) {
		super(aMessage, aFinder);
		tag = aTag;
	}
	
	public Object getTag() {
		return tag;
	}
	
	public static TagNotFound newCase (Object aTag, Object aFinder) {
		String aMessage = "Tag not found";
		TagNotFound retVal = new TagNotFound(aMessage, aTag, aFinder);
		retVal.announce();
		return retVal;
	}

}
