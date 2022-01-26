package model;
/**
 * class Product.java
 */

/**
 * @author Justin Simons
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class holds all product data
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private  int stock;
    private int min;
    private int max;
    /**
     * Product method constructor sets all Product data
     * sets id,name,price,stock,min,and max.
     * @param id grabs id to set
     * @param name grabs name to set
     * @param price grabs price to set
     * @param stock grabs stock to set
     * @param min grabs min to set
     * @param max grabs  max to set
     */
    public Product(int id,String name,double price,int stock,int min,int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * setId sets the id
     * @param id sets id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * setName sets the name
     * @param name sets name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * setPrice sets the price
     * @param price sets price
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * setStock sets the stock
     * @param stock sets stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
    /**
     * setMin sets the min
     * @param min sets min
     */
    public void setMin(int min) {
        this.min = min;
    }
    /**
     * setMax sets the max
     * @param max sets max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * getId returns the id
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * getName returns the name
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * getPrice returns the price
     * @return the price
     */
    public double getPrice() {
        return price;
    }
    /**
     * getStock returns the stock
     * @return the stock
     */
    public int getStock() {
        return stock;
    }
    /**
     * getMin returns the min
     * @return the min
     */
    public int getMin() {
        return min;
    }
    /**
     * getMax returns the max
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     *  addAssociatedParts adds associated parts to the product
     * @param part gets the part to add to the associatedParts
     */
    public void addAssociatedParts(Part part) {
        associatedParts.add(part);
    }

    /**
     * deleteAssociatedPart deletes the associated part
     * if the product contains the part, then it's removed from the associated parts then makes found = true.
     * if the product doesn't contain the part, then found returns false.
     * @param selectedAssociatedPart gets the part to delete from the products associated parts
     * @return found which tells if the product contains the part or not
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        boolean found = false;
        if(associatedParts.contains(selectedAssociatedPart)){
            associatedParts.remove(selectedAssociatedPart);
            found = true;
        }

        return found;
    }
    /**
     * updateAssociatedPart updates the associated part
     * finds the correct associatedPart to update then updates it.
     * @param selectedAssociatedPart gets the associatedPart to update.
     */
    public void updateAssociatedPart(Part selectedAssociatedPart){
        for(int i = 0; i < associatedParts.size() ; i++){
            if(associatedParts.get(i).getId() == selectedAssociatedPart.getId()) {
                associatedParts.set(i ,selectedAssociatedPart);
            }
        }
    }

    /**
     * getAllAssociatedParts gets all associated parts of the product.
     * @return an ObservableList of the associatedParts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}

