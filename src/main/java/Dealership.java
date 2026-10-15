/**
 * The dealership lot. Stores vehicles, trailers, and a sales log in three
 * LinkedChains and holds all the searching, filtering, and selling logic.
 * It only uses LinkedChain through its public methods.
 */
public class Dealership {
    public static final String NO_INVENTORY = "There are no vehicles in inventory.";
    public static final String NOTHING_TO_REMOVE = "There is nothing to remove.";

    private LinkedChain<Vehicles> vehicles;
    private LinkedChain<Trailer> trailers;
    private LinkedChain<String> salesLog;

    public Dealership() {
        vehicles = new LinkedChain<>();
        trailers = new LinkedChain<>();
        salesLog = new LinkedChain<>();
    }

    // ---- adding ----

    /** Adds a vehicle to the back of the lot. False if null or its VIN is taken. O(n) */
    public boolean addVehicle(Vehicles vehicle) {
        if (vehicle == null || containsVin(vehicle.getVin())) {
            return false;
        }
        return vehicles.add(vehicle);
    }

    /** Adds a trailer to the back of the lot. False if null or its VIN is taken. O(n) */
    public boolean addTrailer(Trailer trailer) {
        if (trailer == null || containsVin(trailer.getVin())) {
            return false;
        }
        return trailers.add(trailer);
    }

    // ---- lot info ----

    public LinkedChain<Vehicles> getVehicles() { return vehicles; }
    public LinkedChain<Trailer> getTrailers() { return trailers; }
    public LinkedChain<String> getSalesLog() { return salesLog; }

    /** O(1) */
    public int getTotalItems() {
        return vehicles.getNumOfEntries() + trailers.getNumOfEntries();
    }

    /** O(1) */
    public boolean isEmpty() {
        return getTotalItems() == 0;
    }

    /**
     * Returns the item at a position on the lot, oldest first: every vehicle,
     * then every trailer. Null if the position is out of bounds. O(n)
     */
    public Object getItem(int position) {
        int numVehicles = vehicles.getNumOfEntries();
        if (position < 0 || position >= getTotalItems()) {
            return null;
        }
        if (position < numVehicles) {
            return vehicles.getIndex(position);
        }
        return trailers.getIndex(position - numVehicles);
    }

    /** True if any vehicle or trailer has this VIN (ignoring case). O(n) */
    public boolean containsVin(String vin) {
        if (isBlank(vin)) {
            return false;
        }
        return !vehicles.filter(v -> sameVin(v.getVin(), vin)).isEmpty()
            || !trailers.filter(t -> sameVin(t.getVin(), vin)).isEmpty();
    }

    // ---- searching ----

    /**
     * Returns the vehicle with this VIN, or null if none matches or the VIN
     * is blank. Throws IllegalStateException if the lot is empty. O(n)
     */
    public Vehicles findByVin(String vin) {
        requireInventory();
        if (isBlank(vin)) {
            return null;
        }
        return vehicles.filter(v -> sameVin(v.getVin(), vin)).getIndex(0);
    }

    /** Same as findByVin, but for trailers. O(n) */
    public Trailer findTrailerByVin(String vin) {
        requireInventory();
        if (isBlank(vin)) {
            return null;
        }
        return trailers.filter(t -> sameVin(t.getVin(), vin)).getIndex(0);
    }

    /**
     * Returns vehicles matching every non-blank field, ignoring case.
     * A null or blank field matches anything. O(n)
     */
    public LinkedChain<Vehicles> searchByBrandModelColor(String brand, String model,
        String color) {
        requireInventory();
        return vehicles.filter(v -> matches(v.getBrand(), brand)
            && matches(v.getModel(), model) && matches(v.getColor(), color));
    }

    /**
     * Returns vehicles priced from low to high, inclusive. Throws
     * IllegalArgumentException if either price is negative or low > high. O(n)
     */
    public LinkedChain<Vehicles> filterPrice(double low, double high) {
        requireInventory();
        checkPriceRange(low, high);
        return vehicles.filter(v -> v.getPrice() >= low && v.getPrice() <= high);
    }

    /** Same as filterPrice, but for trailers. O(n) */
    public LinkedChain<Trailer> filterTrailerPrice(double low, double high) {
        requireInventory();
        checkPriceRange(low, high);
        return trailers.filter(t -> t.getPrice() >= low && t.getPrice() <= high);
    }

    /** Returns vehicles that have (true) or have not (false) been in an accident. O(n) */
    public LinkedChain<Vehicles> filterAccidents(boolean hasAccident) {
        requireInventory();
        return vehicles.filter(v -> v.hasAccidents() == hasAccident);
    }

    /**
     * Returns vehicles that can tow at least capacity pounds. Throws
     * IllegalArgumentException if capacity is negative. O(n)
     */
    public LinkedChain<Vehicles> filterCanTow(int capacity) {
        requireInventory();
        if (capacity < 0) {
            throw new IllegalArgumentException("Towing capacity cannot be negative.");
        }
        return vehicles.filter(v -> v.getCanTow() && v.getTowingCapacity() >= capacity);
    }

    /**
     * Stretch goal: returns every trailer on the lot the vehicle can tow.
     * Throws IllegalArgumentException if vehicle is null. O(n)
     */
    public LinkedChain<Trailer> compatibleTrailers(Vehicles vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }
        return trailers.filter(vehicle::canTow);
    }

    // ---- selling ----

    /**
     * Sells (removes) the vehicle or trailer with this VIN and records it in
     * the sales log. Returns false if the VIN is blank or not found, leaving
     * the lot unchanged. Throws IllegalStateException if the lot is empty. O(n)
     */
    public boolean removeByVin(String vin) {
        if (isEmpty()) {
            throw new IllegalStateException(NOTHING_TO_REMOVE);
        }
        if (isBlank(vin)) {
            return false;
        }
        Vehicles vehicle = vehicles.filter(v -> sameVin(v.getVin(), vin)).getIndex(0);
        if (vehicle != null) {
            vehicles.remove(vehicle);
            salesLog.add("SOLD " + vehicle);
            return true;
        }
        Trailer trailer = trailers.filter(t -> sameVin(t.getVin(), vin)).getIndex(0);
        if (trailer != null) {
            trailers.remove(trailer);
            salesLog.add("SOLD " + trailer);
            return true;
        }
        return false;
    }

    // ---- helpers ----

    private void requireInventory() {
        if (isEmpty()) {
            throw new IllegalStateException(NO_INVENTORY);
        }
    }

    private static void checkPriceRange(double low, double high) {
        if (Double.isNaN(low) || Double.isNaN(high) || low < 0 || high < 0 || low > high) {
            throw new IllegalArgumentException("Invalid price range. Prices cannot be "
                + "negative and the minimum must be less than or equal to the maximum.");
        }
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    private static boolean sameVin(String actual, String wanted) {
        return actual.equalsIgnoreCase(wanted.trim());
    }

    private static boolean matches(String actual, String wanted) {
        return isBlank(wanted) || actual.equalsIgnoreCase(wanted.trim());
    }
}
