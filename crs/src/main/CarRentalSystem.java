package main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/** Rental Car Management System */

public class CarRentalSystem {

    /** FIELDS */
    private static final String BINARY_FILE_DAT = "carrentalsystem.dat";

    private HashMap<String, String> rentedCars = new HashMap<>(100, 0.5f);
    private HashMap<String, RentedCars> owners = new HashMap<>(100, 0.5f);

    /** METHODS */
    private static String getPlateNo(Scanner sc) {
        return sc.nextLine();
    }

    private static String getOwnerName(Scanner sc) {
        return sc.nextLine();
    }

    // search for a key in hashtable
    private boolean isCarRent(String plateNo) {
        return rentedCars.containsKey(plateNo);
    }

    // get the value associated to a key
    private String getCarRent(String plateNo) {
        return rentedCars.get(plateNo);
    }

    // add a new (key, value) pair
    private void rentCar(String plateNo, String ownerName) {
        rentedCars.put(plateNo, ownerName);
        RentedCars current = owners.get(ownerName);

        if (current == null) {
            RentedCars carList = new RentedCars();
            carList.add(plateNo);
            owners.put(ownerName, carList);
        }
        else current.add(plateNo);
    }

    // remove an existing (key, value) pair
    private String returnCar(String plateNo) {
        String currentOwner = "";
        for (Map.Entry<String, String> entry : rentedCars.entrySet())
            if (entry.getKey().equals(plateNo)) currentOwner = entry.getValue();

        owners.get(currentOwner).remove(plateNo);
        return rentedCars.remove(plateNo);
    }

    // get the total number of rented cars
    private int totalCarsRented() {
        return rentedCars.size();
    }

    // get the number of cars rented by a specific owner
    private void getCarsNo(String ownerName) {
        RentedCars result = owners.get(ownerName);
        if (result == null) System.out.printf("There are no cars rented by [%s].%n", ownerName);
        else System.out.println(result.size());
    }

    // get the list of cars rented by a specific owner
    private void getCarsList(String ownerName) {
        RentedCars result = owners.get(ownerName);
        if (result == null) System.out.printf("There are no cars rented by [%s].%n", ownerName);
        else System.out.println(result.showCars());
    }

    // write data to a binary file
    private void writeToBinaryFile() throws IOException {
        try (ObjectOutputStream binaryFileOut =
                     new ObjectOutputStream(
                             new BufferedOutputStream(
                                     new FileOutputStream(BINARY_FILE_DAT)))) {

            binaryFileOut.writeObject(rentedCars);
            binaryFileOut.writeObject(owners);
        }
    }

    // read previous stored data from a binary file
    private void readFromBinaryFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream binaryFileIn =
                     new ObjectInputStream(
                             new BufferedInputStream(
                                     new FileInputStream(BINARY_FILE_DAT)))) {

            rentedCars = (HashMap<String, String>) binaryFileIn.readObject();
            owners = (HashMap<String, RentedCars>) binaryFileIn.readObject();
        }
    }

    // reset the CRS application
    private void resetBinaryFile() throws IOException {
        this.rentedCars = new HashMap<>(100, 0.5f);
        this.owners = new HashMap<>(100, 0.5f);
        writeToBinaryFile();
    }

    // display a list of commands
    private static void printCommandsList() {
        System.out.println("""
                        help          - Display the list of commands
                        add           - Rent a car
                        check         - Check if a car is rented or not
                        remove        - Return a rented car
                        getOwner      - The current owner of a car
                        totalRented   - The total number of rented cars
                        getCarsNo     - The number of cars rented by a specific owner
                        getCarsList   - The list of cars rented by a specific owner
                        save          - Save application data to a binary file
                        restore       - Restore the application data from binary file
                        reset         - Reset the CRS application
                        quit          - Close the CRS application""");
    }

    public void run(Scanner sc) {
        boolean quit = false;

        while (!quit) {
            String command = sc.nextLine();

            switch (command) {
                case "help" -> printCommandsList();
                case "add" -> {
                    String plateNo = getPlateNo(sc);
                    String ownerName = getOwnerName(sc);
                    if (isCarRent(plateNo)) {
                        String current = getCarRent(plateNo);
                        System.out.printf("The %s car was already rented by %s.%n", plateNo, current);
                    }
                    else rentCar(plateNo, ownerName);
                }
                case "check" -> {
                    String plateNo = getPlateNo(sc);
                    boolean carRent = isCarRent(plateNo);

                    if (!carRent) {
                        System.out.println("The wanted car was not rented.");
                    }
                    else {
                        String current = getCarRent(plateNo);
                        System.out.printf("The %s car was rented by %s.%n", plateNo, current);
                    }
                }
                case "remove" -> {
                    String plateNo = getPlateNo(sc);
                    boolean carRent = isCarRent(plateNo);

                    if (!carRent) {
                        System.out.println("The car was not found. No changes were made.");
                    }
                    else {
                        String current = returnCar(plateNo);
                        System.out.printf("The %s car rented by %s was returned.%n", plateNo, current);
                    }
                }
                case "getOwner" -> {
                    String plateNo = getPlateNo(sc);
                    String owner = getCarRent(plateNo);

                    if (owner == null) System.out.printf("The %s car was not rented.%n", plateNo);
                    else System.out.println(owner);
                }
                case "save" -> {
                    try {
                        writeToBinaryFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "restore" -> {
                    try {
                        readFromBinaryFile();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "reset" -> {
                    try {
                        resetBinaryFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "totalRented" -> System.out.println(totalCarsRented());
                case "getCarsNo" -> getCarsNo(getOwnerName(sc));
                case "getCarsList" -> getCarsList(getOwnerName(sc));
                case "quit" -> {
                    System.out.println("The application is closing...");
                    quit = true;
                }
                default -> {
                    System.out.println("Unknown command. Try again.");
                    printCommandsList();
                }
            }
        }
    }
}