package CheckOutRushGame;

public class Player
{
    private double totalRating;
    private int ratingCount;

    public void updateReputation(double rating)
    {
        if (Double.isNaN(rating) || Double.isInfinite(rating)
            || rating < 0 || rating > 5)
        {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
        totalRating += rating;
        ratingCount++;
    }

    public double getReputation()
    {
        if (ratingCount == 0)
        {
            return 0;
        }
        return totalRating / ratingCount;
    }

    public int getRatingCount()
    {
        return ratingCount;
    }
}
