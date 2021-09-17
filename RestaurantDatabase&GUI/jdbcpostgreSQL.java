import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

/*
CSCE 315
9-25-2019 Original
2/7/2020 Update for AWS
 */

public class jdbcpostgreSQL {
	
	
	private Connection conn;
  public jdbcpostgreSQL() throws Exception {
    //dbSetup hides my username and password
    dbSetup my = new dbSetup();
    //Building the connection
     //Connection conn = null;
     try {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(
          "jdbc:postgresql://csce-315-db.engr.tamu.edu/db903_group5_project2",
           my.user, my.pswd);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }//end try catch
     
     System.out.println("Opened database successfully");
}
  
  
  
  
/*
 * ----------------------------PRODUCTS FUNCTIONS---------------------------------------
 */
  
 //returns List of all products in products table of database
public List<Product> getAllItems() throws Exception{
	
	  List<Product> products = new ArrayList<>();
	  Statement stmt = null;
	  ResultSet result = null;
	  
	  try {
		  stmt = conn.createStatement();
		  result = stmt.executeQuery("SELECT * FROM products");
		  while(result.next()) {
			Product temp = convertRowtoProduct(result);
			products.add(temp);
		  }
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  
	  return products;
	  
  }



//converts a result into a product object
private Product convertRowtoProduct(ResultSet result) throws SQLException {
	int id = result.getInt("id");
	String name = result.getString("name");
	double price = result.getDouble("price");
	
	Product product = new Product(id, name, price, null);
	return product;
}


//searches for products that start with the string input
//and puts them in a list
public List<Product> searchItems(String input) throws Exception{
	  List<Product> products = new ArrayList<Product>();
	  Statement stmt = null;
	  ResultSet result = null;
	  
	  try {
		  stmt = conn.createStatement();
		  String statement = "SELECT * FROM products WHERE name LIKE '%" + input + "%'";
		  result = stmt.executeQuery(statement);
		  while(result.next()) {
				Product temp = convertRowtoProduct(result);
				products.add(temp);
			  }
		  
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
	  
	  return products;  
}


//gets a specific product given the input string
public Product getProduct(String input) throws Exception{
	  Product item = new Product();
	  Statement stmt = null;
	  ResultSet result = null;
	  
	  try {
		  stmt = conn.createStatement();
		  String statement = "SELECT * FROM products WHERE name LIKE '%" + input + "%'";
		  result = stmt.executeQuery(statement);
		  while(result.next()) {
				item = convertRowtoProduct(result);
		  }
		  
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
	  
	  return item;
}

//gets a specific product given it's ID
public Product getProduct(int id) throws Exception{
	  Product item = new Product();
	  Statement stmt = null;
	  ResultSet result = null;
	  
	  String input = Integer.toString(id);
	  
	  try {
		  stmt = conn.createStatement();
		  String statement = "SELECT * FROM products WHERE id=" + input;
		  result = stmt.executeQuery(statement);
		  while(result.next()) {
				item = convertRowtoProduct(result);
			  }
		  
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
	  
	  return item;
}



//get a specifc group of products given an input
public List<String> getGroup(String input) throws Exception{
	  List<String> entrees = new ArrayList<String>();
	  Statement stmt = null;
	  ResultSet result = null;
	  String newname = null;
	  int quantity = 0;
	  
	  
	  //check availability
	  try {
		  stmt = conn.createStatement();
		  result = stmt.executeQuery("SELECT * FROM inventory WHERE product_name LIKE '%" + input + "%'");
		  while(result.next()) {
			  newname = result.getString("product_name");
			  quantity = result.getInt("quantity");
			  if(quantity > 0) {
				  entrees.add(newname);
			  } 
		  }
		  
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  
	  
	  return entrees;
	  
}

//checks if a product is available
public boolean isAvailable(Product item) {
	Statement stmt = null;
	ResultSet result = null;
	String statement = "";
	int quantity = 0;
	
	String name = item.getName();
	
	try {
		stmt = conn.createStatement();
		statement = "SELECT quantity FROM inventory WHERE product_name LIKE '%" + name + "%'" ;
		result = stmt.executeQuery(statement);
		while(result.next()) {
			quantity = result.getInt("quantity");
		}	

	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	
	if(quantity > 0) {
		return true;
	}
	else {
		return false;
	}
	
}



/*
 * -------------------------PAYMENT INFO FUNCTIONS-----------------------------------
 */


//gets list of all saved cards in database
public List<PaymentInfo> getAllCards() throws Exception{
	  List<PaymentInfo> cards = new ArrayList<>();
	  Statement stmt = null;
	  ResultSet result = null;
	  
	  try {
		  stmt = conn.createStatement();
		  result = stmt.executeQuery("SELECT * FROM payment_info");
		  while(result.next()) {
			PaymentInfo temp = convertRowtoPayment(result);
			cards.add(temp);
			
		  }
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  
	  return cards;
	  
}


//converts a result into a payment object
private PaymentInfo convertRowtoPayment(ResultSet result) throws SQLException {
	String date = result.getString("expiration_date");
	String cvv = result.getString("cvv");
	String ccn = result.getString("ccn");
	String name = result.getString("customer_name");
	int customer_id = result.getInt("customer_id");
	int id = result.getInt("id");
	
	PaymentInfo card = new PaymentInfo(date, cvv, name, customer_id, id, ccn);
	return card;
}


//searches for a specific cards based on input
public List<PaymentInfo> searchCards(String input) throws Exception{
	  List<PaymentInfo> cards = new ArrayList<PaymentInfo>();
	  Statement stmt = null;
	  ResultSet result = null;
	  
	  try {
		  stmt = conn.createStatement();
		  String statement = "SELECT * FROM payment_info WHERE customer_name LIKE '%" + input + "%'";
		  result = stmt.executeQuery(statement);
		  while(result.next()) {
			  PaymentInfo temp = convertRowtoPayment(result);
				cards.add(temp);
		  }
		  
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
	  
	  return cards;  
}



//adds a new card to the database
public void addPayment(PaymentInfo newCard) {
	String statement = "";
	Statement stmt = null;
	ResultSet result = null;
	
	String ccn = newCard.getCcn();
	String name = newCard.getName();
	String cvv = newCard.getCvv();
	String date = newCard.getDate();
	String sId = "";
	String sCusId = "";
	
	//get next customer id
	try {
		sCusId = getNextCusId("payment_info");
		
	}catch(Exception e) {
		 e.printStackTrace();
	}
	
	
	//get next id
	try {
		sId = getNextId("payment_info");

	}catch(Exception e) {
		 e.printStackTrace();
	}
	
	
	
	
	String values = " values ('" +  sId +  "'" + ", " + "'" + sCusId +  "'" +", " 
								+ "'" + ccn + "'" + ", " +"'" + date + "'" + ", " 
								+ "'" + cvv + "'" + ", " + "'" + name + "')";
	
	try {
		stmt = conn.createStatement();
		statement = "INSERT INTO payment_info" + values;
		stmt.executeUpdate(statement);
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	
}

/*
 * ---------------------------ID Functions ------------------------
 */

//gets the next id from the input table
public String getNextId(String table) {
	String statement = "";
	Statement stmt = null;
	ResultSet result = null;
	
	int id = 0;
	
		//get next id
		try {
			stmt = conn.createStatement();
			String getId = "SELECT MAX(id) FROM " + table;
			result = stmt.executeQuery(getId);
			while(result.next()) {
				id = result.getInt("max") + 1;
			}

		}catch(Exception e) {
			 e.printStackTrace();
		}
		
		return Integer.toString(id);
		
}


//gets the next customer id from the input table
public String getNextCusId(String table) {
	
	String statement = "";
	Statement stmt = null;
	ResultSet result = null;
	
	int cusId = 0;
	
	//get next customer id
	try {
		stmt = conn.createStatement();
		String getCusId = "SELECT MAX(customer_id) FROM " + table;
		result = stmt.executeQuery(getCusId);
		while(result.next()) {
			cusId = result.getInt("max") + 1;
		}	
	}
	catch(Exception e) {
		e.printStackTrace();
	}
		
	return Integer.toString(cusId);
	
}

//searches for specific customer ID given a name
public int getCustomerID(String name) {
	String statement = "";
	Statement stmt = null;
	ResultSet result = null;
	int id = 0;
	
	try {
		  stmt = conn.createStatement();
		  statement = "SELECT id FROM customers WHERE name LIKE '%" + name + "%'";
		  result = stmt.executeQuery(statement);
		  while(result.next()) {
				id = result.getInt("id");
		  }
		  
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
	
	return id;
	
	
}



/*
 * ---------------------MISC. FUNCTIONS-------------------
 */


//adds a new customer to the database
public void addCustomer(String name) {
	String statement = "";
	Statement stmt = null;
	ResultSet result = null;
	int cusID = 0;
	
	//get next customer id
	try {
		stmt = conn.createStatement();
		statement = "SELECT MAX(id) FROM customers";
		result = stmt.executeQuery(statement);
		while(result.next()) {
			cusID = result.getInt("max") + 1;
		}	
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	
	String StrCusId = Integer.toString(cusID);
	
	String values = " values ('" + StrCusId + "', '" + name + "')";
	
	//get next customer id
	try {
		stmt = conn.createStatement();
		statement = "INSERT INTO customers" + values;
		stmt.executeUpdate(statement);
	}
	catch(Exception e) {
		e.printStackTrace();
	}
		
	
}




//processes an order and adds to orders table in database
public void processOrder(String date, double total, int cID) {
	Statement stmt = null;
	ResultSet result = null;
	String statement = "";
	String sId = "";
	String sCusId = Integer.toString(cID);
	
	
		
		
	//get next id
	try {
		sId = getNextId("orders");
	}catch(Exception e) {
		 e.printStackTrace();
	}
		
	String values = " values ('"  + sId +  "'" + ", " + "'" + date +  "'" +", " 
				+ "'" + sCusId + "'" + ", " +"'" + total + "')";

	try {
		stmt = conn.createStatement();
		statement = "INSERT INTO orders" + values;
		stmt.executeUpdate(statement);
	}
	catch(Exception e){
		e.printStackTrace();
	}	
}


//finds the product id of the top 2 most bought items
public List<Integer> topRecs(){
	List<Integer> fullList = new ArrayList<Integer>();
	List<Integer> recs = new ArrayList<Integer>();
	Statement stmt = null;
	ResultSet result = null;
	String statement = null;
	
	try {
		stmt= conn.createStatement();
		statement = "SELECT product_id, COUNT (1) FROM ordered_products GROUP BY product_id ORDER BY COUNT(1) DESC";
		result = stmt.executeQuery(statement);
		while(result.next()) {
			fullList.add(result.getInt("product_id"));
		}
	}
	catch(Exception e){
		e.printStackTrace();
		
	}
	
	recs.add(fullList.get(0));
	recs.add(fullList.get(1));
	
	return recs;
}

//closes the connection to the database  
void closeConnection() {
	try {
		conn.close();
		JOptionPane.showMessageDialog(null, "Connection Closed.");
	} catch (SQLException e) {
		JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
	}
}

  

public static void main(String args[]) {
	
	
	
	
}

}
     
     




