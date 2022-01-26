package controller;
/**
 * class ModifyPartController.java
 */

/**
 * @author Justin Simons
 */
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class is the controller for the modifyPart GUI
 * it's in charge of creating a new part to replace the old.
 */
public class ModifyPartController implements Initializable {

    @FXML
    private TextField modifyPartId;

    @FXML
    private Label modifyPartMAndCText;

    @FXML
    private TextField modifyPartName;

    @FXML
    private TextField modifyPartMId;

    @FXML
    private TextField modifyPartMax;

    @FXML
    private TextField modifyPartInv;

    @FXML
    private TextField modifyPartPrice;

    @FXML
    private RadioButton modifyPartInHouseRadio;

    @FXML
    private ToggleGroup addPartRadio;

    @FXML
    private RadioButton modifyPartOutsourcedRadio;

    @FXML
    private TextField modifyPartMin;

    @FXML
    private Button modifyPartSave;

    @FXML
    private Button modifyPartCancel;
    /**
     * modifyPartCancelAction method is closes screen.
     * closes window. Anything the user put in text fields is ignored.
     * @param event called when user clicks the cancel button.
     */
    @FXML
    void modifyPartCancelAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) modifyPartCancel.getScene().getWindow();
        stage.hide();
    }
    /**
     * modifyPartInHouseAction method is sets addPartMAndCText's text to "Machine ID"
     * @param event called when user clicks the InHouse radio button.
     */
    @FXML
    void modifyPartInHouseAction(ActionEvent event) {
        modifyPartMAndCText.setText("Machine ID");
    }
    /**
     * modifyPartOutsourcedAction method sets addPartMAndCText's text to "Company Name"
     * @param event called when user clicks the Outsourced radio button.
     */
    @FXML
    void modifyPartOutsourcedAction(ActionEvent event) {
        modifyPartMAndCText.setText("Company Name");
    }
    /**
     * modifyPartSaveAction method is saves the changes to the part to inventory
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
     * if InHouse is checked and the part is an instanceOf InHouse or Outsourced, then the updateInHouse method is called.
     * if Outsourced is checked and the part is an instanceOf InHouse or Outsourced, then the updateOutsourced method is called.
     * @param event  called when user clicks the save button.
     */
    @FXML
    void modifyPartSaveAction(ActionEvent event) {
        String partName = modifyPartName.getText().trim();
        String partPrice = modifyPartPrice.getText().trim();
        String partInv = modifyPartInv.getText().trim();
        String partMax = modifyPartMax.getText().trim();
        String partMin = modifyPartMin.getText().trim();
        String partMId = modifyPartMId.getText().trim();

        //checks if any field is empty using the isEmpty() on each text field
        if(partName.isEmpty() || partPrice.isEmpty() || partInv.isEmpty() || partMax.isEmpty() || partMin.isEmpty() || partMId.isEmpty()){
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
            if(Character.isLetter(priceCh) && !(priceCh == '.')){
                myAlert("Please only input numbers for Inventory");
                return;
            }
        }
        //checks if max has any letters in it. if so it sends an error and returns.
        for(int i = 0; i < partMax.length(); i++){
            char priceCh = (partMax.charAt(i));
            if(Character.isLetter(priceCh) && !(priceCh == '.')){
                myAlert("Please only input numbers for Max");
                return;
            }
        }
        //checks if min has any letters in it. if so it sends an error and returns.
        for(int i = 0; i < partMin.length(); i++){
            char priceCh = (partMin.charAt(i));
            if(Character.isLetter(priceCh) && !(priceCh == '.')){
                myAlert("Please only input numbers for Min");
                return;
            }
        }
        // check if max is greater than min or not. if so it sends an error and returns.
        if(Integer.parseInt(partMin) > Integer.parseInt(partMax)){
            myAlert("Please make sure that the max is greater than your min.");
            return;
        }
        //checks if inv is less than min. if so it sends an error and returns.
        if(Integer.parseInt(partInv) < Integer.parseInt(partMin)){
            myAlert("Please make sure that the Inventory is greater than your min.");
            return;
        }
        //checks if inv is greater than max. if so it sends an error and returns.
        if(Integer.parseInt(partInv) > Integer.parseInt(partMax)){
            myAlert("Please make sure that the Inventory is less than your max.");
            return;
        }
        Part newPart = Inventory.getModifyPart();
        if(modifyPartInHouseRadio.isSelected() && newPart instanceof InHouse){
            updateInHouse();
        }
        else if(modifyPartInHouseRadio.isSelected() && newPart instanceof Outsourced){
            updateInHouse();
        }
        else if(modifyPartOutsourcedRadio.isSelected() && newPart instanceof InHouse){
            updateOutsourced();
        }
        else if(modifyPartOutsourcedRadio.isSelected() && newPart instanceof Outsourced){
            updateOutsourced();
        }
    }

    /**
     * updateInHouse is called to update the part to a new InHouse part with the same id
     * checks if only numbers were added for machine id. if not, an error appears stating
     *      "Please only input numbers for Machine ID" then returns.
     * loop looks for the part index
     * sends a new part with the user input to the Inventory.updatePart() to update the part
     *      with the same id.
     * the stage is then closed
     */
    private void updateInHouse(){
        for(int i = 0; i < (modifyPartMId.getText().trim()).length(); i++){
            char priceCh = ((modifyPartMId.getText().trim()).charAt(i));
            //send alert if any letters were entered for machine id
            if(Character.isLetter(priceCh) && !(priceCh == '.')){
                myAlert("Please only input numbers for Machine ID");
                return;
            }
        }
        int partIndex = 0;
        ObservableList<Part> parts = Inventory.getAllParts();
        for(int i = 0; i < parts.size(); i++){
            if(parts.get(i).getId() == Inventory.getModifyPart().getId()){
                partIndex = i;
                break;
            }
        }

        Inventory.updatePart(partIndex, (new InHouse(Integer.parseInt(modifyPartId.getText().trim()), modifyPartName.getText().trim(),
                Double.parseDouble(modifyPartPrice.getText().trim()), Integer.parseInt(modifyPartInv.getText().trim()),
                Integer.parseInt(modifyPartMin.getText().trim()),Integer.parseInt(modifyPartMax.getText().trim()), Integer.parseInt(modifyPartMId.getText().trim()))));
        Stage stage = (Stage) modifyPartSave.getScene().getWindow();
        stage.close();
    }
    /**
     * updateOutsourced is called to update the part to a new Outsourced part with the same id
     * loop looks for the part index
     * sends a new part with the user input to the Inventory.updatePart() to update the part
     * with the same id.
     * the stage is then closed
     */
    private void updateOutsourced(){
        int partIndex = 0;
        ObservableList<Part> parts = Inventory.getAllParts();
        for(int i = 0; i < parts.size(); i++){
            if(parts.get(i).getId() == Inventory.getModifyPart().getId()){
                partIndex = i;
                break;
            }
        }
        Inventory.updatePart(partIndex, (new Outsourced(Integer.parseInt(modifyPartId.getText().trim()), modifyPartName.getText().trim(),
                Double.parseDouble(modifyPartPrice.getText().trim()), Integer.parseInt(modifyPartInv.getText().trim()),
                Integer.parseInt(modifyPartMin.getText().trim()), Integer.parseInt(modifyPartMax.getText().trim()), modifyPartMId.getText().trim())));
        Stage stage = (Stage) modifyPartSave.getScene().getWindow();
        stage.close();
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
        a.show();
    }

    /**
     * initialize method sets each text field with the appropriate data from the selected part
     * sets the text for id with the id from the selected part
     * sets the text for name with the name from the selected part
     * sets the text for inv with the inv from the selected part
     * sets the text for price with the price from the selected part
     * sets the text for max with the max from the selected part
     * sets the text for min with the min from the selected part
     * if the selected part is an instance of InHouse:
     *      the InHouse radio button is selected,
     *      sets the text for machine id with the machine id from the selected part,
     *      sets the text next to the machine id textField to Machine ID.
     * if the selected part is an instance of Outsourced:
     *      the Outsourced radio button is selected,
     *      sets the text for company name with the company name from the selected part,
     *      sets the text next to the company name textField to Company Name.
     * @param url called with screen opens.
     * @param rb called with screen opens.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part newPart = Inventory.getModifyPart();
        modifyPartId.setText(String.valueOf(newPart.getId()));
        modifyPartName.setText(newPart.getName());
        modifyPartInv.setText(String.valueOf(newPart.getStock()));
        modifyPartPrice.setText(String.valueOf(newPart.getPrice()));
        modifyPartMax.setText(String.valueOf(newPart.getMax()));
        modifyPartMin.setText(String.valueOf(newPart.getMin()));

        if(newPart instanceof InHouse) {
            modifyPartInHouseRadio.setSelected(true);
            modifyPartMId.setText(String.valueOf(((InHouse) newPart).getMachineId()));
            modifyPartMAndCText.setText("Machine ID");
        }
        if(newPart instanceof Outsourced) {
            modifyPartOutsourcedRadio.setSelected(true);
            modifyPartMId.setText(((Outsourced) newPart).getCompanyName());
            modifyPartMAndCText.setText("Company Name");
        }
    }


}
