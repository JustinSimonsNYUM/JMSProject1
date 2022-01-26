package controller;
/**
 * class MainScreenController.java
 */

/**
 * @author Justin Simons
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainScreenController is the controller for the main screen. this is where all parts and products are shown.
 * this is the screen that sends the user to all other screens
 */
public class MainScreenController implements Initializable {
    Stage stage;

    @FXML
    public AnchorPane MainScreen;

    @FXML
    public TableView<Part> partTable;

    @FXML
    public TableColumn<Part, Integer> partTableIdCol;

    @FXML
    public TableColumn<Part,String> partTableNameCol;

    @FXML
    public TableColumn<Part, Integer> partTableInLevelCol;

    @FXML
    public TextField mainPartSearchField;

    @FXML
    public Button partTableAddButton;

    @FXML
    public Button partTableModifyButton;

    @FXML
    public Button partTableDeleteButton;

    @FXML
    public TableView<Product> productTable;

    @FXML
    public TableColumn<Product, Integer> productTableIdCol;

    @FXML
    public TableColumn<Product, String> productTableNameCol;

    @FXML
    public TableColumn<Product,Integer> productTableInLevelCol;

    @FXML
    public TextField mainProductSearchField;

    @FXML
    public Button productTableAddButton;

    @FXML
    public Button productTableModifyButton;

    @FXML
    public Button productTableDeleteButton;

    @FXML
    public Button mainExitButton;

    private ObservableList<Part> partList = FXCollections.observableArrayList();
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Part> partListSearch = FXCollections.observableArrayList();
    private ObservableList<Product> productListSearch = FXCollections.observableArrayList();

    /**
     * Initialize the controller class
     * calls CreatePartTable() and CreateProductTable().
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        CreatePartTable();
        CreateProductTable();
    }
    /**
     * CreatePartTable method first creates the price column.
     * then adds the price column to the part table with all parts.
     * it sets all the parts to partList by calling Inventory.getAllParts()
     * sets the part table items with partList
     * refreshes the part table
     */
    private void CreatePartTable(){
        TableColumn<Part, Double> mainPartPriceCol = CreatePriceCol();
        partTable.getColumns().addAll(mainPartPriceCol);
        partTable.setItems(partList);
        partList.setAll(Inventory.getAllParts());
        partTable.refresh();
    }
    /**
     * CreateProductTable method first creates the price column.
     * then adds the price column to the product table with all products.
     * it sets all the products to productList by calling Inventory.getAllProducts()
     * sets the product table items with productList
     * refreshes the product table
     */
    private void CreateProductTable(){
        productList.setAll(Inventory.getAllProducts());
        TableColumn<Product, Double> mainProductPriceCol = CreatePriceCol();
        productTable.getColumns().addAll(mainProductPriceCol);
        productTable.setItems(productList);
        productTable.refresh();
    }

