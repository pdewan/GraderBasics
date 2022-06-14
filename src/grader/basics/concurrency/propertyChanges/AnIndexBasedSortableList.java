package grader.basics.concurrency.propertyChanges;

import java.util.ArrayList;
import java.util.Comparator;

public class AnIndexBasedSortableList extends ArrayList<Object[]> implements IndexBasedSortableList {

	@Override
	public int compare(Object[] o1, Object[] o2) {
		if (o1.length == 0 || o2.length == 0) {
			return 0;
		}
		String anIndex1String = o1[0].toString();
		String anIndex2String = o2[0].toString();
		try {
			int anIndex1 = Integer.parseInt(anIndex1String);
			int anIndex2 = Integer.parseInt(anIndex2String);
			return anIndex1 - anIndex2;
		} catch (Exception e) {
			return anIndex1String.compareTo(anIndex2String);
		}
	}

}
