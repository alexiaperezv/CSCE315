import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import javax.swing.table.DefaultTableModel;

public class orderHistoryPanel extends JPanel {
	private JTable table;
	private DefaultTableModel model;
	private Connection conn = null;

	/**
	 * Create the panel.
	 */
	public orderHistoryPanel(Connection conn, JFrame frame) {
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
		
		String[] colNames = {"ID", "Customer", "Order", "Total", "Date"};
		model = new DefaultTableModel(initOrderHistory(), colNames);
		table = new JTable( model );
		Action order = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	JTable tempTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        JOptionPane.showMessageDialog(null, getOrderedProducts((String)(tempTable.getModel().getValueAt(modelRow,0))));
		    }
		}; 
		ButtonColumn buttonColumn = new ButtonColumn(table, order, 2);
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
		
		JButton btnAddItem = new JButton("Search");
		btnAddItem.setBackground(new Color(255, 140, 0));
		btnAddItem.setBorder(new LineBorder(Color.BLACK));
		btnAddItem.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	model.setRowCount(0);
		        String customerName = searchBar.getText();
		        if (customerName.trim().isEmpty()) model = new DefaultTableModel(initOrderHistory(), colNames);
		        else model = new DefaultTableModel(searchFor(customerName), colNames);
		        table.setModel(model);
				Action order = new AbstractAction()
				{
				    public void actionPerformed(ActionEvent a)
				    {
				    	JTable tempTable = (JTable)a.getSource();
				        int modelRow = Integer.valueOf( a.getActionCommand() );
				        JOptionPane.showMessageDialog(null, getOrderedProducts((String)(tempTable.getModel().getValueAt(modelRow,0))));
				    }
				}; 
				ButtonColumn buttonColumn = new ButtonColumn(table, order, 2);
				buttonColumn.setMnemonic(KeyEvent.VK_D);
		    }
		});
		add(btnAddItem, "cell 1 6");

	}
	
	//Populates the order history table with items
	private String[][] initOrderHistory() {
		String[][] names = new String[100][5];
		try{
			//create a statement object
			Statement stmt = this.conn.createStatement();
			//create an SQL statement
			String sqlStatement = "SELECT orders.id, customers.name, orders.total, orders.date\r\n"
					+ "FROM orders\r\n"
					+ "INNER JOIN customers ON customers.id=orders.customer_id\r\n"
					+ "INNER JOIN ordered_products ON orders.id=ordered_products.order_id\r\n"
					+ "INNER JOIN products ON products.id=ordered_products.product_id\r\n"
					+ "ORDER BY orders.date DESC LIMIT 1000";
			//send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);

			//OUTPUT
			int rowIndex = 0, orderID = -1;
			while (result.next() && rowIndex < 100) {
				names[rowIndex][0] = result.getString("id");
				if (Integer.parseInt(names[rowIndex][0]) == orderID) continue;
				orderID = Integer.parseInt(names[rowIndex][0]);
				names[rowIndex][1] = result.getString("name");
				names[rowIndex][2] = "View";
				names[rowIndex][3] = String.format("$%.2f", result.getDouble("total"));
				names[rowIndex][4] = result.getString("date");
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
		List<String[]> names = new ArrayList<String[]>();
		try{
			//create a statement object
			Statement stmt = this.conn.createStatement();
			//create an SQL statement
			String sqlStatement = "SELECT orders.id, customers.name, orders.total, orders.date\r\n"
					+ "FROM orders\r\n"
					+ "INNER JOIN customers ON customers.id=orders.customer_id\r\n"
					+ "INNER JOIN ordered_products ON orders.id=ordered_products.order_id\r\n"
					+ "INNER JOIN products ON products.id=ordered_products.product_id\r\n"
					+ "WHERE customers.name LIKE '%" + query + "%' ORDER BY customers.name";
			//send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);

			//OUTPUT
			int rowIndex = 0, orderID = -1;
			while (result.next()) {
				String[] temp = {result.getString("id"), result.getString("name"), "View", String.format("$%.2f", result.getDouble("total")), result.getString("date")};
				if (orderID == Integer.parseInt(temp[0])) continue;
				orderID = Integer.parseInt(temp[0]);
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
	
	private String getOrderedProducts(String orderID) {
		List<String[]> itemizedOrder = new ArrayList<String[]>();
		String orderSummary = "";
		try{
			Statement stmt = this.conn.createStatement();
			String sqlStatement = "SELECT products.name, products.id, products.price\r\n"
					+ "FROM orders\r\n"
					+ "INNER JOIN ordered_products ON orders.id=ordered_products.order_id\r\n"
					+ "INNER JOIN products ON products.id=ordered_products.product_id\r\n"
					+ "WHERE orders.id=" + orderID + "ORDER BY products.id";
			//send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);

			//OUTPUT
			int rowIndex = -1, productID = -1;
			while (result.next()) {
				if (productID == result.getInt(2)) {
					itemizedOrder.get(rowIndex)[0] = String.valueOf(Integer.parseInt(itemizedOrder.get(rowIndex)[0]) + 1);
					double newTotal = Double.parseDouble(itemizedOrder.get(rowIndex)[0]) + result.getDouble(3);
					itemizedOrder.get(rowIndex)[2] = String.format("%.2f", newTotal);
					continue;
				}
				productID = result.getInt(2);
				String[] temp = {"1",result.getString(1),String.format("%.2f", result.getDouble(3))};
				itemizedOrder.add(temp);
				++rowIndex;
			}
		} catch (Exception e){
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		for (int i = 0; i < itemizedOrder.size(); ++i) {
			orderSummary += "(" + itemizedOrder.get(i)[0] + ") " + itemizedOrder.get(i)[1] + ".....$" + itemizedOrder.get(i)[2] + "\n";
		}
		return orderSummary;
	}

}
