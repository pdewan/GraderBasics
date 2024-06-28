package grader.basics.config;

import java.util.HashMap;

import trace.grader.basics.GraderBasicsTraceUtility;
import util.trace.Tracer;

public class TracingHashMap<ValueType> extends HashMap<String, ValueType> {
	@Override
	public ValueType put (String aKey, ValueType aValue) {
		if (GraderBasicsTraceUtility.getTracerShowInfo()) {
			System.out.println(Tracer.INFO_PREFIX + "Set Attribute " + aKey + "-->" + aValue);
//		Tracer.info(this, "Runtime property " + aKey + "-->" + aValue);
		}
		return super.put(aKey, aValue);
	}
	

}
