package controller;
/**
 * class AddProductController.java
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
import model.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class is the controller for the addProduct GUI
 * it's in charge of creating a new product
 */

public class AddProductController implements Initializable {
    //keeps track of new idNumbers to assign
    static private int idNumber = 400;
    @FXML
    private TextField addProductName;

    @FXML
    private TextField addProductInv;

    @FXML
    private TextField addProductPrice;

    @FXML
    private TextField addProductMax;

    @FXML
    private TextField addProductMin;

    @FXML
    private TextField addProductSearch;

    @FXML
    private TableView<Part> addProductTopTable;

    @FXML
    private TableColumn<Part, Integer> addProductTopTableId;

    @FXML
    private TableColumn<Part, String> addProductTopTableName;

    @FXML
    private TableColumn<Part, Integer> addProductTopTableInLev;

    @FXML
    private Button addProductAdd;

    @FXML
    private TableView<Part> addProductBottomTable;

    @FXML
    private TableColumn<Part, Integer> addProductBottomTableId;

    @FXML
    private TableColumn<Part, String> addProductBottomTableName;

    @FXML
    private TableColumn<Part, Integer> addProductBottomTableInLev;

    @FXML
    private Button addProductRemovePart;

    @FXML
    private Button addProductSave;

    @FXML
    private Button addProductCancel;


    private ObservableList<Part> partList = FXCollections.observableArrayList();
    private ObservableList<Part> partList2 = FXCollections.observableArrayList();
    private ObservableList<Part> partListSearch = FXCollections.observableArrayList();

    /**
     * initialize method calls CreatePartTable() and CreatePartTable2()
     * @param url first called when screen opens.
     * @param rb first called when screen opens
     */
    public void initialize(URL url, ResourceBundle rb) {
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
        addProductTopTable.getColumns().addAll(mainPartPriceCol);
        partList.setAll(Inventory.getAllParts());
        addProductTopTable.setItems(partList);
        addProductTopTable.refresh();
    }

    /**
     * CreatePartTable2 method first creates the price column.
     * then adds the price column to the bottom table with parts.
     */
    private void CreatePartTable2(){
        TableColumn<Part, Double> mainPartPriceCol = CreatePriceCol();
        addProductBottomTable.getColumns().addAll(mainPartPriceCol);
    }

    /**
     * addProductAddAction method adds a part from top table to bottom table.
     * if a part from the top table was not selected, it returns.
     * it checks if the selected part is already included in the bottom table list.
     *      if it's not, it adds that part to the bottom table without removing
     *      it from the top table.
     *      if the selected part has already been added, an error occurs stating "Part already added".
     *      it then returns.
     * @param event called when the add button is pressed.
     */
    @FXML
    void addProductAddAction(MouseEvent event) {
        Part addedPart = addProductTopTable.getSelectionModel().getSelectedItem();
        //checks if part was selected or not
        if(addedPart == null)
            return;
        //sends error and returns if top table selected part was already added to bottom table.
        if(partList2.contains(addedPart)){
            myAlert("Part already added");
            return;
        }
        partList2.add(addedPart);
        addProductBottomTable.setItems(partList2);
        addProductBottomTable.refresh();
    }

    /**
     * addProductCancelAction method hides the stage.
     * All user input for this screen is disregarded.
     * @param event called when cancel button is pressed.
     */
    @FXML
    void addProductCancelAction(MouseEvent event) {
        Stage stage = (Stage) addProductCancel.getScene().getWindow();
        stage.hide();
    }

