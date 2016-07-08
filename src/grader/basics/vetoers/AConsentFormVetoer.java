package grader.basics.vetoers;

import grader.basics.observers.ATestLogFileWriter;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Panel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import util.misc.Common;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class AConsentFormVetoer implements VetoableChangeListener {
	boolean consentFormChecked = false;
	public static final String CONSENT_FILE_NAME = "http://www.cs.unc.edu/~dewan/comp401/current/";
	public static final String AGREE_FILE_NAME = "Agree.txt";
	public static final String AGREE_FILE_FULL_NAME = ATestLogFileWriter.LOG_DIRECTORY
			+ "/" + AGREE_FILE_NAME;

	protected int showConfirmDialog() {
		JPanel panel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(470, 60);
			}
		};

		// We can use JTextArea or JLabel to display messages
		JTextArea textArea = new JTextArea();
		textArea.setText("I have the read the Adult Consent Form Dated March 24, 2016 for IRB STudy 09-1134.\n"
				+ "I agree to particpate in the study.\n."
				+ "I agree to not modify or delete the collected data unless authorized by the study PI."
				);
		textArea.setEditable(false);
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(textArea));

		return JOptionPane.showConfirmDialog(null, panel, // Here goes content
				"Consent for Virtual Radical-Co-location and Beyond",
				// JOptionPane.OK_CANCEL_OPTION, // Options for JOptionPane
				JOptionPane.YES_NO_OPTION, // Options for JOptionPane
				JOptionPane.DEFAULT_OPTION); // Message type
	}

	@Override
	public void vetoableChange(PropertyChangeEvent evt)
			throws PropertyVetoException {
		if (consentFormChecked) {
			return;
		}
		if (agreementFileExists())
			return;
		showConsentForm();
		int retVal = showConfirmDialog();
		if (retVal != 0) {
			throw new PropertyVetoException("Consent form not signed", evt);
		} else {
			Date aDate = new Date(System.currentTimeMillis());
			try {
				Common.writeText(AGREE_FILE_FULL_NAME, aDate.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
		File aFile = new File(AGREE_FILE_FULL_NAME);
		return aFile.exists();
	}

	public static void showConsentForm() {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(CONSENT_FILE_NAME));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

}
