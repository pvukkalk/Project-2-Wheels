public class Vehicles {
    private String vin;
    private String color;
    private String model;
    private boolean canTow;
    private int year;
    private int mileage;
    private Boolean accidents;
    private double price;

    public Vehicles(String vin, String color, String model, int year) {
        this.vin = vin;
        this.color = color;
        this.model = model;
        this.year = year;
        this.canTow = false;
        this.mileage = 0;
        this.accidents = false;
        this.price = 0.0;
    }

    public void setMileage(int miles) {
        if (miles < 0) throw new IllegalArgumentException("Mileage cannot be negative");
        this.mileage = miles;
    }

    public void setAccidents(boolean accident) {
        this.accidents = accident;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public String getVin() { return vin; }
    public String getColor() { return color; }
    public String getModel() { return model; }
    public int getMileage() { return mileage; }
    public double getPrice() { return price; }
    public boolean getCanTow() { return canTow; }
    public int getYear() { return year; }
    public boolean hasAccidents() { return accidents != null && accidents; }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [VIN: " + getVin() + ", Model: " + getModel() + 
            ", Color: " + getColor() + ", Year: " + getYear() + ", Price: $" + getPrice() + 
            ", Mileage: " + getMileage() + ", Accidents: " + (hasAccidents() ? "Yes" : "No") + "]";
    }
}
