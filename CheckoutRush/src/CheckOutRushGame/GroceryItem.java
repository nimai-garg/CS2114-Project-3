
package CheckOutRushGame;

import java.util.Locale;

public class GroceryItem
{
    private GroceryLib item;
    private String name;
    private double price;

    public GroceryItem(GroceryLib item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Grocery item cannot be null.");
        }

        this.item = item;
        name = item.getName();
        price = item.getPrice();
    }


    public String getName()
    {
        return name;
    }


    public double getPrice()
    {
        return price;
    }


    @Override
    public String toString()
    {
        return String.format(Locale.US, "%s - $%.2f", name, price);
    }
}
