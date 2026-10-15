import java.util.Scanner;

public class Main {
    private static LinkedChain<Object> inventory = new LinkedChain<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedData();
        boolean running = true;
        System.out.println("=== Welcome to the Vehicle & Trailer Inventory System ===");

        while (running) {
            System.out.println("\nSelect user type:");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Enter choice (1-3): ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                runAdmin();
            } else if (input.equals("2")) {
                runCustomer();
            } else if (input.equals("3")) {
                running = false;
                System.out.println("Exiting application. Goodbye!");
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private static void seedData() {
        Sedan sedan = new Sedan("VIN101", "Silver", "Camry", 2021);
        sedan.setPrice(22000.0);
        sedan.setMileage(15000);

        SUV suv = new SUV("VIN102", "Black", "Explorer", 2022, 5000);
        suv.setPrice(35000.0);
        suv.setMileage(8000);

        Trailer trailer = new Trailer("Cargo", 2500, 4);

        inventory.add(sedan);
        inventory.add(suv);
        inventory.add(trailer);
    }

    public static void runAdmin() {
    boolean adminActive = true;
    while (adminActive) {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. View All Inventory Details");
        System.out.println("2. Add a Sample Vehicle");
        System.out.println("3. Edit a Vehicle's Details");
        System.out.println("4. Remove Item by VIN");
        System.out.println("5. Return to Main Menu");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            printInventory();
        } else if (choice.equals("2")) {
            Sedan newSedan = new Sedan("VIN" + (System.currentTimeMillis() % 1000), "Blue", "Civic", 2023);
            newSedan.setPrice(24000.0);
            inventory.add(newSedan);
            System.out.println("Sample Sedan added successfully!");
        } else if (choice.equals("3")) {
            if (inventory.getNumOfEntries() == 0) {
                System.out.println("Inventory is empty.");
                continue;
            }
            
            // Re-prompt loop for selecting the vehicle index
            boolean selectingVehicle = true;
            while (selectingVehicle) {
                printInventory();
                System.out.print("Enter the index (0 to " + (inventory.getNumOfEntries() - 1) + ") of the vehicle to edit (or type -1 to cancel): ");
                try {
                    int index = Integer.parseInt(scanner.nextLine().trim());
                    if (index == -1) {
                        selectingVehicle = false;
                        break;
                    }
                    
                    Object item = inventory.getIndex(index);
                    if (item instanceof Vehicles) {
                        Vehicles v = (Vehicles) item;
                        
                        // Sub-menu to choose what attribute to edit
                        boolean editingAttributes = true;
                        while (editingAttributes) {
                            System.out.println("\n--- Editing: " + v.getModel() + " (VIN: " + v.getVin() + ") ---");
                            System.out.println("1. Edit Price (Current: $" + v.getPrice() + ")");
                            System.out.println("2. Edit Mileage (Current: " + v.getMileage() + " miles)");
                            System.out.println("3. Edit Accident Status (Current: " + (v.hasAccidents() ? "Yes" : "No") + ")");
                            System.out.println("4. Finish Editing this Vehicle");
                            System.out.print("Choose attribute to modify (1-4): ");
                            
                            String editChoice = scanner.nextLine().trim();
                            if (editChoice.equals("1")) {
                                System.out.print("Enter new price: ");
                                double price = Double.parseDouble(scanner.nextLine().trim());
                                v.setPrice(price);
                                System.out.println("Price updated successfully!");
                            } else if (editChoice.equals("2")) {
                                System.out.print("Enter new mileage: ");
                                int miles = Integer.parseInt(scanner.nextLine().trim());
                                v.setMileage(miles);
                                System.out.println("Mileage updated successfully!");
                            } else if (editChoice.equals("3")) {
                                System.out.print("Has the vehicle been in an accident? (true/false): ");
                                boolean accident = Boolean.parseBoolean(scanner.nextLine().trim());
                                v.setAccidents(accident);
                                System.out.println("Accident status updated successfully!");
                            } else if (editChoice.equals("4")) {
                                editingAttributes = false;
                            } else {
                                System.out.println("Invalid choice. Please choose a number between 1 and 4.");
                            }
                        }
                        selectingVehicle = false;
                    } else {
                        System.out.println("Selected item is not a vehicle or index is out of bounds. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        } else if (choice.equals("4")) {
            System.out.print("Enter VIN to remove: ");
            String vin = scanner.nextLine().trim();
            boolean removed = inventory.removeByVin(vin);
            if (removed) {
                System.out.println("Item successfully removed.");
            } else {
                System.out.println("Not found. No changes made.");
            }
        } else if (choice.equals("5")) {
            adminActive = false;
        } else {
            System.out.println("Invalid option.");
        }
    }
}

    public static void runCustomer() {
        boolean customerActive = true;
        while (customerActive) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View All Available Inventory");
            System.out.println("2. Search by VIN");
            System.out.println("3. Search by Model & Color");
            System.out.println("4. Filter by Price Range");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                printInventory();
            } else if (choice.equals("2")) {
                if (inventory.getNumOfEntries() == 0) {
                    System.out.println("There are no vehicles in inventory.");
                    continue;
                }
                
                boolean searching = true;
                while (searching) {
                    System.out.print("Enter VIN to search (or type 'cancel' to go back): ");
                    String vin = scanner.nextLine().trim();
                    
                    if (vin.equalsIgnoreCase("cancel")) {
                        searching = false;
                        break;
                    }
                    if (vin.isEmpty()) {
                        System.out.println("Invalid input. Please enter a valid VIN.");
                        continue;
                    }
                    
                    LinkedChain<Object> results = inventory.findByVin(vin);
                    printResults(results);
                    searching = false;
                }
            } else if (choice.equals("3")) {
                if (inventory.getNumOfEntries() == 0) {
                    System.out.println("There are no vehicles in inventory.");
                    continue;
                }
                System.out.print("Enter Model (or leave blank): ");
                String model = scanner.nextLine().trim();
                System.out.print("Enter Color (or leave blank): ");
                String color = scanner.nextLine().trim();

                LinkedChain<Object> results = inventory.searchByBrandModelColor(model, color);
                printResults(results);
            } else if (choice.equals("4")) {
                if (inventory.getNumOfEntries() == 0) {
                    System.out.println("There are no vehicles in inventory.");
                    continue;
                }
                
                boolean filtering = true;
                while (filtering) {
                    try {
                        System.out.print("Enter minimum price (or type -1 to cancel): ");
                        double low = Double.parseDouble(scanner.nextLine().trim());
                        if (low == -1) {
                            filtering = false;
                            break;
                        }
                        
                        System.out.print("Enter maximum price: ");
                        double high = Double.parseDouble(scanner.nextLine().trim());

                        if (low < 0 || high < 0 || low > high) {
                            System.out.println("Invalid price range. Minimum cannot be negative and must be less than or equal to maximum. Please try again.");
                            continue;
                        }

                        LinkedChain<Object> results = inventory.filterPrice("Vehicle", high, low);
                        if (results.getNumOfEntries() == 0) {
                            System.out.println("No items found in that price range.");
                        } else {
                            System.out.println("\nFound " + results.getNumOfEntries() + " matching item(s):");
                            for (int i = 0; i < results.getNumOfEntries(); i++) {
                                System.out.println("- " + results.getIndex(i).toString());
                            }
                        }
                        filtering = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number for prices.");
                    }
                }
            } else if (choice.equals("5")) {
                customerActive = false;
            } else {
                System.out.println("Invalid option. Please choose between 1 and 5.");
            }
        }
    }

    private static void printInventory() {
        if (inventory.getNumOfEntries() == 0) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("\n--- Current Inventory (" + inventory.getNumOfEntries() + " items) ---");
        for (int i = 0; i < inventory.getNumOfEntries(); i++) {
            System.out.println("[" + i + "] " + inventory.getIndex(i).toString());
        }
    }

    private static void printResults(LinkedChain<Object> results) {
        if (results.getNumOfEntries() == 0) {
            System.out.println("Not found.");
        } else {
            System.out.println("\nFound " + results.getNumOfEntries() + " matching item(s):");
            for (int i = 0; i < results.getNumOfEntries(); i++) {
                System.out.println("- " + results.getIndex(i).toString());
            }
        }
    }
}