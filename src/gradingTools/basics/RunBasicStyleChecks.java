package gradingTools.basics;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.TokenUtil;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.junit.BasicJUnitUtils;
import grader.basics.project.BasicProject;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;
import gradingTools.basics.sharedTestCase.checkstyle.predefined.BasicGeneralStyleSuite;
import unc.checks.STBuilderCheck;
import unc.symbolTable.AnSTTypeFromClass;
import unc.symbolTable.STMethod;
import unc.symbolTable.STNameable;
import unc.symbolTable.STType;
import unc.symbolTable.STVariable;
import unc.symbolTable.SymbolTable;

public class RunBasicStyleChecks {
	public static Project runBasicStyleChecks(String[] args) {
		if (args.length < 2) {
			System.err.println(
					"Please enter absolute names of the source code folder followed by the check style configuration file name");
			System.exit(-1);
		}
		File aSourceFolder = new File(args[0]);

		String aCheckstyleConfiguration = args[1];
		
		BasicProject.setCheckEclipseFolder(false);
		BasicProject.setCheckCheckstyleFolder(false);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLogTestData(false);
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLoadClasses(false);

		CurrentProjectHolder.setProjectLocation(aSourceFolder);

		BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.setCheckStyleConfiguration(aCheckstyleConfiguration);
		BasicJUnitUtils.testAll(BasicGeneralStyleSuite.class);
		return CurrentProjectHolder.getCurrentProject();

	}
	public static JSONArray toJSONArray(STNameable[] aTags) {		
		JSONArray retVal = new JSONArray();
		for (STNameable aTag:aTags) {
			retVal.put(aTag.getName());
		}
		return retVal;
	}
	
//	public static List<String> toStrings(STNameable[] aTags) {
//	List<String> retVal = new ArrayList();
////	StringBuilder retVal = new StringBuilder();
//	for (STNameable aTag:aTags) {
//		retVal.add(aTag.getName());
//		
//	}
//	return retVal;
//}
//	public static String toJSONString(STNameable[] aTags) {
//		if (aTags == null || aTags.length == 0)) {
//			return "";
//		}
//		StringBuilder retVal = new StringBuilder();
//		for (int anIndex = 0; anIndex < aTags.length; anIndex++) {
//			if (anIndex != 0) {
//				retVal.append("+");
//			}
//		}
//		return retVal;
//	}
	public static JSONObject toJSONObject (STType anSTType) {
		JSONObject aClassJSON = new JSONObject();			
		JSONArray aMethodsJSON = new JSONArray();
		JSONArray aVariablesJSON = new JSONArray();
		aClassJSON.put("methods", aMethodsJSON);
		aClassJSON.put("variables", aVariablesJSON);
		STNameable[] anAllTags = anSTType.getAllTags();
		JSONArray aTypeTags = toJSONArray(anAllTags);
		
		aClassJSON.put("tags", aTypeTags);
		aClassJSON.put("name", anSTType.getName());
		List<STVariable> aVariables = anSTType.getDeclaredSTGlobals();
		for (STVariable aVariable:aVariables) {
			JSONObject aVariableJSON = toJSONObject(aVariable);
			aVariablesJSON.put(aVariableJSON);
		}
		STMethod[] aMethods = anSTType.getDeclaredMethods();
		for (STMethod aMethod:aMethods) {
			JSONObject aMethodJSON = toJSONObject(aMethod, aTypeTags);
			aMethodsJSON.put(aMethodJSON);
		}

//		for (STVariable aVariable:aVariables) {
//			JSONObject aVariableJSON = toJSONObject(aVariable);
//			aVariablesJSON.put(aVariableJSON);
//		}
		return aClassJSON;
	}
	
