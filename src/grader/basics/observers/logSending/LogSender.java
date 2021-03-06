package grader.basics.observers.logSending;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Calendar;

public class LogSender {
	private static final String reportURL="https://us-south.functions.appdomain.cloud/api/v1/web/ORG-UNC-dist-seed-james_dev/cyverse/add-cyverse-log";
	private static final String uuidFile="LogsUUID.txt";
	private static final File fileStore;
	
	static {
		File searchLoc = new File(System.getProperty("user.home")+"/helper-config/");
		if(searchLoc.exists())
			fileStore=new File(System.getProperty("user.home")+"/helper-config/"+uuidFile);
		else
			fileStore=new File("./Logs/LocalChecks/"+uuidFile);
	}
	
	public static void sendToServer(SendingData sd) throws Exception {
		sendToServer(sd.getLog(),sd.getAssignment(),sd.getIteration());
	}
	
	public static void sendToServer(String log, String assignment, int sessionId) throws Exception{		
		JSONObject message = new JSONObject();
		
		message.put("log_id",System.currentTimeMillis()+"-"+sessionId);
		message.put("session_id",Integer.toString(sessionId));
		message.put("machine_id",getHashMachineId());
		message.put("log_type","LocalChecksLog");
		message.put("course_id",assignment);
		//message.put("course_id",determineSemester());
		
		JSONObject logJSON = new JSONObject();
		logJSON.put("json", log);
		
		message.put("log", logJSON);
		
		JSONObject ret = post(message,reportURL);
		if(ret==null) {
			Thread.sleep(5000);
			post(message,reportURL);
		}
		
	}
	
	private static String getHashMachineId() throws Exception {
		String machineId;
		if(fileStore.exists()) {	
			BufferedReader br = new BufferedReader(new FileReader(fileStore));
			machineId = br.readLine();
			br.close();
		}else {
			InetAddress localHost = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(localHost);
			try {
				machineId=byteToHexString(network.getHardwareAddress());
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				machineId=byteToHexString(digest.digest(machineId.getBytes(StandardCharsets.UTF_8)));
			}catch(Exception e) {
				System.err.println("Warning: Cannot determine hardware addr generating id for assignment...");
				System.err.println("Thrown message:\n"+e.getMessage());
				machineId = "R-"+getRandomID();
			}
			fileStore.createNewFile();
			FileWriter fw = new FileWriter(fileStore);
			fw.write(machineId);
			fw.close();
			fileStore.setWritable(false);
		}
		return machineId;
	}
	
	
	private static String getRandomID(){
		String val = Double.toString(Math.random())+Double.toString(Math.random())+Double.toString(Math.random());
		return val.replaceAll("0.", "");
	}
	
	
	private static String byteToHexString(byte [] arr) {
		StringBuilder sb = new StringBuilder();
		for(byte b:arr) 
			sb.append(Integer.toHexString(Byte.toUnsignedInt(b)));
		return sb.toString();
	}
	
	@SuppressWarnings("unused")
	private static String determineSemester() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		Calendar compare = Calendar.getInstance();
		compare.set(year, Calendar.MAY, 5);
		if(c.before(compare))
			return "Spring"+year;
		compare.set(year, Calendar.JUNE, 20);
		if(c.before(compare))
			return "SummerI"+year;
		compare.set(year, Calendar.AUGUST, 5);
		if(c.before(compare))
			return "SummerII"+year;
		return "Fall"+year;
	}
	
	public static JSONObject post(JSONObject request, String urlString) {
		BufferedReader reader;
		String line;
		StringBuffer sb = new StringBuffer();
		int status = 500;
		JSONObject body = new JSONObject();
		try {
			body.put("body", request);
		} catch (Exception e1) {
			System.err.println(e1.getMessage());
//			e1.printStackTrace();
		}

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Length", (body.toString().length()+2)+"");
			OutputStream os = conn.getOutputStream();
			byte[] input = body.toString().getBytes();
//			System.out.println(body.toString(4));
			os.write(input, 0, input.length);
			os.write("\r\n".getBytes());
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			status = conn.getResponseCode();
			
			if (status > 299) {
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
			} else {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
			}
			conn.disconnect();
		} catch (Exception e) {
			System.err.println("Error sending logs:\n"+e.getMessage());
			return null;
//			e.printStackTrace();
		} 
		try {
			return new JSONObject();
		} catch (Exception e) {
		}
		return null;
	}
}
