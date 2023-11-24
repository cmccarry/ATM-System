package UI_Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import helper.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Transaction;

public class TransactionController implements Initializable 
{
    @FXML
    Button refresh;
    @FXML
    Label test;
	@FXML
    private TableView<Transaction> table;
    @FXML
    private TableColumn<Transaction, String> dateCol;
    @FXML
    private TableColumn<Transaction, String> typeCol;
    @FXML
    private TableColumn<Transaction, String> amountCol;
    @FXML
    private TableColumn<Transaction, String> balanceCol;
    private String id;
    
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Transaction transaction = null ;
    
    ObservableList<Transaction>  transactionList = FXCollections.observableArrayList();
	// Keep track the accID
    public void setID(String id) throws SQLException {
		this.id = id;
	}
	// Run when opening the page
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDate();
    }
    // Get the data from the database
    @FXML
    private void refreshTable() {
        try  {
        	test.setText(id);
        	transactionList.clear();
            // Select all transactions with the id
            query = "SELECT * FROM transaction WHERE checking_id = ? ORDER BY `trans_num` DESC";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
            	transactionList.add(new  Transaction(
                        resultSet.getDate("trans_date"),
                        resultSet.getString("trans_type"),
                        resultSet.getString("amount"),
                        resultSet.getDouble("history")));
            }  
            table.setItems(transactionList);
        } 
        catch (SQLException ex) { // Handle exception
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        }                        
    }
    // Display recent transactions on the table
    public void loadDate() {
        connection = DBConnect.getConnect(); // Connect the database       
        refreshTable();
        // Display data on the columns
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        balanceCol.setCellValueFactory(data -> data.getValue().balanceProperty());
    }
    
    @FXML
	public void cancelAction(ActionEvent event) throws SQLException, IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
    	Parent root = loader.load(); 
		
		// to add name of account to the main menu page
    	preparedStatement = connection.prepareStatement("SELECT `cus_num` FROM atm.checking_account WHERE `acc_id` = " + String.valueOf(id));
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