package CheckOutRushGame;

public enum GroceryLib
{
    // Snacks
    PRINGLES("Pringles Chips", 2.50),
    TORTILLA("Tortilla Chips", 2.25),
    CORN_CHIPS("Corn Chips", 2.00),
    DORITOS("Doritos Chips", 3.50),
    PUFF_CORN("Puffed Corn", 2.75),
    // Beverages
    COCA_COLA("Coca-Cola", 2.00),
    SPRITE("Sprite", 2.00),
    FANTA_ORANGE("Fanta Orange", 1.75),
    WATER("Purified Water", 4.00),
    JUICE("Juice", 3.00),
    JUICE_BOXES("Juice Box", 12.00),
    // Fruit
    BANANA("Banana", 0.25),
    STRAWBERRY("Strawberry", 4.50),
    ORANGE("Orange", 4.25),
    KIWI("Kiwi", 4.50),
    PEAR("Pear", 5.75),
    APPLE("Honey-Crisp Apple", 6.25),
    GRAPES("Grapes", 4.50),
    // Daily essentials
    TOILET_PAPER("Toilet Paper", 19.75),
    PAPER_PLATES("Paper Plates", 8.25),
    DISH_SOAP("Dish Soap", 5.00),
    PAPER_TOWELS("Paper Towels", 6.50),
    PAPER_BOWLS("Paper Bowls", 5.00),
    FACIAL_TISSUES("Tissues", 2.00),
    STEEL_WOOL_PADS("Steel Wool Pads", 1.50);

    private String name;
    private double price;

    private GroceryLib(String n, double p)
    {
        name = n;
        price = p;
    }


    public String getName()
    {
        return name;
    }
    
    public double getPrice()
    {
        return price;
    }

}

