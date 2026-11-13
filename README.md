# Checkout Rush - Project 3 (Deliverable 3)

## How to Run
### 1. Running the Model (Independent of View)
- Run the JUnit test suite (`CustomerTest`, `QueueTest`, `GroceryItemTest`, `CheckoutGameTest`) directly in Eclipse.
- Alternatively, run the tiny console driver `ModelDriver.main()` to execute core queue operations and customer generation without launching any view components.

### 2. Running the Thin View
- Execute `GameController.main()` or the primary application runner to launch the console/thin view. 
- The controller initializes `CheckoutGame` and `CheckoutView`, rendering the queue line and player info independently of core model calculations.

---

## Reflection

### What Changed From Our Specification?
- **Validation Layer:** Moved input validation for calculator expressions (`locateErrors`) directly into `CalculatorView` to keep string parsing and error-reporting localized to the interface, while keeping core arithmetic validation clean.
- **Method Signatures:** Adjusted some accessor and update signatures in `CheckoutSession` to better handle null safety and prevent concurrent state modification during active checkouts.
- **Test Cases:** Added boundary test cases for empty queue handling (`EmptyQueueException`) and invalid operator sequences (`++`, `//`) in `CalculatorView`.

### Did the Model Really Stand Alone?
Yes. All model classes (`Queue`, `Customer`, `GroceryItem`, `CheckoutGame`, `CheckoutSession`, `PlayerClass`, and `GroceryLibrary`) operate entirely independently of `CheckoutView` and `CalculatorView`. JUnit tests and drivers can execute all game logic, queue management, and scoring updates without importing or initializing any view classes.

### What Happened to Our Unknowns?
- **Figure Out:** We successfully resolved how to track and balance parentheses and operator sequences in $\mathcal{O}(n)$ time inside `CalculatorView`.
- **Still Open:** Fine-tuning the exact timer countdown integration with customer patience decay across multi-threaded or frame-based controller ticks.

### What Doesn't Work Yet?
- Full GUI graphical rendering (currently utilizing a clean console-based ASCII representation).
- Advanced 7-day career progression UI triggers at the end of shifts.

---

## Next Steps

### 1. What's Left to Finish the MVP 


### 2. Change Feature Prediction
- **Selected Non-Trivial Feature:** **"Priority Express Lane"** (A secondary queue that bypasses standard checkout rules for customers with 3 items or fewer, routing them through a fast-track terminal).
- **Classes Predicted to Touch:** 
  - *Model:* `CheckoutSession`, `Customer` (to add item count checks / lane flags).
  - *Controller:* `GameController` (to handle routing logic).
- **Classes Predicted NOT to Change:** `GroceryLibrary`, `GroceryItem`, `CalculatorView`. (We will test this prediction in Deliverable 4 against what actually occurs).

### 3. Stretch Goals
- **Committed:** Priority Express Lane routing rule.
- **Dropped:** Graphical animations for customer movement.

### 4. New Tests Needed


### 5. Task Ownership
- **Stephen:** Model refinements, `CheckoutSession` timer logic, and model unit tests.
- **Sudeep:** `CheckoutView` display updates, `CalculatorView` enhancements, and UI validation.
- **Nimai:** `GameController` integration, express lane routing logic, and attribution documentation.

---

## Attribution & GenAI Note

| Class / Test File | Team Member | Contribution Type |
| :--- | :--- | :--- |
| `Queue.java` & `QueueTest.java` | Stephen | Wrote, Tested |
| `Customer.java`, `GroceryItem.java`, `GroceryLibrary.java` | Stephen | Wrote, Tested |
| `CheckoutGame.java`, `CheckoutSession.java`, `PlayerClass.java` | Stephen | Wrote, Tested |
| `CheckoutView.java` | Sudeep | Wrote, Revised |
| `CalculatorView.java` | Sudeep | Wrote, Tested |
| `GameController.java`, `ToolClass.java` | Nimai | Wrote, Reviewed |
| Integration & Driver files | All | Revised, Tested |

**GenAI Note:** GenAI...
