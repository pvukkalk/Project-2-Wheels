public class TrailerTest extends student.TestCase {

    private Trailer trailer;

    public void setUp() {
        trailer = new Trailer("TRL201", "Cargo", 2500, 4);
    }

    private void assertRejected(String vin, String type, int weight, int wheels) {
        try {
            new Trailer(vin, type, weight, wheels);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testConstructorAndGetters() {
        assertEquals("TRL201", trailer.getVin());
        assertEquals("Cargo", trailer.getType());
        assertEquals(2500, trailer.getWeight());
        assertEquals(4, trailer.getWheels());
        assertEquals(0.0, trailer.getPrice(), 0.001);
    }

    public void testConstructorRejectsBadInput() {
        assertRejected(null, "Cargo", 2500, 4);
        assertRejected("TRL1", " ", 2500, 4);
        assertRejected("TRL1", "Cargo", 0, 4);
        assertRejected("TRL1", "Cargo", -10, 4);
        assertRejected("TRL1", "Cargo", 2500, 0);
    }

    public void testSmallestValidTrailer() {
        Trailer tiny = new Trailer("T", "Bike", 1, 1);
        assertEquals(1, tiny.getWeight());
        assertEquals(1, tiny.getWheels());
    }

    public void testSetPrice() {
        trailer.setPrice(6500);
        assertEquals(6500.0, trailer.getPrice(), 0.001);
    }

    public void testSetPriceNegative() {
        trailer.setPrice(6500);
        try {
            trailer.setPrice(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Price cannot be negative", e.getMessage());
        }
        assertEquals(6500.0, trailer.getPrice(), 0.001);
    }

    public void testToString() {
        trailer.setPrice(6500);
        assertEquals("Trailer [VIN: TRL201, Type: Cargo, Weight: 2500 lbs, Wheels: 4, "
            + "Price: $6500.00]", trailer.toString());
    }
}
