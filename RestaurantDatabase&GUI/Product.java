import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/*
 * 1 - E4
 * 2 - S2
 * 3 - B2
 * 4 - D3
 * 5 - E6
 * 6 - B1
 * 7 - E5
 * 8 - S3
 * 9 - B3
 * 10 - D2
 * 11 - S4
 * 12 - B4
 * 13 - E1
 * 14 - S1
 * 15 - D1
 * 16 - E2
 * 17 - E3
 * 18 - B5
 * 19 - E7
 */
 

public class Product {
	private int id;
	private String name;
	private double price;
	private List<String> addons = new ArrayList<String>();
	

	public Product() {
		id = 0;
		name = null;
		price = 0.0;
		addons = null;
	}
	
	public Product(int id, String name, double price, List<String> addons){
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.addons = null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public String getAddOns(){
		String listAdd =  "";
		if(addons != null) {
			for(int i = 0; i < addons.size(); i++) {
				listAdd += addons.get(i) + ", ";
			}
		}
		return listAdd;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setAddons(List<String> addons) {
		this.addons = addons;
	}
	
	@Override
	public String toString() {
		if(addons != null) {
			String result = "id=" + id + ", ";
			for(int i = 0; i < addons.size(); i++) {
				result += addons.get(i) + ", ";
			}
			result += ", name=" + name + ", price=" + price;
			return result;
		}
		else{
			return "id=" + id + ", name=" + name + ", price=" + price;
		}
		
	}
	
	
	
}