    /**
     * mainPartAddAction sends user to the add part screen
     * first creates a loader that gets addPart.fxml
     * creates an AddPartController
     * set's the controller to the loader
     * loads the loader to the root
     * creates a new stage
     * set's the title
     * set's the scene
     * show's the stage and waits
     * gets the new updated list of parts from the inventory
     * sets the new list of parts to partList
     * refreshes the partTable
     * @param event gets called when the add button under the part table is pressed
     * @exception IOException thrown is the addPart.fxml file is not found.
     *      the catch sends alert stating "e.getMessage() + " closing program"
     */
    @FXML
        void mainPartAddAction(ActionEvent event) throws IOException{
        mainPartSearchField.setText("");
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addPart.fxml"));
                AddPartController controller = new AddPartController();
                loader.setController(controller);
                Parent mainRoot = loader.load();
                stage = new Stage();
                stage.setTitle("Add Part");
                stage.setScene(new Scene(mainRoot));
                stage.showAndWait();
                partList.setAll(Inventory.getAllParts());
                partTable.setItems(partList);
                partTable.refresh();

            }
            catch(Exception e){
                myAlert(e.getMessage() + " closing program");
                System.exit(0);
            }
        }

    /**
     * mainExitAction closes program
     * @param event called when exit button is pressed.
     */
        @FXML
        void mainExitAction(ActionEvent event) {
            System.exit(0);
        }

    /**
     * mainPartDeleteAction deletes a part.
     * first checks if a part is selected or not. if not, then it returns.
     * if there was anything in the part searchField, it's cleared.
     * creates a string with all the products that have that part associated to it.
     * sends the part to Inventory.deletePart
     *      if it returns true, then it alerts the user with the confirmation that the part has been deleted.
     *      if any products were associated with it, it alerts the user with a confirmation that it also deleted the part
     *      from the products.
     *      it then deletes the part from the part list and refreshes the table.
     * if Inventory.deletePart returns false, it alerts the user that the part was not found.
     * @param event called when the delete button under the part table is pressed
     */
        @FXML
        void mainPartDeleteAction(ActionEvent event) {
            Part deletedPart = partTable.getSelectionModel().getSelectedItem();
            //return if no part is selected
            if(deletedPart == null)
                return;
            mainPartSearchField.setText("");
            ObservableList<Product> list = Inventory.getAllProducts();
            StringBuilder partsString = null;
            int count = 0;
            int lastProductCounter = 0;
            int lastProduct = list.size();
            boolean endOfListBool = false;

            for(Product product : list){
                lastProductCounter++;
                boolean partBool = product.deleteAssociatedPart(deletedPart);
                if(partBool && count == 0){
                    partsString = new StringBuilder(product.getName());
                    count = 1;
                    endOfListBool = true;
                }
                else if(count == 1)
                    partsString.append(", ").append(product.getName());
                if(endOfListBool && (lastProduct == lastProductCounter))
                    partsString.append(".");

            }
            boolean partBool = Inventory.deletePart(deletedPart);
            if(partBool) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                if(partsString == null)
                    a.setContentText("You have successfully deleted the Part: " + deletedPart.getName() + ".");
                else
                    a.setContentText("You have successfully deleted the Part: "+ deletedPart.getName() +
                            ". You have also deleted this part from the Product(s) " + partsString);
                a.show();
                partList.remove(deletedPart);
                partTable.setItems(partList);
                partTable.refresh();
            }
            //alert if no part is found
            else{
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("part not found");
                a.show();
            }

        }
    /**
     * mainPartModify Action sends user to the modify part screen
     * first creates a loader that gets modifyPart.fxml
     * creates an ModifyPartController
     * set's the controller to the loader
     * loads the loader to the root
     * creates a new stage
     * set's the title
     * set's the scene
     * show's the stage and waits
     * gets the new updated list of parts from the inventory
     * sets the new list of parts to partList
     * refreshes the partTable
     * @param event gets called when the modify button under the part table is pressed
     * @exception IOException thrown is the modifyPart.fxml file is not found.
     *      the catch sends alert stating "e.getMessage() + " closing program"
     */
        @FXML
        void mainPartModifyAction(ActionEvent event) throws IOException {
            Part modifyPart = partTable.getSelectionModel().getSelectedItem();
            if (modifyPart == null)
                return;
            mainPartSearchField.setText("");
            Inventory.setModifyPart(modifyPart);
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/modifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController();
                loader.setController(controller);
                Parent scene = loader.load();
                stage = new Stage();
                stage.setTitle("Modify Part");
                stage.setScene(new Scene(scene));
                stage.showAndWait();
                partList.setAll(Inventory.getAllParts());
                partTable.setItems(partList);
                partTable.refresh();
            }
            catch(Exception e){
                myAlert(e.getMessage() + " closing program");
                System.exit(0);
            }
        }
    /**
     * mainPartSearchChange method searches the part table for parts.
     * if the search field is empty, the part table gets reset with all parts.
     * if a number is entered, it searches if there is a part with the corresponding id number.
     *      if there is a part with an id that matches the number entered, then all parts are
     *      removed from the part table except for the part found.
     * if a letter of number is typed, then it searches all parts.
     *      if any part includes the user input string in it's part name, then it appears in
     *      the part table, while all others are removed.
     * if no part id or name includes the user input, then an error appears stating "Part not found" then returns.
     * @param event called when user types a key on keyboard in the part search field
     */
        @FXML
        void mainPartSearchChange(KeyEvent event) {
            String searchSt = mainPartSearchField.getText().trim();
            if(searchSt.isEmpty()) {
                partTable.setItems(partList);
                partTable.refresh();
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
                            partTable.setItems(partListSearch);
                            partTable.refresh();
                            return;
                        }
                    }
                }
            }
            if(Character.isLetterOrDigit(searchSt.charAt(0)) || check == 1) {
                ObservableList<Part> tempList = Inventory.lookupPart(searchSt);
                if(tempList == null){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Part not found");
                    a.showAndWait();
                    mainPartSearchField.setText("");
                    partTable.setItems(partList);
                    partTable.refresh();
                    return;
                }
                partListSearch = tempList;
                partTable.setItems(partListSearch);
                partTable.refresh();
            }
            else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Part not found");
                a.showAndWait();
            }
        }
    /**
     * mainProductAddAction sends user to the addProduct screen
     * first creates a loader that gets addProduct.fxml
     * creates an AddProductController
     * set's the controller to the loader
     * loads the loader to the root
     * creates a new stage
     * set's the title
     * set's the scene
     * show's the stage and waits
     * gets the new updated list of products from the inventory
     * sets the new list of products to productList
     * refreshes the productTable
     * @param event gets called when the add button under the product table is pressed
     * @exception IOException thrown is the addProduct.fxml file is not found.
     *      the catch sends alert stating "e.getMessage() + " closing program"
     */
        @FXML
        void mainProductAddAction(ActionEvent event) throws IOException{
            mainProductSearchField.setText("");
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addProduct.fxml"));
                AddProductController controller = new AddProductController();
                loader.setController(controller);
                Parent root = loader.load();
                stage = new Stage();
                stage.setTitle("Add Product");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                productList.setAll(Inventory.getAllProducts());
                productTable.setItems(productList);
                productTable.refresh();
            }
            catch(Exception e){
                myAlert(e.getMessage() + " closing program");
                System.exit(0);
            }
        }
    /**
     * mainProductDeleteAction deletes a product.
     * first checks if a product is selected or not. if not, then it returns.
     * if there was anything in the product searchField, it's cleared.
     * if the product has any associated parts, it alerts the user to remove the part from the Product before
     *      it can delete it and then returns.
     * sends the product to Inventory.deleteProduct
     *      if it returns true, then it alerts the user with the confirmation that the product has been deleted.
     *      it then deletes the product from the product list and refreshes the table.
     * if Inventory.deleteProduct returns false, it alerts the user that the product was not found.
     * @param event called when the delete button under the product table is pressed
     */
        @FXML
        void mainProductDeleteAction(ActionEvent event) {
            Product deletedProduct = productTable.getSelectionModel().getSelectedItem();
            if(deletedProduct == null)
                return;
            mainProductSearchField.setText("");
            if(!deletedProduct.getAllAssociatedParts().isEmpty()){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("You must remove the associated parts from the product before you can delete it.");
                a.show();
                return;
            }
            boolean productBool = Inventory.deleteProduct(deletedProduct);
            if(productBool) {
                productList.remove(deletedProduct);
                productTable.setItems(productList);
                productTable.refresh();
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("You have successfully deleted the Product: " + deletedProduct.getName() + ".");
                a.show();
            }
        }
    /**
     * mainProductModify Action sends user to the modify product screen
     * first creates a loader that gets modifyProduct.fxml
     * creates an ModifyProductController
     * set's the controller to the loader
     * loads the loader to the root
     * creates a new stage
     * set's the title
     * set's the scene
     * show's the stage and waits
     * gets the new updated list of products from the inventory
     * sets the new list of products to productList
     * refreshes the productTable
     * @param event gets called when the modify button under the product table is pressed
     * @exception IOException thrown is the modifyProduct.fxml file is not found.
     *      the catch sends alert stating "e.getMessage() + " closing program"
     */
        @FXML
        void mainProductModifyAction(ActionEvent event) throws IOException{
            Product modifyProduct = productTable.getSelectionModel().getSelectedItem();
            if (modifyProduct == null)
                return;
            mainProductSearchField.setText("");
            Inventory.setModifyProduct(modifyProduct);
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyProduct.fxml"));
                ModifyProductController controller = new ModifyProductController();
                loader.setController(controller);
                Parent root = loader.load();
                Stage modifyProductStage = new Stage();
                modifyProductStage.setTitle("Modify Product");
                modifyProductStage.setScene(new Scene(root));
                modifyProductStage.showAndWait();
                productList.setAll(Inventory.getAllProducts());
                productTable.setItems(productList);
                productTable.refresh();
            }
            catch(Exception e){
                myAlert(e.getMessage() + " closing program");
                System.exit(0);
            }
        }
    /**
     * mainProductSearchChange method searches the product table for products.
     * if the search field is empty, the productTable gets reset with all products.
     * if a number is entered, it searches if there is a product with the corresponding id number.
     *      if there is a product with an id that matches the number entered, then all products are
     *      removed from the product table except for the product found.
     * if a letter of number is typed, then it searches all products.
     *      if any product includes the user input string in it's product name, then it appears in
     *      the product table, while all others are removed.
     * if no product id or name includes the user input, then an error appears stating "Product not found" then returns.
     * @param event called when user types a key on keyboard in the product search field
     */
        @FXML
        void mainProductSearchChange(KeyEvent event) {
            String searchSt = mainProductSearchField.getText().trim();
            String tempSt = searchSt;
            if(searchSt.isEmpty()) {
                productTable.setItems(productList);
                productTable.refresh();
                return;
            }
            int check = 0;
            if(searchSt.length() > 3)
                check = 1;
            //check if user if searching for product by ID number
            if(Character.isDigit(searchSt.charAt(0)) && check == 0) {
                if (searchSt.length() == 1)
                    tempSt = searchSt + "00";
                else if (searchSt.length() == 2)
                    tempSt = searchSt + "0";
                if (Character.isDigit(tempSt.charAt(1))) {
                    if (Character.isDigit(tempSt.charAt(2))) {
                        Product tempProduct = Inventory.lookupProduct(Integer.parseInt(tempSt));
                        if (!(tempProduct == null)) {
                            productListSearch.setAll(tempProduct);
                            productTable.setItems(productListSearch);
                            productTable.refresh();
                            return;
                        }
                    }
                }
            }
            if(Character.isLetterOrDigit(searchSt.charAt(0)) || check == 1) {
                ObservableList<Product> tempList = FXCollections.observableArrayList();
                tempList = Inventory.lookupProduct(searchSt);
                if(tempList == null){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Product not found");
                    a.showAndWait();
                    mainProductSearchField.setText("");
                    productTable.setItems(productList);
                    productTable.refresh();
                    return;
                }
                productListSearch = tempList;
                productTable.setItems(productListSearch);
                productTable.refresh();
            }
            else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Product not found");
                a.showAndWait();
            }
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
    /**
     * myAlert method creates a new alert with AlertType ERROR.
     * it sets the content text.
     * shows the alert.
     * @param alert passes string object passed to it into setContentText for alert.
     */
        public void myAlert(String alert){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(alert);
            a.showAndWait();
        }
}

