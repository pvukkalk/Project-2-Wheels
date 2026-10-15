/**
 * A trailer on the lot. Trailers have a VIN and price so they can be
 * found and sold the same way vehicles are.
 */
public class Trailer {
    private String vin;
    private String type;
    private int weight;
    private int wheels;
    private double price;

    public Trailer(String vin, String type, int weight, int wheels) {
        this.vin = Vehicles.requireText(vin, "VIN");
        this.type = Vehicles.requireText(type, "Type");
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        if (wheels <= 0) {
            throw new IllegalArgumentException("Wheels must be positive");
        }
        this.weight = weight;
        this.wheels = wheels;
        this.price = 0.0;
    }

    public void setPrice(double price) {
        if (price < 0 || Double.isNaN(price) || Double.isInfinite(price)) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public String getVin() { return vin; }
    public String getType() { return type; }
    public int getWeight() { return weight; }
    public int getWheels() { return wheels; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Trailer [VIN: " + vin + ", Type: " + type + ", Weight: " + weight
            + " lbs, Wheels: " + wheels + ", Price: $" + String.format("%.2f", price) + "]";
    }
}
