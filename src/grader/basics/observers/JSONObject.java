package grader.basics.observers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JSONObject implements Serializable{

	private static final long serialVersionUID = 1L;

	List<String> keys;
	List<List<Object>> value;
	
	public JSONObject() {
		keys = new ArrayList<String>();
		value = new ArrayList<List<Object>>();
	}
	
	public void put(String str, Object obj) {
		int index = keys.indexOf(str);
		List<Object> obList = new ArrayList<Object>();
		obList.add(obj);
		if(index == -1) {
			keys.add(str);
			value.add(obList);
		} else {
			value.remove(index);
			value.add(index, obList);
		}
	}
	
	public void append(String str, Object obj) {
		int index = keys.indexOf(str);
		if(index == -1) {
			keys.add(str);
			List<Object> obList = new ArrayList<Object>();
			obList.add(obj);
			value.add(obList);
		} else 
			value.get(index).add(obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		
		int size=keys.size();
		
		for(int i=0;i<size;i++) {
			sb.append(putInQuotes(keys.get(i))+":");
			
			if(value.get(i).size()>1) {
				List<Object> obList = value.get(i);
				int obSize=obList.size();
				sb.append('[');
				for(int j=0;j<obSize;j++) {
					Object ob = obList.get(j);
					if(ob instanceof JSONObject) 
						sb.append(ob.toString());
					else
						sb.append(putInQuotes(ob.toString()));
					if(j!=obSize-1)
						sb.append(',');
				}
				sb.append(']');
			}else {
				Object ob = value.get(i).get(0);
				if(ob instanceof JSONObject)
					sb.append(ob.toString());
				else
					sb.append(putInQuotes(ob.toString()));
			}
				
			
			if(i!=size-1)
				sb.append(',');
			
		}
		sb.append('}');	
		
		return sb.toString();
	}
	
	
	private String putInQuotes(String str) {
		return "\""+str+"\"";
	}
	
}
