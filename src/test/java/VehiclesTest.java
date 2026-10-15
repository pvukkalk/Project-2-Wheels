public class VehiclesTest extends student.TestCase {

    private Vehicles car;

    public void setUp() {
        car = new Vehicles("VIN1", "Toyota", "Silver", "Camry", 2021);
    }

    private void assertRejected(String vin, String brand, String color, String model,
        int year) {
        try {
            new Vehicles(vin, brand, color, model, year);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // ---- constructor ----

    public void testConstructorAndGetters() {
        assertEquals("VIN1", car.getVin());
        assertEquals("Toyota", car.getBrand());
        assertEquals("Silver", car.getColor());
        assertEquals("Camry", car.getModel());
        assertEquals(2021, car.getYear());
    }

    public void testDefaults() {
        assertEquals(0, car.getMileage());
        assertEquals(0.0, car.getPrice(), 0.001);
        assertFalse(car.hasAccidents());
        assertFalse(car.getCanTow());
        assertEquals(0, car.getTowingCapacity());
    }

    public void testConstructorTrims() {
        Vehicles v = new Vehicles("  VIN2 ", " Ford", "Red ", " F-150 ", 2020);
        assertEquals("VIN2", v.getVin());
        assertEquals("Ford", v.getBrand());
        assertEquals("Red", v.getColor());
        assertEquals("F-150", v.getModel());
    }

    public void testConstructorRejectsBlankFields() {
        assertRejected(null, "Toyota", "Silver", "Camry", 2021);
        assertRejected(" ", "Toyota", "Silver", "Camry", 2021);
        assertRejected("VIN1", "", "Silver", "Camry", 2021);
        assertRejected("VIN1", "Toyota", null, "Camry", 2021);
        assertRejected("VIN1", "Toyota", "Silver", "  ", 2021);
    }

    public void testConstructorYearBoundary() {
        assertEquals(Vehicles.MIN_YEAR,
            new Vehicles("V", "B", "C", "M", Vehicles.MIN_YEAR).getYear());
        assertRejected("V", "B", "C", "M", Vehicles.MIN_YEAR - 1);
    }

    // ---- setMileage ----

    public void testSetMileage() {
        car.setMileage(15000);
        assertEquals(15000, car.getMileage());
        car.setMileage(0);
        assertEquals(0, car.getMileage());
    }

    public void testSetMileageNegative() {
        car.setMileage(500);
        try {
            car.setMileage(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mileage cannot be negative", e.getMessage());
        }
        assertEquals(500, car.getMileage());
    }

    // ---- setAccidents ----

    public void testSetAccidents() {
        car.setAccidents(true);
        assertTrue(car.hasAccidents());
        car.setAccidents(false);
        assertFalse(car.hasAccidents());
    }

    // ---- setPrice ----

    public void testSetPrice() {
        car.setPrice(22000.50);
        assertEquals(22000.50, car.getPrice(), 0.001);
        car.setPrice(0);
        assertEquals(0.0, car.getPrice(), 0.001);
    }

    public void testSetPriceNegative() {
        car.setPrice(100);
        try {
            car.setPrice(-0.01);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Price cannot be negative", e.getMessage());
        }
        assertEquals(100.0, car.getPrice(), 0.001);
    }

    public void testSetPriceNotANumber() {
        try {
            car.setPrice(Double.NaN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(0.0, car.getPrice(), 0.001);
        }
    }

    // ---- towing ----

    public void testCannotTow() {
        assertFalse(car.canTow(new Trailer("T1", "Cargo", 1, 2)));
        assertFalse(car.canTow(null));
    }

    // ---- toString ----

    public void testToString() {
        car.setPrice(22000);
        car.setMileage(15000);
        car.setAccidents(true);
        assertEquals("Vehicles [VIN: VIN1, Brand: Toyota, Model: Camry, Color: Silver, "
            + "Year: 2021, Price: $22000.00, Mileage: 15000, Accidents: Yes]",
            car.toString());
    }
}
