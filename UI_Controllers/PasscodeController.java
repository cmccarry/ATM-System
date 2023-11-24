package UI_Controllers;

import javafx.fxml.FXML;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import helper.DBConnect;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Timeline;


public class PasscodeController {
	// fxml variables
	@FXML
	private Label label;
	@FXML
	private PasswordField passField;
	@FXML
	private Button continueButton;
	FXMLLoader loader;
	private Stage stage;
	private Scene scene;
	private Parent root;
	

	// class variables
	private String cardNumber;
	private int count = 0;

	// method to set card number
	public void setCardNumber(String cardNumber) throws SQLException {
		this.cardNumber = cardNumber;
	}

	// database variables
	String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    
	@FXML
	private void loginAction(ActionEvent event) throws SQLException, IOException {
		// get entered pin and connect to database
		String pin = passField.getText();
		connection = DBConnect.getConnect();  

		// Search card num and pin in database
        query = "SELECT `acc_id` FROM atm.debit_card WHERE `card_num` = ? and `pin_num` = ?";
    	preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, cardNumber);
        preparedStatement.setString(2, pin);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) { // if card with that pin exists
			// load MainMenu.fxml
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml")); 
        	Parent root = loader.load();

        	// Send account information to MainMenu
        	MainMenuController mainMenuController = loader.getController();
        	mainMenuController.setID(String.valueOf(resultSet.getInt("acc_id")));
        	
        	preparedStatement = connection.prepareStatement("SELECT `cus_num` FROM atm.checking_account WHERE `acc_id` = " + String.valueOf(resultSet.getInt("acc_id")));
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

        else { // Wrong passcode
        	label.setText("Wrong passcode, please try again.");
        	label.setStyle("-fx-font-size: 15px");
        	++count;
        }
        System.out.println(String.valueOf(count));
		
        if (count == 3) { // Wrong 3 times
        	label.setText("You exceeded the limit. Goodbye!");

            // Use a Timeline to create a delay
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ActionEvent -> {
                try {
                	loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            		root = loader.load(); 
                	
                	scene = new Scene(root);
                	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            		stage.setScene(scene);
            		stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }));

            // Start the Timeline
            timeline.play();

        }
	} // end loginAction method

	// methods to store number button presses
	@FXML
	public void pressNum1() {
		passField.setText(passField.getText() + "1");
	}
	@FXML
	public void pressNum2() {
		passField.setText(passField.getText() + "2");
	}
	@FXML
	public void pressNum3() {
		passField.setText(passField.getText() + "3");
	}
	@FXML
	public void pressNum4() {
		passField.setText(passField.getText() + "4");
	}
	@FXML
	public void pressNum5() {
		passField.setText(passField.getText() + "5");
	}
	@FXML
	public void pressNum6() {
		passField.setText(passField.getText() + "6");
	}
	@FXML
	public void pressNum7() {
		passField.setText(passField.getText() + "7");
	}
	@FXML
	public void pressNum8() {
		passField.setText(passField.getText() + "8");
	}
	@FXML
	public void pressNum9() {
		passField.setText(passField.getText() + "9");
	}
	@FXML
	public void pressNum0() {
		passField.setText(passField.getText() + "0");
	}

	// method for backspace button press
	@FXML
	public void pressBackspace() {
		String current = passField.getText();
		if (!current.isEmpty())
		current = current.substring(0, current.length() - 1);
		passField.setText(current);
	}

	// method for clear button press
	@FXML
	public void pressClear() {
		passField.setText("");
	}
}