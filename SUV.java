public class SUV extends Vehicles{
    private int towingCapacity;

    public SUV(String vin, String color, String model, int year, int towingCapacity) {
        super(vin, color, model, year);
        this.towingCapacity = towingCapacity;
        // canTow is set to true per spec
    }

    public int getTowingCapacity() {
        return towingCapacity;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [VIN: " + getVin() + ", Model: " + getModel() + 
            ", Color: " + getColor() + ", Year: " + getYear() + ", Price: $" + getPrice() + 
            ", Mileage: " + getMileage() + ", Accidents: " + (hasAccidents() ? "Yes" : "No") + "]";
    }
}
