package model;
/**
 * class Inventory.java
 */

/**
 * @author Justin Simons
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory class holds the all the data for both parts and products
 */

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static Part modifyPart;
    private static Product modifyProduct;

    /**
     * addPart adds a new part to the list ObservableList of allParts
     * @param newPart gets the new part
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    /**
     * addProduct adds a new Product to the list ObservableList of allProduct
     * @param newProduct gets the new Product
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * lookupPart method looks for parts by id.
     * searches all part ids' to see if they match the partId sent to this method.
     * if any part ids' match the integer sent to method, it's set to newPart.
     * if no part matches, then newPart is assigned null.
     * @param partId grabs an integer sent to method
     * @return the newPart
     */
    public static Part lookupPart(int partId){
        Part oldPart;
        Part newPart = allParts.get(0);
        boolean bool = true;
        for (Part allPart : allParts) {
            oldPart = allPart;
            if (oldPart.getId() == partId) {
                newPart = allPart;
                bool = false;
            }
        }
        if(bool){
            newPart = null;
        }
        return newPart;
    }
    /**
     * lookupProduct method looks for products by id.
     * searches all product ids' to see if they match the productId sent to this method.
     * if any product ids' match the integer sent to method, it's set to newProduct.
     * if no product matches, then newProduct is assigned null.
     * @param productId grabs an integer sent to method
     * @return the newProduct
     */
        public static Product lookupProduct(int productId){
            Product oldProduct;
            Product newProduct = allProducts.get(0);
            boolean bool = true;
            for (Product allProduct : allProducts) {
                oldProduct = allProduct;
                if (oldProduct.getId() == productId) {
                    newProduct = allProduct;
                    bool = false;
                }
            }
            if(bool){
                newProduct = null;
            }

            return newProduct;
        }
    /**
     * lookupPart overloaded method looks for parts by name.
     * searches all part names' to see if any match the partName sent to this method.
     * if any part name matches the String sent to method, the parts are set to tempList.
     * if no part matches, then tempList is assigned null.
     * @param partName grabs an String sent to method
     * @return the tempList
     */
        public static ObservableList<Part> lookupPart(String partName){
            ObservableList<Part> tempList = FXCollections.observableArrayList();
            boolean found = false;
            for (Part tempPart : allParts) {
                String tempSt = tempPart.getName();
                if (tempSt.toLowerCase().contains(partName.toLowerCase())) {
                    tempList.add(tempPart);
                    found = true;
                }
            }
            if(!found)
                tempList = null;
          return tempList;
        }
    /**
     * lookupProduct overloaded method looks for products by names.
     * searches all product names' to see if any match the productName sent to this method.
     * if any product name matches the String sent to method, the products are set to tempList.
     * if no product matches, then tempList is assigned null.
     * @param productName grabs an String sent to method.
     * @return the tempList
     */
        public static ObservableList<Product> lookupProduct(String productName){
            ObservableList<Product> tempList = FXCollections.observableArrayList();
            boolean found = true;
            for (Product tempProduct : allProducts) {
                String tempSt = tempProduct.getName();
                if (tempSt.toLowerCase().contains(productName.toLowerCase())) {
                    tempList.add(tempProduct);
                    found = false;
                }
            }
            if(found)
                tempList = null;
            return tempList;
        }

    /**
     * setModifyPart method sets the part that will be sent to ModifyPart to be changed.
     * if the part sent to this method has an instance of InHouse, a new InHouse object with the
     *      part data sent to the method is made and set to modifyPart
     * if the part sent to this method has an instance of Outsourced, a new Outsourced object with the
     *      part data sent to the method is made and set to modifyPart
     * @param part gets the part
     */
    public static void setModifyPart(Part part){
        if(part instanceof InHouse)
            modifyPart = new InHouse(part.getId(),part.getName(),part.getPrice(),part.getStock(),part.getMin(),part.getMax(),((InHouse) part).getMachineId());
        if(part instanceof Outsourced)
            modifyPart = new Outsourced(part.getId(),part.getName(),part.getPrice(),part.getStock(),part.getMin(),part.getMax(),((Outsourced) part).getCompanyName());
    }

    /**
     * getModifyPart gets the modifyPart to send it to ModifyPart control to be changed
     * @return the modifyPart
     */
    public static Part getModifyPart(){
        return modifyPart;
    }

    /**
     * updatePart method updates the part sent to it.
     * finds the part with the corresponding id that was sent to the method. then sets the new part
     *      in the old parts place.
     * then it looks through all associated parts of every product.
     *      if a product's associated parts contains the part sent to method, then it is also changed
     *      to the new part.
     * @param index gets the index of the part to replace.
     * @param selectedPart gets the new part to replace the old.
     */
    public static void updatePart(int index, Part selectedPart){
        for(Part part : allParts){
            if(part.getId() == selectedPart.getId()) {
                allParts.set(index,selectedPart);
            }
        }

        ObservableList<Product> products = Inventory.getAllProducts();
        ObservableList<Part> associatedParts;
        for (Product tempProduct : products) {
            associatedParts = tempProduct.getAllAssociatedParts();
            for (Part associatedPart : associatedParts) {
                int partId = associatedPart.getId();
                if (partId == selectedPart.getId())
                    tempProduct.updateAssociatedPart(selectedPart);
            }
        }


    }
    /**
     * setModifyProduct method sets the product that will be sent to ModifyProduct to be changed.
     * creates a new Product with the data from the product sent to method and sets it to modifyProduct.
     * @param product gets the product
     */
    public static void setModifyProduct(Product product){
            modifyProduct = new Product(product.getId(),product.getName(),product.getPrice(),
                    product.getStock(),product.getMin(),product.getMax());
         }
    /**
     * getModifyProduct gets the modifyProduct to send it to ModifyProduct to be changed
     * @return the modifyProduct
     */
    public static Product getModifyProduct(){
        return modifyProduct;
    }
    /**
     * updateProduct method updates the product sent to it.
     * finds the product with the corresponding id that was sent to the method. then sets the new product
     *      in the old products place.
     * @param index gets the index of the product to replace.
     * @param newProduct gets the new product to replace the old.
     */
    public static void updateProduct(int index, Product newProduct){
        for(Product product : allProducts){
            if(product.getId() == newProduct.getId()) {
                allProducts.set(index ,newProduct);
            }
        }
    }

    /**
     * deletePart method deletes the part, sent to the method, from allParts.
     * if allParts contains the part sent to method, it's deleted and found is set to true
     * if it's not found, found is set to false
     * @param selectedPart gets the part to delete from allParts
     * @return the boolean found which states if the the part was found and deleted or not
     */
    public static boolean deletePart(Part selectedPart){
        boolean found = false;

        if(allParts.contains(selectedPart)){
            allParts.remove(selectedPart);
            found = true;
        }

        return found;
    }
    /**
     * deleteProduct method deletes the product, sent to the method, from allProducts.
     * if allProducts contains the product sent to method, it's deleted and found is set to true
     * if it's not found, found is set to false
     * @param selectedProduct gets the product to delete from allProducts
     * @return the boolean found which states if the the product was found and deleted or not
     */
    public static boolean deleteProduct(Product selectedProduct){
        boolean found = false;

        if(allProducts.contains(selectedProduct)){
            allProducts.remove(selectedProduct);
            found = true;
        }

        return found;
    }

    /**
     * gets the list of all parts
     * @return the allParts ObservableList
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * gets the list of all products
     * @return the allProducts ObservableList
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }


}

