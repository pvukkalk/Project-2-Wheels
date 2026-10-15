public class SedanTest extends student.TestCase {

    private Sedan sedan;

    public void setUp() {
        sedan = new Sedan("VIN101", "Toyota", "Silver", "Camry", 2021);
    }

    public void testConstructor() {
        assertEquals("VIN101", sedan.getVin());
        assertEquals("Toyota", sedan.getBrand());
        assertEquals("Silver", sedan.getColor());
        assertEquals("Camry", sedan.getModel());
        assertEquals(2021, sedan.getYear());
        assertTrue(sedan instanceof Vehicles);
    }

    public void testConstructorRejectsBlank() {
        try {
            new Sedan("VIN101", "Toyota", "", "Camry", 2021);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Color cannot be blank", e.getMessage());
        }
    }

    public void testCannotTow() {
        assertFalse(sedan.getCanTow());
        assertEquals(0, sedan.getTowingCapacity());
        assertFalse(sedan.canTow(new Trailer("T1", "Utility", 500, 2)));
    }

    public void testInheritedSetters() {
        sedan.setPrice(22000);
        sedan.setMileage(15000);
        assertEquals(22000.0, sedan.getPrice(), 0.001);
        assertEquals(15000, sedan.getMileage());
    }

    public void testToString() {
        assertTrue(sedan.toString().startsWith("Sedan [VIN: VIN101, Brand: Toyota"));
        assertFalse(sedan.toString().contains("Towing"));
    }
}
