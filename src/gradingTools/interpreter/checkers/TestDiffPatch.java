package gradingTools.interpreter.checkers;

import java.util.LinkedList;

import name.fraser.neil.plaintext.diff_match_patch;

public class TestDiffPatch {
	static diff_match_patch differ = new diff_match_patch();
	public static final String STRING_1 = "hello world";
	public static final String STRING_2 = "goodbye world";

	public static void main (String[] args) {
		LinkedList<diff_match_patch.Diff> diff = differ.diff_main(STRING_1, STRING_2);
	    System.out.println(diff);

		differ.diff_cleanupSemantic(diff);
	    // Result: [(-1, "Hello"), (1, "Goodbye"), (0, " World.")]
	    System.out.println(diff);
	    LinkedList<diff_match_patch.Patch> patches = differ.patch_make(STRING_1, diff);
	   Object[] aRetVal = differ.patch_apply(patches, STRING_1);
	   System.out.println("Out:" + aRetVal[0]);
	   
	}
}
