package main;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/** The class keeps track of an owner's number of cars. */

public class RentedCars implements Serializable {
    /** FIELDS */
    // version ID used for serialization
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final ArrayList<String> cars;

    /** CONSTRUCTOR */
    public RentedCars() {
        this.cars = new ArrayList<>();
    }

    /** METHODS */
    public void add(String plateNo) {
        cars.add(plateNo);
    }

    public void remove(String plateNo) {
        cars.remove(plateNo);
    }

    public int size() {
        return cars.size();
    }

    public String showCars() {
        return cars.toString();
    }
}