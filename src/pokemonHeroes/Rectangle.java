package pokemonHeroes;

public class Rectangle {

    private int x, y, width, height;

    public Rectangle(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public boolean hasBeenClicked(int x, int y){
        return this.x < x && x < this.x + width && this.y < y && y < this.y + height;
    }

}
