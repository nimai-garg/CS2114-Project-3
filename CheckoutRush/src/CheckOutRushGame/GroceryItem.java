package CheckOutRushGame;

public class GroceryItem
{
    private GroceryLib item;
    private String name;
    private double price;
    
    public GroceryItem(GroceryLib item) {
        this.item = item;
        name = item.getName();
        price = item.getPrice();
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    @Override
    public String toString() {
        return "[" + getName() + " " + price + "]";
    }

}
