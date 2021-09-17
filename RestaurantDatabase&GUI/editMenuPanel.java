import net.miginfocom.swing.MigLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;

public class editMenuPanel extends JPanel {
	private Connection conn = null;
	private JTable table;
	private DefaultTableModel model;

	/**
	 * Create the panel.
	 */
	public editMenuPanel(Connection conn, JFrame frame) {
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
		model = new DefaultTableModel(getProducts(), colNames);
		table = new JTable( model );
		Action info = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	JTable tempTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		    	JFrame productInfo = new productInfoFrame((String)(tempTable.getModel().getValueAt(modelRow,0)), conn);
		    	productInfo.setVisible(true);
		    }
		}; 
		ButtonColumn buttonColumn = new ButtonColumn(table, info, 2);
		buttonColumn.setMnemonic(KeyEvent.VK_D);
		JScrollPane scroll = new JScrollPane(table);
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
		
		JButton btnChangePrices = new JButton("Edit Menu");
		btnChangePrices.setBackground(new Color(255, 140, 0));
		btnChangePrices.setBorder(new LineBorder(Color.BLACK));
		btnChangePrices.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	frame.setContentPane(new editMenuPanel(conn, frame));
		    	frame.revalidate();
		    }
		});
		add(btnChangePrices, "cell 0 4 1 2");
		
		JTextField searchBar = new JTextField();
		add(searchBar, "cell 1 6");
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBackground(new Color(255, 140, 0));
		btnSearch.setBorder(new LineBorder(Color.BLACK));
		btnSearch.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	model.setRowCount(0);
		        String productName = searchBar.getText();
		        if (productName.trim().isEmpty()) model = new DefaultTableModel(getProducts(), colNames);
		        else model = new DefaultTableModel(searchFor(productName), colNames);
				table.setModel(model);
				Action info = new AbstractAction()
				{
				    public void actionPerformed(ActionEvent e)
				    {
				    	JTable tempTable = (JTable)e.getSource();
				        int modelRow = Integer.valueOf( e.getActionCommand() );
				    	JFrame productInfo = new productInfoFrame((String)(tempTable.getModel().getValueAt(modelRow,0)), conn);
				    	productInfo.setVisible(true);
				    }
				}; 
				ButtonColumn buttonColumn = new ButtonColumn(table, info, 2);
				buttonColumn.setMnemonic(KeyEvent.VK_D);
		    }
		});
		add(btnSearch, "cell 1 6");
		
		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.setBackground(new Color(255, 140, 0));
		btnAddItem.setBorder(new LineBorder(Color.BLACK));
		btnAddItem.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JFrame addProduct = new addProductFrame(conn);
		        addProduct.setVisible(true);
		    }
		});
		add(btnAddItem, "cell 1 7");

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
				names[rowIndex][2] = "Info";
				++rowIndex;
			}
		} catch (Exception e){
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		return names;
	}
	
	//Finds all products that fit query
	private String[][] searchFor(String query) {
		String name = "";
		int numProducts = getNumProducts();
		List<String[]> names = new ArrayList<String[]>();
		try{
			//create a statement object
			Statement stmt = this.conn.createStatement();
			//create an SQL statement
			String sqlStatement = "SELECT name, price FROM products WHERE name LIKE '%" + query + "%' ORDER BY name";
			//send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);

			//OUTPUT
			int rowIndex = 0;
			while (result.next() && rowIndex < numProducts) {
				String[] temp = {result.getString("name"), String.format("$%.2f", Double.parseDouble(result.getString("price"))), "Info"};
				names.add(temp);
				++rowIndex;
			}
		} catch (Exception e){
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		String[][] foundProducts = new String[names.size()][3];
		for (int i = 0; i < names.size(); ++i) foundProducts[i] = names.get(i);
		return foundProducts;
	}

}
