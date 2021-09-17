import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;

public class managerHome {

	private JFrame frame;
	private JPanel panel;

	//dbSetup hides my username and password
	private dbSetup my = new dbSetup();
	//Building the connection
	private Connection conn = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					managerHome window = new managerHome();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public managerHome() {
		try {
			this.conn = DriverManager.getConnection(
					"jdbc:postgresql://csce-315-db.engr.tamu.edu/db903_group5_project2",
					this.my.user, this.my.pswd);
			JOptionPane.showMessageDialog(null, "Connected Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Failed to connect to Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
			System.exit(0);
		}//end try catch
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (disconnect()) System.exit(0);
			}
		});
		panel = new managerHomePanel(conn, frame);
		frame.setContentPane(panel);
		frame.revalidate();
	}
	
	private boolean disconnect() {
		//closing the connection
		try {
			this.conn.close();
			JOptionPane.showMessageDialog(null, "Disconnected Successfully");
			return true;
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
			return false;
		}//end try catch
	}
	
}
