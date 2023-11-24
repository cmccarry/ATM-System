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
import model.SavingAccount;

public class TransferAccountChooserCtrller {
	private double totalAmount;
	private String id;
	private String saving_id;
	private String credit_id;
	private String date;
	private	String transType = "Transfer";
	private String amount;
	private String balance;
	@FXML
	private Button savingBtn;
	@FXML
	private Button creditBtn;
	public void setInfor(String id, double totalAmount) throws SQLException {
		this.id = id;
		this.totalAmount = totalAmount;
		String lastAccDigit = id.substring(id.length() - 1);
		saving_id = "30" + lastAccDigit;
		credit_id = "20" + lastAccDigit;
		savingBtn.setText("Saving " + saving_id);
		creditBtn.setText("Credit " + credit_id);
	}
	
	String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    
	@FXML
	private void savingAction(ActionEvent event) throws SQLException, IOException {
		connection = DBConnect.getConnect();
		makeTransfer("Saving");
        // Go to PrintReceipt interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PrintReceipt.fxml"));
        Parent root = loader.load(); 
		
        PrintReceiptController printReceiptController = loader.getController();
        printReceiptController.receiptInfor(this.date, this.id, this.transType, this.amount, this.balance);     
    	
    	Scene scene = new Scene(root);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	@FXML
	private void creditAction(ActionEvent event) throws SQLException, IOException {
		connection = DBConnect.getConnect();
		makeTransfer("Credit");
        // Go to PrintReceipt interface
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PrintReceipt.fxml"));
        Parent root = loader.load(); 
		
        PrintReceiptController printReceiptController = loader.getController();
        printReceiptController.receiptInfor(this.date, this.id, this.transType, this.amount, this.balance);     
    	
    	Scene scene = new Scene(root);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	public void makeTransfer(String destAccType) throws SQLException {
		System.out.println(id);
		// Search the acc with accID, store main acc in an instance
		query = "SELECT * FROM atm.checking_account WHERE `acc_id` = " + id;
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        CheckingAccount main_acc = new CheckingAccount(Integer.valueOf(id), resultSet.getDouble("balance"));
        // Search the acc with accID, store destination acc in an instance        
        if (destAccType == "Saving") {
        	query = "SELECT * FROM atm.saving_account WHERE `acc_id` = " + saving_id;
        } else if (destAccType == "Credit") {
        	query = "SELECT * FROM atm.checking_account WHERE `acc_id` = " + credit_id;
        }
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        // Transfer
        SavingAccount saving_acc = null;
        CheckingAccount credit_acc = null;
        if (destAccType == "Saving") {
        	saving_acc = new SavingAccount(Integer.parseInt(saving_id), resultSet.getDouble("balance"), 
            		resultSet.getDouble("interest_rate"));
        	main_acc.transfer(saving_acc, totalAmount);
        } else if (destAccType == "Credit") {
        	credit_acc = new CheckingAccount(Integer.parseInt(saving_id), resultSet.getDouble("balance"));
        	main_acc.transferToCredit(credit_acc, totalAmount);
        }             
                
     // Update balance main account
        String newMainBalanceStr = String.valueOf(main_acc.getBalance());
        query = "UPDATE `checking_account` SET `balance`= " + newMainBalanceStr + "WHERE `acc_id` = " + id;
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
        // Update balance destination account
        String newDestBalanceStr = null;       

        if (destAccType == "Saving") {
        	query = "UPDATE atm.saving_account SET `balance` = ? WHERE `acc_id` = ?";
        	preparedStatement = connection.prepareStatement(query);
        	newDestBalanceStr = String.valueOf(saving_acc.getBalance());
        	preparedStatement.setString(1, newDestBalanceStr);
        	preparedStatement.setString(2, saving_id);
        } else if (destAccType == "Credit") {
        	query = "UPDATE atm.checking_account SET `balance` = ? WHERE `acc_id` = ?";
        	preparedStatement = connection.prepareStatement(query);
        	newDestBalanceStr = String.valueOf(credit_acc.getBalance());
        	preparedStatement.setString(1, newDestBalanceStr);
        	preparedStatement.setString(2, credit_id);
        }      
        preparedStatement.executeUpdate();
        // Create record of transaction
        transactionRecord(newMainBalanceStr, newDestBalanceStr, destAccType);
        this.amount = String.format("$%.2f", totalAmount);
        this.balance = String.format("$%.2f", main_acc.getBalance());
	}
	public void transactionRecord(String newMainBalance, String newDestBalance, String destAccType) throws SQLException {
		// Record of main acc
    	query = "INSERT INTO `transaction`(`trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES (?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        String dateStr = String.valueOf(date);
        this.date = dateStr;
        // Set 5 ?'s of the query
        String mainRecordAmountStr = "-" + String.format("%.2f", totalAmount);
        preparedStatement.setString(1, dateStr);
        preparedStatement.setString(2, "Transfer");
        preparedStatement.setString(3, mainRecordAmountStr);
        preparedStatement.setString(4, newMainBalance);
        preparedStatement.setString(5, id);
        preparedStatement.executeUpdate();
     // Record of destination acc       
        // Set 5 ?'s of the query
        String typeStr = "";
        String destRecordAmountStr = "";
        if (destAccType == "Saving") {
        	typeStr = "Get Transferred";
        	destRecordAmountStr = "+" + String.format("%.2f", totalAmount);
        	query = "INSERT INTO `transaction`(`trans_date`, `trans_type`, `amount`, `history`, `saving_id`) VALUES (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
        	preparedStatement.setString(5, saving_id);
        }
        else if (destAccType == "Credit") {
        	typeStr = "Payment";
        	destRecordAmountStr = mainRecordAmountStr;
        	query = "INSERT INTO `transaction`(`trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
        	preparedStatement.setString(5, credit_id);
        }
        preparedStatement.setString(1, dateStr);
        preparedStatement.setString(2, typeStr);
        preparedStatement.setString(3, destRecordAmountStr);
        preparedStatement.setString(4, newDestBalance);
        
        preparedStatement.executeUpdate();
        
    }
	
	@FXML
	public void cancelAction(ActionEvent event) throws SQLException, IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
    	Parent root = loader.load(); 
		
		// to add name of account to the main menu page
    	Connection connection = DBConnect.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT `cus_num` FROM atm.checking_account WHERE `acc_id` = " + id);
        ResultSet resultSet = preparedStatement.executeQuery();
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
    	
    	Scene scene = new Scene(root);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
}
