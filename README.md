# Checkout Rush — Deliverable 4: Second Prototype + Tests

## How to Run the Project

### Running the Console Prototype

1. Open `CS2114-Project-3` in Eclipse.
2. In **Package Explorer**, open `src` → `CheckOutRushGame`.
3. Right-click `Main.java` and select **Run As → Java Application**. Play in the **Console** tab.

In the Eclipse console:

1. Choose **1** to serve the next customer in the active lane or **2** to switch lanes.
2. Type `scan` once per item. Use `calc 2.50 + 3.25` to calculate inside the game.
3. Submit the grocery total in dollars. For cash payments, submit the change when prompted.
4. Clear both lanes to advance to the next day. The career lasts seven shifts. An incorrect answer costs five seconds, and an expired checkout ends the career.

### Running the Model & JUnit Test Suite (Independent of View)

1. Open `src` → `CheckOutRushGame` in Eclipse. The project uses the course's `CS2-Support` project for `student.TestCase` and JUnit.
2. Right-click `QueueTest.java`, `CustomerTest.java`, `GroceryItemTest.java`, `CheckoutGameTest.java`, or `CheckoutSessionTest.java` and select **Run As → JUnit Test**. Run `CheckoutConsoleTest.java` the same way for the console regression.
3. Check the **JUnit** tab for a green bar.

For the course test runner, select **Java 21** on the **JRE** tab in **Run → Run Configurations → JUnit**. On the **Arguments** tab, enter `-Djava.security.manager=allow` under **VM arguments**.

The model-only suite passed 53 tests. The full suite passed 54, including a console input regression test. A separate run through the console completed all 56 checkouts across seven shifts and recovered from selecting an empty lane.

---

Verification used Java 21 with the course student testing library. All 36 existing JUnit tests passed. A separate console run completed all 56 checkouts across seven days and recovered from empty, negative, oversized, and malformed inputs.

## Reflection

### How Did the Design Absorb the Express Lane?

Deliverable 3 predicted changes to `CheckoutSession`, `Customer`, `GameController`, and `CheckoutView`. The feature touched those four classes, as predicted.

* `CheckoutSession` owns both queues and the active lane. It routes carts with at most three items to Express and ends the shift after both queues are cleared.
* `Customer.getItemCount()` lets the session route a cart without removing any items.
* `GameController.handleSwitchLane()` forwards the player's request and handles a rejected switch during checkout.
* `CheckoutView` shows the active lane and both waiting counts from the model each time it refreshes. It stores no copy of those values.

`Queue`, `EmptyQueueException`, `GroceryLib`, `GroceryItem`, and `CalculatorView` stayed unchanged. The view gained access to lane labels and counts through the model's public methods. The routing and switching rules stayed in the model. One detail we hadn't planned for was that later days could generate only regular carts, so every shift now starts with at least one cart in each lane.

### What Changed From the Deliverable 3 Plan?

Players now scan items manually so the early-payment guard is visible in the console. The shift recap appears before advancing. If a checkout expires, the model records a zero rating and `CheckoutGame` ends the career. That class changed for timeout handling. We added a console regression alongside the model tests.

A read-only checkout status object would make the next view feature cheaper to build. It could supply the lane counts, timer, and payment phase together so a second display could render the same information through one call.

### Did the Model Really Stand Alone?
Yes. All core model classes (`Queue`, `Customer`, `GroceryItem`, `CheckoutGame`, `CheckoutSession`, `Player`, `GroceryLib`, and `EmptyQueueException`) compile and pass all JUnit model tests with zero imports from `CheckoutView`, `CalculatorView`, or `GameController`. Nothing had to be moved across the model/view boundary to make the test suite run, and the model holds zero references to the view.

### What Happened to Our Unknowns?
* **Figured Out:**
  * *Checkout & Calculator Interactions:* The player types `scan` to scan the next item and can use `CalculatorView` during checkout. `CheckoutSession` tracks scanning and enforces sequential checkout states (`submitCheckout` followed by `submitChange`).
  * *Grocery & Customer Class Design:* Resolved by giving `GroceryItem` item attributes (name, price) populated via `GroceryLib`, and giving `Customer` a `Queue<GroceryItem>` cart alongside customer state methods.
  * *Tracking Mistakes / Firing Condition:* Resolved by introducing a reputation system in `Player` to track the average customer rating. Incorrect totals or change subtract five seconds from the active checkout timer. The resulting customer rating is added to the average after checkout.
* **Timer System Granularity:** One timer starts when checkout begins and covers the whole cart. Expiration ends the career. Tuning the time limits is still open.

### What Doesn't Work Yet?

* **Timer Display:** The console refreshes after input. If time runs out while the terminal is waiting, the game reports it on the next input.
* **Final Playtest:** Later shifts have larger regular carts and less time per item. We still need to playtest the difficulty curve and check that the shift recap is clear.

---

## Next Steps

### 1. Plan to Finish the Final Build (In Priority Order)

