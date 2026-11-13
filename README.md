# Checkout Rush — Deliverable 3: First Prototype

## How to Run the Project

### 1. Running the Model & JUnit Test Suite (Independent of View)
The core Queue-backed model operates completely independently of any GUI components. To execute the automated model tests:
1. Open the project (`CS2114-Project-3`) in Eclipse.
2. Navigate to `src/CheckOutRushGame/` (or `src/CheckoutRushTest/`) in the **Package Explorer**.
3. Right-click **`CheckoutGameTest.java`** (or `QueueTest.java`, `CustomerTest.java`, `GroceryItemTest.java`) and select **Run As > JUnit Test**.
4. Verify that the JUnit runner tab displays green passing bars for all queue operations (`enqueue`, `dequeue`, `front`, `isEmpty`) and session progression logic (`CheckoutGame`, `CheckoutSession`).

### 2. Running the Thin View (MVP End-to-End Path)
The thin view communicates strictly through `GameController` without the model holding any references to UI classes:
1. Locate **`Main.java`** or **`GameController.java`** inside `src/CheckOutRushGame/`.
2. Right-click and select **Run As > Java Application**.
3. Use the interactive view controls to start a checkout session, scan items from the customer's `Queue`, submit the checkout total, and issue change.

---

## Reflection

### What Changed From Our Specification?
* **Validation Layer & Bad-Input Handling:** Our Deliverable 1 specification grouped all careless and adversarial input handling (empty inputs, invalid calculator operators like `++`, non-numeric entries) conceptually under the model/checkout flow. During implementation, we moved arithmetic string parsing and syntax error detection (`locateErrors`) directly into `CalculatorView`, while keeping domain-state guards (preventing duplicate checkout submissions or premature checkout before scanning all items) inside `CheckoutSession`. **What prompted this:** Separating string-parsing rules into `CalculatorView` kept UI formatting out of core domain logic and made `CheckoutSession` unit tests cleaner to write.
* **Nested Queue Architecture (`Customer` & `GroceryItem`):** Deliverable 1 specified using a custom singly linked `Queue` for the checkout line and noted uncertainty about how to structure `Customer` and `GroceryItem`. In implementation, we used our custom `Queue` both for the line of waiting `Customer` objects and for each customer's individual shopping cart of `GroceryItem` objects. **What prompted this:** Using FIFO queues for both customers and items unified our data structure design and enforced sequential item scanning (`dequeue` item-by-item).
* **Custom Queue Exception Handling (`EmptyQueueException`):** Rather than returning `null` or relying solely on `isEmpty()` checks when calling `dequeue()` or `front()`, we introduced `EmptyQueueException` and added dedicated boundary unit tests (`QueueTest`, `CheckoutGameTest`, `CustomerTest`, `GroceryItemTest`). **What prompted this:** Explicit exceptions prevented silent null-pointer bugs when scanning the final item in a cart or advancing an empty customer line.
* **Two-Step Checkout State (`submitCheckout` vs. `submitChange`):** Deliverable 1 described checkout as a single step where players calculate totals and change. We split this into two distinct model states (`isAwaitingChange()`) in `CheckoutSession`. **What prompted this:** Separating total calculation from cash change calculation allowed `GameController` and `CheckoutView` to pause cleanly between user inputs without blocking the model.

### Did the Model Really Stand Alone?
Yes. All core model classes (`Queue`, `Customer`, `GroceryItem`, `CheckoutGame`, `CheckoutSession`, `PlayerClass`, `GroceryLibrary`, and `EmptyQueueException`) compile and pass all `student.TestCase` unit tests with zero imports from `CheckoutView`, `CalculatorView`, or `GameController`. Nothing had to be moved across the model/view boundary to make the test suite run, and the model holds zero references to the view.

### What Happened to Our Unknowns?
* **Figured Out:** 
  * *Checkout & Calculator Interactions:* Resolved by making `CalculatorView` an independent helper tool available during the shift while `CheckoutSession` tracks item scanning and enforces sequential checkout states (`submitCheckout` followed by `submitChange`).
  * *Grocery & Customer Class Design:* Resolved by giving `GroceryItem` item attributes (name, price) populated via `GroceryLibrary`, and giving `Customer` a `Queue<GroceryItem>` cart alongside customer state methods.
  * *Tracking Mistakes / Firing Condition:* Resolved by introducing a reputation/strikes system in `PlayerClass` so calculation errors and shift failures decrement reputation rather than causing an immediate crash, leading to failure if reputation drops to zero.
* **Still Open:** 
  * *Timer System Granularity:* In Deliverable 1, we asked whether to use a single shift timer or individual customer patience timers. We currently use a shift-level timer in `CheckoutSession`, while individual customer patience timers (one of our stretch goals) remain open for fine-tuning across Days 1–7.

### What Doesn't Work Yet?
* **Full GUI Graphical Rendering:** The view currently runs as a clean console/thin interface rather than full graphical animations.
* **7-Day Career Progression UI:** While `CheckoutGame` tracks multi-day progression (`currDay` and `nextSession()`), the end-of-shift career summary screen and difficulty scaling across all 7 days are not yet fully wired into `CheckoutView`.
* **Adversarial & Edge-Case Guards in Controller:** While `CalculatorView` catches invalid operators (`++`, `//`), premature button/action triggers in `GameController` when the queue is empty still need full end-to-end UI guard coverage.

---

## Next Steps

