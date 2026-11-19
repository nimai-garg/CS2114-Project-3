package CheckOutRushGame;

import java.util.NoSuchElementException;

public class GameController
{
    private CheckoutGame game;
    private final CheckoutView checkoutView;
    private final CalculatorView calculatorView;
    private boolean checkoutPending;

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
            if (checkoutView.chooseLaneAction() == 1)
            {
                handleCheckout();
            }
            else
            {
                handleSwitchLane();
            }
            return;
        }
        String input = ToolClass.readInput();
        if (session.expireCheckoutIfNeeded())
        {
            endCheckOut();
            return;
        }
        if ("scan".equalsIgnoreCase(input))
        {
            if (session.getCurrentCustomer().getItemCount() == 0)
            {
                checkoutView.displayMessage("All items have already been scanned.");
            }
            else
            {
                checkoutView.displayScannedItem(session.scanNextItem());
            }
            return;
        }
        if ("switch".equalsIgnoreCase(input))
        {
            handleSwitchLane();
            return;
        }
        if ("calc".equals(input) || input.startsWith("calc "))
        {
            String expression = input.substring(4).replaceAll("([+*/-]) +(?=[+*/-])", "$1");
            calculatorView.displayResult(calculatorView.calculate(expression));
            return;
        }
        if (session.getCurrentCustomer().getItemCount() > 0)
        {
            checkoutView.displayMessage("Scan every item before submitting a total.");
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
        try
        {
            session.startCheckOut();
            checkoutPending = true;
        }
        catch (IllegalStateException exception)
        {
            checkoutView.displayMessage(exception.getMessage());
        }
    }

    public void handleSwitchLane()
    {
        CheckoutSession session = game.getCheckoutSession();
        try
        {
            session.switchActiveLane();
        }
        catch (IllegalStateException exception)
        {
            checkoutView.displayMessage(exception.getMessage());
        }
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
            checkoutView.displayShiftResult(game.getCurrDay(), session.hasTimedOut());
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
        if (!game.isFinished())
        {
            CheckoutSession session = game.getCheckoutSession();
            if (session.expireCheckoutIfNeeded())
            {
                endCheckOut();
            }
            else
            {
                checkoutView.displayStatus(session);
            }
        }
    }

    public void run()
    {
        try
        {
            boolean playAgain = true;
            while (playAgain)
            {
                startGame();
                playAgain = ToolClass.askOption(
                    new String[] { "Play again", "Exit" }, 1, 2) == 1;
            }
        }
        catch (NoSuchElementException exception)
        {
            checkoutView.displayMessage("Checkout Rush closed.");
        }
    }
}
