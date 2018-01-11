import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Pre_requisite_check_control {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Program execution begins here
		Login runDB = new Login();

		try {

			runDB.loadDriver();
			runDB.makeConnection();
			runDB.buildStatement();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
