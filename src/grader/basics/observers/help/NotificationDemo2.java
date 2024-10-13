package grader.basics.observers.help;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

public class NotificationDemo2 {
	 public static void main(String[] args) {

	        // Create a new BufferedImage with specified width, height, and image type
	        int width = 40;
	        int height = 20;
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	        // Manipulate the image pixels (e.g., set colors)
	        for (int x = 0; x < width; x++) {
	            for (int y = 0; y < height; y++) {
	                int rgb = (x * y) % 256; // Example: create a gradient
	                int color = (rgb << 16) | (rgb << 8) | rgb;
	                image.setRGB(x, y, color);
	            }
	        }
	        
	        System.out.println("Created Image");
	     // Load the image (replace with your image path)

	        SwingUtilities.invokeLater(() -> new ImageDisplay(image));
	        
	        try {
				displayTray(image);
//				System.exit(0);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	       
	    }

	 
	 public static void displayTray(Image image) throws AWTException {
	        //Obtain only one instance of the SystemTray object
	        SystemTray tray = SystemTray.getSystemTray();

	        //If the icon is a file
//	        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
	        //Alternative (if the icon is on the classpath):
	        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

	        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
	        //Let the system resize the image if needed
	        trayIcon.setImageAutoSize(true);
	        //Set tooltip text for the tray icon
	        trayIcon.setToolTip("System tray icon demo");
	        tray.add(trayIcon);

	        trayIcon.displayMessage("Hello, World", "notification demo", MessageType.INFO);
	    }
	}

