package pokemonHeroes;

public class Tile {

    private int X, Y, leftX, rightX, topY, bottomY;

    public int getLeftX() {
        return leftX;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public Tile(int X, int Y, int leftX, int rightX, int topY, int bottomY) {
        this.X=X;
        this.Y=Y;
        this.leftX = leftX;
        this.rightX = rightX;
        this.topY = topY;
        this.bottomY = bottomY;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "leftX=" + leftX +
                ", rightX=" + rightX +
                ", topY=" + topY +
                ", bottomY=" + bottomY +
                '}';
    }
}
