package CheckOutRushGame;

/** Connects player input and both views to the game model. */
public class GameController
{
    private CheckoutGame game;
    private final CheckoutView checkoutView;
    private final CalculatorView calculatorView;
    private boolean checkoutPending;
    private String calculatorResult;

    public GameController()
    {
        game = new CheckoutGame();
        checkoutView = new CheckoutView();
        calculatorView = new CalculatorView();
    }

    /** Starts a fresh seven-day game. */
    public void startGame()
    {
        game = new CheckoutGame();
        checkoutPending = false;
        calculatorResult = null;
        checkoutView.displayDay(game.getCurrDay());
        while (!game.isFinished())
        {
            updateView();
            handlePlayerInput();
        }
    }

    /** Processes one menu selection, calculator expression, or payment answer. */
    public void handlePlayerInput()
    {
        if (game.isFinished())
        {
            return;
        }
        String input = ToolClass.readInput();
        CheckoutSession session = game.getCheckoutSession();
        if (session.getCurrentCustomer() == null)
        {
            if ("1".equals(input))
            {
                handleCheckout();
            }
            else
            {
                checkoutView.displayMessage("Choose 1 to welcome the next customer.");
            }
            return;
        }
        if (input.startsWith("calc "))
        {
            calculatorResult = calculatorView.calculate(input.substring(5));
            return;
        }
        try
        {
            double amount = Double.parseDouble(input);
            if (session.isAwaitingChange())
            {
                if (!session.submitChange(amount))
                {
                    checkoutView.displayMessage(
                        "Customer: This change isn't right! (-5 seconds)");
                }
            }
            else if (!session.submitCheckout(amount))
            {
                checkoutView.displayMessage("Incorrect total. Try again. (-5 seconds)");
            }
        }
        catch (IllegalArgumentException exception)
        {
            checkoutView.displayMessage(
                "Enter a valid amount or use calc followed by an expression.");
            return;
        }
        if (session.getCurrentCustomer() == null)
        {
            endCheckOut();
        }
    }

    /** Starts the chosen customer's checkout and asks the model to scan the basket. */
    public void handleCheckout()
    {
        if (game.isFinished())
        {
            throw new IllegalStateException("The game is finished.");
        }
        CheckoutSession session = game.getCheckoutSession();
        session.startCheckOut();
        checkoutPending = true;
        Customer customer = session.getCurrentCustomer();
        checkoutView.displayCustomer(customer);
        while (!customer.getGroceryQueue().isEmpty())
        {
            checkoutView.displayScannedItem(session.scanNextItem());
        }
        checkoutView.displayMessage(
            "Enter an amount, or type calc followed by an expression.");
    }

    /** Displays the model's completed result and advances a completed day. */
    public void endCheckOut()
    {
        if (!checkoutPending)
        {
            return;
        }
        CheckoutSession session = game.getCheckoutSession();
        if (session.getCurrentCustomer() != null)
        {
            throw new IllegalStateException("Complete payment first.");
        }
        // The session records the rating when payment succeeds, exactly once.
        checkoutPending = false;
        checkoutView.displayCheckoutResult(session);
        if (session.isFinished())
        {
            game.nextSession();
            if (game.isFinished())
            {
                checkoutView.displayGameResult(game);
            }
            else
            {
                checkoutView.displayDay(game.getCurrDay());
            }
        }
    }

    /** Refreshes calculator output, reputation, customer timer, and input prompt. */
    public void updateView()
    {
        if (calculatorResult != null)
        {
            calculatorView.displayResult(calculatorResult);
            calculatorResult = null;
        }
        if (!game.isFinished())
        {
            checkoutView.displayStatus(game.getCheckoutSession());
        }
    }

    /** Keeps the existing entry point and replay menu. */
    public void run()
    {
        boolean playAgain = true;
        while (playAgain)
        {
            startGame();
            checkoutView.displayMessage("[1] Play again\n[2] Exit");
            String choice = ToolClass.readInput();
            while (!"1".equals(choice) && !"2".equals(choice))
            {
                checkoutView.displayMessage("Choose 1 to play again or 2 to exit.");
                choice = ToolClass.readInput();
            }
            playAgain = "1".equals(choice);
        }
    }
}
