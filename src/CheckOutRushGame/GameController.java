package CheckOutRushGame;

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

    public void handlePlayerInput()
    {
        if (game.isFinished())
        {
            return;
        }
        CheckoutSession session = game.getCheckoutSession();
        if (session.getCurrentCustomer() == null)
        {
            ToolClass.askOption(new String[] { "Next customer" }, 1, 1);
            handleCheckout();
            return;
        }
        String input = ToolClass.readInput();
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

    public void handleCheckout()
    {
        if (game.isFinished())
        {
            throw new IllegalStateException("The game is finished.");
        }
        CheckoutSession session = game.getCheckoutSession();
        if (session.getCurrentCustomer() != null)
        {
            checkoutView.displayMessage("Complete the current checkout first.");
            return;
        }
        if (session.getCustomersRemaining() == 0)
        {
            checkoutView.displayMessage("No customers are waiting.");
            return;
        }
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

    public void run()
    {
        boolean playAgain = true;
        while (playAgain)
        {
            startGame();
            playAgain = ToolClass.askOption(
                new String[] { "Play again", "Exit" }, 1, 2) == 1;
        }
    }
}