### 1. Plan to Finish the MVP (In Priority Order)
1. **End-of-Shift Career Progression UI (`CheckoutView` & `GameController`):** Wire the end-of-day summary trigger so completing a shift cleanly advances `CheckoutGame.nextSession()` through the 7-day career mode.
2. **Complete Reputation & Mistake Penalties:** Connect calculation errors and shift timer expirations directly to `PlayerClass` reputation updates and game-over ("fired") triggers.
3. **Harden Careless & Adversarial Input Cases:** Ensure empty inputs, negative/out-of-range prices, and duplicate checkout submissions are gracefully caught in `GameController` and `CheckoutView` as specified in Deliverable 1.

### 2. Deliverable 4 Change Feature: **Priority Express Lane**
* **Selected Non-Trivial Feature:** **"Priority Express Lane"** — In Deliverable 1, we explicitly noted that a single FIFO `Queue` does not handle priority routing and restricted our MVP to a single standard line. For Deliverable 4, we will add a secondary **Express Lane (`<= 3` items)** with a routing rule that sends small-cart customers to a fast-track queue while larger carts go to the regular queue, allowing the player to switch between lanes through the controller and view.
* **Architectural Impact Prediction (Specific Classes & Methods):**
  * **Model Classes That WILL Change:**
    * `CheckoutSession`: Add a second `Queue<Customer> expressQueue` field and an active-lane boolean flag. Update `addCustomer(Customer)` to route customers with $\le 3$ items to `expressQueue`. Add `switchActiveLane()` and `getExpressQueueSize()`, and update `startCheckOut()` and `isFinished()` to account for both queues.
    * `Customer`: Add a helper method `getItemCount()` so `CheckoutSession` can evaluate the 3-item routing rule without dequeuing items.
  * **Controller & View Classes That WILL Change:**
    * `GameController`: Add `handleSwitchLane()` to forward user lane-switching input to `CheckoutSession.switchActiveLane()`.
    * `CheckoutView`: Add a "Switch Lane" control and a second queue display showing customers waiting in the Express Lane.
  * **Classes That Should NOT Need to Change (Protected by Design):**
    * `Queue` and `EmptyQueueException`: Our custom singly linked FIFO structure is completely agnostic to whether it stores regular or express customers.
    * `GroceryLibrary` and `GroceryItem`: Product definitions and prices are unaffected by queue routing.
    * `CalculatorView`: Arithmetic evaluation and syntax validation remain completely unchanged.

### 3. Stretch Goals (Committed vs. Dropped)
* **Committed:** 
  * *Priority Express Lane Routing Rule:* Adds dual-queue routing and lane-switching gameplay.
  * *Difficulty Scaling Across Shifts:* Increasing cart sizes and tighter shift timers across later days of the 7-day career mode.
* **Dropped:** 
  * *Helper Mode (Guiding Customers to Aisles) & Weirdo Customer Types:* Dropped to keep scope focused on cashier queue management, calculator accuracy, and dual-lane routing.
  * *Graphical Animations:* Dropped in favor of a clean, responsive thin interface.

### 4. New Tests Needed for Deliverable 4
* `CheckoutSessionTest`: 
  * `testExpressLaneRouting()`: Verify customers with $\le 3$ items are enqueued into `expressQueue` and customers with $> 3$ items enter the standard queue.
  * `testSwitchActiveLane()`: Verify `startCheckOut()` serves from whichever lane is currently active and toggles cleanly.
  * `testSessionFinishedRequiresBothQueuesEmpty()`: Verify `isFinished()` returns `false` if one queue still has waiting customers.
  * Bad-input tests: Attempting to switch lanes mid-checkout (`IllegalStateException`) and attempting to serve from an empty express lane.
* `CustomerTest`:
  * `testGetItemCount()`: Verify accurate item counts before and after scanning items.

### 5. Task Ownership
* **Stephen (Siqi Chen):** Model refinements, `CheckoutSession` dual-queue routing logic, `Customer.getItemCount()`, and model unit tests (`QueueTest`, `CustomerTest`, `CheckoutGameTest`).
* **Sudeep Rawal:** `CheckoutView` display updates, express lane UI controls, `CalculatorView` enhancements, and UI/calculator validation tests.
* **Nimai Garg:** `GameController.handleSwitchLane()` integration, careless/adversarial input guards in the controller, and integration/driver verification.

---

## Attribution & GenAI Note

### File Attribution Table

| Class / Test File | Team Member | Contribution Type |
| :--- | :--- | :--- |
| `Queue.java` & `QueueTest.java` | Stephen, Sudeep | Wrote, Tested |
| `EmptyQueueException.java` | Stephen | Wrote, Reviewed |
| `Customer.java` & `CustomerTest.java` | Stephen, Sudeep | Wrote, Tested |
| `GroceryItem.java` & `GroceryItemTest.java` | Stephen | Wrote, Tested |
| `GroceryLibrary.java` | Stephen, Sudeep | Wrote, Reviewed |
| `CheckoutGame.java`, `CheckoutSession.java`, `PlayerClass.java`, `CheckoutGameTest.java` | Stephen, Sudeep | Wrote, Revised, Tested |
| `CheckoutView.java` (View components & UI tests) | Sudeep | Wrote, Revised, Tested |
| `CalculatorView.java` (Calculator logic & unit tests) | Sudeep | Wrote, Revised, Tested |
| `GameController.java`, `ToolClass.java` (Controller & integration tests) | Nimai | Wrote, Reviewed, Tested |
| `Main.java` & Console Driver files | All Members | Revised, Tested |

### GenAI Usage Note
GenAI...
