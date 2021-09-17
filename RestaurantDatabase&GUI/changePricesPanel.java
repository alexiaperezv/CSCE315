import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class changePricesPanel extends JPanel {
	private Connection conn = null;
	private JTable table;

	/**
	 * Create the panel.
	 */
	public changePricesPanel(Connection conn, JFrame frame) {
		this.conn = conn;
		
		setLayout(new MigLayout("", "[fill][grow,fill]", "[20:20:20,fill][20:20:20,fill][20:20:20,fill][20:20:20,fill][20:20:20,fill][20:20:20,fill][fill]"));
		
		JButton btnHome = new JButton("Home");
		btnHome.setBackground(new Color(255, 140, 0));
		btnHome.setBorder(new LineBorder(Color.BLACK));
		btnHome.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	frame.setContentPane(new managerHomePanel(conn, frame));
		    	frame.revalidate();
		    }
		});
		add(btnHome, "cell 0 0 1 2");
		
		String[] colNames = {"Product","Price",""};
		table = new JTable(getProducts(), colNames);
		JScrollPane scroll = new JScrollPane(table);
//		table.setFillsViewportHeight(true);
		add(scroll, "cell 1 0 1 6");
		
		JButton btnOrderHistory = new JButton("Order History");
		btnOrderHistory.setBackground(new Color(255, 140, 0));
		btnOrderHistory.setBorder(new LineBorder(Color.BLACK));
		btnOrderHistory.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	frame.setContentPane(new orderHistoryPanel(conn, frame));
		    	frame.revalidate();
		    }
		});
		add(btnOrderHistory, "cell 0 2 1 2");
		
		JButton btnChangePrices = new JButton("Change Prices");
		btnChangePrices.setBackground(new Color(255, 140, 0));
		btnChangePrices.setBorder(new LineBorder(Color.BLACK));
		btnChangePrices.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	frame.setContentPane(new changePricesPanel(conn, frame));
		    	frame.revalidate();
		    }
		});
		add(btnChangePrices, "cell 0 4 1 2");
		
		JButton btnAddItem = new JButton("Search");
		btnAddItem.setBackground(new Color(255, 140, 0));
		btnAddItem.setBorder(new LineBorder(Color.BLACK));
		btnAddItem.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        // Put code to add item to menu here
		    }
		});
		add(btnAddItem, "cell 1 6");

	}
	
	private int getNumProducts() {
		String id = "";
		int numProducts = 0;
		try{
			//create a statement object
			Statement stmt = this.conn.createStatement();
			//create an SQL statement
			String sqlStatement = "SELECT id FROM products ORDER BY id DESC LIMIT 1";
			//send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);
			while (result.next()) {
				numProducts = Integer.parseInt(result.getString("id"));
			}
		} catch (Exception e){
			e.printStackTrace();
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		return numProducts;
	}
	
	//Populates the table with popular menu items
	private String[][] getProducts() {
		String name = "";
		int numProducts = getNumProducts();
		String[][] names = new String[numProducts][3];
		try{
			//create a statement object
			Statement stmt = this.conn.createStatement();
			//create an SQL statement
			String sqlStatement = "SELECT name, price FROM products ORDER BY name";
			//send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);

			//OUTPUT
			int rowIndex = 0;
			while (result.next() && rowIndex < numProducts) {
				names[rowIndex][0] = result.getString("name");
				names[rowIndex][1] = String.format("$%.2f", Double.parseDouble(result.getString("price")));
				names[rowIndex][2] = "Edit";
				++rowIndex;
			}
		} catch (Exception e){
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		return names;
	}

}
