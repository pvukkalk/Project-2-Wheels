public class SUV extends Vehicles {
    private int towingCapacity;

    public SUV(String vin, String brand, String color, String model, int year,
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

    // SUVs can tow per spec
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
