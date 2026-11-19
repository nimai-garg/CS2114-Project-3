# Checkout Rush — Deliverable 4: Second Prototype + Tests

## How to Run the Project

Open `CS2114-Project-3` in Eclipse with `CS2-Support`, `CS2GraphWindowLib`, and `CS2DataStructuresLib` in the same workspace. The code and tests are in `src/CheckOutRushGame/`. Enable **Project > Build Automatically** to compile them.

- **Game:** Right-click `Main.java` and select **Run As > Java Application**. In the console, choose **1** to serve the next customer or **2** to switch lanes. Type `scan` once per item, then enter the total and the change if prompted.
- **Tests:** Right-click the package and select **Run As > JUnit Test**. In the JUnit run configuration, select **Java 21** under **JRE** and enter `-Djava.security.manager=allow` under **Arguments > VM arguments** for `student.TestCase`. All 54 tests passed. The 53 model tests run independently of the view. `CheckoutConsoleTest` checks console input recovery.

## Reflection

Deliverable 3 predicted changes to four classes. The Express Lane touched those four:

- `CheckoutSession` owns both FIFO queues, routes carts with three or fewer items to Express, and ends a successful shift after both queues are clear.
- `Customer.getItemCount()` lets the session route carts without removing items.
- `GameController.handleSwitchLane()` forwards the switch request and reports a rejection during checkout.
- `CheckoutView` reads the active lane and both waiting counts from the model on each refresh. It keeps no copy of model state.

`Queue`, `EmptyQueueException`, `GroceryLib`, `GroceryItem`, and `CalculatorView` stayed unchanged. For the Express Lane, the view reads the active-lane flag and waiting counts. Routing and switch validation stay in the model. We guaranteed one cart in each lane per shift because later days can otherwise generate only regular carts.

Compared with the D3 plan, we added manual scanning to exercise the early-payment guard and a shift recap before advancing. Timeout records a zero rating and ends the career, which required a change to `CheckoutGame`. The tests cover interleaved FIFO, lane boundaries, early or repeated payment, invalid amounts and expressions, and timeout.

The console updates after input, so it reports expiration on the next input if the player pauses. A read-only checkout status object would make the next display cheaper to add by providing the lane counts, timer, and payment phase in one call.

## Next Steps

1. **Stephen (Siqi Chen)** — Playtest early and late shifts, adjust difficulty if needed, and rerun the model tests.
2. **Sudeep Rawal** — Polish the console prompts and shift recap based on the playtest.
3. **Nimai Garg** — Verify the game and tests from a clean checkout and lead the demo rehearsal.

- **Committed:** Express Lane and difficulty scaling across shifts.
- **Dropped:** Helper mode, unusual customer types, graphical animations, coupons, separate sale prices, inflation, staggered arrivals, and patience decay while waiting.

### Three-Minute Demo

| Time | What to show |
| --- | --- |
| 0:00–0:35 | Launch Day 1, show both waiting counts, and explain the three-item Express limit. |
| 0:35–1:10 | Start the Regular checkout, try paying early, scan the four items, and complete payment. |
| 1:10–1:25 | Switch to Express and show the active lane. |
| 1:25–1:50 | Start the Express checkout, try switching mid-checkout, then finish the three-item sale. |
| 1:50–2:45 | Show the updated waiting counts and the passing tests for routing, interleaved FIFO, and shift completion. |
| 2:45–3:00 | Allow time for transitions and closing. |

## Attribution & GenAI Note

### File Attribution Table

| Class / Test File | Team Member | Contribution Type |
| --- | --- | --- |
| `CalculatorView.java` | Sudeep, Nimai | Wrote, revised, tested |
| `CheckoutConsoleTest.java` | Nimai, Sudeep | Wrote, revised |
| `CheckoutGame.java` | Stephen, Sudeep, Nimai | Wrote, revised, tested |
| `CheckoutGameTest.java` | Stephen, Sudeep, Nimai | Wrote, revised, tested |
| `CheckoutSession.java` | Nimai, Stephen, Sudeep | Wrote, revised, tested |
| `CheckoutSessionTest.java` | Nimai, Stephen | Wrote, revised |
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

GenAI was used for revising a minor part of our checkoutView class