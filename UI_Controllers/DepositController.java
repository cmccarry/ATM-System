package UI_Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import helper.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.CheckingAccount;

public class DepositController 
{
	@FXML
	HBox myVBox;
	@FXML
	Label moneyLabel;
	@FXML
	Button continueBtn;
	
	String query = null;
	Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    private String currentAmount = "";
    private String id;
	private String date;
	private	String transType;
	private String amount;
	private String balance;
    public void setID(String id) throws SQLException {
		this.id = id;
	}
    
	@FXML
	public void continueAction(ActionEvent event) throws SQLException, IOException {
		String amountStr = moneyLabel.getText().substring(1); // Get the amount as a string without $ sign
		double depositAmount = Double.parseDouble(amountStr);
		if (depositAmount > 0) {
			connection = DBConnect.getConnect();
			// Search the acc with accID, store in an instance
			query = "SELECT * FROM atm.checking_account WHERE `acc_id` = " + id;
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            CheckingAccount acc = new CheckingAccount(Integer.parseInt(id), resultSet.getDouble("balance"));
            
            int idInt = Integer.parseInt(id);
            boolean debit = false;
            boolean credit = false;
            // Check accID to see if it's a debit or credit
            if (idInt > 100 && idInt < 200) debit = true;
            else if (idInt > 200 && idInt < 300) credit = true;
            
            // Update balance
            if (debit) acc.deposit(depositAmount);
            else if (credit) acc.makePayment(depositAmount);
            String newBalanceStr = String.valueOf(acc.getBalance());
            
        	query = "UPDATE `checking_account` SET `balance`= " + newBalanceStr + "WHERE `acc_id` = " + id;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            // Create record of transaction
            query = "INSERT INTO `transaction`(`trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            // Set values for query
            String typeStr = "";
            String recordAmountStr = "";
            if (debit) {
            	typeStr = "Deposit";
            	recordAmountStr = "+" + amountStr;
            }
            else if (credit) {
            	typeStr = "Payment";
            	recordAmountStr = "-" + amountStr;
            }
            
            // Set 5 ?'s of the query
            preparedStatement.setString(1, String.valueOf(date));
            preparedStatement.setString(2, typeStr);
            preparedStatement.setString(3, recordAmountStr);
            preparedStatement.setString(4, newBalanceStr);
            preparedStatement.setString(5, id);
            preparedStatement.executeUpdate();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PrintReceipt.fxml"));
        	Parent root = loader.load(); 
        	
        	PrintReceiptController printReceiptController = loader.getController();
        	this.date = String.valueOf(date);
        	this.transType = typeStr;
        	this.amount = moneyLabel.getText();
        	this.balance = String.format("$%.2f", acc.getBalance());
        	printReceiptController.receiptInfor(this.date, this.id, this.transType, this.amount , this.balance);    

        	Scene scene = new Scene(root);
        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		stage.setScene(scene);
    		stage.show();
		}		
	}
	
	@FXML
	public void cancelAction(ActionEvent event) throws SQLException, IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
    	Parent root = loader.load(); 
    	connection = DBConnect.getConnect();
		// to add name of account to the main menu page
        preparedStatement = connection.prepareStatement("SELECT `cus_num` FROM atm.checking_account WHERE `acc_id` = " + id);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String cus_num = String.valueOf(resultSet.getInt("cus_num"));
        
    	preparedStatement = connection.prepareStatement("SELECT `full_name` FROM atm.customer WHERE `customer_num` = " + cus_num);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String fullNameString = resultSet.getString("full_name");
        String[] fullName = fullNameString.split(" ");
    	String firstName = fullName[0];
    	MainMenuController mainMenuController = loader.getController();
    	mainMenuController.displayName(firstName);
    	mainMenuController.setID(id); 
    	
    	// check if card is credit
    	if (Integer.valueOf(id) > 200 && Integer.valueOf(id) < 300) {
    		mainMenuController.changeToCreditInterface();
    	}
    	
    	Scene scene = new Scene(root);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	public void pressNum1() {
		currentAmount += "1";
		setLabel();
	}
	@FXML
	public void pressNum2() {
		currentAmount += "2";
		setLabel();
	}
	@FXML
	public void pressNum3() {
		currentAmount += "3";
		setLabel();
	}
	@FXML
	public void pressNum4() {
		currentAmount += "4";
		setLabel();
	}
	@FXML
	public void pressNum5() {
		currentAmount += "5";
		setLabel();
	}
	@FXML
	public void pressNum6() {
		currentAmount += "6";
		setLabel();
	}
	@FXML
	public void pressNum7() {
		currentAmount += "7";
		setLabel();		
	}
	@FXML
	public void pressNum8() {
		currentAmount += "8";
		setLabel();
	}
	@FXML
	public void pressNum9() {
		currentAmount += "9";
		setLabel();
	}
	@FXML
	public void pressNum0() {
		currentAmount += "0";
		setLabel();
	}
	@FXML
	public void pressBackspace() {
		if (!currentAmount.isEmpty()) {
			currentAmount = currentAmount.substring(0, currentAmount.length() - 1);
		}
		setLabel();
	}
	@FXML
	public void resetLabel() {
		currentAmount = "";
		setLabel();
	}
	// Set the money label every time the money change
	public void setLabel() {
		int len = currentAmount.length();
		if (len == 0) moneyLabel.setText("$0.00");
		else if (len == 1) moneyLabel.setText("$0.0" + currentAmount);
		else if (len == 2) moneyLabel.setText("$0." + currentAmount);
		else if (len >= 3) moneyLabel.setText("$" + currentAmount.substring(0, len - 2) + "." + currentAmount.substring(len - 2));
		if (len > 0) {
			continueBtn.setDisable(false);
		} else {
			continueBtn.setDisable(true);
		}
	}
}