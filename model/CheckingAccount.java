package model;

public class CheckingAccount {
	// attributes
	private int checkingAccId;
	private double accBalance;
	
	// constructor
	public CheckingAccount(int id, double balance) {
		checkingAccId = id;
		accBalance = balance;
	}
	
	// getters
	public int getId() {
		return checkingAccId;
	}
	public double getBalance() {
		return accBalance;
	}
	
	// setters
	public void setId(int id) {
		checkingAccId = id;
	}
	public void setBalance(double balance) {
		accBalance = balance;
	}

	// methods
	public void withdraw(double withdrawAmt) {
		accBalance -= withdrawAmt;
	}
	public void deposit(double depositAmt) {
		accBalance += depositAmt;
	}
	// to transfer money into another checking account
	public void transfer(CheckingAccount toAccount, double transferAmt) {
		this.withdraw(transferAmt);
		toAccount.deposit(transferAmt);
	}
	// to transfer money into a savings account
	public void transfer(SavingAccount toAccount, double transferAmt) {
		this.withdraw(transferAmt);
		toAccount.deposit(transferAmt);
	}
	// to transfer money into a credit account
	public void transferToCredit(CheckingAccount toAccount, double transferAmt) {
		this.withdraw(transferAmt);
		toAccount.makePayment(transferAmt);
	}
	// methods for the case of a credit card
	public void getCash(double amount) {
		accBalance += amount;
	}
	public void makePayment(double amount) {
		accBalance -= amount;
	}
}