    /**
     * addProductRemovePartAction method removes a part from bottom table.
     * if no part from bottom table is selected, it returns.
     * if a part is selected, it is deleted from the bottom table, not affecting the top table.
     * a confirmation alert appears with the content text "You have successfully deleted the part:
     *      " + deletedPart.getName() + " from your new product " + addProductName.getText().trim()
     * it then updates the bottom table and refreshes it.
     * @param event called when remove part button is pressed.
     */
    @FXML
    void addProductRemovePartAction(MouseEvent event) {
        Part deletedPart = addProductBottomTable.getSelectionModel().getSelectedItem();
        if(deletedPart == null)
            return;
        addProductSearch.setText("");
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("You have successfully deleted the part: " + deletedPart.getName() + " from your new product " + addProductName.getText().trim());
        a.show();
        partList2.remove(deletedPart);
        addProductBottomTable.setItems(partList2);
        addProductBottomTable.refresh();
    }
    /**
     * addProductSaveAction method saves the product to inventory.
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
     * if no errors have occurred than, it creates a new product with the user input, adds the associated parts to it, and
     *      sends it to inventory products.
     * the screen is then closed.
     * @param event called when user clicks the save button.
     */
    @FXML
    void addProductSaveAction(MouseEvent event) {
        String productName = addProductName.getText().trim();
        String productInv = addProductInv.getText().trim();
        String productPrice = addProductPrice.getText().trim();
        String productMax = addProductMax.getText().trim();
        String productMin = addProductMin.getText().trim();

        //Alert if any field is empty
        if(productName.isEmpty() || productInv.isEmpty() || productPrice.isEmpty()
                || productMax.isEmpty() || productMin.isEmpty()){
            myAlert("Please fill out each field.");
            return;
        }

        // Alert if price contains any letters. if so it returns
        for(int i = 0; i < productPrice.length(); i++){
            char priceCh = (productPrice.charAt(i));
            if(Character.isLetter(priceCh) && !(priceCh == '.')){
                myAlert("Please only input numbers for price");
                return;
            }
        }
        // Alert if inv contains any letters. if so it returns
        for(int i = 0; i < productInv.length(); i++){
            char priceCh = (productInv.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Inventory");
                return;
            }
        }
        // Alert if max contains any letters. if so it returns
        for(int i = 0; i < productMax.length(); i++){
            char priceCh = (productMax.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Max");
                return;
            }
        }
        // Alert if min contains any letters. if so it returns
        for(int i = 0; i < productMin.length(); i++){
            char priceCh = (productMin.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Min");
                return;
            }
        }
        // check if max is greater than min or not. if not, send alert and return
        if(Integer.parseInt(productMin) > Integer.parseInt(productMax)){
            myAlert("Please make sure that the max is greater than your min.");
            return;
        }
        //if inv is less than min, send alert and return.
        if(Integer.parseInt(productInv) < Integer.parseInt(productMin)){
            myAlert("Please make sure that the Inventory is greater than your min.");
            return;
        }
        //if inv is more than max, send alert and return.
        if(Integer.parseInt(productInv) > Integer.parseInt(productMax)){
            myAlert("Please make sure that the Inventory is less than your max.");
            return;
        }

        double totalPartPrice = 0.0;
        for (Part part : partList2) {
            totalPartPrice += part.getPrice();;
        }
        //if the total price of the associated parts is greater than the price of the product, send alert and return.
        if ((Double.parseDouble(productPrice)) < totalPartPrice) {
            myAlert("Please make sure your product price is greater than the total cost of added parts.");
        }
        else{
            idNumber = idNumber + 100;
            Product product = new Product(idNumber, productName, Double.parseDouble(productPrice),
                    Integer.parseInt(productInv), Integer.parseInt(productMin), Integer.parseInt(productMax));
            for (Part part : partList2) {
                product.addAssociatedParts(part);
            }
            Inventory.addProduct(product);
            Stage stage = (Stage) addProductSave.getScene().getWindow();
            stage.hide();
        }
    }

    /**
     * addProductSearchAction method searches the top table for parts.
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
    void addProductSearchAction(KeyEvent event) {
        String searchSt = addProductSearch.getText().trim();
        if(searchSt.isEmpty()) {
            addProductTopTable.setItems(partList);
            addProductTopTable.refresh();
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
                        addProductTopTable.setItems(partListSearch);
                        addProductTopTable.refresh();
                        return;
                    }
                }
            }
        }
        if(Character.isLetterOrDigit(searchSt.charAt(0)) || check == 1) {
            ObservableList<Part> tempList = Inventory.lookupPart(searchSt);
            if(tempList == null){
                myAlert("Part not found");
                addProductSearch.setText("");
                addProductTopTable.setItems(partList);
                addProductTopTable.refresh();
                return;
            }
            partListSearch = tempList;
            addProductTopTable.setItems(partListSearch);
        }
        else {
            myAlert("Part not found");
            addProductSearch.setText("");
            addProductTopTable.setItems(partList);
        }
        addProductTopTable.refresh();
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

