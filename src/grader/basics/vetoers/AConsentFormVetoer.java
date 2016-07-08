package grader.basics.vetoers;

import grader.basics.observers.ATestLogFileWriter;

import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import util.misc.Common;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class AConsentFormVetoer implements VetoableChangeListener{
	boolean consentFormChecked = false;
	public static final String CONSENT_FILE_NAME = "http://www.cs.unc.edu/~dewan/comp401/current/";
	public static final String AGREE_FILE_NAME = "Agree.txt";
	public static final String AGREE_FILE_FULL_NAME = ATestLogFileWriter.LOG_DIRECTORY + "/" + AGREE_FILE_NAME;
	

	
			

	@Override
	public void vetoableChange(PropertyChangeEvent evt)
			throws PropertyVetoException {
		if (consentFormChecked) {
			return;
		}
		if (agreementFileExists())
			return;
		
		
		
	}
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() + "\n\r" : "";
	}
	public static boolean agreementFileExists() {
		File aFile = new File(AGREE_FILE_FULL_NAME);
		return aFile.exists();
	}
	
	public static void showConsentForm() {
		if(Desktop.isDesktopSupported())
		{
		  try {
			Desktop.getDesktop().browse(new URI(CONSENT_FILE_NAME));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
//		InputStream aStream = AConsentFormVetoer.class.getClassLoader().getResourceAsStream(CONSENT_FILE_NAME);
////		 aStream = AConsentFormVetoer.class.getClassLoader().getResourceAsStream("LICENSE");
//
//		String aText = convertStreamToString(aStream);
//		try {
//			Common.writeText(aText, "Checks/ConsentForm.text");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
