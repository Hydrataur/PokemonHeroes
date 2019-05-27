package pokemonHeroes;

/**
 * This class represents a simple rectangle
 * It's coordinates and length/height are integers
 */
public class Rectangle {

    /**
     * X coordinate of the rectangle's left side
     * Y coordinate of the rectangle's top side
     * The width of the rectangle
     * The height of the rectangle
     */
    private int x, y, width, height;

    /**
     * Constructs a new rectangle
     * @param x X coordinate of left side
     * @param y Y coordinate of top side
     * @param width Width of rectangle
     * @param height Height of rectangle
     */
    public Rectangle(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Get the rectangle's left side X coordinate
     * @return
     */
    public int getX() { return x; }
    /**
     * Get the rectangle's top side Y coordinate
     * @return
     */
    public int getY() { return y; }
    /**
     * Get the rectangle's width
     * @return
     */
    public int getWidth() { return width; }
    /**
     * Get the rectangle's height
     * @return
     */
    public int getHeight() { return height; }

    /**
     * Checks if a rectangle has been clicked on
     * Sees if x is between rectangle x and rectangle x+width
     * Sees if y is between rectangly y and rectangly y+height
     * @param x The x coordinate of the click
     * @param y The y coordinate of the click
     * @return
     */
    public boolean hasBeenClicked(int x, int y){
        return this.x < x && x < this.x + width && this.y < y && y < this.y + height;
    }

}
