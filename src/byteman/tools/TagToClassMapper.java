package byteman.tools;

public class TagToClassMapper {
	public static String determineClass(String clazz) {
		String clazzName = BytemanRulesGenerator.getDisplayNameMapping().get(clazz);
		if (clazzName == null) {
			clazzName = clazz;
		}
		return clazzName;
	}

}
