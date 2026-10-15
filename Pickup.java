
/**
 * Pickup - a Vehicles subclass that can tow. It adds a towing capacity (in
 * pounds)
 * on top of the shared Vehicles fields.
 */
public class Pickup extends Vehicles {

    private final int towingCapacity;

    /**
     * Creates a Pickup. The shared fields are handed to the Vehicles
     * constructor.
     * O(1)
     */
    public Pickup(
        String vin,
        String color,
        String model,
        int year,
        int towingCapacity) {
        super(vin, color, model, year);
        this.towingCapacity = towingCapacity;
    }


    /** O(1) */
    public int getTowingCapacity() {
        return towingCapacity;
    }


    /**
     * A pickup can always tow, so this replaces Vehicles' default of false.
     * (The spec says the constructor "sets canTow to true"; Vehicles' canTow
     * field is private
     * with no setter, so overriding the getter gives the same result.)
     * O(1)
     */
    @Override
    public boolean getCanTow() {
        return true;
    }


    /**
     * True if this pickup can tow the given trailer, meaning the trailer's
     * weight
     * is no more than the towing capacity. A null trailer can't be towed.
     * O(1)
     */
    public boolean canTow(Trailer trailer) {
        if (trailer == null) {
            return false;
        }
        return towingCapacity >= trailer.getWeight();
    }
}
