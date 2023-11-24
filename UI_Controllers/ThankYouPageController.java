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

public class ThankYouPageController {
	private String id;
		public void setID(String id) throws SQLException {
		this.id = id;
	}

	// database variables
	String query = null;
	Connection connection = null ;
	PreparedStatement preparedStatement = null ;
	ResultSet resultSet = null ;
	
	@FXML
	private void backAction(ActionEvent event) throws SQLException, IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
    	Parent root = loader.load(); 
    	
		// to add name of account to the main menu page
    	/*
    	Connection connection = DBConnect.getConnect(); 
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT `full_name` FROM atm.customer");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String fullNameString = resultSet.getString("full_name");
        String[] fullName = fullNameString.split(" ");
    	String firstName = fullName[0];
    	MainMenuController mainMenuController = loader.getController();
    	mainMenuController.displayName(firstName); */
    	connection = DBConnect.getConnect();
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
	
}