1. **Stephen (Siqi Chen):** Playtest early and late shifts, tune the difficulty if needed, and rerun the model tests.
2. **Sudeep Rawal:** Polish the console prompts and shift recap based on the playtest.
3. **Nimai Garg:** Verify the program and tests from a clean checkout and lead the timed demo rehearsal.

These proposed owners follow our Deliverable 3 roles.

### 2. Stretch Goals (Committed vs. Dropped)
* **Committed:**
  * *Priority Express Lane Routing Rule:* Keep the implemented dual-queue routing and lane switches.
  * *Difficulty Scaling Across Shifts:* Increasing cart sizes and tighter shift timers across later days of the 7-day career mode.
* **Dropped:**
  * *Helper Mode (Guiding Customers to Aisles) & Weirdo Customer Types:* Dropped to keep scope focused on cashier queue management, calculator accuracy, and dual-lane routing.
  * *Other scope ideas:* Drop coupons, separate sale prices, inflation, staggered arrivals, and patience decay while waiting for the final build. Cash and card already work in the prototype. Keep the console interface.

---

## Attribution & GenAI Note

### File Attribution Table

The team member column records D3 contributions. Confirm and add each teammate's actual D4 contribution before submitting.

| Class or test file | Team member(s) | Contribution recorded by the team |
| --- | --- | --- |
| `CalculatorView.java` | Sudeep, Nimai | Wrote, revised, tested |
| `CheckoutConsoleTest.java` | Wrote and ran the console input regression |
| `CheckoutGame.java` | Stephen, Sudeep, Nimai| Wrote, revised, tested |
| `CheckoutGameTest.java` | Stephen, Sudeep, Nimai | Wrote, revised, tested |
| `CheckoutSession.java` | Nimai, Stephen, Sudeep | Wrote, revised, tested |
| `CheckoutSessionTest.java` | Wrote and ran the independent model tests |
| `CheckoutView.java` | Sudeep | Wrote, revised, tested |
| `Customer.java` | Nimai, Stephen, Sudeep | Wrote, tested |
| `CustomerTest.java` | Stephen, Sudeep, Nimai | Wrote, tested |
| `EmptyQueueException.java` | Stephen | Wrote, reviewed |
| `GameController.java` | Nimai | Wrote, reviewed, tested |
| `GroceryItem.java` | Stephen | Wrote, tested |
| `GroceryItemTest.java` | Stephen | Wrote, tested |
| `GroceryLib.java` | Stephen, Sudeep | Wrote, reviewed |
| `Main.java` | Stephen, Sudeep, Nimai | Revised, tested |
| `Player.java` | Nimai, Sudeep | Wrote, revised, tested |
| `Queue.java` | Nimai, Stephen | Wrote, tested |
| `QueueTest.java` | Stephen, Sudeep | Wrote, tested |
| `ToolClass.java` | Nimai | Wrote, reviewed, tested |

### GenAI Usage Note
GenAI was used for revising a minor part of our checkoutView class.

<details>
<summary>Deliverable 3 reflection and plan</summary>

## Reflection

### What Changed From Our Specification?
* **Validation Layer & Bad-Input Handling:** Our Deliverable 1 specification grouped all careless and adversarial input handling (empty inputs, invalid calculator operators like `++`, non-numeric entries) conceptually under the model/checkout flow. During implementation, we moved arithmetic string parsing and syntax error detection (`locateErrors`) directly into `CalculatorView`, while keeping domain-state guards (preventing duplicate checkout submissions or premature checkout before scanning all items) inside `CheckoutSession`. **What prompted this:** Separating string-parsing rules into `CalculatorView` kept UI formatting out of core domain logic and made `CheckoutSession` unit tests cleaner to write.
* **Nested Queue Architecture (`Customer` & `GroceryItem`):** Deliverable 1 specified using a custom singly linked `Queue` for the checkout line and noted uncertainty about how to structure `Customer` and `GroceryItem`. In implementation, we used our custom `Queue` both for the line of waiting `Customer` objects and for each customer's individual shopping cart of `GroceryItem` objects. **What prompted this:** Using FIFO queues for both customers and items unified our data structure design and enforced sequential item scanning (`dequeue` item-by-item).
* **Custom Queue Exception Handling (`EmptyQueueException`):** Rather than returning `null` or relying solely on `isEmpty()` checks when calling `dequeue()` or `front()`, we introduced `EmptyQueueException` and added dedicated boundary unit tests (`QueueTest`, `CheckoutGameTest`, `CustomerTest`, `GroceryItemTest`). **What prompted this:** Explicit exceptions prevented silent null-pointer bugs when scanning the final item in a cart or advancing an empty customer line.
* **Two-Step Checkout State (`submitCheckout` vs. `submitChange`):** Deliverable 1 described checkout as a single step where players calculate totals and change. We split this into two distinct model states (`isAwaitingChange()`) in `CheckoutSession`. **What prompted this:** Separating total calculation from cash change calculation allowed `GameController` and `CheckoutView` to pause cleanly between user inputs without blocking the model.

