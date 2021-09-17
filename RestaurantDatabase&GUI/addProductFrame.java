import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import java.awt.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class addProductFrame extends JFrame {
	private Connection conn;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Connection conn) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					addProductFrame frame = new addProductFrame(conn);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public addProductFrame(Connection conn) {
		this.conn = conn;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[30%][70%,grow]", "[][][][][]"));
		
		JLabel lblHeader = new JLabel("Add New Product");
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblHeader, "cell 0 0 2 1,alignx center,growy");
		
		JLabel lblName = new JLabel("Name");
		contentPane.add(lblName, "cell 0 1,alignx trailing");
		
		JFormattedTextField ftfName = new JFormattedTextField();
		contentPane.add(ftfName, "cell 1 1,growx");
		
		JLabel lblPrice = new JLabel("Price");
		contentPane.add(lblPrice, "cell 0 2,alignx trailing");
		
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		JFormattedTextField ftfPrice = new JFormattedTextField(nf);
		contentPane.add(ftfPrice, "cell 1 2,growx");
        try {
            MaskFormatter priceMask = new MaskFormatter("$##.##");
            priceMask.setPlaceholderCharacter('0');
            priceMask.install(ftfPrice);
        } catch (ParseException ex) {
        	JOptionPane.showMessageDialog(null, "Unable to apply input formatting.");
        }
		
		JLabel lblQuantity = new JLabel("Quantity");
		contentPane.add(lblQuantity, "cell 0 3,alignx trailing");
		
		JFormattedTextField ftfQuantity = new JFormattedTextField();
		contentPane.add(ftfQuantity, "cell 1 3,growx");
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBackground(new Color(255, 140, 0));
		btnAdd.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	addProduct(ftfName.getText(), Double.parseDouble(ftfPrice.getText().substring(1)), Integer.parseInt(ftfQuantity.getText()));
		    }
		});
		contentPane.add(btnAdd, "cell 0 4 2 1,grow");
	}
	
	void addProduct(String name, Double price, int quantity) {
		try{
			Statement stmt = this.conn.createStatement();
			String sqlStatement = "INSERT INTO products (name, price) VALUES ('" + name + "'," + price + ")";
			stmt.executeUpdate(sqlStatement);
			
			stmt = this.conn.createStatement();
			sqlStatement = "SELECT id FROM products WHERE name='" + name + "' ORDER BY id DESC LIMIT 1";
			ResultSet result = stmt.executeQuery(sqlStatement);
			int productID = 0;
			while (result.next()) {
				productID = result.getInt("id");
			}
			
			stmt = this.conn.createStatement();
			sqlStatement = "INSERT INTO inventory (product_id, product_name, quantity) VALUES (" + productID + ",'" + name + "'," + quantity + ")";
			stmt.executeUpdate(sqlStatement);
			JOptionPane.showMessageDialog(null, "Product successfully added to menu.");
		} catch (Exception e){
			String errorMessage = "Error modifying Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
	}

}
