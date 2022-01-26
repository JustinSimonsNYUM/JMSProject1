package model;
/**
 * class Outsourced.java
 */

/**
 * @author Justin Simons
 */

/**
 * Outsourced class extends Part class. it stores any Outsourced Part data.
 */
public class Outsourced extends Part{
    private String companyName;
    /**
     * Outsourced method constructor sets all Outsourced part data
     * first sends id,name,price,stock,min,and max to super(Part) constructor
     * sets the companyName.
     * @param id grabs id to set
     * @param name grabs name to set
     * @param price grabs price to set
     * @param stock grabs stock to set
     * @param min grabs min to set
     * @param max grabs  max to set
     * @param companyName grabs companyName to set
     */
    public Outsourced(int id, String name,double price, int stock, int min, int max, String companyName){
        super(id,name,price,stock,min,max);
        this.companyName = companyName;
    }
    /**
     * setCompanyName sets companyName
     * @param companyName grabs companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    /**
     * getCompanyName gets companyName
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
