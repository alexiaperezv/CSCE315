import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.lang.Math;

public class managerHomePanel extends JPanel {
	private JTable table;
	private Connection conn = null;

	/**
	 * Create the panel.
	 */

	public managerHomePanel(Connection conn, JFrame frame) {
		this.conn = conn;

		setLayout(new MigLayout("", "[fill][grow,fill]",
				"[20:20:20,fill][20:20:20,fill][20:20:20,fill][20:20:20,fill][20:20:20,fill][20:20:20,fill][fill]"));

		JLabel header = new JLabel("Popular Items");
		header.setHorizontalAlignment(SwingConstants.CENTER);
		add(header, "cell 1 0,alignx center");

		JButton btnHome = new JButton("Home");
		btnHome.setBackground(new Color(255, 140, 0));
		btnHome.setBorder(new LineBorder(Color.BLACK));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new managerHomePanel(conn, frame));
				frame.revalidate();
			}
		});
		add(btnHome, "cell 0 0 1 2");

		String[] colNames = { "1", "2", "3" };
		table = new JTable(getPopularItems(), colNames);
		table.setTableHeader(null);
		table.setRowHeight(50);
		JScrollPane scroll = new JScrollPane(table);
		add(scroll, "cell 1 1 1 5");

		JButton btnOrderHistory = new JButton("Order History");
		btnOrderHistory.setBackground(new Color(255, 140, 0));
		btnOrderHistory.setBorder(new LineBorder(Color.BLACK));
		btnOrderHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new orderHistoryPanel(conn, frame));
				frame.revalidate();
			}
		});
		add(btnOrderHistory, "cell 0 2 1 2");

		JButton btnChangePrices = new JButton("Edit Menu");
		btnChangePrices.setBackground(new Color(255, 140, 0));
		btnChangePrices.setBorder(new LineBorder(Color.BLACK));
		btnChangePrices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new editMenuPanel(conn, frame));
				frame.revalidate();
			}
		});
		add(btnChangePrices, "cell 0 4 1 2");

		JButton btnAddItem = new JButton("Update Trending Items");
		btnAddItem.setBackground(new Color(255, 140, 0));
		btnAddItem.setBorder(new LineBorder(Color.BLACK));
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// When the "Update Trending Items" button is clicked, the GUI will indicate
				// trending products
				int[] array = getTrendingItems();
				for (int i = 0; i < 4; i++) {
					// row = floor(id/3), column = id%3 - 1
					int row = (int) Math.floor(array[i] / 3);
					int column = array[i] % 3;
					if (i == 0) {
						String text = table.getModel().getValueAt(row, column - 1) + " Most Popular Item";
						table.getModel().setValueAt(text, row, column - 1);
					} else if (i == 1) {
						String text = table.getModel().getValueAt(row - 1, column + 2) + " 2nd Most Popular Item";
						table.getModel().setValueAt(text, row - 1, column + 2);
					} else if (i == 2) {
						String text = table.getModel().getValueAt(row, column - 1) + " Least Popular Item";
						table.getModel().setValueAt(text, row, column - 1);
					} else if (i == 3) {
						String text = table.getModel().getValueAt(row, column - 1) + " 2nd Least Popular Item";
						table.getModel().setValueAt(text, row, column - 1);
					}
				}
				// change cell colors ??
			}
		});
		add(btnAddItem, "cell 1 6");

	}

	private int getNumProducts() {
		String id = "";
		int numProducts = 0;
		try {
			// create a statement object
			Statement stmt = this.conn.createStatement();
			// create an SQL statement
			String sqlStatement = "SELECT id FROM products ORDER BY id DESC LIMIT 1";
			// send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);
			while (result.next()) {
				numProducts = Integer.parseInt(result.getString("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName() + ": " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		return numProducts;
	}

	// Populates the table with popular menu items
	private String[][] getPopularItems() {
		String name = "", price = "";
		int numProducts = getNumProducts();
		int rowLimit = ((numProducts / 3) + (numProducts % 3));
		String[][] names = new String[rowLimit][3];
		try {
			// create a statement object
			Statement stmt = this.conn.createStatement();
			// create an SQL statement
			String sqlStatement = "SELECT name, price FROM products";
			// send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);

			// OUTPUT
			int rowIndex = 0, colIndex = 0;
			while (result.next() && rowIndex < rowLimit) {
				names[rowIndex][colIndex] = result.getString("name") + "  "
						+ String.format("$%.2f", Double.parseDouble(result.getString("price")));
				if (colIndex == 2)
					++rowIndex;
				colIndex = (colIndex + 1) % 3;
			}
		} catch (Exception e) {
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName() + ": " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		return names;
	}

	// Finds items trending up and down
	private int[] getTrendingItems() {
		int[] product_ids = new int[4];
		// Find most popular item
		try {
			// create a statement object
			Statement stmt = this.conn.createStatement();
			// create an SQL statement
			String sqlStatement = "SELECT PRODUCT_ID, TIMES_ORDERED FROM ORDERED_QUANTITIES WHERE TIMES_ORDERED = (SELECT MAX(TIMES_ORDERED)FROM ORDERED_QUANTITIES);";
			// send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);
			// OUTPUT
			int index = 0;
			while (result.next()) {
				product_ids[index] = result.getInt("product_id");
			}
		} catch (Exception e) {
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName() + ": " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		// Find second most popular item
		try {
			// create a statement object
			Statement stmt = this.conn.createStatement();
			// create an SQL statement
			String sqlStatement = "SELECT PRODUCT_ID,TIMES_ORDERED FROM ORDERED_QUANTITIES WHERE TIMES_ORDERED = (SELECT MAX (TIMES_ORDERED) FROM ORDERED_QUANTITIES WHERE TIMES_ORDERED NOT IN (SELECT MAX (TIMES_ORDERED) FROM ORDERED_QUANTITIES));";
			// send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);
			// OUTPUT
			int index = 1;
			while (result.next()) {
				product_ids[index] = result.getInt("product_id");
			}
		} catch (Exception e) {
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName() + ": " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		try {
			// create a statement object
			Statement stmt = this.conn.createStatement();
			// create an SQL statement
			String sqlStatement = "SELECT PRODUCT_ID, TIMES_ORDERED FROM ORDERED_QUANTITIES WHERE TIMES_ORDERED = (SELECT MIN(TIMES_ORDERED)FROM ORDERED_QUANTITIES);";
			// send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);
			// OUTPUT
			int index = 2;
			while (result.next()) {
				product_ids[index] = result.getInt("product_id");
			}
		} catch (Exception e) {
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName() + ": " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		try {
			// create a statement object
			Statement stmt = this.conn.createStatement();
			// create an SQL statement
			String sqlStatement = "SELECT PRODUCT_ID,TIMES_ORDERED FROM ORDERED_QUANTITIES WHERE TIMES_ORDERED = (SELECT MIN (TIMES_ORDERED) FROM ORDERED_QUANTITIES WHERE TIMES_ORDERED NOT IN (SELECT MIN (TIMES_ORDERED) FROM ORDERED_QUANTITIES));";
			// send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);
			// OUTPUT
			int index = 3;
			while (result.next()) {
				product_ids[index] = result.getInt("product_id");
			}
		} catch (Exception e) {
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName() + ": " + e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		return product_ids;
	}
}