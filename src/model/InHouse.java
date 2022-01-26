package model;
/**
 * class InHouse.java
 */

/**
 * @author Justin Simons
 */

/**
 * InHouse class extends Part class. it stores any InHouse Part data.
 */
public class InHouse extends Part{
    private int machineId;

    /**
     * InHouse method constructor sets all InHouse part data
     * first sends id,name,price,stock,min, and max to super(Part) constructor
     * sets the machineId.
     * @param id grabs id to set
     * @param name grabs name to set
     * @param price grabs price to set
     * @param stock grabs stock to set
     * @param min grabs min to set
     * @param max grabs  max to set
     * @param machineId grabs machineId to set
     */
    public InHouse(int id, String name,double price, int stock, int min, int max, int machineId){
        super(id,name,price,stock,min,max);
        this.machineId = machineId;
    }

    /**
     * setMachineId sets machineId
     * @param machineId grabs machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * getMachineId gets machineId
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }
}

