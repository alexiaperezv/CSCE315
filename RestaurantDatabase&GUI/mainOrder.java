import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.EventQueue;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;


public class mainOrder{

	private JFrame frame;
	private JTable cart;
	public List<Product> ordered_Products = new ArrayList<Product>();
	private static jdbcpostgreSQL database;
	private int entreeSelect;
	private int sideSelect;
	private int drinkSelect;
	private int dessertSelect;
	private DecimalFormat totalFormat = new DecimalFormat("#.00");
	public static double totalPrice;
	private List<String> entreeList = new ArrayList<String>();
	private List<String> sideList = new ArrayList<String>();
	private List<String> drinkList = new ArrayList<String>();
	private List<String> dessertList = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainOrder window = new mainOrder();
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
	public mainOrder() {
		initialize();
	}
	
	
	
	
	private void initialize() {
		
		/*
		 * -----------Connect to Database-------------------
		 */
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
		
		
		
		/*
		 * --------------MENU PANELS ----------------------
		 */
		JPanel mainMenu = new JPanel();
		frame.getContentPane().add(mainMenu, "name_126924497813900");
		mainMenu.setLayout(new BorderLayout(0, 0));
		mainMenu.setVisible(true);
		
		JPanel EntreeMenu = new JPanel();
		frame.getContentPane().add(EntreeMenu, "name_126930520844900");
		EntreeMenu.setLayout(new BorderLayout(0, 0));
		EntreeMenu.setVisible(false);
		
		JPanel SidesMenu = new JPanel();
		SidesMenu.setBackground(Color.ORANGE);
		frame.getContentPane().add(SidesMenu, "name_126934377853400");
		SidesMenu.setLayout(new BorderLayout(0, 0));
		SidesMenu.setVisible(false);
		
		JPanel DrinkMenu = new JPanel();
		frame.getContentPane().add(DrinkMenu, "name_126958429058300");
		DrinkMenu.setLayout(new BorderLayout(0, 0));
		DrinkMenu.setVisible(false);
		
		JPanel DessertMenu = new JPanel();
		DessertMenu.setBackground(Color.ORANGE);
		frame.getContentPane().add(DessertMenu, "name_126968348752500");
		DessertMenu.setLayout(new BorderLayout(0, 0));
		DessertMenu.setVisible(false);
		
		
		
		/*
		 * --------------NAVIGATION BAR ----------------------
		 */
		
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
		order.setForeground(Color.WHITE);
		order.setBackground(Color.DARK_GRAY);
		order.setFont(new Font("Tahoma", Font.BOLD, 30));
		GridBagConstraints gbc_order = new GridBagConstraints();
		gbc_order.insets = new Insets(0, 0, 5, 0);
		gbc_order.gridx = 0;
		gbc_order.gridy = 10;
		NavigationBar.add(order, gbc_order);
		
		JButton payment = new JButton("Payment");
		payment.setFont(new Font("Tahoma", Font.PLAIN, 30));
		GridBagConstraints gbc_payment = new GridBagConstraints();
		gbc_payment.insets = new Insets(0, 0, 5, 0);
		gbc_payment.gridx = 0;
		gbc_payment.gridy = 13;
		NavigationBar.add(payment, gbc_payment);
		payment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paymentMenu paymenu = new paymentMenu();
				paymenu.main(null);
				frame.dispose();
			}
		});
		
		
		
		//--------------CART------------------//
		
		JPanel cartPanel = new JPanel();
		mainMenu.add(cartPanel, BorderLayout.CENTER);
		
		cart = new JTable();
		cartPanel.add(cart, BorderLayout.CENTER);
		cartPanel.add(new JScrollPane(cart));
		cart.setFillsViewportHeight(true);
		orderTable model = new orderTable(ordered_Products);
		cart.setModel(model);
			
		
		//--------PRODUCT MENU--------------------------//
		
		JPanel chooseMenu = new JPanel();
		mainMenu.add(chooseMenu, BorderLayout.NORTH);
		
		JButton entrees = new JButton("Entrees");
		entrees.setFont(new Font("Tahoma", Font.PLAIN, 30));
		entrees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EntreeMenu.setVisible(true);
				mainMenu.setVisible(false);
			}
		});
		chooseMenu.add(entrees);
		
		JButton sides = new JButton("Sides");
		sides.setFont(new Font("Tahoma", Font.PLAIN, 30));
		sides.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SidesMenu.setVisible(true);
				mainMenu.setVisible(false);
			}
		});
		chooseMenu.add(sides);
		
		JButton drinks = new JButton("Beverages");
		drinks.setFont(new Font("Tahoma", Font.PLAIN, 30));
		drinks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DrinkMenu.setVisible(true);
				mainMenu.setVisible(false);
			}
		});
		chooseMenu.add(drinks);
		
		JButton desserts = new JButton("Desserts");
		desserts.setFont(new Font("Tahoma", Font.PLAIN, 30));
		desserts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DessertMenu.setVisible(true);
				mainMenu.setVisible(false);
			}
		});
		chooseMenu.add(desserts);
		
		JPanel bottomBar = new JPanel();
		mainMenu.add(bottomBar, BorderLayout.SOUTH);
		
		JLabel total = new JLabel("Total: ");
		total.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomBar.add(total);

		
		
		total.setText("Total: $0");
		
		JButton remove = new JButton("Remove Item");
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = cart.getSelectedRow();
				//remove row
				if(i >= 0) {
					model.removeRow(i);
				}
				else {
					JOptionPane.showMessageDialog(null, "No Item Selected");
				}
				
				//remove product
				ordered_Products.remove(i);
				
				totalPrice = 0;
				for(int j = 0; j < ordered_Products.size(); j++) {
					Product item = ordered_Products.get(j);
					double price = item.getPrice();
					totalPrice += price;
				}
				
				total.setText("Total: $" + totalFormat.format(totalPrice));
			}
		});
		remove.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bottomBar.add(remove);
		
		
		//-----------RECOMMENDATIONS--------------//
		
		JPanel recommendPanel = new JPanel();
		mainMenu.add(recommendPanel, BorderLayout.EAST);
		recommendPanel.setBackground(Color.ORANGE);
		GridBagLayout gbl_recommendPanel = new GridBagLayout();
		gbl_recommendPanel.columnWidths = new int[]{163, 0};
		gbl_recommendPanel.rowHeights = new int[]{224, 25, 0, 0, 0, 0, 0, 0, 0};
		gbl_recommendPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_recommendPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		recommendPanel.setLayout(gbl_recommendPanel);
		
		JLabel recommendLabel = new JLabel("Recommendations");
		recommendLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_recommendLabel = new GridBagConstraints();
		gbc_recommendLabel.insets = new Insets(0, 0, 5, 0);
		gbc_recommendLabel.anchor = GridBagConstraints.WEST;
		gbc_recommendLabel.gridx = 0;
		gbc_recommendLabel.gridy = 0;
		recommendPanel.add(recommendLabel, gbc_recommendLabel);
		
		JLabel product1 = new JLabel("New label");
		product1.setBackground(Color.WHITE);
		product1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_product1 = new GridBagConstraints();
		gbc_product1.insets = new Insets(0, 0, 5, 0);
		gbc_product1.gridx = 0;
		gbc_product1.gridy = 3;
		recommendPanel.add(product1, gbc_product1);
		
		JLabel product2 = new JLabel("New label");
		product2.setBackground(Color.WHITE);
		product2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_product2 = new GridBagConstraints();
		gbc_product2.gridx = 0;
		gbc_product2.gridy = 7;
		recommendPanel.add(product2, gbc_product2);
		
		List<Integer> idRecs = database.topRecs();
		
		List<String> recs = new ArrayList<String>();
		
		//get names of recommended products
		for(int i = 0; i < idRecs.size(); i++) {
			
			Product temp = new Product();
			try {
				temp = database.getProduct(idRecs.get(i));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			recs.add(temp.getName());
		}
		
		//print those recommendations on the panel
		product1.setText(recs.get(0));
		product2.setText(recs.get(1));
		
		
		
		
		
		
		
		//---------------ENTREES MENU------------------------------//
		
		
		JPanel chooseEntree = new JPanel();
		chooseEntree.setBackground(Color.WHITE);
		EntreeMenu.add(chooseEntree, BorderLayout.NORTH);
		
		JButton entreeBack = new JButton("Go Back");
		entreeBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(true);
				EntreeMenu.setVisible(false);
			}
		});
		entreeBack.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chooseEntree.add(entreeBack);
		
		JLabel EntreeLabel = new JLabel("Choose one Entree");
		EntreeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		EntreeLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chooseEntree.add(EntreeLabel);
		
		//get available entrees
				try {
					entreeList = database.getGroup("E");
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				for(int i = 0; i < entreeList.size(); i++) {
					Product newEntree = new Product();
					try {
						newEntree = database.getProduct(entreeList.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					JCheckBox entree = new JCheckBox(newEntree.getName());
					int num = Integer.parseInt(newEntree.getName().substring(1));
					entree.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if(entree.isSelected() == true) {
								entreeSelect = num;
							}
						}
					});
					entree.setFont(new Font("Tahoma", Font.PLAIN, 30));
					entree.setBackground(Color.WHITE);
					chooseEntree.add(entree);
				}
		
		JPanel customizations = new JPanel();
		customizations.setBackground(Color.ORANGE);
		EntreeMenu.add(customizations, BorderLayout.CENTER);
		customizations.setLayout(null);
		
		JLabel addOnLabel = new JLabel("Choose Add-Ons:");
		addOnLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		addOnLabel.setBounds(31, 32, 378, 49);
		customizations.add(addOnLabel);
		
		JToggleButton cheeseButton = new JToggleButton("Add Cheese");
		cheeseButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		cheeseButton.setBounds(212, 126, 201, 57);
		customizations.add(cheeseButton);
		
		JToggleButton picklesButton = new JToggleButton("Add Pickles");
		picklesButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		picklesButton.setBounds(549, 126, 201, 57);
		customizations.add(picklesButton);
		
		JToggleButton mayoButton = new JToggleButton("Add Mayo");
		mayoButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		mayoButton.setBounds(212, 243, 201, 57);
		customizations.add(mayoButton);
		
		JToggleButton baconButton = new JToggleButton("Add Bacon");
		baconButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		baconButton.setBounds(549, 243, 201, 57);
		customizations.add(baconButton);
		
		JToggleButton onionButton = new JToggleButton("Add Onion");
		onionButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		onionButton.setBounds(212, 374, 201, 57);
		customizations.add(onionButton);
		
		JToggleButton tomatoButton = new JToggleButton("Add Tomato");
		tomatoButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tomatoButton.setBounds(549, 374, 201, 57);
		customizations.add(tomatoButton);
		
		
		JButton addToCart = new JButton("Add To Cart");
		addToCart.setBackground(Color.WHITE);
		addToCart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Product temp = new Product();
				List<String> addons = new ArrayList<String>();
				
				String finalEntree = "E" + Integer.toString(entreeSelect);
				
				try {
					temp = database.getProduct(finalEntree);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				

				//add add-ons to order
				if(cheeseButton.isSelected() == true) {
					addons.add("cheese");
				}
				if(mayoButton.isSelected() == true) {
					addons.add("mayo");
				}
				if(picklesButton.isSelected() == true) {
					addons.add("pickles");
				}
				if(baconButton.isSelected() == true) {
					addons.add("bacon");
				}
				if(tomatoButton.isSelected() == true) {
					addons.add("tomato");
				}
				if(onionButton.isSelected() == true) {
					addons.add("onion");
				}
				
				temp.setAddons(addons);
				ordered_Products.add(temp);
				
				totalPrice = 0;
				for(int i = 0; i < ordered_Products.size(); i++) {
					Product item = ordered_Products.get(i);
					double price = item.getPrice();
					totalPrice += price;	
				}
				total.setText("Total: $" + totalFormat.format(totalPrice));
				
				
				mainMenu.setVisible(true);
				EntreeMenu.setVisible(false);
				
				}
			
		});
		
		addToCart.setFont(new Font("Tahoma", Font.PLAIN, 30));
		EntreeMenu.add(addToCart, BorderLayout.SOUTH);
		
		
		
		
		//-------------------SIDES MENU------------------------------//
		

		
		JPanel chooseSide = new JPanel();
		chooseSide.setBackground(Color.WHITE);
		SidesMenu.add(chooseSide, BorderLayout.NORTH);
		
		JButton sideBack = new JButton("Go Back");
		sideBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(true);
				SidesMenu.setVisible(false);
			}
		});
		sideBack.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chooseSide.add(sideBack);
		
		JLabel SideLabel = new JLabel("Choose one Side");
		SideLabel.setHorizontalAlignment(SwingConstants.LEFT);
		SideLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chooseSide.add(SideLabel);
		
		
		//get available sides
		try {
			sideList = database.getGroup("S");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		for(int i = 0; i < sideList.size(); i++) {
			Product newSide = new Product();
			try {
				newSide = database.getProduct(sideList.get(i));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JCheckBox side = new JCheckBox(newSide.getName());
			int num = Integer.parseInt(newSide.getName().substring(1));
			side.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(side.isSelected() == true) {
						sideSelect = num;
					}
				}
			});
			side.setFont(new Font("Tahoma", Font.PLAIN, 30));
			side.setBackground(Color.WHITE);
			chooseSide.add(side);
		}
		
		
		JPanel customizations2 = new JPanel();
		customizations2.setBackground(Color.ORANGE);
		SidesMenu.add(customizations2, BorderLayout.CENTER);
		customizations2.setLayout(null);
		
		JLabel addOnLabel2 = new JLabel("Choose Add-Ons:");
		addOnLabel2.setFont(new Font("Tahoma", Font.PLAIN, 40));
		addOnLabel2.setBounds(31, 32, 378, 49);
		customizations2.add(addOnLabel2);
		
		JToggleButton cheeseButton2 = new JToggleButton("Add Cheese");
		cheeseButton2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		cheeseButton2.setBounds(212, 126, 201, 57);
		customizations2.add(cheeseButton2);
		
		JToggleButton saltButton = new JToggleButton("Add Salt");
		saltButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		saltButton.setBounds(549, 126, 201, 57);
		customizations2.add(saltButton);
		
		JToggleButton dressingButton = new JToggleButton("Add Dressing");
		dressingButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		dressingButton.setBounds(180, 243, 255, 57);
		customizations2.add(dressingButton);
		
		JToggleButton ketchupButton = new JToggleButton("Add Ketchup");
		ketchupButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		ketchupButton.setBounds(523, 243, 261, 57);
		customizations2.add(ketchupButton);
		
		
		JButton addToCart2 = new JButton("Add To Cart");
		addToCart2.setBackground(Color.WHITE);
		addToCart2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Product temp = new Product();
				List<String> addons = new ArrayList<String>();
				
				
				String finalSide = "S" + Integer.toString(sideSelect);
				
				try {
					temp = database.getProduct(finalSide);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				//add add-ons to order
				if(cheeseButton2.isSelected() == true) {
					addons.add("cheese");
				}
				if(saltButton.isSelected() == true) {
					addons.add("salt");
				}
				if(dressingButton.isSelected() == true) {
					addons.add("dressing");
				}
				if(ketchupButton.isSelected() == true) {
					addons.add("ketchup");
				}
				
				temp.setAddons(addons);
				ordered_Products.add(temp);
				
				totalPrice = 0;
				for(int i = 0; i < ordered_Products.size(); i++) {
					Product item = ordered_Products.get(i);
					double price = item.getPrice();
					totalPrice += price;
				}
				total.setText("Total: $" + totalFormat.format(totalPrice));
				
				mainMenu.setVisible(true);
				SidesMenu.setVisible(false);
				
			}
		});
		addToCart2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		SidesMenu.add(addToCart2, BorderLayout.SOUTH);
		
		
		//-------------------DRINKS MENU------------------------------//
		

		JPanel chooseDrink = new JPanel();
		chooseDrink.setBackground(Color.WHITE);
		DrinkMenu.add(chooseDrink, BorderLayout.NORTH);
		
		JButton drinkBack = new JButton("Go Back");
		drinkBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(true);
				DrinkMenu.setVisible(false);
			}
		});
		drinkBack.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chooseDrink.add(drinkBack);
		
		JLabel DrinkLabel = new JLabel("Choose one Drink");
		DrinkLabel.setHorizontalAlignment(SwingConstants.LEFT);
		DrinkLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chooseDrink.add(DrinkLabel);
		
		//get available drinks
				try {
					drinkList = database.getGroup("B");
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				for(int i = 0; i < drinkList.size(); i++) {
					Product newDrink = new Product();
					try {
						newDrink = database.getProduct(drinkList.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					JCheckBox drink = new JCheckBox(newDrink.getName());
					int num = Integer.parseInt(newDrink.getName().substring(1));
					drink.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if(drink.isSelected() == true) {
								drinkSelect = num;
							}
						}
					});
					drink.setFont(new Font("Tahoma", Font.PLAIN, 30));
					drink.setBackground(Color.WHITE);
					chooseDrink.add(drink);
				}
		
		
		
		JPanel customizations3 = new JPanel();
		customizations3.setBackground(Color.ORANGE);
		DrinkMenu.add(customizations3, BorderLayout.CENTER);
		customizations3.setLayout(null);
		
		JLabel addOnLabel3 = new JLabel("Choose Add-Ons:");
		addOnLabel3.setFont(new Font("Tahoma", Font.PLAIN, 40));
		addOnLabel3.setBounds(31, 32, 378, 49);
		customizations3.add(addOnLabel3);
		
		JToggleButton iceButton = new JToggleButton("Add Ice");
		iceButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		iceButton.setBounds(344, 131, 201, 57);
		customizations3.add(iceButton);
		
		JToggleButton creamButton = new JToggleButton("Add Whipped Cream");
		creamButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		creamButton.setBounds(266, 279, 357, 57);
		customizations3.add(creamButton);
		
		
		JButton addToCart3 = new JButton("Add To Cart");
		addToCart3.setBackground(Color.WHITE);
		addToCart3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Product temp = new Product();
				List<String> addons = new ArrayList<String>();
				
				String finalSide = "B" + Integer.toString(drinkSelect);
				
				try {
					temp = database.getProduct(finalSide);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				//add add-ons to order
				if(iceButton.isSelected() == true) {
					addons.add("Ice");
				}
				if(creamButton.isSelected() == true) {
					addons.add("Whipped Cream");
				}
				
				temp.setAddons(addons);
				ordered_Products.add(temp);
				
				totalPrice = 0;
				for(int i = 0; i < ordered_Products.size(); i++) {
					Product item = ordered_Products.get(i);
					double price = item.getPrice();
					totalPrice += price;
				}
				total.setText("Total: $" + totalFormat.format(totalPrice));
				
				mainMenu.setVisible(true);
				DrinkMenu.setVisible(false);
			}
		});
		
		addToCart3.setFont(new Font("Tahoma", Font.PLAIN, 30));
		DrinkMenu.add(addToCart3, BorderLayout.SOUTH);
		
		
		
		
		
		//-------------------DESSERTS MENU------------------------------//
		
		
		JPanel chooseDessert = new JPanel();
		chooseDessert.setBackground(Color.WHITE);
		DessertMenu.add(chooseDessert, BorderLayout.NORTH);
		JButton dessertBack = new JButton("Go Back");
		dessertBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(true);
				DessertMenu.setVisible(false);
			}
		});
		dessertBack.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chooseDessert.add(dessertBack);
		
		JLabel DessertLabel = new JLabel("Choose one Dessert");
		DessertLabel.setHorizontalAlignment(SwingConstants.LEFT);
		DessertLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chooseDessert.add(DessertLabel);
		
		
		try {
			dessertList = database.getGroup("D");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		for(int i = 0; i < dessertList.size(); i++) {
			Product newDessert = new Product();
			try {
				newDessert = database.getProduct(dessertList.get(i));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JCheckBox dessert = new JCheckBox(newDessert.getName());
			int num = Integer.parseInt(newDessert.getName().substring(1));
			dessert.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(dessert.isSelected() == true) {
						dessertSelect = num;
					}
				}
			});
			dessert.setFont(new Font("Tahoma", Font.PLAIN, 30));
			dessert.setBackground(Color.WHITE);
			chooseDessert.add(dessert);
		}

		JPanel customizations4 = new JPanel();
		customizations4.setBackground(Color.ORANGE);
		DessertMenu.add(customizations4, BorderLayout.CENTER);
		customizations4.setLayout(null);
		
		JLabel addOnLabel4 = new JLabel("Choose Add-Ons:");
		addOnLabel4.setFont(new Font("Tahoma", Font.PLAIN, 40));
		addOnLabel4.setBounds(31, 32, 378, 49);
		customizations4.add(addOnLabel4);
		
		JToggleButton creamButton2 = new JToggleButton("Add Whipped Cream");
		creamButton2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		creamButton2.setBounds(144, 250, 332, 57);
		customizations4.add(creamButton2);
		
		JToggleButton chocolateButton = new JToggleButton("Add Chocolate Syrup");
		chocolateButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		chocolateButton.setBounds(549, 250, 332, 57);
		customizations4.add(chocolateButton);
		
		JToggleButton icingButton = new JToggleButton("Add Icing");
		icingButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		icingButton.setBounds(382, 127, 255, 57);
		customizations4.add(icingButton);
		
		
		JButton addToCart4 = new JButton("Add To Cart");
		addToCart4.setBackground(Color.WHITE);
		addToCart4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Product temp = new Product();
				List<String> addons = new ArrayList<String>();
				
				String finalSide = "D" + Integer.toString(dessertSelect);
				
				try {
					temp = database.getProduct(finalSide);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				//add add-ons to order
				if(icingButton.isSelected() == true) {
					addons.add("Icing");
				}
				if(creamButton2.isSelected() == true) {
					addons.add("Whipped Cream");
				}
				if(chocolateButton.isSelected() == true) {
					addons.add("Chocolate Syrup");
				}
				
				
				temp.setAddons(addons);
				ordered_Products.add(temp);
				
				totalPrice = 0;
				for(int i = 0; i < ordered_Products.size(); i++) {
					Product item = ordered_Products.get(i);
					double price = item.getPrice();
					totalPrice += price;
				}
				total.setText("Total: $" + totalFormat.format(totalPrice));
				
				
				mainMenu.setVisible(true);
				DessertMenu.setVisible(false);
				
			}
		});
		addToCart4.setFont(new Font("Tahoma", Font.PLAIN, 30));
		DessertMenu.add(addToCart4, BorderLayout.SOUTH);
		
	
	}
}
