package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart){
            allParts.add(newPart);
    }

    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    public static boolean isNumeric(String number){
        try{
            int i = Integer.parseInt(number);
        } catch (NumberFormatException | NullPointerException nfe){
            return false;
        }

        return true;
    }

    public static Part lookupPart(int partId){

        for(int x = 0; x<allParts.size(); x++){
            if (partId == allParts.get(x).getId()){
                return allParts.get(x);
            }
        }
        return null;
    }

    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> locatedParts = FXCollections.observableArrayList();
        boolean checkNumeric;

        for(int x = 0; x<allParts.size(); x++){
            if (partName.equals(allParts.get(x).getName())){
                locatedParts.add(allParts.get(x));
            }
        }
        checkNumeric = isNumeric(partName);
        if(checkNumeric){
            int potentialPartID = Integer.parseInt(partName);
            if(lookupPart(potentialPartID) != null) {
                locatedParts.add(lookupPart(potentialPartID));
            }
        }

        return locatedParts;
    }

    public static Product lookupProduct(int productId){
        for(int x = 0; x<allProducts.size(); x++){
            if (productId == allProducts.get(x).getId()){
                return allProducts.get(x);
            }
        }

        return null;
    }

    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> locatedProducts = FXCollections.observableArrayList();
        boolean checkNumeric;

        for(int x = 0; x<allProducts.size(); x++){
            if (productName.equals(allProducts.get(x).getName())){
                locatedProducts.add(allProducts.get(x));
            }
        }
        checkNumeric = isNumeric(productName);
        if(checkNumeric){
            int potentialProductID = Integer.parseInt(productName);
            if(lookupProduct(potentialProductID) != null) {
                locatedProducts.add(lookupProduct(potentialProductID));
            }
        }

        return locatedProducts;
    }

    public static void updatePart(int index, Part updatedPart){
        allParts.set(index, updatedPart);
    }

    public static void updateProduct(int index, Product updatedProduct){
        allProducts.set(index, updatedProduct);
    }

    public static void deletePart(Part selectedPart){
        allParts.remove(selectedPart);
    }

    public static void deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
