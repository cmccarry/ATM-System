package UI_Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import helper.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PrintReceiptController {
	private String id;
	private String date;
	private	String transType;
	private String amount;
	private String balance;
	public void receiptInfor(String date, String accID, String transType,
			String amount, String balance) {
		this.date = date;
		this.id = accID;
		this.transType = transType;
		this.amount = amount;
		this.balance = balance;
	}
    
	// user does not want receipt
	@FXML
	public void noAction(ActionEvent event) throws SQLException, IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ThankYouPage.fxml"));
    	Parent root = loader.load(); 
		
		
    	ThankYouPageController thankYouPageController = loader.getController();
    	thankYouPageController.setID(id);
    	
    	Scene scene = new Scene(root);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	// user wants receipt
	@FXML
	public void yesAction(ActionEvent event) throws SQLException, IOException {
        // Go to Receipt interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Receipt.fxml"));
        Parent root = loader.load(); 
		
        ReceiptController receiptController = loader.getController();
        receiptController.receiptInfor(this.date, this.id, this.transType, this.amount, this.balance);     
    	
    	Scene scene = new Scene(root);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
}