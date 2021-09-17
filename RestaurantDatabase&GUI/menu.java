import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.*;
import java.util.List;

import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;




public class menu extends JFrame {

	private JPanel contentPane;
	private JTextField itemSearch;
	private JTable table;
	private static jdbcpostgreSQL database;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menu frame = new menu();
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
	public menu() {
		
		//connect to database
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				database.closeConnection();
			}
		});
		
		try {
			database = new jdbcpostgreSQL();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(this, "Error:" + e , "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1040, 719);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(Color.WHITE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		panel.setOpaque(true);
		contentPane.add(panel, BorderLayout.WEST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {100};
		gbl_panel.rowHeights = new int[] {30, 30, 30, 30, 70, 30, 30, 70, 30, 30, 0, 30, 70, 30, 30, 0, 30, 70, 30};
		gbl_panel.columnWeights = new double[]{0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		
		/*
		 * ----------------------NAVIGATION BAR---------------------------------
		 */
		JButton btnNewButton = new JButton("Home");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 3;
		panel.add(btnNewButton, gbc_btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new home().setVisible(true);
				menu.this.dispose();
			}
		});
		
		JButton btnNewButton_1 = new JButton("Menu");
		btnNewButton_1.setBackground(Color.DARK_GRAY);
		btnNewButton_1.setForeground(Color.WHITE);
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 30));
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 6;
		panel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Order");
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 0;
		gbc_btnNewButton_2.gridy = 10;
		panel.add(btnNewButton_2, gbc_btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainOrder order = new mainOrder();
				order.main(null);
				menu.this.dispose();
			}
		});
		
		JButton btnNewButton_3 = new JButton("Payment");
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_3.gridx = 0;
		gbc_btnNewButton_3.gridy = 14;
		panel.add(btnNewButton_3, gbc_btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paymentMenu paymenu = new paymentMenu();
				paymenu.main(null);
				menu.this.dispose();
			}
		});
		
		
		
		
		
		/*
		 * ------------------------------SEARCH BAR------------------------------
		 */
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Enter Item");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblNewLabel);
		
		itemSearch = new JTextField();
		itemSearch.setFont(new Font("Tahoma", Font.PLAIN, 30));
		panel_1.add(itemSearch);
		itemSearch.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("Search");
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 30));
		panel_1.add(btnNewButton_4);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		contentPane.add(table, BorderLayout.CENTER);
		getContentPane().add(new JScrollPane(table));
		
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get item name from text field
				try {
					String item = itemSearch.getText();
					List<Product> products = null;
					if(item != null && item.trim().length() > 0) {
						products = database.searchItems(item);
					
					}
					else {
						 products = database.getAllItems();
					}
					
					boolean check;
					
					//check if all products are available
					for(int i = 0; i < products.size(); i++) {
						check = database.isAvailable(products.get(i));
						if(!check) {
							products.remove(i);
							//de-increment i because array size changes when 
							//product is removed
							i--;
						}
					}
					
					MenuTable model = new MenuTable(products);
					table.setModel(model);
					
				}
				catch(Exception exc) {
					exc.printStackTrace();
				}
			}
			
			
		});
		
	}

}
