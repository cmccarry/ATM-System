package UI_Controllers;

import java.io.IOException;
import java.sql.SQLException;

import helper.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ReceiptController {	
	@FXML
	Label dateLabel;
	@FXML
	Label accLabel;
	@FXML
	Label typeLabel;
	@FXML
	Label amountLabel;
	@FXML
	Label balanceLabel;
	private String id;
	public void receiptInfor(String date, String acc, String transType,
			String amount, String balance) {
		this.id = acc;
		dateLabel.setText("Date: " + date);
		accLabel.setText("Account: " + acc);
		typeLabel.setText("Type: " + transType);
		amountLabel.setText("Amount: " + amount);
		balanceLabel.setText("Balance: " + balance);
	}
	@FXML
	public void doneAction(ActionEvent event) throws SQLException, IOException {
		// Go to thank you page
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ThankYouPage.fxml"));
        Parent root = loader.load();  
        ThankYouPageController thankYouPageController = loader.getController();
        thankYouPageController.setID(id);     
    	Scene scene = new Scene(root);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
}
