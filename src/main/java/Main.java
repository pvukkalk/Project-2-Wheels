import java.time.Year;
import java.util.Scanner;

/**
 * Console interface for the Wheels dealership. Reads and validates all user
 * input here (re-prompting on bad input), then calls Dealership to do the work.
 */
public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static Dealership dealership = new Dealership();

    public static final String CANCEL = "cancel";

    public static void main(String[] args) {
        seedData(dealership);
        run();
    }

    /** Main menu loop. Ends on Exit or when input runs out. */
    public static void run() {
        System.out.println("=== Welcome to WHEELS: Vehicle & Trailer Inventory ===");
        while (true) {
            System.out.println("\nSelect user type:");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Enter choice (1-3): ");

            String input = readLine();
            if (input == null || input.equals("3")) {
                System.out.println("Exiting application. Goodbye!");
                return;
            } else if (input.equals("1")) {
                runAdmin();
            } else if (input.equals("2")) {
                runCustomer();
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    /** Fills the lot with sample inventory, oldest arrivals first. */
    public static void seedData(Dealership lot) {
        if (lot == null) {
            return;
        }
        Sedan sedan = new Sedan("VIN101", "Toyota", "Silver", "Camry", 2021);
        sedan.setPrice(22000.0);
        sedan.setMileage(15000);

        SUV suv = new SUV("VIN102", "Ford", "Black", "Explorer", 2022, 5000);
        suv.setPrice(35000.0);
        suv.setMileage(8000);

        Pickup pickup = new Pickup("VIN103", "Ford", "Red", "F-150", 2020, 11000);
        pickup.setPrice(41000.0);
        pickup.setMileage(30000);
        pickup.setAccidents(true);

        Trailer cargo = new Trailer("TRL201", "Cargo", 2500, 4);
        cargo.setPrice(6500.0);

        Trailer hauler = new Trailer("TRL202", "Car Hauler", 7000, 4);
        hauler.setPrice(9800.0);

        lot.addVehicle(sedan);
        lot.addVehicle(suv);
        lot.addVehicle(pickup);
        lot.addTrailer(cargo);
        lot.addTrailer(hauler);
    }

    // ---- admin ----

    public static void runAdmin() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Inventory");
            System.out.println("2. Browse Inventory (Swipe)");
            System.out.println("3. Add a Vehicle");
            System.out.println("4. Add a Trailer");
            System.out.println("5. Edit a Vehicle's Details");
            System.out.println("6. Sell / Remove Item by VIN");
            System.out.println("7. View Sales History");
            System.out.println("8. Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = readLine();
            if (choice == null || choice.equals("8")) {
                return;
            } else if (choice.equals("1")) {
                printInventory();
            } else if (choice.equals("2")) {
                browse();
            } else if (choice.equals("3")) {
                addVehicle();
            } else if (choice.equals("4")) {
                addTrailer();
            } else if (choice.equals("5")) {
                editVehicle();
            } else if (choice.equals("6")) {
                sellItem();
            } else if (choice.equals("7")) {
                printSalesHistory();
            } else {
                System.out.println("Invalid option. Please choose between 1 and 8.");
            }
        }
    }

    private static void addVehicle() {
        Integer type = promptInt("Vehicle type - 1. Sedan  2. SUV  3. Pickup (or 'cancel'): ",
            1, 3);
        if (type == null) {
            System.out.println("Cancelled. No vehicle added.");
            return;
        }
        String vin = promptNewVin();
        String brand = vin == null ? null : promptText("Brand: ");
        String model = brand == null ? null : promptText("Model: ");
        String color = model == null ? null : promptText("Color: ");
        Integer year = color == null ? null
            : promptInt("Year: ", Vehicles.MIN_YEAR, Year.now().getValue() + 1);
        Double price = year == null ? null : promptDouble("Price: ", 0);
        Integer mileage = price == null ? null
            : promptInt("Mileage: ", 0, Integer.MAX_VALUE);
        Boolean accidents = mileage == null ? null
            : promptYesNo("Has it been in an accident? (yes/no): ");
        Integer towing = 0;
        if (accidents != null && type != 1) {
            towing = promptInt("Towing capacity (lbs): ", 0, Integer.MAX_VALUE);
        }
        if (accidents == null || towing == null) {
            System.out.println("Cancelled. No vehicle added.");
            return;
        }

        Vehicles vehicle;
        if (type == 1) {
            vehicle = new Sedan(vin, brand, color, model, year);
        } else if (type == 2) {
            vehicle = new SUV(vin, brand, color, model, year, towing);
        } else {
            vehicle = new Pickup(vin, brand, color, model, year, towing);
        }
        vehicle.setPrice(price);
        vehicle.setMileage(mileage);
        vehicle.setAccidents(accidents);
        dealership.addVehicle(vehicle);
        System.out.println("Vehicle added: " + vehicle);
    }

    private static void addTrailer() {
        String vin = promptNewVin();
        String type = vin == null ? null : promptText("Trailer type: ");
        Integer weight = type == null ? null
            : promptInt("Weight (lbs): ", 1, Integer.MAX_VALUE);
        Integer wheels = weight == null ? null : promptInt("Wheels: ", 1, Integer.MAX_VALUE);
        Double price = wheels == null ? null : promptDouble("Price: ", 0);
        if (price == null) {
            System.out.println("Cancelled. No trailer added.");
            return;
        }
        Trailer trailer = new Trailer(vin, type, weight, wheels);
        trailer.setPrice(price);
        dealership.addTrailer(trailer);
        System.out.println("Trailer added: " + trailer);
    }

    private static void editVehicle() {
        if (dealership.getVehicles().isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        Vehicles v = null;
        while (v == null) {
            String vin = promptText("Enter the VIN of the vehicle to edit (or 'cancel'): ");
            if (vin == null) {
                return;
            }
            v = dealership.findByVin(vin);
            if (v == null) {
                System.out.println("Not found. Please try again.");
            }
        }

        while (true) {
            System.out.println("\n--- Editing: " + v.getBrand() + " " + v.getModel()
                + " (VIN: " + v.getVin() + ") ---");
            System.out.println("1. Edit Price (Current: $" + String.format("%.2f", v.getPrice())
                + ")");
            System.out.println("2. Edit Mileage (Current: " + v.getMileage() + " miles)");
            System.out.println("3. Edit Accident Status (Current: "
                + (v.hasAccidents() ? "Yes" : "No") + ")");
            System.out.println("4. Finish Editing this Vehicle");
            System.out.print("Choose attribute to modify (1-4): ");

            String choice = readLine();
            if (choice == null || choice.equals("4")) {
                return;
            } else if (choice.equals("1")) {
                Double price = promptDouble("Enter new price (or 'cancel'): ", 0);
                if (price != null) {
                    v.setPrice(price);
                    System.out.println("Price updated successfully!");
                }
            } else if (choice.equals("2")) {
                Integer miles = promptInt("Enter new mileage (or 'cancel'): ", 0,
                    Integer.MAX_VALUE);
                if (miles != null) {
                    v.setMileage(miles);
                    System.out.println("Mileage updated successfully!");
                }
            } else if (choice.equals("3")) {
                Boolean accident = promptYesNo("Has the vehicle been in an accident? (yes/no): ");
                if (accident != null) {
                    v.setAccidents(accident);
                    System.out.println("Accident status updated successfully!");
                }
            } else {
                System.out.println("Invalid choice. Please choose a number between 1 and 4.");
            }
        }
    }

    private static void sellItem() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NOTHING_TO_REMOVE);
            return;
        }
        String vin = promptText("Enter VIN of the item sold (or 'cancel'): ");
        if (vin == null) {
            return;
        }
        try {
            if (dealership.removeByVin(vin)) {
                System.out.println("Item successfully sold and removed.");
            } else {
                System.out.println("Not found. No changes made.");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printSalesHistory() {
        LinkedChain<String> log = dealership.getSalesLog();
        if (log.isEmpty()) {
            System.out.println("No sales recorded yet.");
            return;
        }
        System.out.println("\n--- Sales History (" + log.getNumOfEntries() + ") ---");
        for (int i = 0; i < log.getNumOfEntries(); i++) {
            System.out.println((i + 1) + ". " + log.getIndex(i));
        }
    }

    // ---- customer ----

    public static void runCustomer() {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View All Inventory");
            System.out.println("2. Browse Inventory (Swipe)");
            System.out.println("3. Search by VIN");
            System.out.println("4. Search by Brand, Model & Color");
            System.out.println("5. Filter by Price Range");
            System.out.println("6. Filter by Accident History");
            System.out.println("7. Find Vehicles That Can Tow");
            System.out.println("8. Find Trailers a Vehicle Can Tow");
            System.out.println("9. Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = readLine();
            if (choice == null || choice.equals("9")) {
                return;
            } else if (choice.equals("1")) {
                printInventory();
            } else if (choice.equals("2")) {
                browse();
            } else if (choice.equals("3")) {
                searchByVin();
            } else if (choice.equals("4")) {
                searchByBrandModelColor();
            } else if (choice.equals("5")) {
                filterByPrice();
            } else if (choice.equals("6")) {
                filterByAccidents();
            } else if (choice.equals("7")) {
                filterCanTow();
            } else if (choice.equals("8")) {
                findCompatibleTrailers();
            } else {
                System.out.println("Invalid option. Please choose between 1 and 9.");
            }
        }
    }

    private static void searchByVin() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        String vin = promptText("Enter VIN to search (or 'cancel'): ");
        if (vin == null) {
            return;
        }
        Object found = dealership.findByVin(vin);
        if (found == null) {
            found = dealership.findTrailerByVin(vin);
        }
        System.out.println(found == null ? "Not found." : "Found: " + found);
    }

    private static void searchByBrandModelColor() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        String brand = promptOptional("Enter Brand (or leave blank): ");
        String model = brand == null ? null : promptOptional("Enter Model (or leave blank): ");
        String color = model == null ? null : promptOptional("Enter Color (or leave blank): ");
        if (color == null) {
            return;
        }
        printResults(dealership.searchByBrandModelColor(brand, model, color));
    }

    private static void filterByPrice() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        while (true) {
            Double low = promptDouble("Enter minimum price (or 'cancel'): ", -Double.MAX_VALUE);
            Double high = low == null ? null
                : promptDouble("Enter maximum price: ", -Double.MAX_VALUE);
            if (high == null) {
                return;
            }
            try {
                LinkedChain<Vehicles> cars = dealership.filterPrice(low, high);
                LinkedChain<Trailer> trailers = dealership.filterTrailerPrice(low, high);
                if (cars.isEmpty() && trailers.isEmpty()) {
                    System.out.println("No items found in that price range.");
                } else {
                    printResults(cars);
                    printResults(trailers);
                }
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Please try again.");
            }
        }
    }

    private static void filterByAccidents() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        Boolean hadAccident = promptYesNo(
            "Show vehicles that HAVE been in an accident? (yes/no): ");
        if (hadAccident != null) {
            printResults(dealership.filterAccidents(hadAccident));
        }
    }

    private static void filterCanTow() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        while (true) {
            Integer capacity = promptInt("Minimum towing capacity in lbs (or 'cancel'): ",
                Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (capacity == null) {
                return;
            }
            try {
                printResults(dealership.filterCanTow(capacity));
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Please try again.");
            }
        }
    }

    private static void findCompatibleTrailers() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        String vin = promptText("Enter the VIN of your vehicle (or 'cancel'): ");
        if (vin == null) {
            return;
        }
        Vehicles vehicle = dealership.findByVin(vin);
        if (vehicle == null) {
            System.out.println("Not found.");
        } else if (!vehicle.getCanTow()) {
            System.out.println("That vehicle cannot tow trailers.");
        } else {
            LinkedChain<Trailer> matches = dealership.compatibleTrailers(vehicle);
            if (matches.isEmpty()) {
                System.out.println("No trailers on the lot are light enough for that vehicle.");
            } else {
                printResults(matches);
            }
        }
    }

    // ---- shared views ----

    /** Shows one item at a time, oldest first, moving forward and back. */
    private static void browse() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        int position = 0;
        while (true) {
            int total = dealership.getTotalItems();
            System.out.println("\n[" + (position + 1) + " of " + total + "] "
                + dealership.getItem(position));
            System.out.print("(N)ext, (P)revious, or (Q)uit browsing: ");

            String input = readLine();
            if (input == null) {
                return;
            }
            input = input.toLowerCase();
            if (input.equals("n") || input.equals("next")) {
                if (position < total - 1) {
                    position++;
                } else {
                    System.out.println("You've reached the newest item on the lot.");
                }
            } else if (input.equals("p") || input.equals("previous")) {
                if (position > 0) {
                    position--;
                } else {
                    System.out.println("You're already at the oldest item on the lot.");
                }
            } else if (input.equals("q") || input.equals("quit")) {
                return;
            } else {
                System.out.println("Invalid choice. Enter N, P, or Q.");
            }
        }
    }

    private static void printInventory() {
        if (dealership.isEmpty()) {
            System.out.println(Dealership.NO_INVENTORY);
            return;
        }
        LinkedChain<Vehicles> vehicles = dealership.getVehicles();
        LinkedChain<Trailer> trailers = dealership.getTrailers();
        System.out.println("\n--- Vehicles (" + vehicles.getNumOfEntries() + ") ---");
        for (int i = 0; i < vehicles.getNumOfEntries(); i++) {
            System.out.println("[" + i + "] " + vehicles.getIndex(i));
        }
        System.out.println("--- Trailers (" + trailers.getNumOfEntries() + ") ---");
        for (int i = 0; i < trailers.getNumOfEntries(); i++) {
            System.out.println("[" + i + "] " + trailers.getIndex(i));
        }
    }

    private static <E> void printResults(LinkedChain<E> results) {
        if (results.isEmpty()) {
            System.out.println("Not found.");
            return;
        }
        System.out.println("\nFound " + results.getNumOfEntries() + " matching item(s):");
        for (int i = 0; i < results.getNumOfEntries(); i++) {
            System.out.println("- " + results.getIndex(i));
        }
    }

    // ---- input helpers (null means the user cancelled or input ran out) ----

    /** Next trimmed line, or null if there is no more input. */
    private static String readLine() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    /** Re-prompts until the user types something that isn't blank. */
    private static String promptText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = readLine();
            if (input == null || input.equalsIgnoreCase(CANCEL)) {
                return null;
            }
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Invalid input. This field cannot be blank.");
        }
    }

    /** Like promptText but blank is allowed (returns ""). */
    private static String promptOptional(String prompt) {
        System.out.print(prompt);
        String input = readLine();
        if (input == null || input.equalsIgnoreCase(CANCEL)) {
            return null;
        }
        return input;
    }

    /** Re-prompts until the user types a VIN that is not already on the lot. */
    private static String promptNewVin() {
        while (true) {
            String vin = promptText("VIN (or 'cancel'): ");
            if (vin == null || !dealership.containsVin(vin)) {
                return vin;
            }
            System.out.println("An item with that VIN is already on the lot. Please try again.");
        }
    }

    /** Re-prompts until the user types a whole number from min to max. */
    private static Integer promptInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = readLine();
            if (input == null || input.equalsIgnoreCase(CANCEL)) {
                return null;
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /** Re-prompts until the user types a number that is at least min. */
    private static Double promptDouble(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String input = readLine();
            if (input == null || input.equalsIgnoreCase(CANCEL)) {
                return null;
            }
            try {
                double value = Double.parseDouble(input);
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    System.out.println("Invalid input. Please enter a valid number.");
                } else if (value < min) {
                    System.out.println("Value cannot be less than " + (int) min + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /** Re-prompts until the user answers yes/no (or true/false, y/n). */
    private static Boolean promptYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = readLine();
            if (input == null || input.equalsIgnoreCase(CANCEL)) {
                return null;
            }
            input = input.toLowerCase();
            if (input.equals("yes") || input.equals("y") || input.equals("true")) {
                return true;
            }
            if (input.equals("no") || input.equals("n") || input.equals("false")) {
                return false;
            }
            System.out.println("Invalid input. Please answer yes or no.");
        }
    }
}
