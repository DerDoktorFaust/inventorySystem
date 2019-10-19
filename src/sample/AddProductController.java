package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Inventory;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import sample.Product;
import javax.swing.*;
import static sample.Inventory.getAllParts;
import static sample.Inventory.getAllProducts;
import static sample.InventoryController.returnProductToModify;
import static sample.InventoryController.returnProductToModifyIndex;


public class AddProductController implements Initializable {

    private ObservableList<Part> partsAdded = FXCollections.observableArrayList();


    @FXML
    private TextField productIDField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productInventoryField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField productMinimumField;
    @FXML
    private TextField productMaximumField;

    @FXML
    private TableView<Part> ProductPartstoAddTableView;
    @FXML
    private TableColumn<Part, Integer> toAddPartIDCol;
    @FXML
    private TableColumn<Part, String> toAddPartNameCol;
    @FXML
    private TableColumn<Part, Integer> toAddInvLvlCol;
    @FXML
    private TableColumn<Part, Double> toAddPricePerUnitCol;

    @FXML
    private TableView<Part> ProductPartsAddedTableView;

    @FXML
    private TableColumn<Part, Integer> addedPartIDCol;
    @FXML
    private TableColumn<Part, String> addedPartNameCol;
    @FXML
    private TableColumn<Part, Integer> addedInvLvlCol;
    @FXML
    private TableColumn<Part, Double> addedPricePerUnitCol;

    @FXML
    private TextField partsSearchField;


    public AddProductController() { //constructor
            //partsAdded = null;
    }

    public void initialize(URL url, ResourceBundle rb) {

        toAddPartIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        toAddPartNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        toAddInvLvlCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        toAddPricePerUnitCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        populatePartsToAddTable();

        addedPartIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        addedPartNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        addedInvLvlCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        addedPricePerUnitCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        populatePartsAddedTable();


    }
    public void populatePartsToAddTable() {
        ProductPartstoAddTableView.setItems(getAllParts());
    }

    public void populatePartsAddedTable() {
        ProductPartsAddedTableView.setItems(partsAdded);
    }

    @FXML
    public void handleAdd(ActionEvent event) { //this is for adding parts
        Part part = ProductPartstoAddTableView.getSelectionModel().getSelectedItem();
        partsAdded.add(part);
        populatePartsAddedTable();
    }

    @FXML
    public void handleDelete(ActionEvent event) throws IOException { //this is for deleting parts already added
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Deletionn");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete the part from the product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            partsAdded.remove(ProductPartsAddedTableView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void handlePartSearch(ActionEvent event) {
        String query = partsSearchField.getText();
        ObservableList<Part> locatedParts = FXCollections.observableArrayList();
        locatedParts = Inventory.lookupPart(query);
        ProductPartstoAddTableView.setItems(locatedParts);
    }

    @FXML
    void handleClearSearch(ActionEvent event) {
        populatePartsToAddTable();
    }

    @FXML
    public void handleSave(ActionEvent event) throws IOException {

            int id = Integer.parseInt(productIDField.getText());
            String name = productNameField.getText();
            int productInventory = Integer.parseInt(productInventoryField.getText());
            double productPrice = Double.parseDouble(productPriceField.getText());
            int productMin = Integer.parseInt(productMinimumField.getText());
            int productMax = Integer.parseInt(productMaximumField.getText());

        if(productInventory<productMin || productInventory>productMax) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.NONE);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("Please be aware that you have set current inventory levels either below the product's minimum or above it's maximum.");
            Optional<ButtonType> result = alert.showAndWait();
        }

            Product newProduct = new Product(id, name, productPrice, productInventory, productMin, productMax);
            for(int x=0; x<partsAdded.size(); x++)
            {
                newProduct.addAssociatedPart(partsAdded.get(x));
            }
            Inventory.addProduct(newProduct);

            returnToMainScreen(event);

    }


    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Product Addition");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel the addition of the product?");
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