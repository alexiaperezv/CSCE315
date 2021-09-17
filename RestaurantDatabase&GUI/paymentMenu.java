import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class paymentMenu extends mainOrder{

	private JFrame frame;
	private static jdbcpostgreSQL database;
	private JTable findPayment;
	private JTextField nameText;
	private JTextField CCNText;
	private JTextField dateText;
	private JTextField CVVText;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					paymentMenu window = new paymentMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public paymentMenu() {
		initialize();
	}
	
	public void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 977, 639);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		frame.addWindowListener(new WindowAdapter() {
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
			JOptionPane.showMessageDialog(frame, "Error:" + e , "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		
		
		//-----------PANELS--------------------//
		JPanel mainMenu = new JPanel();
		frame.getContentPane().add(mainMenu, "name_126924497813900");
		mainMenu.setLayout(new BorderLayout(0, 0));
		mainMenu.setVisible(true);
		
		JPanel existing = new JPanel();
		existing.setBackground(Color.ORANGE);
		frame.getContentPane().add(existing, "name_140394728324300");
		existing.setLayout(new BorderLayout(0, 0));
		existing.setVisible(false);
		
		
		
		//-------------ADD NEW PAYMENT-----------------//
		
		JPanel addNew = new JPanel();
		addNew.setFont(new Font("Tahoma", Font.PLAIN, 30));
		addNew.setAutoscrolls(true);
		addNew.setBackground(Color.ORANGE);
		frame.getContentPane().add(addNew, "name_140404488550300");
		addNew.setLayout(null);
		addNew.setVisible(false);
		
		JLabel newName = new JLabel("Enter Name (Last, First):");
		newName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		newName.setBounds(306, 42, 355, 40);
		addNew.add(newName);
		
		JLabel enterCCN = new JLabel("Enter CCN:");
		enterCCN.setFont(new Font("Tahoma", Font.PLAIN, 30));
		enterCCN.setBounds(306, 162, 336, 40);
		addNew.add(enterCCN);
		
		JLabel enterCVV = new JLabel("Enter CVV:");
		enterCVV.setFont(new Font("Tahoma", Font.PLAIN, 31));
		enterCVV.setBounds(306, 280, 355, 40);
		addNew.add(enterCVV);
		
		JLabel enterDate = new JLabel("Enter Date:");
		enterDate.setFont(new Font("Tahoma", Font.PLAIN, 30));
		enterDate.setBounds(306, 392, 355, 40);
		addNew.add(enterDate);
		
		nameText = new JTextField();
		nameText.setBounds(316, 92, 326, 40);
		addNew.add(nameText);
		nameText.setColumns(50);
		
		
		CCNText = new JTextField();
		CCNText.setBounds(316, 212, 326, 40);
		addNew.add(CCNText);
		CCNText.setColumns(50);
		
		
		CVVText = new JTextField();
		CVVText.setBounds(316, 330, 326, 40);
		addNew.add(CVVText);
		CVVText.setColumns(50);
		
		
		dateText = new JTextField();
		dateText.setBounds(316, 442, 326, 40);
		addNew.add(dateText);
		dateText.setColumns(50);
		
		
		JButton confirmPayment = new JButton("Confirm Payment");
		confirmPayment.setBackground(Color.WHITE);
		confirmPayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				
				try {
					String date = dateText.getText();
					String cvv = CVVText.getText();
					String ccn = CCNText.getText();
					String name = nameText.getText();
					
					
					if(name != null && name.trim().length() > 0 &&
					   date != null && date.trim().length() > 0 &&
					   ccn != null && ccn.trim().length() > 0 &&
					   cvv != null && cvv.trim().length() > 0) {
						//add payment to database
						PaymentInfo newCard = new PaymentInfo(date, cvv, name, 0, 0, ccn);
						database.addCustomer(name);
						database.addPayment(newCard);
						//process Order here
						
						int cID = database.getCustomerID(name);
						
						LocalDate currDate = java.time.LocalDate.now();
						String strDate = currDate.toString();
						database.processOrder(strDate, mainOrder.totalPrice, cID);
						
						JOptionPane.showMessageDialog(frame, "Order Processed. Thank You!");
						new home().setVisible(true);
						frame.dispose();
						database.closeConnection();
						
					}
					else {
						JOptionPane.showMessageDialog(frame, "Information Still Needed.");
					}
				}
				catch(Exception e2) {
					e2.printStackTrace();
				}
				
				
				
			}
		});
		confirmPayment.setFont(new Font("Tahoma", Font.PLAIN, 40));
		confirmPayment.setBounds(0, 537, 963, 55);
		addNew.add(confirmPayment);
		
		JButton goBack2 = new JButton("Go Back");
		goBack2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(true);
				addNew.setVisible(false);
			}
		});
		goBack2.setBackground(Color.WHITE);
		goBack2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		goBack2.setBounds(10, 10, 166, 55);
		addNew.add(goBack2);
		
		

		
		
		//---------------------------------NAVIGATION BAR------------------------------//
		
		JPanel NavigationBar = new JPanel();
		mainMenu.add(NavigationBar, BorderLayout.WEST);
		NavigationBar.setBackground(Color.ORANGE);
		NavigationBar.setOpaque(true);
		GridBagLayout gbl_NavigationBar = new GridBagLayout();
		gbl_NavigationBar.columnWidths = new int[] {100};
		gbl_NavigationBar.rowHeights = new int[] {30, 30, 30, 30, 70, 30, 30, 70, 30, 30, 70, 30, 30, 70, 30, 30, 30};
		gbl_NavigationBar.columnWeights = new double[]{0.0};
		gbl_NavigationBar.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		NavigationBar.setLayout(gbl_NavigationBar);
		
		JButton home = new JButton("Home");
		home.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_home = new GridBagConstraints();
		gbc_home.insets = new Insets(0, 0, 5, 0);
		gbc_home.gridx = 0;
		gbc_home.gridy = 4;
		NavigationBar.add(home, gbc_home);
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new home().setVisible(true);
				frame.dispose();
			}
		});
		
		JButton menu = new JButton("Menu");
		menu.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_menu = new GridBagConstraints();
		gbc_menu.insets = new Insets(0, 0, 5, 0);
		gbc_menu.gridx = 0;
		gbc_menu.gridy = 7;
		NavigationBar.add(menu, gbc_menu);
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new menu().setVisible(true);
				frame.dispose();
			}
		});
		
		JButton order = new JButton("Order");
		order.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_order = new GridBagConstraints();
		gbc_order.insets = new Insets(0, 0, 5, 0);
		gbc_order.gridx = 0;
		gbc_order.gridy = 10;
		NavigationBar.add(order, gbc_order);
		order.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainOrder order = new mainOrder();
				order.main(null);
				frame.dispose();
			}
		});
		
		JButton payment = new JButton("Payment");
		payment.setForeground(Color.WHITE);
		payment.setBackground(Color.DARK_GRAY);
		payment.setFont(new Font("Tahoma", Font.BOLD, 30));
		GridBagConstraints gbc_payment = new GridBagConstraints();
		gbc_payment.insets = new Insets(0, 0, 5, 0);
		gbc_payment.gridx = 0;
		gbc_payment.gridy = 13;
		NavigationBar.add(payment, gbc_payment);

		
		
		
		//--------------------------------ADD EXISTING CARD------------------------//
		
		
		JPanel choice = new JPanel();
		choice.setBackground(Color.WHITE);
		mainMenu.add(choice, BorderLayout.CENTER);
		choice.setLayout(null);
		
		JButton existingCard = new JButton("Existing Card");
		existingCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				existing.setVisible(true);
				mainMenu.setVisible(false);
				
			}
		});
		existingCard.setFont(new Font("Tahoma", Font.PLAIN, 30));
		existingCard.setBounds(63, 260, 317, 75);
		choice.add(existingCard);
		
		JButton addNewCard = new JButton("Add New Card");
		addNewCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNew.setVisible(true);
				mainMenu.setVisible(false);
				
			}
		});
		addNewCard.setFont(new Font("Tahoma", Font.PLAIN, 30));
		addNewCard.setBounds(461, 260, 317, 75);
		choice.add(addNewCard);
		
		JLabel question = new JLabel("Use Existing Card or Add New?");
		question.setHorizontalAlignment(SwingConstants.CENTER);
		question.setFont(new Font("Tahoma", Font.PLAIN, 30));
		question.setBounds(132, 29, 545, 158);
		choice.add(question);
		
		
		//-------------------SEARCH BAR-------------------------
		
		JPanel searchBar = new JPanel();
		searchBar.setBackground(Color.ORANGE);
		existing.add(searchBar, BorderLayout.NORTH);
		
		JButton goBack = new JButton("Go Back");
		goBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(true);
				existing.setVisible(false);
			}
		});
		goBack.setBackground(Color.WHITE);
		goBack.setFont(new Font("Tahoma", Font.PLAIN, 30));
		searchBar.add(goBack);
		
		JLabel enterName = new JLabel("Enter Name");
		enterName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		enterName.setHorizontalAlignment(SwingConstants.LEFT);
		searchBar.add(enterName);
		
		JTextField paymentText = new JTextField();
		paymentText.setFont(new Font("Tahoma", Font.PLAIN, 30));
		searchBar.add(paymentText);
		paymentText.setColumns(10);
		
		findPayment = new JTable();
		existing.add(findPayment, BorderLayout.CENTER);
		existing.add(new JScrollPane(findPayment));
		
		JButton search = new JButton("Search");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get customer name from text field
				try {
					
					String name = paymentText.getText();
					List<PaymentInfo> cards = null;
					if(name != null && name.trim().length() > 0) {
						cards = database.searchCards(name);
					
					}
					else {
						 cards = database.getAllCards();
					}
					
					PaymentTable model = new PaymentTable(cards);
					findPayment.setModel(model);
					
				}
				catch(Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		search.setFont(new Font("Tahoma", Font.PLAIN, 30));
		searchBar.add(search);
		
		
		
		JButton confirm = new JButton("Confirm Payment");
		confirm.setBackground(Color.WHITE);
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = findPayment.getSelectedRow();
				//check if row selected
				if(i >=0) {
					//process order
					String str_cID = findPayment.getModel().getValueAt(i, 1).toString();
					int cID = Integer.valueOf(str_cID);
					
					LocalDate currDate = java.time.LocalDate.now();
					String strDate = currDate.toString();
					database.processOrder(strDate, mainOrder.totalPrice, cID);
					
					JOptionPane.showMessageDialog(frame, "Order Processed. Thank You!");
					new home().setVisible(true);
					frame.dispose();
					database.closeConnection();
					
				}
				else {
					JOptionPane.showMessageDialog(frame, "Payment Not Chosen");
				}
			}
		});
		confirm.setFont(new Font("Tahoma", Font.PLAIN, 30));
		existing.add(confirm, BorderLayout.SOUTH);	
		
	}
}
