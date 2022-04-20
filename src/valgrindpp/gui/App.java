package valgrindpp.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import valgrindpp.grader.Test;

public class App implements Runnable {

	public void run() {
		JFrame frame = new JFrame("Valgrind Auto Grader");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500, 300));
		frame.setLocationRelativeTo(null);
		frame.setLayout(new FlowLayout());
		
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JTextField pathTextField = new JTextField(30);
		
		JButton browseButton = new JButton("Browse");		
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int option = fc.showOpenDialog(frame);
				if(option == JFileChooser.APPROVE_OPTION) {
					pathTextField.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		JPanel results = new JPanel();
		results.setLayout(new BoxLayout(results, BoxLayout.PAGE_AXIS));
		
		JButton testButton = new JButton("Test");
		testButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					Paths.get(pathTextField.getText());
					List<Test> tests = valgrindpp.main.Main.testMakefileDirectory(pathTextField.getText());
					
					results.removeAll();
					
					for(Test test: tests) {
						results.add(test.view());
					}
					
					SwingUtilities.updateComponentTreeUI(results);
				} catch (IllegalArgumentException 
						| FileSystemNotFoundException 
						| SecurityException exception) {
					pathTextField.setText("Invalid Path");
				}
			}
		});
		
		frame.add(new JLabel("Select assignment directory:"));
		frame.add(pathTextField);
		frame.add(browseButton);
		frame.add(testButton);
		frame.add(results);
		
		frame.pack();
		frame.setVisible(true);
	}
}
