package pokemonHeroes;

import java.awt.*;

public class SceneFunctions {

    @SuppressWarnings("WeakerAccess")

    protected static Unit[] createQueue(Trainer trainer1, Trainer trainer2) { //Creates the queue according to unit initiative
        Unit[] queue = new Unit[14];
        for (int i = 0; i < 7; i++) {
            if (trainer1.getUnits()[i] != null)
                queue[i] = trainer1.getUnits()[i];
            else
                queue[i] = null;
            if (trainer2.getUnits()[i] != null)
                queue[i + 7] = trainer2.getUnits()[i];
            else
                queue[i+7] = null;
        }

        Unit.fillStats(queue);

        Unit temp;
        for (int i = 0; i < queue.length; i++) {
            for (int j = 1; j < queue.length - i; j++) {
                if (queue[j-1]!=null && queue[j]!=null)
                    if (queue[j - 1].getInitiative() < queue[j].getInitiative()) {
                        temp = queue[j - 1];
                        queue[j - 1] = queue[j];
                        queue[j] = temp;
                    }

            }
        }

        return queue;
    }

    public static Unit[] updateQueue(Unit[] queue){

        System.out.println(queue[0].getUnitName());

        int numAlive = 0; //To tell how long the new queue needs to be

        for (Unit unit : queue)
            if (unit.getCurrentHealth()>0)
                numAlive++;

        Unit[] newQueue = new Unit[numAlive]; //New queue to contain only those who are still alive
        System.out.println(numAlive);
        int counter = 0; //Skips over dead units

        for (int i = 0; i<newQueue.length; i++){
            while (queue[counter].getCurrentHealth()<=0){
                counter++;
            }
            newQueue[i] = queue[counter];
            counter++;
        }

        Unit temp = newQueue[0];

        for (int i=1; i<newQueue.length; i++) //Move entire queue by 1
            newQueue[i-1] = newQueue[i];
        newQueue[newQueue.length-1] = temp;

        return newQueue;
    }

    public static boolean spotTaken(int X, int Y, Unit[] queue){ //Checks if the spot a unit wants to move to is taken

        for (Unit unit : queue)
            if (unit.getTileX()==X && unit.getTileY()==Y)
                return true;

        return false;

    }

    public static boolean inTile(int x, int y, Tile tile){ //Checks which tile was clicked
        if (tile.getLeftX()<x && x<tile.getRightX() && tile.getTopY()<y && y<tile.getBottomY())
            return true;
        return false;
    }

    public static boolean inRange(int x, int y, Unit poke){ //Returns true if tile is within movement range
        if(Math.abs(poke.getTileX()-x)+Math.abs(poke.getTileY()-y)<=poke.getMovement())
            return true;
        return false;
    }

    public static boolean[] enemyInRange(Unit[] units){ //Returns true if enemy is in attack range
        boolean[] inRange = new boolean[units.length];
        for(int i=1; i<units.length; i++){
            if (Math.abs(units[0].getTileX()-units[i].getTileX())<=1 && Math.abs(units[0].getTileY()-units[i].getTileY())<=1)
                if (units[0].isTeam()!=units[i].isTeam())
                    inRange[i] = true;
        }
        return inRange;
    }

    public static int unitInSpot(Unit[] queue, int x, int y){
        for (int i = 0; i<queue.length; i++){
            if (queue[i].getTileX() == x && queue[i].getTileY() == y)
                return i;
        }
        return -1;
    }

    public static void takeDamage(Unit unit, int damage){
        System.out.println("Before attack: "+unit.getCurrentHealth());
        unit.setCurrentHealth(unit.getCurrentHealth()-damage);
        System.out.println("After attack: "+unit.getCurrentHealth());
    }

    public static void Attack(Unit attacker, Unit defender){
        takeDamage(defender, attacker.getAttack());
        if (defender.getCurrentHealth()<=0){
            System.out.println(defender.getUnitName() + " is dead");
        }
    }

    public static Cursor makeCursor(boolean canAttack, boolean canFly){ //Changes cursor according to situation.
        Cursor c = null;
        
        return c;
    }

}
