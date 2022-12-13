package byteman.tools;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

public class BytemanRegistryFactory {

	private static Registry registry;
	private static final int registryPort = 50505;
	private static boolean serverIsSet=false;
	public static final String BYTEMAN_SERVER_NAME = "btm_server";
	private static BytemanDataServerProxy proxy;
	
	
	public static Registry getRegistry(boolean creator){
		if(registry == null) {
			try {
				if(creator) {
					try {
						registry = LocateRegistry.createRegistry(registryPort);
					}catch(ExportException e) {
						registry = LocateRegistry.getRegistry(registryPort);
						serverIsSet=true;
					}
				}else {
					registry = LocateRegistry.getRegistry(registryPort);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return registry;
	}

	
	public static BytemanDataServerProxy getBytemanDataServerProxy() throws AccessException, RemoteException, NotBoundException {
		if(proxy==null) {
			Registry registry = getRegistry(false);
			proxy =(BytemanDataServerProxy)registry.lookup(BYTEMAN_SERVER_NAME);
		}
		return proxy;
	}
	
	public static BytemanDataServerProxy getAndPossiblySetServerProxy() throws AccessException, RemoteException, NotBoundException, AlreadyBoundException {
		Registry registry = getRegistry(true);
		if(serverIsSet) {
			return (BytemanDataServerProxy)registry.lookup(BYTEMAN_SERVER_NAME);
		}
		proxy = new BytemanDataServer();
		UnicastRemoteObject.exportObject(proxy, registryPort);
		registry.bind(BYTEMAN_SERVER_NAME, proxy);
		serverIsSet = true;
		return proxy;
	}
}
