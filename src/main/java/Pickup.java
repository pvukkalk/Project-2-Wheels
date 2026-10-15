public class Pickup extends Vehicles {
    private int towingCapacity;

    public Pickup(String vin, String brand, String color, String model, int year,
        int towingCapacity) {
        super(vin, brand, color, model, year);
        if (towingCapacity < 0) {
            throw new IllegalArgumentException("Towing capacity cannot be negative");
        }
        this.towingCapacity = towingCapacity;
    }

    @Override
    public int getTowingCapacity() {
        return towingCapacity;
    }

    @Override
    public boolean getCanTow() {
        return true;
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + ", Towing: " + towingCapacity + " lbs]";
    }
}
