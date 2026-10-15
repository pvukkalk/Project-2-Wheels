# Vehicle & Trailer Inventory System — WHEELS

## Reflection

### What Changed from Your Specification?
* **Editing Workflow:** The original specification envisioned a general edit command, but during implementation, we refined it into an interactive sub-menu. When editing a vehicle, admins can now choose specifically *what* they want to modify (Price, Mileage, or Accident status) rather than being forced to overwrite all attributes at once.
* **Validation Handling:** Validation was moved into interactive re-prompt loops inside the console handlers (`runAdmin` and `runCustomer`). Instead of throwing an exception or crashing the application on bad inputs (such as invalid index choices or negative prices), the system catches the error, displays an informative message, and re-prompts the user until valid input or a cancel command (`-1` / `cancel`) is given.
* **Method Signatures:** Added helper methods and flexible string handling (such as `equalsIgnoreCase` and null-safety checks) to accommodate both vehicles and trailers seamlessly within the same generic collection.

### What Changed in the LinkedChain?
* **Node Encapsulation:** We added standard helper methods (`getData()`, `getNext()`, and `setNext()`) inside the private static `Node<T>` class to enable clean and safe pointer traversal.
* **Domain-Specific Queries & Filters:** While `LinkedChain<T>` remains generic, we implemented helper query methods (`findByVin`, `removeByVin`, `searchByBrandModelColor`, `filterPrice`, and `filterAccidents`) that safely check node instances using `instanceof` to filter and retrieve inventory items without requiring raw types or unsafe casts.

### What Happened to Your Unknowns?
* **Generic Type Constraints:** Initially, filtering attributes like price or VIN on a generic type `T` presented a challenge since `T` could be anything (including a `Trailer`). We resolved this by performing safe runtime checks (`instanceof`) inside the `LinkedChain` methods, allowing vehicles and trailers to coexist in the same chain while safely querying vehicle-specific attributes only when applicable.

### What Doesn’t Work Yet?
* **File Persistence:** The current prototype stores inventory in memory during execution; data does not persist to a file or database after closing the application.
* **Advanced Formatting:** Console output is text-based, so large inventories rely on sequential text rendering rather than a tabular UI grid.

---

## Next Steps

### What is Left to Finish the MVP?
1. **Additional Filtering Methods:** Finalize remaining filter methods such as `filterCanTow(int capacity)` for customers looking for towing-capable vehicles.
2. **Edge-Case Validation Polish:** Ensure rigorous input trimming and numeric bounds checks across all remaining admin entry points.

### Which Stretch Goals Will You Attempt?
* **Attempted:** File I/O persistence (saving and loading inventory to/from a CSV file on startup/shutdown).
* **Dropped:** Graphical User Interface (GUI) via JavaFX, keeping the focus strictly on a robust console-based architecture for this iteration.

### What New Tests Will You Need?
* Unit tests covering `filterCanTow` with normal and boundary cases (e.g., capacity matching exact vs. lower limits).
* Tests for empty inventory checks during search operations.
* Negative and boundary price tests inside `TestCase.java`.

### Who Does What?
* **Class Design & Core Data Structures (`LinkedChain`, `Vehicle`, `Trailer`):** All team members (Collaborative baseline).
* **Validation & Search Logic (`findByVin`, `searchByBrandModelColor`):** Jack H.
* **Test Plan & JUnit Test Suite (`Student.TestCase`):** Preeti V.
* **System Diagrams & Console Menu UI (`Main.java`, Admin/Customer loops):** Jack M.
