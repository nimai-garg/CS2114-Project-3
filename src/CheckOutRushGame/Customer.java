package CheckOutRushGame;

import static CheckOutRushGame.ToolClass.*;

public class Customer
{
    private String[] identities = { "Student", "Worker", "Housewife" };

    private String identity;
    private Queue<GroceryItem> groceryQueue;
    private String payment;
    private int patience;

    public Customer(int currDay)
    {
        this(currDay, randomInt(currDay + 2, currDay + 4));
    }

    Customer(int currDay, int itemCount)
    {
        if (currDay < 1 || currDay > 7)
        {
            throw new IllegalArgumentException("Day must be between 1 and 7.");
        }

        if (itemCount < 1)
        {
            throw new IllegalArgumentException("A cart must contain at least one item.");
        }

        identity = identities[randomInt(0, identities.length - 1)];
        payment = randomInt(0, 1) == 0 ? "cash" : "credit";

        patience = (int)Math.ceil(itemCount * (10 - 0.5 * (currDay - 1)));
        GroceryLib[] products = GroceryLib.values();
        groceryQueue = new Queue<>();
        for (int i = 0; i < itemCount; i++)
        {
            GroceryLib product = products[randomInt(0, products.length - 1)];
            groceryQueue.enqueue(new GroceryItem(product));
        }
    }


    public String getPayment()
    {
        return payment;
    }


    public void setPayment(String method)
    {
        if (method == null
            || (!"cash".equals(method) && !"credit".equals(method)))
        {
            throw new IllegalArgumentException(
                "Payment must be cash or credit.");
        }
        payment = method;
    }


    public int getItemCount()
    {
        return groceryQueue.size();
    }


    public Queue<GroceryItem> getGroceryQueue()
    {
        return groceryQueue;
    }


    public void setGroceryQueue(Queue<GroceryItem> groceryQueue)
    {
        if (groceryQueue == null || groceryQueue.isEmpty())
        {
            throw new IllegalArgumentException(
                "The queue must have a first item.");
        }
        this.groceryQueue = groceryQueue;
    }


    public String getIdentity()
    {
        return identity;
    }


    public void setIdentity(String id)
    {
        for (String str : identities)
        {
            if (str.equals(id))
            {
                identity = str;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid customer identity.");
    }


    public int getPatience()
    {
        return patience;
    }


    public void setPatience(int patientTime)
    {
        if (patientTime < 0)
        {
            throw new IllegalArgumentException(
                "Patience must be a positive integer.");
        }
        patience = patientTime;
    }


    @Override
    public String toString()
    {
        return "Customer: " + identity + " | Payment: " + payment
            + " | Patience: " + patience + " seconds";
    }

}
