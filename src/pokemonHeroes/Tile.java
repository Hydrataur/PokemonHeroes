package pokemonHeroes;

@SuppressWarnings("WeakerAccess")

/**
 * This class represents a tile within the tile array.
 * It extends Rectangle and adds two more integer variables
 */
public class Tile extends Rectangle{
    /**
     * tileX is the x spot in the tile array
     * tileY is the y spot in the tile array
     */
    private int tileX, tileY;

    /**
     * Tile constructor
     * @param tileX X position in array
     * @param tileY Y position in array
     * @param x X coordinate on the screen
     * @param y Y coordinate on the screen
     * @param width Width of tile
     * @param height Height of tile
     */
    public Tile(int tileX, int tileY, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.tileX = tileX;
        this.tileY = tileY;
    }

    /**
     * Returns the X position in the array
     * @return
     */
    public int getTileX() { return tileX; }

    /**
     * Returns the Y position in the array
     * @return
     */
    public int getTileY() { return tileY; }

}
