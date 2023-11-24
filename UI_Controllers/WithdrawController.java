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
import javafx.stage.Stage;
import model.CheckingAccount;
public class WithdrawController {
	private String currentAmount = "";
	@FXML
	private Label messageLabel;
	@FXML
	private Label moneyLabel;
	@FXML
	private Button continueBtn;	
	private final double CREDIT_LIMIT = 500;
	private String id;
    public void setID(String id) throws SQLException {
		this.id = id;
	}
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    
   
    
	@FXML
	private void cancelAction(ActionEvent event) throws SQLException, IOException {
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
	public void continueAction(ActionEvent event) throws SQLException, IOException {   	
    	int idInt = Integer.parseInt(id);
    	connection = DBConnect.getConnect();
    	// Search the acc with accID, store the acc in a CheckingAccount instance
    	query = "SELECT * FROM atm.checking_account WHERE `acc_id` = " + id;
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        CheckingAccount acc = new CheckingAccount(idInt, resultSet.getDouble("balance"));
        // Get withdraw amount from amount String
        double withdrawAmount = Double.parseDouble(currentAmount);
        double balance = acc.getBalance();
        boolean done = false;
    	if (idInt > 100 && idInt < 200) { // Debit account
    		// if withdraw amount > balance, prevent withdrawing
    		if (withdrawAmount > balance) { // Display message
    			messageLabel.setText("Not enough funds in your account!");
    		} else { // Valid amount
    			done = true;
    		}   		
    	} else if (idInt > 200 && idInt < 300) { // Credit account
    		// Select type = withdraw
    		connection = DBConnect.getConnect();
    		java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime()); // Get today's date
    		String dateStr = String.valueOf(date); // Convert to string
    		// Find all the withdraw transactions customer have done today
    		query = "SELECT * FROM atm.transaction WHERE `checking_id` = '" + id + "' and `trans_type` = 'Get Cash'" +
    				"and `trans_date` = '" + dateStr + "'";
    		preparedStatement = connection.prepareStatement(query);
    		resultSet = preparedStatement.executeQuery(query); 		
    		// Sum all of the today's withdraw amount
    		double totalWithdrew = 0; // Initial the sum of today withdraw
    		// Sum all of today withdraw
    		while (resultSet.next()) {
    			totalWithdrew += Double.parseDouble(resultSet.getString("amount").substring(1));			
    		}
    		// If it's exceed the LIMIT
    		if (totalWithdrew + withdrawAmount > CREDIT_LIMIT) {
    			messageLabel.setText("It exceeded the limit for today!"); // Display message
    		} else // Valid amount
    			done = true;
    	}
    	// If valid amount, go to ChooseBill page, send the account information to ChooseBill
    	if (done) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChooseBill.fxml"));
        	Parent root = loader.load();
        	ChooseBillController chooseBillController = loader.getController();
        	// Send id and amount to withdraw to ChooseBill page
        	chooseBillController.setInfor(id, Integer.parseInt(currentAmount));
        	
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            preparedStatement.close();
	        resultSet.close();           
		}
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
		if (currentAmount != "$0") {
			currentAmount += "0";
			setLabel();
		}	
	}
	@FXML
	public void pressBackspace() {
		if (!currentAmount.isEmpty()) {
			currentAmount = currentAmount.substring(0, currentAmount.length() - 1);
		}
		if (currentAmount.isEmpty()) { // After backspacing, if it's $0
			continueBtn.setDisable(true);
		}
		setLabel();
	}
	@FXML
	public void resetLabel() {
		currentAmount = "";
		continueBtn.setDisable(true);
		setLabel();
	}
	// Set the money label every time the money change
	public void setLabel() {
		if (currentAmount.isEmpty()) {
			moneyLabel.setText("$0");
		} else {
			continueBtn.setDisable(false);
			moneyLabel.setText("$" + currentAmount);
		}
	}
}