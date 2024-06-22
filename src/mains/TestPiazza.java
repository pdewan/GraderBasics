package mains;
import java.io.IOException;

import main.TesterPD;
import piazza.LoginFailedException;
import piazza.NotLoggedInException;
public class TestPiazza {
public static void main(String[] args) {
	try {
		main.TesterPD.main(args);
	} catch (IOException | LoginFailedException | NotLoggedInException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
