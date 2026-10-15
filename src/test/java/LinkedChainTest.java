public class LinkedChainTest extends student.TestCase {

    private LinkedChain<String> chain;
    private LinkedChain<Vehicles> cars;
    private LinkedChain<Trailer> trailers;

    public void setUp() {
        chain = new LinkedChain<>();
        cars = new LinkedChain<>();
        trailers = new LinkedChain<>();
    }

    private void fill(String... items) {
        for (String item : items) {
            chain.add(item);
        }
    }

    // ---- constructor ----

    public void testStartsEmpty() {
        assertEquals(0, chain.getNumOfEntries());
        assertTrue(chain.isEmpty());
        assertNull(chain.getIndex(0));
    }

    // ---- add ----

    public void testAddToEmpty() {
        assertTrue(chain.add("a"));
        assertEquals(1, chain.getNumOfEntries());
        assertFalse(chain.isEmpty());
        assertEquals("a", chain.getIndex(0));
    }

    public void testAddGoesToBack() {
        fill("oldest", "middle", "newest");
        assertEquals("oldest", chain.getIndex(0));
        assertEquals("middle", chain.getIndex(1));
        assertEquals("newest", chain.getIndex(2));
    }

    public void testAddAllowsDuplicates() {
        fill("a", "a");
        assertEquals(2, chain.getNumOfEntries());
    }

    public void testAddNull() {
        fill("a");
        assertFalse(chain.add(null));
        assertEquals(1, chain.getNumOfEntries());
    }

    // ---- remove ----

    public void testRemoveFirst() {
        fill("a", "b", "c");
        assertEquals("a", chain.remove("a"));
        assertEquals(2, chain.getNumOfEntries());
        assertEquals("b", chain.getIndex(0));
    }

    public void testRemoveMiddle() {
        fill("a", "b", "c");
        assertEquals("b", chain.remove("b"));
        assertEquals("a", chain.getIndex(0));
        assertEquals("c", chain.getIndex(1));
    }

    public void testRemoveLast() {
        fill("a", "b", "c");
        assertEquals("c", chain.remove("c"));
        assertEquals(2, chain.getNumOfEntries());
        assertNull(chain.getIndex(2));
    }

    public void testRemoveOnlyFirstDuplicate() {
        fill("a", "b", "a");
        chain.remove("a");
        assertEquals(1, chain.getInstancesOf("a"));
        assertEquals("b", chain.getIndex(0));
    }

    public void testRemoveNotThere() {
        fill("a", "b");
        assertNull(chain.remove("z"));
        assertEquals(2, chain.getNumOfEntries());
    }

    public void testRemoveFromEmpty() {
        assertNull(chain.remove("a"));
        assertEquals(0, chain.getNumOfEntries());
    }

    public void testRemoveNull() {
        fill("a");
        assertNull(chain.remove(null));
        assertEquals(1, chain.getNumOfEntries());
    }

    // ---- clear ----

    public void testClear() {
        fill("a", "b", "c");
        chain.clear();
        assertTrue(chain.isEmpty());
        assertNull(chain.getIndex(0));
        assertTrue(chain.add("d"));
        assertEquals("d", chain.getIndex(0));
    }

    public void testClearEmpty() {
        chain.clear();
        assertEquals(0, chain.getNumOfEntries());
    }

    // ---- getIndex ----

    public void testGetIndexDoesNotRemove() {
        fill("a", "b");
        assertEquals("b", chain.getIndex(1));
        assertEquals(2, chain.getNumOfEntries());
    }

    public void testGetIndexOutOfBounds() {
        fill("a", "b");
        assertNull(chain.getIndex(-1));
        assertNull(chain.getIndex(2));
    }

    // ---- contains ----

    public void testContains() {
        fill("a", "b");
        assertTrue(chain.contains("b"));
        assertFalse(chain.contains("z"));
    }

    public void testContainsNullAndEmpty() {
        assertFalse(chain.contains("a"));
        fill("a");
        assertFalse(chain.contains(null));
    }

    // ---- getInstancesOf ----

    public void testGetInstancesOf() {
        fill("a", "b", "a", "a");
        assertEquals(3, chain.getInstancesOf("a"));
        assertEquals(1, chain.getInstancesOf("b"));
    }

    public void testGetInstancesOfMissingAndNull() {
        fill("a");
        assertEquals(0, chain.getInstancesOf("z"));
        assertEquals(0, chain.getInstancesOf(null));
        assertEquals(0, new LinkedChain<String>().getInstancesOf("a"));
    }

    // ---- filter ----

    public void testFilterKeepsOrderAndLeavesOriginal() {
        fill("apple", "bean", "avocado", "corn");
        LinkedChain<String> result = chain.filter(s -> s.startsWith("a"));
        assertEquals(2, result.getNumOfEntries());
        assertEquals("apple", result.getIndex(0));
        assertEquals("avocado", result.getIndex(1));
        assertEquals(4, chain.getNumOfEntries());
    }

    public void testFilterResultIsIndependent() {
        fill("a", "b");
        LinkedChain<String> result = chain.filter(s -> true);
        result.add("c");
        result.remove("a");
        assertEquals(2, chain.getNumOfEntries());
        assertEquals("a", chain.getIndex(0));
        assertEquals("c", result.getIndex(1));
    }

    public void testFilterNoMatchesAndNull() {
        fill("a");
        assertTrue(chain.filter(s -> false).isEmpty());
        assertTrue(chain.filter(null).isEmpty());
        assertTrue(new LinkedChain<String>().filter(s -> true).isEmpty());
    }

    // ---- generic reuse ----

    public void testHoldsVehiclesAndTrailers() {
        Sedan sedan = new Sedan("V1", "Honda", "Blue", "Civic", 2020);
        SUV suv = new SUV("V2", "Ford", "Black", "Explorer", 2021, 5000);
        Trailer trailer = new Trailer("T1", "Cargo", 2000, 4);
        assertTrue(cars.add(sedan));
        assertTrue(cars.add(suv));
        assertTrue(trailers.add(trailer));
        assertSame(sedan, cars.getIndex(0));
        assertSame(suv, cars.getIndex(1));
        assertSame(trailer, trailers.getIndex(0));
        assertTrue(cars.contains(suv));
        assertSame(sedan, cars.remove(sedan));
        assertEquals(1, cars.getNumOfEntries());
    }

    public void testIdenticalVehiclesAreSeparate() {
        Sedan first = new Sedan("V1", "Honda", "Blue", "Civic", 2020);
        Sedan second = new Sedan("V1", "Honda", "Blue", "Civic", 2020);
        cars.add(first);
        cars.add(second);
        assertEquals(1, cars.getInstancesOf(first));
        cars.remove(second);
        assertSame(first, cars.getIndex(0));
    }
}
