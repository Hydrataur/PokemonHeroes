package pokemonHeroes;

import java.awt.*;

@SuppressWarnings("WeakerAccess")

public class Tile extends Rectangle{

    private int tileX, tileY; //X, Y in the tile arry, and actual X, Y coords

    public Tile(int tileX, int tileY, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public int getTileX() { return tileX; }

    public int getTileY() { return tileY; }

}
