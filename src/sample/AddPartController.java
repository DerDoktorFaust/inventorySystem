package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import sample.Inventory;
import sample.StartProgram;


public class AddPartController implements Initializable {

    //Declare all the variables that are on AddPart.fxml so they can be parsed later

    @FXML
    private TextField partID;
    @FXML
    private TextField partName;
    @FXML
    private TextField inventory;
    @FXML
    private TextField price;
    @FXML
    private TextField minimum;
    @FXML
    private TextField maximum;
    @FXML
    private TextField companyName;
    @FXML
    private TextField machineID;
    @FXML
    private RadioButton inHouse;
    @FXML
    private RadioButton outSourced;



    public AddPartController(){

    }
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    void handleSave(ActionEvent event) throws IOException {
        // Get data from the GUI
        int id = Integer.parseInt(partID.getText());
        String name = partName.getText();
        int partInventory = Integer.parseInt(inventory.getText());
        double partPrice = Double.parseDouble(price.getText());
        int partMin = Integer.parseInt(minimum.getText());
        int partMax = Integer.parseInt(maximum.getText());
        String comName = companyName.getText();
        int mid = Integer.parseInt(machineID.getText());
        boolean isInHouse = inHouse.isSelected();
        boolean isOutSourced = outSourced.isSelected();

        if(partInventory<partMin || partInventory>partMax) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Please be aware that you have set current inventory levels either below the part's minimum or above it's maximum.");
            Optional<ButtonType> result = alert.showAndWait();
        }


        if(isInHouse){
            InHouse newInHousePart = new InHouse(id, name, partPrice, partInventory, partMin, partMax, mid);
            Inventory.addPart(newInHousePart);
            returnToMainScreen(event);
        }

        else if (isOutSourced){
            Outsourced newOutSourcedPart = new Outsourced(id, name, partPrice, partInventory, partMin, partMax, comName);
            Inventory.addPart(newOutSourcedPart);
            returnToMainScreen(event);
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Part Addition");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Do you really wish to cancel the addition of the part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            returnToMainScreen(event);
        }
    }

    public void returnToMainScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
