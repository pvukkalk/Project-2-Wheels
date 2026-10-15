/**
 * Shared attributes for every vehicle type on the lot (Sedan, SUV, Pickup).
 * VIN, brand, color, model, and year are fixed once the vehicle is created.
 */
public class Vehicles {
    public static final int MIN_YEAR = 1886;

    private String vin;
    private String brand;
    private String color;
    private String model;
    private boolean canTow;
    private int year;
    private int mileage;
    private boolean accidents;
    private double price;

    public Vehicles(String vin, String brand, String color, String model, int year) {
        this.vin = requireText(vin, "VIN");
        this.brand = requireText(brand, "Brand");
        this.color = requireText(color, "Color");
        this.model = requireText(model, "Model");
        if (year < MIN_YEAR) {
            throw new IllegalArgumentException("Year must be " + MIN_YEAR + " or later");
        }
        this.year = year;
        this.canTow = false;
        this.mileage = 0;
        this.accidents = false;
        this.price = 0.0;
    }

    /** Trims value, or throws if it is null or blank. */
    static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value.trim();
    }

    public void setMileage(int miles) {
        if (miles < 0) {
            throw new IllegalArgumentException("Mileage cannot be negative");
        }
        this.mileage = miles;
    }

    public void setAccidents(boolean accident) {
        this.accidents = accident;
    }

    public void setPrice(double price) {
        if (price < 0 || Double.isNaN(price) || Double.isInfinite(price)) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public String getVin() { return vin; }
    public String getBrand() { return brand; }
    public String getColor() { return color; }
    public String getModel() { return model; }
    public int getMileage() { return mileage; }
    public double getPrice() { return price; }
    public boolean getCanTow() { return canTow; }
    public int getYear() { return year; }
    public boolean hasAccidents() { return accidents; }

    /** Pounds this vehicle can tow. Vehicles that cannot tow return 0. */
    public int getTowingCapacity() {
        return 0;
    }

    /** True if this vehicle can tow the given trailer. */
    public boolean canTow(Trailer trailer) {
        return trailer != null && getCanTow() && getTowingCapacity() >= trailer.getWeight();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [VIN: " + getVin() + ", Brand: " + getBrand()
            + ", Model: " + getModel() + ", Color: " + getColor() + ", Year: " + getYear()
            + ", Price: $" + String.format("%.2f", getPrice()) + ", Mileage: " + getMileage()
            + ", Accidents: " + (hasAccidents() ? "Yes" : "No") + "]";
    }
}