	public static JSONObject toJSONObject (STMethod anSTMethod,  JSONArray aTypeTags) {
		
		DetailAST aMethodAST = anSTMethod.getAST();
		JSONObject aMethodJSON = toJSONObject(aMethodAST);
		STNameable[] aMethodTags = anSTMethod.getTags();
		JSONArray aMethodJSONTags = toJSONArray(aMethodTags);
//		if (aMethodJSONTags.length() > 0) {
		aMethodJSON.put("methodTags", aMethodJSONTags);	
		aMethodJSON.put("declaringTypeTags", aTypeTags);	
		return aMethodJSON;
	}
	
	public static JSONObject toJSONObject (STVariable aVariable) {
		JSONObject aVariableJSON = toJSONObject(aVariable.getAST());
		STType aVariableType = aVariable.getDeclaringSTType();
		STNameable[] anAllTypeTags = aVariableType.getAllTags();
		JSONArray aDeclaringTypeTags = toJSONArray(anAllTypeTags) ;
		aVariableJSON.put("typeTags", aDeclaringTypeTags);
		return aVariableJSON;
	}
	
	public static void writeToFile(JSONObject aJSONObject, String aFileName) {
		try {
			FileWriter file = new FileWriter(aFileName);
			file.write(aJSONObject.toString());
			file.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public static void writeToFile(JSONObject aJSONObject) {
		writeToFile(aJSONObject, "projects.json");
	}
	
	public static void writeJSONSymbolTable(Project aProject) {
		JSONObject aJSONObject = toJSONObject(aProject);
		writeToFile(aJSONObject);
	}
	public static void writeJSONSymbolTable(Project aProject, String aFileName) {
		JSONObject aJSONObject = toJSONObject(aProject);
		writeToFile(aJSONObject, aFileName);
	}

	public static JSONObject toJSONObject (Project aProject) {
		SymbolTable aSymbolTable = aProject.getSymbolTable();
		JSONObject aProjectJSON = new JSONObject();
		aProjectJSON.put("name", aProject.getProjectFolder());
		JSONArray aClassesJSON = new JSONArray();
		aProjectJSON.put("classes", aClassesJSON);
		Collection<STType> anSTTypes = aSymbolTable.getAllSTTypes();
		for (STType anSTType:anSTTypes) {
			if (anSTType instanceof AnSTTypeFromClass) {
				continue;
			}		
			JSONObject aClassJSON = toJSONObject(anSTType);	
			aClassesJSON.put(aClassJSON);
//			JSONArray aMethodsJSON = new JSONArray();
//			JSONArray aVariablesJSON = new JSONArray();
//			aClassJSON.put("methods", aMethodsJSON);
//			aClassJSON.put("variables", aVariablesJSON);
//			STNameable[] anAllTags = anSTType.getAllTags();
//			JSONArray aTypeTags = toJSONArray(anAllTags);
//			aClassJSON.put("tags", aTypeTags);
//			aClassJSON.put("name", anSTType.getName());
//			List<STVariable> aVariables = anSTType.getDeclaredSTGlobals();
//			for (STVariable aVariable:aVariables) {
//				JSONObject aVariableJSON = toJSONObject(aVariable.getAST());
//				STType aVariableType = aVariable.getDeclaringSTType();
//				STNameable[] anAllTypeTags = anSTType.getAllTags();
////				if (anAllTypeTags.length > 0) {
//					JSONArray aDeclaringTypeTags = toJSONArray(anAllTypeTags) ;
//					aVariableJSON.put("typeTags", aDeclaringTypeTags);					
////				}
//				aVariablesJSON.put(aVariableJSON);
//
////				String aName = aVariable.getName();
////				STType anAssignment = aVariable.get
//			}
//			aClassJSON.put("Variables", aVariablesJSON);
//
//			String aClassName = anSTType.getName();
//			
//////			STNameable[] anAllComputedTags = anSTType.getAllComputedTags();
////			STNameable[] aTags = anSTType.getTags();
////			STNameable[] aComputedTags = anSTType.getComputedTags();
////			STNameable[] adeclaredTags = anSTType.getConfiguredTags();
////			STNameable[] aDerivedTags = anSTType.getDerivedTags();
//
//
//
//
//			STMethod[] anSTMethods = anSTType.getDeclaredMethods();
////			if (anSTMethods.length > 0) {
//				aClassJSON.put("methods", aMethodsJSON);
////			}
//				aClassesJSON.put(aClassJSON);
//			
//			for (STMethod anSTMethod:anSTMethods) {
//				DetailAST aMethodAST = anSTMethod.getAST();
//				JSONObject aMethodJSON = toJSONObject(aMethodAST);
//				aMethodJSON.put("className", aClassName);
////				if (aTypeTags.length() > 0) {
//					aMethodJSON.put("classTags", aTypeTags);					
////				}
//				STNameable[] aMethodTags = anSTMethod.getTags();
//				JSONArray aMethodJSONTags = toJSONArray(aMethodTags);
////				if (aMethodJSONTags.length() > 0) {
//					aMethodJSON.put("methodTags", aMethodTags);				
//
////				}
//				aMethodsJSON.put(aMethodJSON);
//			
//			}
//			DetailAST anAST = anSTType.getAST();
//			if (anAST == null) {
//				continue;
//			}
//			RunBasicStyleChecks.toJSOnArray(anSTType.getAST()); 
		}
		return aProjectJSON;
	}
	public static JSONArray toJSOnArray(DetailAST aDetailAST) {
		int aTokenType = aDetailAST.getType();
		String aName = TokenUtil.getTokenName(aTokenType);
		int aDerivedTokenId = TokenUtil.getTokenId(aName);
		
		int anInt = TokenTypes.CLASS_DEF;
		String aText = STBuilderCheck.toStringList(aDetailAST);
		return null;
		
	}
	public static JSONObject toJSONObject(DetailAST aDetailAST) {
		JSONObject retVal = new JSONObject();
		JSONArray aChildren = new JSONArray();
		int aTokenType = aDetailAST.getType();
		String aText = aDetailAST.getText();
		String aTokenString = TokenUtil.getTokenName(aTokenType);
		retVal.put("type", aTokenString);
		retVal.put("text", aText);
		if (!aDetailAST.hasChildren()) {
			return retVal;
		}
		retVal.put("children", aChildren);
		DetailAST aChild = aDetailAST.getFirstChild();
		while (aChild != null) {
			JSONObject aJSONObject = toJSONObject(aChild);
			aChildren.put(aJSONObject);
			aChild = aChild.getNextSibling();
		}
		return retVal;

//		int aDerivedTokenId = TokenUtil.getTokenId(aTokenString);
//		
//		int anInt = TokenTypes.CLASS_DEF;
//		String aText = STBuilderCheck.toStringList(aDetailAST);
//		return null;
		
	}
	
	
	
//	public static JSONArray toJSOnArray(DetailAST aDetailAST) {
//		String aName = TokenUtil.getTokenName(aDetailAST.getType());
//		int anInt = TokenTypes.CLASS_DEF;
//		String aText = STBuilderCheck.toStringList(aDetailAST);
//		return null;
//		
//	}
//	
	public static void main(String[] args) {
		Project aProject = runBasicStyleChecks(args);
//		SymbolTable aSymbolTable = aProject.getSymbolTable();
//		List<STType> anSTTypes = aSymbolTable.getAllSTTypes();
		
		
//		if (args.length < 2) {
//			System.err.println(
//					"Please enter absolute names of the source code folder followed by the check style configuration file name");
//			System.exit(-1);
//		}
//		File aSourceFolder = new File(args[0]);
//
//		String aCheckstyleConfiguration = args[1];
//		
//		BasicProject.setCheckEclipseFolder(false);
//		BasicProject.setCheckCheckstyleFolder(false);
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLogTestData(false);
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setLoadClasses(false);
//
//		CurrentProjectHolder.setProjectLocation(aSourceFolder);
//
//		BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
//				.setCheckStyleConfiguration(aCheckstyleConfiguration);
//		BasicJUnitUtils.testAll(BasicGeneralStyleSuite.class);

	}
}
