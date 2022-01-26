package controller;
/**
 * class ModifyProductController.java
 */

/**
 * @author Justin Simons
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class is the controller for the modifyProduct GUI
 * it's in charge of creating a new product to replace the old.
 */
public class ModifyProductController implements Initializable {

    @FXML
    private TextField modifyProductSearch;

    @FXML
    private TableView<Part> modifyProductTopTable;

    @FXML
    private TableColumn<Part, Integer> modifyProductTopTableId;

    @FXML
    private TableColumn<Part, String> modifyProductTopTableName;

    @FXML
    private TableColumn<Part, Integer> modifyProductTopTableInLev;

    @FXML
    private TableColumn<Part, Double> modifyProductTopTablePrice;

    @FXML
    private TableView<Part> modifyProductBottomTable;

    @FXML
    private TableColumn<Part, Integer> modifyProductBottomTableId;

    @FXML
    private TableColumn<Part, String> modifyProductBottomTableName;

    @FXML
    private TableColumn<Part, Integer> modifyProductBottomTableInLev;

    @FXML
    private TableColumn<Part, Double> modifyProductBottomTablePrice;

    @FXML
    private Button modifyProductAdd;

    @FXML
    private Button modifyProductSave;

    @FXML
    private Button modifyProductCancel;

    @FXML
    private Button modifyProductRemovePart;

    @FXML
    private TextField modifyProductId;

    @FXML
    private TextField modifyProductName;

    @FXML
    private TextField modifyProductInv;

    @FXML
    private TextField modifyProductPrice;

    @FXML
    private TextField modifyProductMax;

    @FXML
    private TextField modifyProductMin;

