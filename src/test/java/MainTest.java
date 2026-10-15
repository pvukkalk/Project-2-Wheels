public class MainTest extends student.TestCase {

    public void setUp() {
        Main.dealership = new Dealership();
        Main.seedData(Main.dealership);
    }

    private void script(String... lines) {
        setIn(lines);
        Main.scanner = in();
    }

    private String output() {
        return systemOut().getHistory();
    }

    private Dealership lot() {
        return Main.dealership;
    }

    // ---- main / run ----

    public void testMainSeedsAndExits() {
        Main.dealership = new Dealership();
        script("3");
        Main.main(new String[0]);
        assertEquals(5, lot().getTotalItems());
        assertTrue(output().contains("Goodbye!"));
    }

    public void testRunInvalidChoiceThenExit() {
        script("7", "abc", "3");
        Main.run();
        assertTrue(output().contains("Invalid choice. Please enter 1, 2, or 3."));
        assertTrue(output().contains("Goodbye!"));
    }

    public void testRunEndsWhenInputRunsOut() {
        script();
        Main.run();
        assertTrue(output().contains("Goodbye!"));
    }

    public void testRunGoesToBothMenus() {
        script("1", "8", "2", "9", "3");
        Main.run();
        assertTrue(output().contains("--- Admin Menu ---"));
        assertTrue(output().contains("--- Customer Menu ---"));
    }

    public void testSeedData() {
        Dealership fresh = new Dealership();
        Main.seedData(fresh);
        assertEquals(3, fresh.getVehicles().getNumOfEntries());
        assertEquals(2, fresh.getTrailers().getNumOfEntries());
        assertEquals("VIN101", fresh.getVehicles().getIndex(0).getVin());
    }

    public void testSeedDataNull() {
        Main.seedData(null);
        assertEquals(5, lot().getTotalItems());
    }

    // ---- view inventory ----

    public void testViewInventory() {
        script("1", "8");
        Main.runAdmin();
        assertTrue(output().contains("--- Vehicles (3) ---"));
        assertTrue(output().contains("--- Trailers (2) ---"));
        assertTrue(output().contains("[0] Sedan [VIN: VIN101"));
    }

    public void testViewEmptyInventory() {
        Main.dealership = new Dealership();
        script("1", "9");
        Main.runCustomer();
        assertTrue(output().contains(Dealership.NO_INVENTORY));
    }

    public void testInvalidMenuOptions() {
        script("0", "8");
        Main.runAdmin();
        assertTrue(output().contains("Invalid option. Please choose between 1 and 8."));
        script("x", "9");
        Main.runCustomer();
        assertTrue(output().contains("Invalid option. Please choose between 1 and 9."));
    }

    // ---- browse (swipe) ----

    public void testBrowseForwardAndBack() {
        script("2", "n", "next", "p", "q", "9");
        Main.runCustomer();
        String out = output();
        assertTrue(out.contains("[1 of 5] Sedan"));
        assertTrue(out.contains("[2 of 5] SUV"));
        assertTrue(out.contains("[3 of 5] Pickup"));
        assertTrue(out.lastIndexOf("[2 of 5] SUV") > out.indexOf("[3 of 5] Pickup"));
    }

    public void testBrowseStopsAtEnds() {
        script("2", "p", "n", "n", "n", "n", "n", "q", "8");
        Main.runAdmin();
        assertTrue(output().contains("You're already at the oldest item on the lot."));
        assertTrue(output().contains("[5 of 5] Trailer [VIN: TRL202"));
        assertTrue(output().contains("You've reached the newest item on the lot."));
    }

    public void testBrowseBadInput() {
        script("2", "sideways", "Q", "9");
        Main.runCustomer();
        assertTrue(output().contains("Invalid choice. Enter N, P, or Q."));
    }

    public void testBrowseEmpty() {
        Main.dealership = new Dealership();
        script("2", "9");
        Main.runCustomer();
        assertTrue(output().contains(Dealership.NO_INVENTORY));
    }

    // ---- add vehicle ----

    public void testAddSedan() {
        script("3", "1", "VIN200", "Honda", "Civic", "Blue", "2023", "24000", "10",
            "no", "8");
        Main.runAdmin();
        Vehicles added = lot().getVehicles().getIndex(3);
        assertTrue(added instanceof Sedan);
        assertEquals("VIN200", added.getVin());
        assertEquals("Civic", added.getModel());
        assertEquals(24000.0, added.getPrice(), 0.001);
        assertEquals(10, added.getMileage());
        assertFalse(added.hasAccidents());
        assertTrue(output().contains("Vehicle added:"));
    }

    public void testAddSuvAndPickupAskForTowing() {
        script("3", "2", "VIN201", "Kia", "Telluride", "Gray", "2024", "45000", "0",
            "y", "5500",
            "3", "3", "VIN202", "Ram", "1500", "White", "2022", "39000", "2000",
            "n", "12000", "8");
        Main.runAdmin();
        Vehicles suv = lot().findByVin("VIN201");
        Vehicles truck = lot().findByVin("VIN202");
        assertTrue(suv instanceof SUV);
        assertEquals(5500, suv.getTowingCapacity());
        assertTrue(suv.hasAccidents());
        assertTrue(truck instanceof Pickup);
        assertEquals(12000, truck.getTowingCapacity());
    }

    public void testAddVehicleRepromptsBadInput() {
        script("3",
            "4", "one", "1",          // bad type
            "VIN101", "", "VIN300",   // duplicate VIN, blank VIN
            "", "Honda",              // blank brand
            "Civic", "Blue",
            "1700", "abc", "2023",    // bad year
            "pink", "", "-5", "20000", // bad price
            "-1", "100",              // bad mileage
            "maybe", "no",            // bad yes/no
            "8");
        Main.runAdmin();
        String out = output();
        assertTrue(out.contains("Please enter a number from 1 to 3."));
        assertTrue(out.contains("An item with that VIN is already on the lot."));
        assertTrue(out.contains("Invalid input. This field cannot be blank."));
        assertTrue(out.contains("Invalid input. Please enter a valid number."));
        assertTrue(out.contains("Value cannot be less than 0."));
        assertTrue(out.contains("Invalid input. Please answer yes or no."));
        assertNotNull(lot().findByVin("VIN300"));
        assertEquals(20000.0, lot().findByVin("VIN300").getPrice(), 0.001);
    }

    public void testAddVehicleCancel() {
        script("3", "1", "VIN400", "Honda", "cancel", "3", "cancel", "8");
        Main.runAdmin();
        assertTrue(output().contains("Cancelled. No vehicle added."));
        assertEquals(3, lot().getVehicles().getNumOfEntries());
    }

    // ---- add trailer ----

    public void testAddTrailer() {
        script("4", "TRL300", "Boat", "1800", "2", "3200", "8");
        Main.runAdmin();
        Trailer added = lot().findTrailerByVin("TRL300");
        assertEquals("Boat", added.getType());
        assertEquals(1800, added.getWeight());
        assertEquals(3200.0, added.getPrice(), 0.001);
    }

    public void testAddTrailerRepromptsBadInput() {
        script("4", "TRL300", " ", "Boat", "0", "heavy", "1800", "-2", "2", "x", "3200",
            "8");
        Main.runAdmin();
        assertTrue(output().contains("Invalid input. This field cannot be blank."));
        assertTrue(output().contains("Please enter a number from 1 to "));
        assertEquals(3, lot().getTrailers().getNumOfEntries());
    }

    public void testAddTrailerCancel() {
        script("4", "cancel", "8");
        Main.runAdmin();
        assertTrue(output().contains("Cancelled. No trailer added."));
        assertEquals(2, lot().getTrailers().getNumOfEntries());
    }

    // ---- edit vehicle ----

    public void testEditAllFields() {
        script("5", "vin101", "1", "19999.99", "2", "16000", "3", "yes", "4", "8");
        Main.runAdmin();
        Vehicles v = lot().findByVin("VIN101");
        assertEquals(19999.99, v.getPrice(), 0.001);
        assertEquals(16000, v.getMileage());
        assertTrue(v.hasAccidents());
        assertTrue(output().contains("Price updated successfully!"));
        assertTrue(output().contains("Mileage updated successfully!"));
        assertTrue(output().contains("Accident status updated successfully!"));
    }

    public void testEditRepromptsBadInput() {
        script("5", "", "NOPE", "VIN102",
            "9",
            "1", "-100", "free", "30000",
            "2", "-1", "8001",
            "3", "sure", "no",
            "4", "8");
        Main.runAdmin();
        String out = output();
        assertTrue(out.contains("Not found. Please try again."));
        assertTrue(out.contains("Invalid choice. Please choose a number between 1 and 4."));
        assertTrue(out.contains("Value cannot be less than 0."));
        Vehicles v = lot().findByVin("VIN102");
        assertEquals(30000.0, v.getPrice(), 0.001);
        assertEquals(8001, v.getMileage());
    }

    public void testEditCancel() {
        script("5", "VIN101", "1", "cancel", "4", "5", "cancel", "8");
        Main.runAdmin();
        assertEquals(22000.0, lot().findByVin("VIN101").getPrice(), 0.001);
    }

    public void testEditNoVehicles() {
        Main.dealership = new Dealership();
        script("5", "8");
        Main.runAdmin();
        assertTrue(output().contains(Dealership.NO_INVENTORY));
    }

    // ---- sell / remove ----

    public void testSellItem() {
        script("6", "vin102", "6", "TRL201", "7", "8");
        Main.runAdmin();
        assertNull(lot().findByVin("VIN102"));
        assertNull(lot().findTrailerByVin("TRL201"));
        assertEquals(3, lot().getTotalItems());
        assertTrue(output().contains("Item successfully sold and removed."));
        assertTrue(output().contains("--- Sales History (2) ---"));
        assertTrue(output().contains("1. SOLD SUV [VIN: VIN102"));
    }

    public void testSellNotFound() {
        script("6", "orange", "6", "", "cancel", "8");
        Main.runAdmin();
        assertTrue(output().contains("Not found. No changes made."));
        assertEquals(5, lot().getTotalItems());
    }

    public void testSellFromEmptyLot() {
        Main.dealership = new Dealership();
        script("6", "8");
        Main.runAdmin();
        assertTrue(output().contains(Dealership.NOTHING_TO_REMOVE));
    }

    public void testEmptySalesHistory() {
        script("7", "8");
        Main.runAdmin();
        assertTrue(output().contains("No sales recorded yet."));
    }

    // ---- customer: search by VIN ----

    public void testSearchByVin() {
        script("3", "vin103", "3", "trl202", "9");
        Main.runCustomer();
        assertTrue(output().contains("Found: Pickup [VIN: VIN103"));
        assertTrue(output().contains("Found: Trailer [VIN: TRL202"));
    }

    public void testSearchByVinBadInput() {
        script("3", "", "  ", "ZZZ999", "3", "cancel", "9");
        Main.runCustomer();
        assertTrue(output().contains("Invalid input. This field cannot be blank."));
        assertTrue(output().contains("Not found."));
        assertEquals(5, lot().getTotalItems());
    }

    public void testSearchByVinEmptyLot() {
        Main.dealership = new Dealership();
        script("3", "9");
        Main.runCustomer();
        assertTrue(output().contains(Dealership.NO_INVENTORY));
    }

    // ---- customer: brand / model / color ----

    public void testSearchByBrandModelColor() {
        script("4", "FORD", "", "black", "9");
        Main.runCustomer();
        assertTrue(output().contains("Found 1 matching item(s):"));
        assertTrue(output().contains("- SUV [VIN: VIN102"));
    }

    public void testSearchByBrandModelColorNotFound() {
        script("4", "Tesla", "", "", "9");
        Main.runCustomer();
        assertTrue(output().contains("Not found."));
    }

    public void testSearchByBrandModelColorEmptyLot() {
        Main.dealership = new Dealership();
        script("4", "9");
        Main.runCustomer();
        assertTrue(output().contains(Dealership.NO_INVENTORY));
    }

    // ---- customer: price ----

    public void testFilterByPrice() {
        script("5", "6000", "22000", "9");
        Main.runCustomer();
        assertTrue(output().contains("- Sedan [VIN: VIN101"));
        assertTrue(output().contains("- Trailer [VIN: TRL201"));
        assertFalse(output().contains("- SUV"));
    }

    public void testFilterByPriceBadInput() {
        script("5", "pink", "", "-5", "100", "50000", "100", "0", "1", "9");
        Main.runCustomer();
        String out = output();
        assertTrue(out.contains("Invalid input. Please enter a valid number."));
        assertTrue(out.contains("Invalid price range."));
        assertTrue(out.contains("No items found in that price range."));
    }

    public void testFilterByPriceCancel() {
        script("5", "cancel", "9");
        Main.runCustomer();
        assertFalse(output().contains("Found"));
    }

    public void testFilterByPriceEmptyLot() {
        Main.dealership = new Dealership();
        script("5", "9");
        Main.runCustomer();
        assertTrue(output().contains(Dealership.NO_INVENTORY));
    }

    // ---- customer: accidents ----

    public void testFilterByAccidents() {
        script("6", "yes", "9");
        Main.runCustomer();
        assertTrue(output().contains("- Pickup [VIN: VIN103"));
        assertFalse(output().contains("- Sedan"));
    }

    public void testFilterByAccidentsBadInput() {
        script("6", "kinda", "no", "9");
        Main.runCustomer();
        assertTrue(output().contains("Invalid input. Please answer yes or no."));
        assertTrue(output().contains("Found 2 matching item(s):"));
    }

    // ---- customer: towing ----

    public void testFilterCanTow() {
        script("7", "6000", "9");
        Main.runCustomer();
        assertTrue(output().contains("- Pickup [VIN: VIN103"));
        assertFalse(output().contains("- SUV"));
    }

    public void testFilterCanTowBadInput() {
        script("7", "lots", "-5", "20000", "9");
        Main.runCustomer();
        assertTrue(output().contains("Invalid input. Please enter a valid number."));
        assertTrue(output().contains("Towing capacity cannot be negative. Please try again."));
        assertTrue(output().contains("Not found."));
    }

    public void testFindCompatibleTrailers() {
        script("8", "VIN102", "9");
        Main.runCustomer();
        assertTrue(output().contains("- Trailer [VIN: TRL201"));
        assertFalse(output().contains("- Trailer [VIN: TRL202"));
    }

    public void testFindCompatibleTrailersBadInput() {
        script("8", "VIN101", "8", "NOPE", "8", "cancel", "9");
        Main.runCustomer();
        assertTrue(output().contains("That vehicle cannot tow trailers."));
        assertTrue(output().contains("Not found."));
    }

    public void testFindCompatibleTrailersNoneLightEnough() {
        lot().removeByVin("TRL201");
        script("8", "VIN102", "9");
        Main.runCustomer();
        assertTrue(output().contains("No trailers on the lot are light enough for that vehicle."));
    }
}
