package UI_Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import helper.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CheckingAccount;

public class MainMenuController
{
	@FXML
	private VBox leftVbox;
	@FXML
	private Button transferBtn;
	@FXML
	private Button withdrawBtn;
	@FXML
	private Button depositBtn;
	
	private String id;
	public void setID(String id) throws SQLException {
		this.id = id;
	}
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
	@FXML
	private Label name;
	public void displayName(String firstName) {
		name.setText(firstName);
	}
	
	// for when a credit card is entered
	public void changeToCreditInterface() {
		leftVbox.getChildren().remove(transferBtn);
		withdrawBtn.setText("Get Cash");
		depositBtn.setText("Make Payment");
	}
	
	FXMLLoader loader;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// for withdraw/get cash button
	@FXML
	public void withdrawAction(ActionEvent event) throws SQLException, IOException {
		loader = new FXMLLoader(getClass().getResource("Withdraw.fxml"));
		root = loader.load(); 
		
		WithdrawController withdrawController = loader.getController();
    	withdrawController.setID(id);     
    	
    	scene = new Scene(root);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	// for deposit/make payment button
	@FXML
	public void depositAction(ActionEvent event) throws SQLException, IOException {
		loader = new FXMLLoader(getClass().getResource("Deposit.fxml"));
		root = loader.load(); 
		
		DepositController depositController = loader.getController();
		depositController.setID(id);     
    	
    	scene = new Scene(root);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	// for transfer button
	@FXML
	public void transferAction(ActionEvent event) throws SQLException, IOException {
		loader = new FXMLLoader(getClass().getResource("TransferAmount.fxml"));
		root = loader.load(); 
		
		TransferAmountController transferAmountController = loader.getController();
		transferAmountController.setID(id);     
    	
    	scene = new Scene(root);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	// for balance button
	@FXML
	public void balanceAction(ActionEvent event) throws SQLException, IOException {
		connection = DBConnect.getConnect();
    	// Search the acc with accID, store the acc in a CheckingAccount instance
    	query = "SELECT * FROM atm.checking_account WHERE `acc_id` = " + id;
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        CheckingAccount acc = new CheckingAccount(Integer.parseInt(id), resultSet.getDouble("balance"));
		loader = new FXMLLoader(getClass().getResource("Balance.fxml"));
		root = loader.load(); 		
		BalanceController balanceController = loader.getController();
		balanceController.setAccInfor(id, acc.getBalance());     
    	
    	scene = new Scene(root);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	// for transactions button
	@FXML
	public void viewTransactions(ActionEvent event) throws SQLException, IOException {
		loader = new FXMLLoader(getClass().getResource("Transactions.fxml"));
		root = loader.load(); 
		
		TransactionController transactionController = loader.getController();
		transactionController.setID(id);     
		transactionController.loadDate();
    	
    	scene = new Scene(root);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	// to log out
	@FXML
	public void cancelAction(ActionEvent event) throws SQLException, IOException {
		loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		root = loader.load(); 
    	
    	scene = new Scene(root);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
}