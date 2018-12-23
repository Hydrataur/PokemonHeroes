package pokemonHeroes;

@SuppressWarnings("WeakerAccess")

public class Unit {

    private int maxHealth;
    private int currentHealth;
    private Move[] moves = new Move[4];
    private int defense;
    private int movement;  //Amount of tiles a Pokemon can move during its turn
    private int initiative; //Affects the Pokemon's spot in Queue. Higher initiative means it'll be higher in the queue.
    private int currentShots;  //If the Pokemon is ranged, then this counts how many shots it has left before it must resort to melee combat

    private boolean team; //Tells which team this unit belongs to
    private boolean large;  //True if a Pokemon is large (takes up four spaces) or false if small (takes up one space)
    private boolean flying; //Tells if a Pokemon is able to fly and ignore hazards while moving

    private int unitsInStack;  //Number of units currently in a stack. Might need to move somewhere else
    private String unitName;

    private int x; //Actual X, Y coords for place on board
    private int y;

    private int tileX; //X, Y of tile they're on
    private int tileY;

    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setCurrentShots(int currentShots) {
        this.currentShots = currentShots;
    }

    public void setUnitsInStack(int unitsInStack) {
        this.unitsInStack = unitsInStack;
    }

    public void setInitiative(int initiative){
        this.initiative = initiative;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public Move[] getMoves() { return moves; }

    public int getDefense() {
        return defense;
    }

    public int getMovement() {
        return movement;
    }

    public int getCurrentShots() {
        return currentShots;
    }

    public boolean isLarge() {
        return large;
    }

    public boolean isTeam() { return team; }

    public int getUnitsInStack() {
        return unitsInStack;
    }

    public String getUnitName() {
        return unitName;
    }

    public int getInitiative(){return initiative;}

    public boolean isFlying() {
        return flying;
    }

    public Unit(int maxHealth, int currentHealth, Move[] moves, int defense, int movement, int currentShots, boolean large, boolean flying, int unitsInStack, String unitName, int initiative, boolean team){
        this.maxHealth=maxHealth;
        this.currentHealth=currentHealth;
        this.moves=moves;
        this.defense=defense;
        this.movement=movement;
        this.currentShots=currentShots;
        this.large=large;
        this.unitsInStack=unitsInStack;
        this.unitName=unitName;
        this.initiative=initiative;
        this.team=team;

        if (team) //Make sure the units on each team are facing the correct direction
            setDirection("Right");
        else
            setDirection("Left");
    }

}

