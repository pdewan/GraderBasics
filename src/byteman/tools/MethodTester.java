package byteman.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MethodTester {

	//Arg0 = className
	public static void main(String [] args) {
		if(args.length<1) {
			System.out.println("Not enough args");
			System.exit(4);
		}
		InjectionTargetFileReader.readFile("./injectionTarget.txt", false);
		
		try {
//			System.out.println(MethodTester.class + ":class loader:" + MethodTester.class.getClassLoader());

			BytemanDataServerProxy proxy = BytemanRegistryFactory.getBytemanDataServerProxy();
			//BytemanDataServerProxy clientProxy = BytemanRegistryFactory.getBytemanDataServerProxy(args[1]);
			
			for(BytemanData btmData:proxy.getClassData(args[0])) {
				Class<?> clazz = btmData.getTargetedClass();
//				System.out.println(clazz + ":class loader:" + clazz.getClassLoader());
				
				Constructor<?> con = clazz.getConstructor(btmData.getConTypes());
				Object obj = con.newInstance(btmData.getConArgs());
				
				for(BytemanMethodData methodData:btmData.getMethods()) {
					Method m = clazz.getMethod(methodData.getMethodName(), methodData.getParams());
					System.out.println("Running method: "+m.getName()+" in class: "+clazz.getName());
					Object retval = m.invoke(obj, methodData.getArguments().toArray());
					
					proxy.putResult(clazz.getName(), m.getName(), retval);
					
				}
			}
		} catch (RemoteException | NotBoundException e) {
			System.out.println("Error finding classes");
			e.printStackTrace();
			System.exit(4);
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Error finding methods");
			e.printStackTrace();
			System.exit(4);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Error creating instance of class or calling method");
			e.printStackTrace();
			System.exit(4);
		}

	}

}
