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
//import static sample.InventoryController;
import static sample.InventoryController.returnPartToModify;
import sample.Part;
import sample.InHouse;
import sample.Outsourced;


public class ModifyPartController implements Initializable {

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

    private final Part partToModify; //this is the part that will be modified --> data filled by constructor method call
    private final int partToModifyIndex;

    public ModifyPartController(){
        this.partToModify = InventoryController.returnPartToModify();
        this.partToModifyIndex = InventoryController.returnPartToModifyIndex();
    }

    public void initialize(URL url, ResourceBundle rb){
        partID.setText(Integer.toString(partToModify.getId()));
        partName.setText(partToModify.getName());
        inventory.setText(Integer.toString(partToModify.getStock()));
        price.setText(Double.toString(partToModify.getPrice()));
        minimum.setText(Integer.toString(partToModify.getMin()));
        maximum.setText(Integer.toString(partToModify.getMax()));

        if (partToModify instanceof InHouse) { //this is where we do all things related to figuring out if it is inhouse or outsourced
            machineID.setText(Integer.toString(((InHouse) partToModify).getMachineId()));
            inHouse.setSelected(true); //set radio button

        }
        else if (partToModify instanceof Outsourced) {
            companyName.setText(((Outsourced) partToModify).getCompanyName());
            outSourced.setSelected(true); //set radio button
        }
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
            partToModify.setId(id);
            partToModify.setName(name);
            partToModify.setStock(partInventory);
            partToModify.setPrice(partPrice);
            partToModify.setMin(partMin);
            partToModify.setMax(partMax);
            int mid = Integer.parseInt(machineID.getText());
            ((InHouse) partToModify).setMachineId(mid); //to get to inhouse class methods
            Inventory.updatePart(partToModifyIndex, partToModify);
            returnToMainScreen(event);
        }

        else if (isOutSourced){
            partToModify.setId(id);
            partToModify.setName(name);
            partToModify.setStock(partInventory);
            partToModify.setPrice(partPrice);
            partToModify.setMin(partMin);
            partToModify.setMax(partMax);
            ((Outsourced) partToModify).setCompanyName(comName); //to get to outsourced class methods
            Inventory.updatePart(partToModifyIndex, partToModify);
            returnToMainScreen(event);
        }
    }


    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modification");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel the modification of the part?");
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
