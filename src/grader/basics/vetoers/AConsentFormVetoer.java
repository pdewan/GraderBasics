package grader.basics.vetoers;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class AConsentFormVetoer implements VetoableChangeListener {
	
	boolean consentFormChecked = false;
	public static final String CONSENT_INFO_FILE_NAME = "http://www.cs.unc.edu/~dewan/comp401/current/Downloads/Adult%20Consent%20Form.htm";
	public static final String CONSENT_FILE_NAME = "Consent.txt";
//	public static final String LOG_DIRECTORY = "Checks";
	public static final String LOG_DIRECTORY = "Logs/LocalChecks";

	public static final String AGREE_FILE_FULL_NAME = LOG_DIRECTORY
			+ "/" + CONSENT_FILE_NAME;
	public static final String MESSAGE_CONTENTS = 
			"I have the read the Adult Consent Form at:" + CONSENT_INFO_FILE_NAME + "\n"

//			"I have the read the Adult Consent Form Dated March 24, 2016 for IRB STudy 09-1134.\n"
					+ "I agree to participate in the study.\n"
					+ "I agree to not modify or delete the collected data unless authorized by the study PI.";
	

	public static int showConfirmDialog() {
		JPanel panel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(720, 300);
			}
		};

		// We can use JTextArea or JLabel to display messages
		JTextArea textArea = new JTextArea();
//		textArea.setText("I have the read the Adult Consent Form Dated March 24, 2016 for IRB STudy 09-1134.\n"
//				+ "I agree to particpate in the study.\n."
//				+ "I agree to not modify or delete the collected data unless authorized by the study PI."
//				);
		textArea.setText(MESSAGE_CONTENTS);
		textArea.setEditable(false);
		panel.setLayout(new BorderLayout());
//		panel.setPreferredSize(new Dimension(200, 200));
		JScrollPane aScrolledPane = new JScrollPane(textArea);
		panel.add(aScrolledPane);

		return JOptionPane.showConfirmDialog(null, panel, // Here goes content
				"Consenting to Study Form at \n " + CONSENT_INFO_FILE_NAME,
				// JOptionPane.OK_CANCEL_OPTION, // Options for JOptionPane
				JOptionPane.YES_NO_OPTION, // Options for JOptionPane
				JOptionPane.DEFAULT_OPTION); // Message type
	}
	
	public static boolean checkConstentForm(String aFileName) {
		if (agreementFileExists(aFileName))
			return true;
		showConsentForm();
		int retVal = showConfirmDialog();
		if (retVal != 0) {
			return false;
		} else {
			Date aDate = new Date(System.currentTimeMillis());
			try {
//				Common.writeText(AGREE_FILE_FULL_NAME, aDate.toString());
				writeText(new File(aFileName), MESSAGE_CONTENTS + "\n" + aDate.toString());
				return true;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;

			}
		}
	}

	@Override
	public void vetoableChange(PropertyChangeEvent evt)
			throws PropertyVetoException {
		if (!isCheckConsentForm()) {
			return;
		}
		if (consentFormChecked) {
			return;
		}
		if (!checkConstentForm(AGREE_FILE_FULL_NAME))
			throw new PropertyVetoException("Consent form not signed", evt);
//		if (agreementFileExists())
//			return;
//		showConsentForm();
//		int retVal = showConfirmDialog();
//		if (retVal != 0) {
//			throw new PropertyVetoException("Consent form not signed", evt);
//		} else {
//			Date aDate = new Date(System.currentTimeMillis());
//			try {
////				Common.writeText(AGREE_FILE_FULL_NAME, aDate.toString());
//				writeText(new File(AGREE_FILE_FULL_NAME), aDate.toString());
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		// if (JOptionPane.showConfirmDialog(null, "I Agree",
		// "Consent to Participate in IRB Study 09-1134 (Virtual Radical-Co-location and Beyond)",
		// JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		// return;
		// } else {
		// throw new PropertyVetoException("Consent form not signed", evt);
		// }

	}

	public static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() + "\n\r" : "";
	}

	public static boolean agreementFileExists() {
		return agreementFileExists(AGREE_FILE_FULL_NAME);
//		File aFile = new File(AGREE_FILE_FULL_NAME);
//		return aFile.exists();
	}
	public static boolean agreementFileExists(String aFileName) {
		File aFile = new File(aFileName);
		return aFile.exists();
	}

	public static void showConsentForm() {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(CONSENT_INFO_FILE_NAME));
			} catch (Exception e) {
			//catch (IOException | URISyntaxException e) {
				System.err.println("Could not sdisplay browser. Please see: " + CONSENT_INFO_FILE_NAME);
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		// InputStream aStream =
		// AConsentFormVetoer.class.getClassLoader().getResourceAsStream(CONSENT_FILE_NAME);
		// // aStream =
		// AConsentFormVetoer.class.getClassLoader().getResourceAsStream("LICENSE");
		//
		// String aText = convertStreamToString(aStream);
		// try {
		// Common.writeText(aText, "Checks/ConsentForm.text");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
	public static void writeText(File file, String text) throws IOException {
		if (!file.exists()) {
			File aFolder = file.getParentFile();
			if (!aFolder.exists()) 
				aFolder.mkdirs();
			file.createNewFile();
		}
//		if (!file.exists())
//			file.mkdirs();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(text.getBytes());
			fos.flush();
			
		} catch (IOException e) {
			System.out.println(file.getName() + " " + e.getMessage());
			fos.close();
			throw e;
		}
		fos.close();
	}
	static boolean checkConsentForm = false;
	public static void setCheckConsentForm(boolean newVal) {
		checkConsentForm = newVal;
	}
	public static boolean isCheckConsentForm() {
		return checkConsentForm;
	}
}
