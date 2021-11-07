package gradingTools.fileTree.comparators;

import java.util.Comparator;

public 	class SortByMatches implements Comparator<KeyValue>{
	@Override
	public int compare(KeyValue arg0, KeyValue arg1) {
		return arg1.getValue()-arg0.getValue();
	}
}
