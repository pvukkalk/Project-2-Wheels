public class DealershipTest extends student.TestCase {

    private Dealership lot;
    private Dealership empty;
    private Sedan sedan;
    private SUV suv;
    private Pickup pickup;
    private Trailer cargo;
    private Trailer hauler;

    public void setUp() {
        lot = new Dealership();
        empty = new Dealership();

        sedan = new Sedan("VIN101", "Toyota", "Silver", "Camry", 2021);
        sedan.setPrice(22000);
        suv = new SUV("VIN102", "Ford", "Black", "Explorer", 2022, 5000);
        suv.setPrice(35000);
        pickup = new Pickup("VIN103", "Ford", "Red", "F-150", 2020, 11000);
        pickup.setPrice(41000);
        pickup.setAccidents(true);
        cargo = new Trailer("TRL201", "Cargo", 2500, 4);
        cargo.setPrice(6500);
        hauler = new Trailer("TRL202", "Car Hauler", 7000, 4);
        hauler.setPrice(9800);

        lot.addVehicle(sedan);
        lot.addVehicle(suv);
        lot.addVehicle(pickup);
        lot.addTrailer(cargo);
        lot.addTrailer(hauler);
    }

    private void assertNoInventory(Runnable search) {
        try {
            search.run();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals(Dealership.NO_INVENTORY, e.getMessage());
        }
    }

    private void assertBadRange(double low, double high) {
        try {
            lot.filterPrice(low, high);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Invalid price range"));
        }
    }

    // ---- constructor / lot info ----

    public void testNewDealershipIsEmpty() {
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.getTotalItems());
        assertTrue(empty.getVehicles().isEmpty());
        assertTrue(empty.getTrailers().isEmpty());
        assertTrue(empty.getSalesLog().isEmpty());
    }

    public void testTotals() {
        assertFalse(lot.isEmpty());
        assertEquals(5, lot.getTotalItems());
        assertEquals(3, lot.getVehicles().getNumOfEntries());
        assertEquals(2, lot.getTrailers().getNumOfEntries());
    }

    // ---- addVehicle / addTrailer ----

    public void testAddVehicleKeepsArrivalOrder() {
        Sedan newest = new Sedan("VIN104", "Honda", "Blue", "Civic", 2023);
        assertTrue(lot.addVehicle(newest));
        assertSame(sedan, lot.getVehicles().getIndex(0));
        assertSame(newest, lot.getVehicles().getIndex(3));
    }

    public void testAddVehicleRejectsNullAndDuplicateVin() {
        assertFalse(lot.addVehicle(null));
        assertFalse(lot.addVehicle(new Sedan("vin101", "Kia", "White", "Rio", 2020)));
        assertFalse(lot.addVehicle(new Sedan("TRL201", "Kia", "White", "Rio", 2020)));
        assertEquals(3, lot.getVehicles().getNumOfEntries());
    }

    public void testAddTrailer() {
        Trailer boat = new Trailer("TRL203", "Boat", 1500, 2);
        assertTrue(lot.addTrailer(boat));
        assertSame(boat, lot.getTrailers().getIndex(2));
    }

    public void testAddTrailerRejectsNullAndDuplicateVin() {
        assertFalse(lot.addTrailer(null));
        assertFalse(lot.addTrailer(new Trailer("VIN102", "Boat", 1500, 2)));
        assertEquals(2, lot.getTrailers().getNumOfEntries());
    }

    // ---- getItem ----

    public void testGetItemVehiclesThenTrailers() {
        assertSame(sedan, lot.getItem(0));
        assertSame(pickup, lot.getItem(2));
        assertSame(cargo, lot.getItem(3));
        assertSame(hauler, lot.getItem(4));
    }

    public void testGetItemOutOfBounds() {
        assertNull(lot.getItem(-1));
        assertNull(lot.getItem(5));
        assertNull(empty.getItem(0));
    }

    // ---- containsVin ----

    public void testContainsVin() {
        assertTrue(lot.containsVin("VIN102"));
        assertTrue(lot.containsVin(" trl202 "));
        assertFalse(lot.containsVin("NOPE"));
        assertFalse(lot.containsVin(null));
        assertFalse(lot.containsVin(""));
    }

    // ---- findByVin / findTrailerByVin ----

    public void testFindByVin() {
        assertSame(suv, lot.findByVin("VIN102"));
        assertSame(suv, lot.findByVin("  vin102 "));
    }

    public void testFindByVinNotFound() {
        assertNull(lot.findByVin("orange"));
        assertNull(lot.findByVin("TRL201"));
        assertNull(lot.findByVin(null));
        assertNull(lot.findByVin("   "));
        assertEquals(5, lot.getTotalItems());
    }

    public void testFindTrailerByVin() {
        assertSame(hauler, lot.findTrailerByVin("trl202"));
        assertNull(lot.findTrailerByVin("VIN101"));
        assertNull(lot.findTrailerByVin(null));
    }

    public void testFindOnEmptyLot() {
        assertNoInventory(() -> empty.findByVin("VIN101"));
        assertNoInventory(() -> empty.findTrailerByVin("TRL201"));
    }

    // ---- searchByBrandModelColor ----

    public void testSearchAllFields() {
        LinkedChain<Vehicles> result = lot.searchByBrandModelColor("Ford", "F-150", "Red");
        assertEquals(1, result.getNumOfEntries());
        assertSame(pickup, result.getIndex(0));
    }

    public void testSearchIgnoresCaseAndBlankFields() {
        LinkedChain<Vehicles> fords = lot.searchByBrandModelColor(" ford ", "", null);
        assertEquals(2, fords.getNumOfEntries());
        assertSame(suv, fords.getIndex(0));
        assertSame(pickup, fords.getIndex(1));
        assertEquals(3, lot.searchByBrandModelColor(null, null, null).getNumOfEntries());
    }

    public void testSearchNoMatch() {
        assertTrue(lot.searchByBrandModelColor("Ford", "Camry", "").isEmpty());
        assertTrue(lot.searchByBrandModelColor("Tesla", null, null).isEmpty());
    }

    public void testSearchEmptyLot() {
        assertNoInventory(() -> empty.searchByBrandModelColor("Ford", null, null));
    }

    // ---- filterPrice / filterTrailerPrice ----

    public void testFilterPriceInclusive() {
        LinkedChain<Vehicles> result = lot.filterPrice(22000, 35000);
        assertEquals(2, result.getNumOfEntries());
        assertSame(sedan, result.getIndex(0));
        assertSame(suv, result.getIndex(1));
    }

    public void testFilterPriceSinglePrice() {
        LinkedChain<Vehicles> result = lot.filterPrice(41000, 41000);
        assertEquals(1, result.getNumOfEntries());
        assertSame(pickup, result.getIndex(0));
    }

    public void testFilterPriceNoMatch() {
        assertTrue(lot.filterPrice(0, 1000).isEmpty());
    }

    public void testFilterPriceBadRange() {
        assertBadRange(-1, 50000);
        assertBadRange(0, -1);
        assertBadRange(50000, 100);
        assertBadRange(Double.NaN, 100);
    }

    public void testFilterTrailerPrice() {
        LinkedChain<Trailer> result = lot.filterTrailerPrice(6000, 7000);
        assertEquals(1, result.getNumOfEntries());
        assertSame(cargo, result.getIndex(0));
        try {
            lot.filterTrailerPrice(9000, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Invalid price range"));
        }
    }

    public void testFilterPriceEmptyLot() {
        assertNoInventory(() -> empty.filterPrice(0, 100));
        assertNoInventory(() -> empty.filterTrailerPrice(0, 100));
    }

    // ---- filterAccidents ----

    public void testFilterAccidents() {
        LinkedChain<Vehicles> wrecked = lot.filterAccidents(true);
        assertEquals(1, wrecked.getNumOfEntries());
        assertSame(pickup, wrecked.getIndex(0));
        LinkedChain<Vehicles> clean = lot.filterAccidents(false);
        assertEquals(2, clean.getNumOfEntries());
        assertSame(sedan, clean.getIndex(0));
    }

    public void testFilterAccidentsEmptyLot() {
        assertNoInventory(() -> empty.filterAccidents(true));
    }

    // ---- filterCanTow ----

    public void testFilterCanTow() {
        LinkedChain<Vehicles> any = lot.filterCanTow(0);
        assertEquals(2, any.getNumOfEntries());
        assertSame(suv, any.getIndex(0));
        assertSame(pickup, any.getIndex(1));
    }

    public void testFilterCanTowBoundary() {
        assertEquals(2, lot.filterCanTow(5000).getNumOfEntries());
        assertEquals(1, lot.filterCanTow(5001).getNumOfEntries());
        assertEquals(1, lot.filterCanTow(11000).getNumOfEntries());
        assertTrue(lot.filterCanTow(11001).isEmpty());
    }

    public void testFilterCanTowNegative() {
        try {
            lot.filterCanTow(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Towing capacity cannot be negative.", e.getMessage());
        }
    }

    public void testFilterCanTowEmptyLot() {
        assertNoInventory(() -> empty.filterCanTow(0));
    }

    // ---- compatibleTrailers ----

    public void testCompatibleTrailers() {
        LinkedChain<Trailer> forSuv = lot.compatibleTrailers(suv);
        assertEquals(1, forSuv.getNumOfEntries());
        assertSame(cargo, forSuv.getIndex(0));
        assertEquals(2, lot.compatibleTrailers(pickup).getNumOfEntries());
        assertTrue(lot.compatibleTrailers(sedan).isEmpty());
    }

    public void testCompatibleTrailersNull() {
        try {
            lot.compatibleTrailers(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Vehicle cannot be null.", e.getMessage());
        }
    }

    // ---- removeByVin ----

    public void testRemoveVehicleByVin() {
        assertTrue(lot.removeByVin("vin102"));
        assertEquals(2, lot.getVehicles().getNumOfEntries());
        assertNull(lot.findByVin("VIN102"));
        assertSame(sedan, lot.getVehicles().getIndex(0));
        assertSame(pickup, lot.getVehicles().getIndex(1));
    }

    public void testRemoveTrailerByVin() {
        assertTrue(lot.removeByVin("TRL201"));
        assertEquals(1, lot.getTrailers().getNumOfEntries());
        assertSame(hauler, lot.getTrailers().getIndex(0));
    }

    public void testRemoveRecordsSale() {
        lot.removeByVin("VIN101");
        lot.removeByVin("TRL202");
        LinkedChain<String> log = lot.getSalesLog();
        assertEquals(2, log.getNumOfEntries());
        assertEquals("SOLD " + sedan, log.getIndex(0));
        assertEquals("SOLD " + hauler, log.getIndex(1));
    }

    public void testRemoveNotThere() {
        assertFalse(lot.removeByVin("orange"));
        assertFalse(lot.removeByVin(null));
        assertFalse(lot.removeByVin(" "));
        assertEquals(5, lot.getTotalItems());
        assertTrue(lot.getSalesLog().isEmpty());
    }

    public void testRemoveAlreadySold() {
        assertTrue(lot.removeByVin("VIN103"));
        assertFalse(lot.removeByVin("VIN103"));
        assertEquals(4, lot.getTotalItems());
        assertEquals(1, lot.getSalesLog().getNumOfEntries());
    }

    public void testRemoveFromEmptyLot() {
        try {
            empty.removeByVin("VIN101");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals(Dealership.NOTHING_TO_REMOVE, e.getMessage());
        }
        assertTrue(empty.isEmpty());
        assertTrue(empty.getSalesLog().isEmpty());
    }

    public void testSellEverything() {
        String[] vins = {"VIN101", "VIN102", "VIN103", "TRL201", "TRL202"};
        for (String vin : vins) {
            assertTrue(lot.removeByVin(vin));
        }
        assertTrue(lot.isEmpty());
        assertEquals(5, lot.getSalesLog().getNumOfEntries());
    }
}
