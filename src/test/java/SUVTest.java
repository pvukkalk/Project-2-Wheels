public class SUVTest extends student.TestCase {

    private SUV suv;

    public void setUp() {
        suv = new SUV("VIN102", "Ford", "Black", "Explorer", 2022, 5000);
    }

    public void testConstructor() {
        assertEquals("VIN102", suv.getVin());
        assertEquals("Ford", suv.getBrand());
        assertEquals("Explorer", suv.getModel());
        assertEquals(2022, suv.getYear());
        assertEquals(5000, suv.getTowingCapacity());
        assertTrue(suv instanceof Vehicles);
    }

    public void testConstructorRejectsNegativeCapacity() {
        try {
            new SUV("VIN102", "Ford", "Black", "Explorer", 2022, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Towing capacity cannot be negative", e.getMessage());
        }
    }

    public void testZeroCapacity() {
        SUV small = new SUV("V", "Kia", "White", "Soul", 2020, 0);
        assertEquals(0, small.getTowingCapacity());
        assertTrue(small.getCanTow());
    }

    public void testCanTowFlag() {
        assertTrue(suv.getCanTow());
    }

    public void testCanTowTrailer() {
        assertTrue(suv.canTow(new Trailer("T1", "Cargo", 4999, 4)));
        assertTrue(suv.canTow(new Trailer("T2", "Cargo", 5000, 4)));
        assertFalse(suv.canTow(new Trailer("T3", "Camper", 5001, 4)));
        assertFalse(suv.canTow(null));
    }

    public void testToString() {
        String text = suv.toString();
        assertTrue(text.startsWith("SUV [VIN: VIN102"));
        assertTrue(text.endsWith(", Towing: 5000 lbs]"));
    }
}
