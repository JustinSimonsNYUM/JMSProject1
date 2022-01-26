package main;
/**
 * class AddPartController.java
 */

/**
 * @author Justin Simons
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.*;

import javax.naming.InvalidNameException;

/**
 * main class it what starts the application
 */
public class Main extends Application {
    /**
     * main launches args/program
     * @param args sent to launch
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * start starts the program
     * first calls addPreData
     * creates a loader that gets mainScreen.fxml
     * creates an MainScreenController
     * set's the controller to the loader
     * loads the loader to the root
     * creates a new stage
     *  set's the title
     * set's the scene
     * show's the stage
     * @param primaryStage creates the primary stage
     * @throws Exception gets thrown if mainScreen.fxml fails to load
     *      the catch sends alert stating "e.getMessage() + " closing program"
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        addPreData();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainScreen.fxml"));
            controller.MainScreenController controller = new controller.MainScreenController();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root, 860, 300);
            primaryStage.setTitle("Main Form");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch(Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage() + " closing program");
            a.showAndWait();
            System.exit(0);
        }

    }


    /**
     * addPreData sends parts and products to inventory for sample data
     * creates InHouse Parts then adds them to inventory
     * creates Outsourced Parts then adds them to inventory
     * creates products then adds there associated parts. then adds them to inventory.
     */
    void addPreData(){//Inventory data
        //add the InHouse parts
        Part alWrench = new InHouse(1,"Allen Wrench",0.99,50,5,100,101);
        Part screws = new InHouse(2,"Screws",2.99, 100,30,1000,102);
        Part nuts = new InHouse(3,"Nuts",1.99,100,30,1000,103);
        Part washer = new InHouse(4, "Washer", 4.99, 40, 5,100,104);
        Inventory.addPart(alWrench);
        Inventory.addPart(screws);
        Inventory.addPart(nuts);
        Inventory.addPart(washer);
        // add the OutSourced Parts
        Part gear = new Outsourced(5,"Gear",4.99,10,5,50,"Gear co.");
        Part spring = new Outsourced(6,"Spring",1.99,5,2,20,"Gear co.");
        Part wire = new Outsourced(7,"Wires",5.99,10,2,30,"Electro co.");
        Part plug = new Outsourced(8,"Plug",9.99,10,2,40,"Electro co.");
        Inventory.addPart(gear);
        Inventory.addPart(spring);
        Inventory.addPart(wire);
        Inventory.addPart(plug);
        // add the products
        Product table = new Product(100,"Table",100.89,30,5,50);
            table.addAssociatedParts(alWrench);
            table.addAssociatedParts(washer);
            table.addAssociatedParts(screws);
            Inventory.addProduct(table);
        Product dresser = new Product(200, "Dresser",89.99,40,5,80);
            dresser.addAssociatedParts(alWrench);
            dresser.addAssociatedParts(washer);
            dresser.addAssociatedParts(screws);
            Inventory.addProduct(dresser);
        Product toyCar = new Product(300,"Toy Car",19.99,5,2,20);
            toyCar.addAssociatedParts(plug);
            toyCar.addAssociatedParts(gear);
            toyCar.addAssociatedParts(alWrench);
            Inventory.addProduct(toyCar);
        Product phone = new Product(400,"iPhone",399.99,20,5,50);
            phone.addAssociatedParts(plug);
            phone.addAssociatedParts(wire);
            phone.addAssociatedParts(alWrench);
            Inventory.addProduct(phone);
    }


}