### Did the Model Really Stand Alone?
Yes. All core model classes (`Queue`, `Customer`, `GroceryItem`, `CheckoutGame`, `CheckoutSession`, `Player`, `GroceryLib`, and `EmptyQueueException`) compile and pass all `student.TestCase` unit tests with zero imports from `CheckoutView`, `CalculatorView`, or `GameController`. Nothing had to be moved across the model/view boundary to make the test suite run, and the model holds zero references to the view.

### What Happened to Our Unknowns?
* **Figured Out:**
  * *Checkout & Calculator Interactions:* Resolved by making `CalculatorView` an independent helper tool available during the shift while `CheckoutSession` tracks item scanning and enforces sequential checkout states (`submitCheckout` followed by `submitChange`).
  * *Grocery & Customer Class Design:* Resolved by giving `GroceryItem` item attributes (name, price) populated via `GroceryLib`, and giving `Customer` a `Queue<GroceryItem>` cart alongside customer state methods.
  * *Tracking Mistakes / Firing Condition:* Resolved by introducing a reputation system in `Player` to track the average customer rating. Incorrect totals or change subtract five seconds from the active checkout timer. The resulting customer rating is added to the average after checkout.
* **Still Open:**
  * *Timer System Granularity:* One timer starts when checkout begins for a customer and covers the whole cart. Waiting customers do not lose patience in line. Tuning the time limits and deciding how expiration should end a shift are still open.

### What Doesn't Work Yet?
* **Full GUI Graphical Rendering:** The view currently runs as a clean console/thin interface rather than full graphical animations.
* **Career Summary:** The console already advances through seven days, and later days generate larger carts. A dedicated end-of-shift summary is still missing.
* **Failure Rules and Regression Tests:** Time expiration affects the customer rating. The firing and game-over rules still need implementation. Dedicated tests for early payment, duplicate checkout actions, and serving an empty line remain to be added.

---

## Next Steps

### 1. Plan to Finish the MVP (In Priority Order)
1. **End-of-Shift Career Progression UI (`CheckoutView` & `GameController`):** Use the existing transition in `GameController.endCheckOut()` to show a shift summary before advancing.
2. **Complete Reputation & Mistake Penalties:** Connect calculation errors and shift timer expirations directly to `Player` reputation updates and game-over ("fired") triggers.
3. **Harden Careless & Adversarial Input Cases:** Ensure empty inputs, negative/out-of-range prices, and duplicate checkout submissions are gracefully caught in `GameController` and `CheckoutView` as specified in Deliverable 1.

### 2. Deliverable 4 Change Feature: **Priority Express Lane**
* **Selected Non-Trivial Feature:** **"Priority Express Lane"** — In Deliverable 1, we explicitly noted that a single FIFO `Queue` does not handle priority routing and restricted our MVP to a single standard line. For Deliverable 4, we will add a secondary **Express Lane (`<= 3` items)** with a routing rule that sends small-cart customers to a fast-track queue while larger carts go to the regular queue, allowing the player to switch between lanes through the controller and view.
* **Architectural Impact Prediction (Specific Classes & Methods):**
  * **Model Classes That WILL Change:**
    * `CheckoutSession`: Add a second `Queue<Customer> expressQueue` field and an active-lane boolean flag. Add `addCustomer(Customer)` to route customers with $\le 3$ items to `expressQueue`. Add `switchActiveLane()` and `getExpressQueueSize()`, and update `startCheckOut()`, `getCustomersRemaining()`, `completeCheckout()`, and `endSession()` to account for both queues. Keep `isFinished()` as the accessor for the resulting session state.
    * `Customer`: Add a helper method `getItemCount()` so `CheckoutSession` can evaluate the 3-item routing rule without dequeuing items.
  * **Controller & View Classes That WILL Change:**
    * `GameController`: Add `handleSwitchLane()` to forward user lane-switching input to `CheckoutSession.switchActiveLane()`.
    * `CheckoutView`: Add a "Switch Lane" control and a second queue display showing customers waiting in the Express Lane.
  * **Classes That Should NOT Need to Change (Protected by Design):**
    * `Queue` and `EmptyQueueException`: Our custom singly linked FIFO structure is completely agnostic to whether it stores regular or express customers.
    * `GroceryLib` and `GroceryItem`: Product definitions and prices are unaffected by queue routing.
    * `CalculatorView`: Arithmetic evaluation and syntax validation remain completely unchanged.

### 3. Stretch Goals (Committed vs. Dropped)
* **Committed:**
  * *Priority Express Lane Routing Rule:* Adds dual-queue routing and lane-switching gameplay.
  * *Difficulty Scaling Across Shifts:* Increasing cart sizes and tighter shift timers across later days of the 7-day career mode.
* **Dropped:**
  * *Helper Mode (Guiding Customers to Aisles) & Weirdo Customer Types:* Dropped to keep scope focused on cashier queue management, calculator accuracy, and dual-lane routing.
  * *Other scope ideas:* Drop coupons, separate sale prices, inflation, staggered arrivals, and patience decay while waiting for this checkpoint. Cash and card already work in the prototype. Keep the console interface.

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

</details>
