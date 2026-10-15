public class PickupTest extends student.TestCase {

    private Pickup pickup;

    public void setUp() {
        pickup = new Pickup("VIN103", "Ford", "Red", "F-150", 2020, 11000);
    }

    public void testConstructor() {
        assertEquals("VIN103", pickup.getVin());
        assertEquals("Ford", pickup.getBrand());
        assertEquals("Red", pickup.getColor());
        assertEquals("F-150", pickup.getModel());
        assertEquals(2020, pickup.getYear());
        assertEquals(11000, pickup.getTowingCapacity());
        assertTrue(pickup instanceof Vehicles);
    }

    public void testConstructorRejectsBadInput() {
        try {
            new Pickup("VIN103", "Ford", "Red", "F-150", 2020, -500);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Towing capacity cannot be negative", e.getMessage());
        }
        try {
            new Pickup(null, "Ford", "Red", "F-150", 2020, 500);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("VIN cannot be blank", e.getMessage());
        }
    }

    public void testCanTowFlag() {
        assertTrue(pickup.getCanTow());
    }

    public void testCanTowTrailer() {
        assertTrue(pickup.canTow(new Trailer("T1", "Car Hauler", 11000, 4)));
        assertFalse(pickup.canTow(new Trailer("T2", "Fifth Wheel", 11001, 6)));
        assertFalse(pickup.canTow(null));
    }

    public void testToString() {
        pickup.setAccidents(true);
        String text = pickup.toString();
        assertTrue(text.startsWith("Pickup [VIN: VIN103"));
        assertTrue(text.contains("Accidents: Yes"));
        assertTrue(text.endsWith(", Towing: 11000 lbs]"));
    }
}
