package model;

public class Bill {
	private String bill_type;
	private int quantity = 500;
    private int bill_ID;
	
    // constuctor
	public Bill(int bill_ID, String bill_type, int quantity) {
        this.bill_type = bill_type;
        this.quantity = quantity;
        this.bill_ID = bill_ID;
    }
	
    // getters
    public int getBillID() {
        return this.bill_ID;
    }
	public String getBill_type() {
        return this.bill_type;
    }
	public int getQuantity() {
        return this.quantity;
    }
	
    // setters
    public void setBillID(int bill_ID) {
        this.bill_ID = bill_ID;
    }
	public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }
	public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // change bill quantity
    public void addQuantity(int change) {
        this.quantity += change;
    }
    public void subQuantity(int change) {
        this.quantity -= change;
    }
}
