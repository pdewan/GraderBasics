package byteman.tools;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BytemanDataServerProxy extends Remote, Serializable{

	public void addClassData(BytemanData data) throws RemoteException;
	public List<BytemanData> getClassData(String className) throws RemoteException;
	public void putResult(String className, String methodName, Object retval) throws RemoteException;
	public Object getResult(String className, String methodName) throws RemoteException;
	
	//Get test class name (interface called test that returns a serializable returned to the server)
	//putResult (any serializable obj)
	
	
}
