package pokemonHeroes;

public class Unit {

    private int maxHealth;
    private int currentHealth;
    private int attack;
    private int defense;
    private int movement;  //Amount of tiles a Pokemon can move during its turn
    private int initiative; //Affects the Pokemon's spot in Queue. Higher initiative means it'll be higher in the queue.
    private int currentShots;  //If the Pokemon is ranged, then this counts how many shots it has left before it must resort to melee combat

    private boolean team; //Tells which team this unit belongs to
    private boolean large;  //True if a Pokemon is large (takes up four spaces) or false if small (takes up one space)

    private int unitsInStack;  //Number of units currently in a stack. Might need to move somewhere else
    private String unitName;

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

    public int getAttack() {
        return attack;
    }

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

    public Unit(int maxHealth, int currentHealth, int attack, int defense, int movement, int currentShots, boolean large, int unitsInStack, String unitName, int initiative, boolean team){
        this.maxHealth=maxHealth;
        this.currentHealth=currentHealth;
        this.attack=attack;
        this.defense=defense;
        this.movement=movement;
        this.currentShots=currentShots;
        this.large=large;
        this.unitsInStack=unitsInStack;
        this.unitName=unitName;
        this.initiative=initiative;
        this.team=team;
    }




}
