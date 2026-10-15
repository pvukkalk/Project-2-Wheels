public class Sedan extends Vehicles{
    public Sedan(String vin, String color, String model, int year) {
        super(vin, color, model, year);
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [VIN: " + getVin() + ", Model: " + getModel() + 
            ", Color: " + getColor() + ", Year: " + getYear() + ", Price: $" + getPrice() + 
            ", Mileage: " + getMileage() + ", Accidents: " + (hasAccidents() ? "Yes" : "No") + "]";
    }
}
