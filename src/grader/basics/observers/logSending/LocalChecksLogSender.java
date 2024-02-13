package grader.basics.observers.logSending;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Calendar;

import analyzer.extension.timerTasks.LogNameManager;
import grader.basics.observers.LogEntryKind;

public class LocalChecksLogSender {
	private static long totalLogSizeSent = 0;
	private static long totalTimeTaken = 0;
	private static long totalSends = 0;
	private static final String TIME_STATISTICS_FILE_NAME = "timeStatistics.csv";
	private static final String reportURL=
			"https://us-east-1.aws.data.mongodb-api.com/app/rest-api-vsfoo/endpoint/add_log?db=studies&collection=dewan-localchecks";
	private static final String password = "sYCUBa*shZKU4F-yxHrTk8D7FHo4xbBBV.-BK!-L";
//	private static final String reportURL="https://us-south.functions.appdomain.cloud/api/v1/web/ORG-UNC-dist-seed-james_dev/cyverse/add-cyverse-log";
	private static String lastLogFilePath = null;
	
	private static File lastLogDirectory = null;
	
//	private static final String uuidFile="LogsUUID.txt";
//	private static final File fileStore;
//	
//	static {
//		File searchLoc = new File(System.getProperty("user.home")+"/helper-config/");
//		if(searchLoc.exists())
//			fileStore=new File(System.getProperty("user.home")+"/helper-config/"+uuidFile);
//		else
//			fileStore=new File("./Logs/LocalChecks/"+uuidFile);
//	}
	public static void appendStatistics()  {
		if (totalSends > 0) {
		appendStatistics(totalSends + "," + totalLogSizeSent + "," + totalTimeTaken);
		}
	}

	public static void appendStatistics(final String aStats)  {
		PrintWriter out = null;
		try {
		if (lastLogDirectory == null) {
			return;
		}
		File aStatsFile = new File(lastLogDirectory, TIME_STATISTICS_FILE_NAME);
		
		
		    out = new PrintWriter(new BufferedWriter(new FileWriter(aStatsFile, true)));
		    out.println(aStats);
		} catch (IOException e) {
		    System.err.println(e);
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}
	
	public static void sendToServer(SendingData sd) throws Exception {
//		sendToServer(sd.isTests(), sd.getLogFileName(), sd.getLog(),sd.getAssignment(),sd.getIteration());
		sendToServer(sd.getLogEntryKind(), sd.getLogFileName(), sd.getLog(),sd.getAssignment(),sd.getIteration());

	}
	
	private static void maybeUpdateLogDirectory(String aLogFilePath) {
		if (aLogFilePath.equals(lastLogFilePath)) {
			return;
		}
		File aLogFile = new File(aLogFilePath);
		lastLogFilePath = aLogFilePath;
		lastLogDirectory = aLogFile.getParentFile();
	}
	
//	public static void sendToServer(boolean anIsTests, String aLogFilePath, String log, String assignment, int sessionId) throws Exception{		
	public static void sendToServer(LogEntryKind aLogEntryKind, String aLogFilePath, String log, String assignment, int sessionId) throws Exception{		
	
//		if (anIsTests) {
		if (aLogEntryKind == LogEntryKind.TEST) {
			maybeUpdateLogDirectory(aLogFilePath);
		}
		File aFile = new File(aLogFilePath);
		long aStartTime = System.currentTimeMillis();
		JSONObject message = new JSONObject();
		String aLogId = aLogEntryKind + " " + LogNameManager.getLoggedName()+" "+ " "+sessionId + " " + System.currentTimeMillis()+ " " + aFile.getName();

//		String aLogId = anIsTests + " " + LogNameManager.getLoggedName()+" "+ " "+sessionId + " " + System.currentTimeMillis()+ " " + aFile.getName();
//		message.put("log_id",System.currentTimeMillis()+"-"+sessionId);
		message.put("log_id",aLogId);

		message.put("session_id",Integer.toString(sessionId));
		message.put("machine_id",LogNameManager.getLoggedName());

//		message.put("machine_id",getHashMachineId());
		message.put("log_type","LocalChecksLog");
		message.put("course_id",assignment);
		message.put("password",password);
		//message.put("course_id",determineSemester());
//		String BS = "\\\\\\";
		String BS = " B*S ";
		log = log.replaceAll("\n",  BS + "n");
		log = log.replaceAll("\r", BS + "r");
		log = log.replaceAll("	", BS + "t");
		log = log.replaceAll("\t", BS + "t");
		log = log.replaceAll("\f", BS + "f");
		log = log.replaceAll("\"",  BS + "q");
		log = log.replaceAll("\\=", BS + "=" );
		log = log.replaceAll("\\-", BS + "-" );
		log = log.replaceAll("\\+", BS + "+" );

//		log = log.replaceAll("\\", "BSBS");


		

//		log = log.replaceAll("\\", "");
		JSONObject logJSON = new JSONObject();
//		log = JSON
		logJSON.put("json", log);
		

		
		message.put("log", logJSON);
		if (log.length() == 0) {
			return;
		}
//		System.out.println("Posting message:" +message );
		JSONObject ret = post(message,reportURL);
//		System.out.println("Return value from post:" + ret);
		if(ret==null) {
			Thread.sleep(5000);
			post(message,reportURL);
		}
		long anEndTime = System.currentTimeMillis();
		long aSendTime = anEndTime - aStartTime;
		totalSends++;
		totalLogSizeSent += log.length();
		totalTimeTaken += aSendTime;
		
	}
	
//	private static String getHashMachineId() throws Exception {
//		String machineId;
//		if(fileStore.exists()) {	
//			BufferedReader br = new BufferedReader(new FileReader(fileStore));
//			machineId = br.readLine();
//			br.close();
//		}else {
//			InetAddress localHost = InetAddress.getLocalHost();
//			NetworkInterface network = NetworkInterface.getByInetAddress(localHost);
//			try {
//				machineId=byteToHexString(network.getHardwareAddress());
//				MessageDigest digest = MessageDigest.getInstance("SHA-256");
//				machineId=byteToHexString(digest.digest(machineId.getBytes(StandardCharsets.UTF_8)));
//			}catch(Exception e) {
//				System.err.println("Warning: Cannot determine hardware addr generating id for assignment...");
//				System.err.println("Thrown message:\n"+e.getMessage());
//				machineId = "R-"+getRandomID();
//			}
//			fileStore.createNewFile();
//			FileWriter fw = new FileWriter(fileStore);
//			fw.write(machineId);
//			fw.close();
//			fileStore.setWritable(false);
//		}
//		return machineId;
//	}
//	
//	
//	private static String getRandomID(){
//		String val = Double.toString(Math.random())+Double.toString(Math.random())+Double.toString(Math.random());
//		return val.replaceAll("0.", "");
//	}
//	
//	
//	private static String byteToHexString(byte [] arr) {
//		StringBuilder sb = new StringBuilder();
//		for(byte b:arr) 
//			sb.append(Integer.toHexString(Byte.toUnsignedInt(b)));
//		return sb.toString();
//	}
	
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
