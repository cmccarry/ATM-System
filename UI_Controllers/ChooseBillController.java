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

public class ChooseBillController {
	String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    private int totalAmount;
	private int currentAmount = 0;
	// Bill array, index from  0  ,  1  , 2,   3,      4
	//  Coresspond:          one, five, ten, twenty, hundred bill
	private int[] billArray = {0, 0, 0, 0, 0};
	// Infor for receipt
	private String id;
	private String date;
	private	String transType;
	private String amount;
	private String balance;
	public void setInfor(String id, int totalAmount) throws SQLException {
		this.id = id;
		this.totalAmount = totalAmount;
		messageLabel.setText(String.format("Choose Bills for $%d?", totalAmount));
	}
	@FXML
	Label messageLabel;
	@FXML
	Label currentAmountLabel;
	@FXML
	Label oneQuantityLabel;
	@FXML
	Label fiveQuantityLabel;
	@FXML
	Label tenQuantityLabel;
	@FXML
	Label twentyQuantityLabel;
	@FXML
	Label hundredQuantityLabel;
	@FXML
	Button onePlus;
	@FXML
	Button oneMinus;
	@FXML
	Button fivePlus;
	@FXML
	Button fiveMinus;
	@FXML
	Button tenPlus;
	@FXML
	Button tenMinus;
	@FXML
	Button twentyPlus;
	@FXML
	Button twentyMinus;
	@FXML
	Button hundredPlus;
	@FXML
	Button hundredMinus;
	@FXML
	Button continueBtn;
	
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
		double withdrawAmount = totalAmount;
		String amountStr = String.format("%.2f", withdrawAmount);	
		connection = DBConnect.getConnect();
		// Get balance
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
        if (debit) acc.withdraw(withdrawAmount);
        else if (credit) acc.getCash(withdrawAmount);
        String newBalanceStr = String.valueOf(acc.getBalance());
    	query = "UPDATE `checking_account` SET `balance`= " + newBalanceStr + "WHERE `acc_id` = " + id;
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
        // Create record of transaction
        query = "INSERT INTO `transaction`(`trans_date`, `trans_type`, `amount`, `history`, `checking_id`) VALUES (?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        
        String typeStr = "";
        String recordAmountStr = "";
        if (debit) {
        	typeStr = "Withdraw";
        	recordAmountStr = "-" + amountStr;
        }
        else if (credit) {
        	typeStr = "Get Cash";
        	recordAmountStr = "+" + amountStr;
        }
        preparedStatement.setString(1, String.valueOf(date));
        preparedStatement.setString(2, typeStr);
        preparedStatement.setString(3, recordAmountStr);
        preparedStatement.setString(4, newBalanceStr);
        preparedStatement.setString(5, id);
        preparedStatement.executeUpdate();
        // Deduct bills in database
    	query = "SELECT * FROM bill";
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        int i = 0;
        int billID = 1;
    	while (resultSet.next()) {
    		if (billArray[i] > 0) {
    			int newQuantity = resultSet.getInt("quantity") - billArray[i];
    			query = "UPDATE `bill` SET `quantity` = " + "'" + String.valueOf(newQuantity) + "'" +
    					"WHERE `bill_id` = '" + String.valueOf(billID) + "'";
    	        preparedStatement = connection.prepareStatement(query);
    	        preparedStatement.executeUpdate();
    		}
    		++billID;
    		++i;
    	}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("PrintReceipt.fxml"));
    	Parent root = loader.load(); 
    	
    	PrintReceiptController printReceiptController = loader.getController();
    	this.date = String.valueOf(date);
    	this.transType = typeStr;
    	double amountForReceipt = totalAmount;
    	this.amount = String.format("$%.2f", amountForReceipt);
    	this.balance = String.format("$%.2f", acc.getBalance());
    	printReceiptController.receiptInfor(this.date, this.id, this.transType, this.amount , this.balance);  
    	
    	Scene scene = new Scene(root);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	public void onePlusAction() {
		currentAmount += 1;
		setLabel();
		billArray[0] = billArray[0] + 1;
		oneQuantityLabel.setText(String.format("%d", billArray[0]));
		if (billArray[0] > 0) oneMinus.setDisable(false);
		setDisablePlus();
	}
	@FXML
	public void oneMinusAction() {
		currentAmount -= 1;
		setLabel();
		billArray[0] = billArray[0] - 1;
		oneQuantityLabel.setText(String.format("%d", billArray[0]));
		setEnablePlus();
		if (billArray[0] == 0) oneMinus.setDisable(true);
	}
	@FXML
	public void fivePlusAction() {
		currentAmount += 5;
		setLabel();
		billArray[1] = billArray[1] + 1;
		fiveQuantityLabel.setText(String.format("%d", billArray[1]));
		if (billArray[1] > 0) fiveMinus.setDisable(false);
		setDisablePlus();
	}
	@FXML
	public void fiveMinusAction() {
		currentAmount -= 5;
		setLabel();
		billArray[1] = billArray[1] - 1;
		fiveQuantityLabel.setText(String.format("%d", billArray[1]));
		setEnablePlus();
		if (billArray[1] == 0) fiveMinus.setDisable(true);
	}
	@FXML
	public void tenPlusAction() {
		currentAmount += 10;
		setLabel();
		billArray[2] = billArray[2] + 1;
		tenQuantityLabel.setText(String.format("%d", billArray[2]));
		if (billArray[2] > 0) tenMinus.setDisable(false);
		setDisablePlus();
	}
	@FXML
	public void tenMinusAction() {
		currentAmount -= 10;
		setLabel();
		billArray[2] = billArray[2] - 1;
		tenQuantityLabel.setText(String.format("%d", billArray[2]));
		setEnablePlus();
		if (billArray[2] == 0) tenMinus.setDisable(true);
	}
	@FXML
	public void twentyPlusAction() {
		currentAmount += 20;
		setLabel();
		billArray[3] = billArray[3] + 1;
		twentyQuantityLabel.setText(String.format("%d", billArray[3]));
		if (billArray[3] > 0) twentyMinus.setDisable(false);
		setDisablePlus();
	}
	@FXML
	public void twentyMinusAction() {
		currentAmount -= 20;
		setLabel();
		billArray[3] = billArray[3] - 1;
		twentyQuantityLabel.setText(String.format("%d", billArray[3]));
		setEnablePlus();
		if (billArray[3] == 0) twentyMinus.setDisable(true);
	}
	@FXML
	public void hundredPlusAction() {
		currentAmount += 100;
		setLabel();
		billArray[4] = billArray[4] + 1;
		hundredQuantityLabel.setText(String.format("%d", billArray[4]));
		if (billArray[4] > 0) hundredMinus.setDisable(false);
		setDisablePlus();
	}
	@FXML
	public void hundredMinusAction() {
		currentAmount -= 100;
		setLabel();
		billArray[4] = billArray[4] - 1;
		hundredQuantityLabel.setText(String.format("%d", billArray[4]));
		setEnablePlus();
		if (billArray[4] == 0) hundredMinus.setDisable(true);
	}
	@FXML
	public void resetAction() {
		currentAmount = 0;
		billArray[0] = 0;
		billArray[1] = 0;
		billArray[2] = 0;
		billArray[3] = 0;
		billArray[4] = 0;
		oneQuantityLabel.setText(String.valueOf(0));
		fiveQuantityLabel.setText(String.valueOf(0));
		tenQuantityLabel.setText(String.valueOf(0));
		twentyQuantityLabel.setText(String.valueOf(0));
		hundredQuantityLabel.setText(String.valueOf(0));
		currentAmountLabel.setText("$0");
		setDisableMinus();
	}
	// Set money label, set disable continue button, only allow continue when the customer
	public void setLabel() {
		
		currentAmountLabel.setText(String.format("$%d", currentAmount));
		if (currentAmount == totalAmount) {
			continueBtn.setDisable(false);
		} else {
			continueBtn.setDisable(true);
		}
	}
	
	public void setDisablePlus() {
		if (currentAmount + 1 > totalAmount) onePlus.setDisable(true);
		if (currentAmount + 5 > totalAmount) fivePlus.setDisable(true);
		if (currentAmount + 10 > totalAmount) tenPlus.setDisable(true);
		if (currentAmount + 20 > totalAmount) twentyPlus.setDisable(true);
		if (currentAmount + 100 > totalAmount) hundredPlus.setDisable(true);
	}
	public void setEnablePlus() {
		if (currentAmount + 1 <= totalAmount) onePlus.setDisable(false);
		if (currentAmount + 5 <= totalAmount) fivePlus.setDisable(false);
		if (currentAmount + 10 <= totalAmount) tenPlus.setDisable(false);
		if (currentAmount + 20 <= totalAmount) twentyPlus.setDisable(false);
		if (currentAmount + 100 <= totalAmount) hundredPlus.setDisable(false);
	}
	public void setDisableMinus() {
		if (billArray[0] == 0) oneMinus.setDisable(true);
		if (billArray[1] == 0) fiveMinus.setDisable(true);
		if (billArray[2] == 0) tenMinus.setDisable(true);
		if (billArray[3] == 0) twentyMinus.setDisable(true);
		if (billArray[4] == 0) hundredMinus.setDisable(true);
		
	}
}