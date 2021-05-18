package grader.basics.observers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//import org.json.JSONObject;

public class LogSender {
	private static final String reportURL = "https://us-south.functions.appdomain.cloud/api/v1/web/ORG-UNC-dist-seed-james_dev/cyverse/add-cyverse-log";
	private static MessageDigest digest = null;
	
//	public static void sendToServer(String log, int sessionId) throws Exception{
//		if(digest==null) 
//			digest = MessageDigest.getInstance("SHA-256");
//		
//		JSONObject message = new JSONObject();
//		
//		message.put("log_id",System.currentTimeMillis()+"-"+sessionId);
//		message.put("session_id",Integer.toString(sessionId));
//		message.put("machine_id",getHashMachineId());
//		message.put("log_type","LocalChecksLog");
//		message.put("course_id",determineSemester());
//		
//		JSONObject logJSON = new JSONObject();
//		logJSON.put("json", log);
//		
//		message.put("log", logJSON);
//		
//		JSONObject ret = post(message,reportURL);
//		
//		if(ret==null) {
//			Thread.sleep(5000);
//			post(message,reportURL);
//		}
//		
//	}
//	
//	private static String getHashMachineId() throws Exception {
//		InetAddress localHost = InetAddress.getLocalHost();
//		NetworkInterface network = NetworkInterface.getByInetAddress(localHost);
//		String machineId = byteToHexString(network.getHardwareAddress());
//		return byteToHexString(digest.digest(machineId.getBytes(StandardCharsets.UTF_8)));
//	}
//	private static String byteToHexString(byte [] arr) {
//		StringBuilder sb = new StringBuilder();
//		for(byte b:arr) 
//			sb.append(Integer.toHexString(Byte.toUnsignedInt(b)));
//		return sb.toString();
//	}
//	private static String determineSemester() {
//		Calendar c = Calendar.getInstance();
//		int year = Calendar.YEAR;
//		Calendar compare = Calendar.getInstance();
//		compare.set(year, Calendar.MAY, 5);
//		if(c.before(compare))
//			return "Spring"+year;
//		compare.set(year, Calendar.JUNE, 20);
//		if(c.before(compare))
//			return "SummerI"+year;
//		compare.set(year, Calendar.AUGUST, 5);
//		if(c.before(compare))
//			return "SummerII"+year;
//		return "Fall"+year;
//	}
//	
//	public static JSONObject post(JSONObject request, String urlString) {
//		BufferedReader reader;
//		String line;
//		StringBuffer sb = new StringBuffer();
//		int status = 500;
//		JSONObject body = new JSONObject();
//		try {
//			body.put("body", request);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//		try {
//			URL url = new URL(urlString);
//			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//			conn.setDoOutput(true);
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setRequestProperty("Accept", "application/json");
//			conn.setRequestProperty("Content-Length", (body.toString().length()+2)+"");
//			OutputStream os = conn.getOutputStream();
//			byte[] input = body.toString().getBytes();
////			System.out.println(body.toString(4));
//			os.write(input, 0, input.length);
//			os.write("\r\n".getBytes());
//			conn.setConnectTimeout(5000);
//			conn.setReadTimeout(5000);
//			
//			status = conn.getResponseCode();
//			
//			if (status > 299) {
//				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//				while ((line = reader.readLine()) != null) {
//					sb.append(line);
//				}
//				reader.close();
//			} else {
//				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				while ((line = reader.readLine()) != null) {
//					sb.append(line);
//				}
//				reader.close();
//			}
//			conn.disconnect();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//		try {
//			return new JSONObject(sb.toString().substring(sb.toString().indexOf("{")));
//		} catch (Exception e) {
//		}
//		return null;
//	}
}
