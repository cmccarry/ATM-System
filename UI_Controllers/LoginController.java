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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField numberField;
	
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    
    // Action when clicking login
	@FXML
	private void loginAction(ActionEvent event) throws SQLException, IOException {
		String cardNumberStr = numberField.getText();
		int card_num = Integer.valueOf(cardNumberStr);
		// Check card number, 401-499 is debit cards
		if (card_num > 400 && card_num < 500) {
			// Open Passcode page
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PassCodePage.fxml"));
	    	Parent root = loader.load();
	    	// Send card number infor to PasscodePage to keep track
	    	PasscodeController passcodeController = loader.getController();
	    	passcodeController.setCardNumber(cardNumberStr);
	    	
	        Scene scene = new Scene(root);
	        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        stage.setScene(scene);
	        stage.show();
		}
		// It's a credit card, no passcode needed
		else if (card_num > 500 && card_num < 600) {
			connection = DBConnect.getConnect();
	        preparedStatement = connection.prepareStatement("SELECT `acc_id` FROM atm.credit_card WHERE `card_num` = " + cardNumberStr);
	        resultSet = preparedStatement.executeQuery();
	        resultSet.next();
	        // Get the accID linked to the card
	        int acc_id = resultSet.getInt("acc_id");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml")); // Set to pen MainMenu page
	    	Parent root = loader.load();
	    	// Send account infor to MainMenu page
	    	MainMenuController mainMenuController = loader.getController();
	    	mainMenuController.setID(String.valueOf(acc_id));
	    	mainMenuController.changeToCreditInterface(); // Credit Interface
	    	
		// to add name of account to the main menu page
	    	preparedStatement = connection.prepareStatement("SELECT `cus_num` FROM atm.checking_account WHERE `acc_id` = " + String.valueOf(acc_id));
	        resultSet = preparedStatement.executeQuery();
	        resultSet.next();
	        String cus_num = String.valueOf(resultSet.getInt("cus_num"));
	        
	    	preparedStatement = connection.prepareStatement("SELECT `full_name` FROM atm.customer WHERE `customer_num` = " + cus_num);
	        resultSet = preparedStatement.executeQuery();
	        resultSet.next();
	        String fullNameString = resultSet.getString("full_name");
	        String[] fullName = fullNameString.split(" ");
	    	String firstName = fullName[0];
	    	mainMenuController.displayName(firstName);
	    	
	        Scene scene = new Scene(root);
	        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        stage.setScene(scene);
	        stage.show();
	        preparedStatement.close();
	        resultSet.close();
		}
	}
}