    private ObservableList<Part> partList = FXCollections.observableArrayList();
    private ObservableList<Part> partList2 = FXCollections.observableArrayList();
    private ObservableList<Part> partListSearch = FXCollections.observableArrayList();
    /**
     * modifyProductAddAction method adds a part from top table to bottom table.
     * if a part from the top table was not selected, it returns.
     * it checks if the selected part is already included in the bottom table list.
     *      if it's not, it adds that part to the bottom table without removing
     *      it from the top table.
     *      if the selected part has already been added, an error occurs stating "Part already added".
     *      it then returns.
     * @param event called when the add button is pressed.
     */
    @FXML
    void modifyProductAddAction(MouseEvent event) {
        Part addedPart = modifyProductTopTable.getSelectionModel().getSelectedItem();
        if(addedPart == null)
            return;
        if(partList2.contains(addedPart)){
            myAlert("Part already added");
            return;
        }
        partList2.add(addedPart);
        modifyProductBottomTable.setItems(partList2);
        modifyProductBottomTable.refresh();
    }
    /**
     * modifyProductCancelAction method is closes screen.
     * closes window. Anything the user put in text fields is ignored.
     * @param event called when user clicks the cancel button.
     */
    @FXML
    void modifyProductCancelAction(MouseEvent event) {
        Stage stage = (Stage) modifyProductCancel.getScene().getWindow();
        stage.hide();
    }
    /**
     * modifyProductRemovePartAction method removes a part from bottom table.
     * if no part from bottom table is selected, it returns.
     * if a part is selected, it is deleted from the bottom table, not affecting the top table.
     * a confirmation alert appears with the content text "You have successfully deleted the part:
     *      " + deletedPart.getName() + " from your new product " + modifyProductName.getText().trim()
     * it then updates the bottom table and refreshes it.
     * @param event called when remove part button is pressed.
     */
    @FXML
    void modifyProductRemovePartAction(MouseEvent event) {
        Part deletedPart = modifyProductBottomTable.getSelectionModel().getSelectedItem();
        if(deletedPart == null)
            return;
        modifyProductSearch.setText("");
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("You have successfully deleted the part: " + deletedPart.getName() + " from the product: "
         + Inventory.getModifyProduct().getName() + ".");
        a.show();
        partList2.remove(deletedPart);
        modifyProductBottomTable.setItems(partList2);
        modifyProductBottomTable.refresh();
    }
    /**
     * modifyProductSaveAction method saves the product to inventory.
     * before the input is saved, this method goes through multiple checks to make sure the info is correct.
     * It checks if every field is empty or not. if any are, it sends an error stating "Please fill out each field.".
     * It checks to see if only numbers were entered for price. if any letters were entered, an error appears
     *     stating "Please only input numbers for price". It then returns.
     * It checks to see if only numbers were entered for inv. if any letters were entered, an error appears
     *     stating "Please only input integers for Inventory".It then returns.
     * It checks to see if only numbers were entered for max. if any letters were entered, an error appears
     *     stating "Please only input integers for max". It then returns.
     * It checks to see if only numbers were entered for min. if any letters were entered, an error appears
     *     stating "Please only input integers for min". It then returns.
     * It checks to see if max is greater than min. if so, an error appears stating "Please make sure that
     *     the max is greater than your min.". It then returns.
     * It checks to see if inv greater than min. if not, an error appears stating
     *     "Please make sure that the Inventory is greater than your min.". It then returns.
     * It checks to see if inv less than max. if not, an error appears stating
     *     "Please make sure that the Inventory is less than your max.". It then returns.
     * It adds up the total cost of all associated parts
     * It checks to see if the price of the product is more than the total cost of its' associated parts.
     *      if the price is less than the cost of its' associated parts, an error appears stating
     *      "Please make sure your product price is greater than the total cost of added parts."
     * gets the index of the product
     * if no errors have occurred than, it creates a new product with the user input, adds the associated parts to it, and
     *      sends it to inventory.updateProduct() to replace the old product with the new updated one.
     * the screen is then closed.
     * @param event called when user clicks the save button.
     */
    @FXML
    void modifyProductSaveAction(MouseEvent event) {
        String productId = modifyProductId.getText().trim();
        String productName = modifyProductName.getText().trim();
        String productInv = modifyProductInv.getText().trim();
        String productPrice = modifyProductPrice.getText().trim();
        String productMax = modifyProductMax.getText().trim();
        String productMin = modifyProductMin.getText().trim();

        //Alert if any field is empty
        if(productName.isEmpty() || productInv.isEmpty() || productPrice.isEmpty()
                || productMax.isEmpty() || productMin.isEmpty()){
            myAlert("Please fill out each field.");
            return;
        }


        for(int i = 0; i < productPrice.length(); i++){
            char priceCh = (productPrice.charAt(i));
            if(Character.isLetter(priceCh) && !(priceCh == '.')){
                myAlert("Please only input numbers for price");
                return;
            }
        }

        for(int i = 0; i < productInv.length(); i++){
            char priceCh = (productInv.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Inventory");
                return;
            }
        }

        for(int i = 0; i < productMax.length(); i++){
            char priceCh = (productMax.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Max");
                return;
            }
        }

        for(int i = 0; i < productMin.length(); i++){
            char priceCh = (productMin.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Min");
                return;
            }
        }
        // check if max is greater than min or not
        if(Integer.parseInt(productMin) > Integer.parseInt(productMax)){
            myAlert("Please make sure that the max is greater than your min.");
            return;
        }
        //next two ifs make sure the inv is in between max and min
        if(Integer.parseInt(productInv) < Integer.parseInt(productMin)){
            myAlert("Please make sure that the Inventory is greater than your min.");
            return;
        }

        if(Integer.parseInt(productInv) > Integer.parseInt(productMax)){
            myAlert("Please make sure that the Inventory is less than your max.");
            return;
        }

        double totalPartPrice = 0.0;
        for (Part part : partList2) {
            totalPartPrice += part.getPrice();;
        }
        if ((Double.parseDouble(productPrice)) < totalPartPrice) {
            myAlert("Please make sure your product price is greater than the total cost of added parts.");
            return;
        }
        ObservableList<Product> productList = Inventory.getAllProducts();
        Product oldProduct = Inventory.getModifyProduct();
        boolean found = false;
        int productIndex = 0;
        for(int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId() == oldProduct.getId()) {
                productIndex = i;
                found = true;
                break;
            }
        }
        if(found) {
            Product tempProduct = new Product(Integer.parseInt(productId), productName, Double.parseDouble(productPrice),
                    Integer.parseInt(productInv), Integer.parseInt(productMin), Integer.parseInt(productMax));
            for (Part part : partList2) {
                tempProduct.addAssociatedParts(part);
            }
            Inventory.updateProduct(productIndex, tempProduct);
            Stage stage = (Stage) modifyProductSave.getScene().getWindow();
            stage.close();
        }
    }
    /**
     * modifyProductSearchAction method searches the top table for parts.
     * if the search field is empty, the top table gets reset with all parts.
     * if a number is entered, it searches if there is a part with the corresponding id number.
     *      if there is a part an id that matches the number entered, then all parts are
     *      removed from the top table except for the part found.
     * if a letter of number is typed, then it searches all parts.
     *      if any part includes the user input string in it's part name, then it appears in
     *      the top table, while all others are removed.
     * if no part id or name includes the user input, then an error appears stating "Part not found" then returns.
     * @param event called when user types a key on keyboard in the search field
     */
    @FXML
    void modifyProductSearchAction(KeyEvent event) {
        String searchSt = modifyProductSearch.getText().trim();
        if(searchSt.isEmpty()) {
            modifyProductTopTable.setItems(partList);
            modifyProductTopTable.refresh();
            return;
        }
        int check = 0;
        // check if searching for part by ID number
        if(searchSt.length() > 3)
            check = 1;
        if(Character.isDigit(searchSt.charAt(0)) && check == 0) {
            String tempSt = searchSt + "  ";
            if(Character.isDigit(tempSt.charAt(1)) || Character.isSpaceChar(tempSt.charAt(1))){
                if(Character.isDigit(tempSt.charAt(2)) || Character.isSpaceChar(tempSt.charAt(2))){
                    Part tempPart = Inventory.lookupPart(Integer.parseInt(searchSt));
                    if(!(tempPart == null)){
                        partListSearch.setAll(tempPart);
                        modifyProductTopTable.setItems(partListSearch);
                        modifyProductTopTable.refresh();
                        return;
                    }
                }
            }
        }
        if(Character.isLetterOrDigit(searchSt.charAt(0)) || check == 1) {
            ObservableList<Part> tempList = Inventory.lookupPart(searchSt);
            if(tempList == null){
                myAlert("Part not found");
                modifyProductSearch.setText("");
                modifyProductTopTable.setItems(partList);
                modifyProductTopTable.refresh();
                return;
            }
            partListSearch = tempList;
            modifyProductTopTable.setItems(partListSearch);
            modifyProductTopTable.refresh();
        }
        else {
            myAlert("Part not found");
        }
    }
    /**
     * myAlert method creates a new alert with AlertType ERROR.
     * it sets the content text.
     * shows the alert.
     * @param alert passes string object passed to it into setContentText for alert.
     */
    private void myAlert(String alert){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(alert);
        a.show();
    }
    /**
     * initialize method sets the tables and sets each text field with the appropriate data from the selected product
     * sets the text for id with the id from the selected product
     * sets the text for name with the name from the selected product
     * sets the text for inv with the inv from the selected product
     * sets the text for price with the price from the selected product
     * sets the text for max with the max from the selected product
     * sets the text for min with the min from the selected product
     * calls CreatePartTable() and CreatePartTable2().
     * @param url first called when screen opens.
     * @param rb first called when screen opens
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product newProduct = Inventory.getModifyProduct();
        modifyProductId.setText(String.valueOf(newProduct.getId()));
        modifyProductName.setText(newProduct.getName());
        modifyProductInv.setText(String.valueOf(newProduct.getStock()));
        modifyProductPrice.setText(String.valueOf(newProduct.getPrice()));
        modifyProductMax.setText(String.valueOf(newProduct.getMax()));
        modifyProductMin.setText(String.valueOf(newProduct.getMin()));
        CreatePartTable();
        CreatePartTable2();
    }
    /**
     * CreatePartTable method first creates the price column.
     * then adds the price column to the top table with parts.
     * it sets all the parts to partList by calling Inventory.getAllParts()
     * sets the top table items with partList
     * refreshes the top table
     */
    private void CreatePartTable(){
        TableColumn<Part, Double> mainPartPriceCol = CreatePriceCol();
        modifyProductTopTable.getColumns().addAll(mainPartPriceCol);
        partList.setAll(Inventory.getAllParts());
        modifyProductTopTable.setItems(partList);
        modifyProductTopTable.refresh();
    }
    /**
     * CreatePartTable2 method first creates the price column.
     * then adds the price column to the bottom table with parts.
     * it finds the correct product from the product list, then grabs it's associated parts
     * sets the associated parts to the partList2
     * if the product was not found, then an alert appears stating "Product not found" and then it closes the stage.
     * if no errors occurred, then the partList2 is set to the bottom table, and the bottom table is refreshed.
     */
    private void CreatePartTable2(){
        TableColumn<Part, Double> mainPartPriceCol = CreatePriceCol();
        modifyProductBottomTable.getColumns().addAll(mainPartPriceCol);
        Product tempProduct = Inventory.getModifyProduct();
        ObservableList<Product> productList = Inventory.getAllProducts();
        boolean found = false;
        for(Product product : productList){
            if(product.getId() == tempProduct.getId()) {
                found = true;
                partList2.setAll(product.getAllAssociatedParts());
            }
        }
        //alert if the product is not found then close the stage.
        if(!found) {
            myAlert("Product not found");
            Stage stage = (Stage) modifyProductCancel.getScene().getWindow();
            stage.close();
        }
        modifyProductBottomTable.setItems(partList2);
        modifyProductBottomTable.refresh();
    }
    /**
     * CreatePriceCol creates a price column for the tables.
     * sets the header to "Price/ Cost per Unit")
     * sets the min width to 120
     * sets the text so that the price appears as a double rounded to the 100th place past the decimal.
     * @return returns the price column for the tables
     */
    private <T> TableColumn<T , Double> CreatePriceCol(){
        TableColumn<T, Double> partTablePriceCol = new TableColumn<>("Price/ Cost per Unit");
        partTablePriceCol.setMinWidth(120);
        partTablePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTablePriceCol.setCellFactory((TableColumn<T, Double> column) -> new TableCell<T, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty){
                if (!empty){
                    setText(String.format("%.2f",item));
                }
            }
        });
        return partTablePriceCol;
    }
}

