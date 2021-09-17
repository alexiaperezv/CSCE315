import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JFormattedTextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class productInfoFrame extends JFrame {
	private Connection conn = null;
	private JPanel contentPane;
	public String name;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Connection conn) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					productInfoFrame frame = new productInfoFrame(args[0], conn);
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
	public productInfoFrame(String name, Connection conn) {
		this.name = name;
		this.conn = conn;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[46%][grow]", "[15%][10%][10%][10%][15%][10%][10%][10%]"));
		
		JLabel lblProductName = new JLabel(name);
		lblProductName.setBackground(new Color(255, 140, 0));
		lblProductName.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblProductName, "cell 0 0 2 1,grow");
		
		JLabel lblCurrentPrice = new JLabel("Price");
		contentPane.add(lblCurrentPrice, "cell 0 1");
		
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		JFormattedTextField ftfPrice = new JFormattedTextField(nf);
		contentPane.add(ftfPrice, "cell 1 1,grow");
        try {
            MaskFormatter priceMask = new MaskFormatter("$##.##");
            DecimalFormat df = new DecimalFormat("#00.00");
            priceMask.setPlaceholder("$" + df.format(getPrice()));
            priceMask.install(ftfPrice);
        } catch (ParseException ex) {
        	JOptionPane.showMessageDialog(null, "Unable to load product info.");
        }
		
		JLabel lblRemainingStock = new JLabel("Remaining Stock");
		contentPane.add(lblRemainingStock, "cell 0 2");
		
		JFormattedTextField ftfStock = new JFormattedTextField();
		contentPane.add(ftfStock, "cell 1 2,grow");
        try {
            MaskFormatter stockMask = new MaskFormatter("#####");
            stockMask.setPlaceholder(String.format("%05d", getQuantity()));
            stockMask.install(ftfStock);
        } catch (ParseException ex) {
        	JOptionPane.showMessageDialog(null, "Unable to load product info.");
        }
		
		JButton btnNewButton = new JButton("Update Product");
		btnNewButton.setBackground(new Color(255, 140, 0));
		btnNewButton.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	updateProduct(Double.parseDouble(ftfPrice.getText().substring(1)), Integer.parseInt(ftfStock.getText()));
		    }
		});
		contentPane.add(btnNewButton, "cell 0 3 2 1,grow");
		
		JLabel lblPriceAnalysis = new JLabel("Pricing Analysis");
		lblPriceAnalysis.setBackground(new Color(255, 140, 0));
		lblPriceAnalysis.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblPriceAnalysis, "cell 0 4 2 1,grow");
		
		JLabel lblNewPrice = new JLabel("Test Price");
		contentPane.add(lblNewPrice, "cell 0 5");
		
		JFormattedTextField ftfTestPrice = new JFormattedTextField(nf);
		contentPane.add(ftfTestPrice, "cell 1 5,grow");
        try {
            MaskFormatter priceMask = new MaskFormatter("$##.##");
            priceMask.setPlaceholderCharacter('0');
            priceMask.install(ftfTestPrice);
        } catch (ParseException ex) {
        	JOptionPane.showMessageDialog(null, "Unable to load product info.");
        }
		
		JLabel lblDate = new JLabel("Reference Date");
		contentPane.add(lblDate, "cell 0 6");
		
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		JFormattedTextField ftfDate = new JFormattedTextField(df);
		contentPane.add(ftfDate, "cell 1 6,grow");
        try {
            MaskFormatter dateMask = new MaskFormatter("####/##/##");
            dateMask.setPlaceholder("2015/03/27");
            dateMask.install(ftfDate);
        } catch (ParseException ex) {
        	JOptionPane.showMessageDialog(null, "Unable to load product info.");
        }
		
		JButton btnSetPrice = new JButton("Calculate Price Change Impact");
		btnSetPrice.setBackground(new Color(255, 140, 0));
		btnSetPrice.setBorder(new LineBorder(Color.BLACK));
		btnSetPrice.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	priceChangeEffect(ftfDate.getText(), Double.parseDouble(ftfTestPrice.getText().substring(1)));
		    }
		});
		contentPane.add(btnSetPrice, "cell 0 7 2 1,grow");
	}
	
	double getPrice() {
		Double price = 0.0;
		try{
			Statement stmt = this.conn.createStatement();
			String sqlStatement = "SELECT price FROM products WHERE name='" + this.name + "'";
			ResultSet result = stmt.executeQuery(sqlStatement);
			//OUTPUT
			while (result.next()) {
				price = result.getDouble("price");
			}
		} catch (Exception e){
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		return price;
	}
	
	int getQuantity() {
		int quantity = 0;
		try{
			Statement stmt = this.conn.createStatement();
			String sqlStatement = "SELECT quantity FROM inventory WHERE product_name='" + this.name + "'";
			ResultSet result = stmt.executeQuery(sqlStatement);
			//OUTPUT
			while (result.next()) {
				quantity = result.getInt("quantity");
			}
		} catch (Exception e){
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		return quantity;
	}
	
	void updateProduct(Double newPrice, int newQuantity) {
		try{
			Statement stmt = this.conn.createStatement();
			String sqlStatement = "UPDATE products SET price=" + newPrice + " WHERE name='" + this.name + "'";
			stmt.executeUpdate(sqlStatement);
			
			stmt = this.conn.createStatement();
			sqlStatement = "UPDATE inventory SET quantity=" + newQuantity + " WHERE product_name='" + this.name + "'";
			stmt.executeUpdate(sqlStatement);
		} catch (Exception e){
			String errorMessage = "Error modifying Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
	}
	
	void priceChangeEffect(String date, Double newPrice) {
		List<String[]> names = new ArrayList<String[]>();
		String date2 = "";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Double change = 0.0;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(df.parse(date));   
			calendar.add(Calendar.DATE, 7);
			date2 = df.format(calendar.getTime());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unable to parse date");
		}
		try{
			Statement stmt = this.conn.createStatement();
			String sqlStatement = "SELECT products.name, products.price, orders.date\r\n"
					+ "FROM orders\r\n"
					+ "INNER JOIN ordered_products ON orders.id=ordered_products.order_id\r\n"
					+ "INNER JOIN products ON products.id=ordered_products.product_id\r\n"
					+ "WHERE products.name='" + this.name + "' AND orders.date BETWEEN '" + date + "' AND '" + date2 +"'";
			ResultSet result = stmt.executeQuery(sqlStatement);
			//OUTPUT
			while (result.next()) {
				change += newPrice - result.getDouble(2);
			}
		} catch (Exception e){
			String errorMessage = "Error accessing Database:\n" + e.getClass().getName()+": "+e.getMessage();
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		String[][] foundProducts = new String[names.size()][3];
		for (int i = 0; i < names.size(); ++i) foundProducts[i] = names.get(i);

		String revenueChange;
		if (change >= 0) revenueChange = String.format("$%.2f", change);
		else revenueChange = String.format("-$%.2f", -1*change);
		JOptionPane.showMessageDialog(null, "The week span of " + date + "-" + date2 + "\n would have reported a change of " + revenueChange + " in revenue.");
	}

}
