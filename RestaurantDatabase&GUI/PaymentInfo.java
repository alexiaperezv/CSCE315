
public class PaymentInfo {
	
	private String date;
	private String cvv;
	private String name;
	private int customer_id;
	private int id;
	private String ccn;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCcn() {
		return ccn;
	}
	public void setCcn(String ccn) {
		this.ccn = ccn;
	}
	public PaymentInfo(String date, String cvv, String name, int customer_id, int id, String ccn) {
		super();
		this.date = date;
		this.cvv = cvv;
		this.name = name;
		this.customer_id = customer_id;
		this.id = id;
		this.ccn = ccn;
	}
	

	

}
