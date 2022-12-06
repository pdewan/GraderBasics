package byteman.tools;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BytemanDataServer implements BytemanDataServerProxy{
	private static final long serialVersionUID = -9189666880348789699L;
	private Map<String, List<BytemanData>> dataSet = new HashMap<>();
	private Map<String, Map<String, Object>> returnValues = new HashMap<>();
	
	@Override
	public void addClassData(BytemanData data) throws RemoteException {
		if(!dataSet.containsKey(data.getTargetedClass().getName())) {
			dataSet.put(data.getTargetedClass().getName(), new ArrayList<>());
		}
		dataSet.get(data.getTargetedClass().getName()).add(data);
	}

	@Override
	public List<BytemanData> getClassData(String className) throws RemoteException {
		return dataSet.get(className);
	}

	@Override
	public void putResult(String className, String methodName, Object retval) throws RemoteException {
		if(!returnValues.containsKey(className)) {
			returnValues.put(className, new HashMap<>());
		}
		returnValues.get(className).put(methodName, retval);
	}
	
	@Override
	public Object getResult(String className, String methodName) throws RemoteException {
		if(returnValues.containsKey(className) && returnValues.get(className).containsKey(methodName)) {
			return returnValues.get(className).get(methodName);
		}
		return null;
	}
}
