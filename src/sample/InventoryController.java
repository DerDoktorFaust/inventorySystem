package sample;

//import com.sun.tools.javac.Main;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.io.IOException;
import javafx.event.ActionEvent;
//import java.awt.event.ActionEvent;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Part;
import sample.Product;
import static sample.Inventory.getAllParts; //added to avoid referencing from static context
import static sample.Inventory.getAllProducts;


public class InventoryController implements Initializable {

    @FXML
    private TableView<Part> MainPartsTableView;
    @FXML
    private TableColumn<Part, Integer> mainPartIDColumn;
    @FXML
    private TableColumn<Part, String> mainPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> mainPartInventoryColumn;
    @FXML
    private TableColumn<Part, Double> mainPartPriceColumn;

    private static Part partToModify; //this is the part selected that will then be modified (or deleted)
    private static int partToModifyIndex; //this is the index of the part to be modified or deleted

    @FXML
    private TableView<Product> MainProductsTableView;
    @FXML
    private TableColumn<Product, Integer> mainProductIDCol;
    @FXML
    private TableColumn<Product, String> mainProductNameCol;
    @FXML
    private TableColumn<Product, Integer> mainProductInvLvlCol;
    @FXML
    private TableColumn<Product, Double> mainProductPriceCol;

    private static Product productToModify; //this is the product selected that will then be modified (or deleted)
    private static int productToModifyIndex; //this is the index of the product to be modified or deleted

    @FXML
    private TextField partsSearchField;
    @FXML
    private TextField productSearchField;

    public InventoryController(){

    }

    public void initialize(URL url, ResourceBundle rb){

        productToModify=null; //these reset the references each time so that "modify" data doesn't flow over if user next clicks add
        partToModify=null; //otherwise, data will carry over in form fields
        mainPartIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        mainPartNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        mainPartInventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        mainPartPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        populatePartsTable();

        mainProductIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        mainProductNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        mainProductInvLvlCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        mainProductPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        populateProductsTable();
    }

    @FXML
    public void handlePopulateTestData(ActionEvent event){
        InHouse newInHousePart = new InHouse(33, "Widget", 2.0, 45, 5, 56, 3243);
        Inventory.addPart(newInHousePart);
        InHouse newInHousePart2 = new InHouse(54, "Cable", 4.0, 60, 5, 75, 3244);
        Inventory.addPart(newInHousePart2);
        InHouse newInHousePart3 = new InHouse(89, "Bracket", 5.0, 60, 5, 75, 3249);
        Inventory.addPart(newInHousePart3);
        Outsourced newOutsourcedPart = new Outsourced(34, "Screws", 1.0, 34, 5, 200, "Acme");
        Inventory.addPart((newOutsourcedPart));

        Product newProduct = new Product(11, "Hard Drive", 50, 15, 5, 50);
        newProduct.addAssociatedPart(newInHousePart);
        newProduct.addAssociatedPart(newInHousePart2);
        Product newProduct2 = new Product(13, "Computer Case", 60, 15, 5, 50);
        newProduct2.addAssociatedPart(newInHousePart3);
        Product newProduct3 = new Product(15, "Monitor", 40, 15, 5, 50);
        newProduct3.addAssociatedPart(newOutsourcedPart);
        Inventory.addProduct(newProduct);
        Inventory.addProduct(newProduct2);
        Inventory.addProduct(newProduct3);
    }

    public void populatePartsTable() {
        MainPartsTableView.setItems(getAllParts());
    }

    public void populateProductsTable(){
        MainProductsTableView.setItems(getAllProducts());
    }

    public static Part returnPartToModify(){ //need this method in order to access variable in ModifyPartController
        return partToModify;
    }

    public static int returnPartToModifyIndex(){ //need this method in order to access variable in ModifyPartController
        return partToModifyIndex;
    }

    public static Product returnProductToModify(){
        return productToModify;
    }

    public static int returnProductToModifyIndex(){
        return productToModifyIndex;
    }

    @FXML
    public void showAddPartDialog(ActionEvent event) throws IOException{
        Parent addParts = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(addParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        partToModify = MainPartsTableView.getSelectionModel().getSelectedItem();
        partToModifyIndex = getAllParts().indexOf(partToModify);

        Parent modifyParts = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
        Scene scene = new Scene(modifyParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void handleDeletePart(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Deletion");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete the part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            partToModify = MainPartsTableView.getSelectionModel().getSelectedItem();
            Inventory.deletePart(partToModify);
        }
    }

    @FXML
    void handlePartSearch(ActionEvent event) {
        String query = partsSearchField.getText();
        ObservableList<Part> locatedParts = FXCollections.observableArrayList();
        locatedParts = Inventory.lookupPart(query);
        MainPartsTableView.setItems(locatedParts);
    }

    @FXML
    void handleClearPartsSearch(ActionEvent event) {
        populatePartsTable();
    }

    @FXML
    void handleProductSearch(ActionEvent event){
        String query = productSearchField.getText();
        ObservableList<Product> locatedProducts = FXCollections.observableArrayList();
        locatedProducts = Inventory.lookupProduct(query);
        MainProductsTableView.setItems(locatedProducts);
    }

    @FXML
    void handleClearProductsSearch(ActionEvent event) {
        populateProductsTable();
    }

    @FXML
    public void handleAddProductDialog(ActionEvent event) throws IOException{
        Parent addProduct = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void handleModifyProduct(ActionEvent event) throws IOException {
        productToModify = MainProductsTableView.getSelectionModel().getSelectedItem();
        productToModifyIndex = getAllProducts().indexOf(productToModify);

        Parent addProduct = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    @FXML
    public void handleDeleteProduct(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete the product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            productToModify = MainProductsTableView.getSelectionModel().getSelectedItem();
            Inventory.deleteProduct(productToModify);
        }
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }



}
