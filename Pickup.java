
public class Pickup extends Vehicles {
    private int towingCapacity;

    public Pickup(String vin, String color, String model, int year, int towingCapacity) {
        super(vin, color, model, year);
        this.towingCapacity = towingCapacity;
    }

    public int getTowingCapacity() {
        return towingCapacity;
    }

    @Override
    public boolean getCanTow() {
        return true;
    }

    public boolean canTow(Trailer trailer) {
        if (trailer == null) {
            return false;
        }
        return towingCapacity >= trailer.getWeight();
    }
}