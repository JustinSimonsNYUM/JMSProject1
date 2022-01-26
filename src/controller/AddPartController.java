package controller;
/**
 * class AddPartController.java
 */

/**
 * @author Justin Simons
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;



/**
 * this class is the controller for the addPart GUI
 * it's in charge of creating a new part
 */

public class AddPartController{
    //keeps track of new idNumbers to assign
    private static int idNumber = 8;

    @FXML
    private TextField addPartId;

    @FXML
    private Label addPartMAndCText;

    @FXML
    private TextField addPartName;

    @FXML
    private TextField addPartInv;

    @FXML
    private TextField addPartPrice;

    @FXML
    private TextField addPartMax;

    @FXML
    private TextField addPartMin;

    @FXML
    private TextField addPartMId;

    @FXML
    private RadioButton addPartInHouseRadio;

    @FXML
    private ToggleGroup addPartRadio;

    @FXML
    private RadioButton addPartOutsourcedRadio;

    @FXML
    private Button addPartSave;

    @FXML
    private Button addPartCancel;

    /**
     * addPartInHouseAction method sets addPartMAndCText's text to "Machine ID"
     * @param event called when user clicks the InHouse radio button.
     */

    @FXML
    void addPartInHouseAction(ActionEvent event) {
        addPartMAndCText.setText("Machine ID");
    }
    /**
     * addPartOutsourcedAction method sets addPartMAndCText's text to "Company Name"
     * @param event called when user clicks the Outsourced radio button.
     */
    @FXML
    void addPartOutsourcedAction(ActionEvent event) {
        addPartMAndCText.setText("Company Name");
    }

    /**
     * addPartCancelAction method closes the screen.
     * closes window. Anything the user put in text fields is ignored.
     * @param event called when user clicks the cancel button.
     */
    @FXML
    void addPartCancelAction(ActionEvent event) {
        Stage stage = (Stage) addPartCancel.getScene().getWindow();
        stage.hide();
    }
    /**
     * addPartSaveAction method is saves the part to inventory
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
     * It then checks if InHouse or Outsourced is checked.
     * If inHouse is checked, then it checks if only numbers were added for machine id. if not, an error appears stating
     *      "Please only input numbers for Machine ID". if this error occurs it returns.
     *      if no error occurred, then a new InHouse object is created and sent to Inventory parts.
     * if Outsourced is checked, then a new Outsourced object is created and sent to Inventory parts.
     * the screen is then closed
     * @param event  called when user clicks the save button.
     */
    @FXML
    void addPartSaveAction(ActionEvent event) {
        String partName = addPartName.getText().trim();
        String partPrice = addPartPrice.getText().trim();
        String partInv = addPartInv.getText().trim();
        String partMax = addPartMax.getText().trim();
        String partMin = addPartMin.getText().trim();
        String partMId = addPartMId.getText().trim();


         //checks if any field is empty using the isEmpty() on each text field
        if(partName.isEmpty() || partInv.isEmpty() || partPrice.isEmpty()
                || partMax.isEmpty() || partMin.isEmpty() || partMId.isEmpty()){
            myAlert("Please fill out each field.");
            return;
        }

        //checks if price has any letters in it. if so it sends an error and returns.
        for(int i = 0; i < partPrice.length(); i++){
            char priceCh = (partPrice.charAt(i));
            if(Character.isLetter(priceCh) && !(priceCh == '.')){
                myAlert("Please only input numbers for price");
                return;
            }
        }
        //checks if inv has any letters in it. if so it sends an error and returns.
        for(int i = 0; i < partInv.length(); i++){
            char priceCh = (partInv.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Inventory");
                return;
            }
        }
        //checks if max has any letters in it. if so it sends an error and returns.
        for(int i = 0; i < partMax.length(); i++){
            char priceCh = (partMax.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Max");
                return;
            }
        }
        //checks if min has any letters in it. if so it sends an error and returns.
        for(int i = 0; i < partMin.length(); i++){
            char priceCh = (partMin.charAt(i));
            if(Character.isLetter(priceCh)){
                myAlert("Please only input integers for Min");
                return;
            }
        }
        // checks if max is greater than min or not. if so it sends an error and returns.
        if(Integer.parseInt(partMin) > Integer.parseInt(partMax)){
            myAlert("Please make sure that the max is greater than your min.");
            return;
        }
        //checks if inv is less than min. if so it sends an error and returns.
        if(Integer.parseInt(partInv) < Integer.parseInt(partMin)){
            myAlert("Please make sure that the Inventory is greater than your min.");
            return;
        }
        //checks if inv is more than max. if so it sends an error and returns.
        if(Integer.parseInt(partInv) > Integer.parseInt(partMax)){
            myAlert("Please make sure that the Inventory is less than your max.");
            return;
        }
        //checks if only numbers were input for machine id. if not, an error appears then it returns.
        if(addPartInHouseRadio.isSelected()){
            for(int i = 0; i < partMId.length(); i++){
                char priceCh = (partMId.charAt(i));
                if(Character.isLetter(priceCh)){
                    myAlert("Please only input numbers for Machine ID");
                    return;
                }
            }
            idNumber++;
            Inventory.addPart(new InHouse(idNumber, partName, Double.parseDouble(partPrice), Integer.parseInt(partInv),
                   Integer.parseInt(partMin), Integer.parseInt(partMax),  Integer.parseInt(partMId)));
            Stage stage = (Stage) addPartSave.getScene().getWindow();
            stage.hide();
        }
        else if(addPartOutsourcedRadio.isSelected()){
            idNumber++;
            Inventory.addPart(new Outsourced(idNumber, partName, Double.parseDouble(partPrice), Integer.parseInt(partInv),
                    Integer.parseInt(partMin),Integer.parseInt(partMax), partMId));
            Stage stage = (Stage) addPartSave.getScene().getWindow();
            stage.hide();
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
}
