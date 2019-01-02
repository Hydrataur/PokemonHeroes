package pokemonHeroes;

import javafx.util.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
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

        int numAlive = 0; //To tell how long the new queue needs to be

        for (Unit unit : queue)
            if (unit.getCurrentHealth()>0)
                numAlive++;

        Unit[] newQueue = new Unit[numAlive]; //New queue to contain only those who are still alive
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

    public static void Attack(Unit attacker, Unit defender, Trainer attackingTrainer, Trainer defendingTrainer){

        System.out.println(attacker.getUnitName() + " has " + attacker.getUnitsInStack() + " units");

        System.out.println(defender.getUnitName() + " has " + defender.getUnitsInStack() + " units");
        System.out.println(defender.getUnitName() + " has " + defender.getCurrentHealth() + " current health");

        System.out.println("Attacking trainer: "+attackingTrainer.getName() + " Defending trainer: " + defendingTrainer.getName());

        double dmgB, I1, I2, I3, I4, R1, R2, R3, R4; //Variables that influence the final damage. I for increase, R for reduce

        dmgB = attacker.getUnitsInStack() * attacker.getAttack();

        if (attacker.getAttack()>defender.getDefense()) { //Attack bonus if attack>defense, Defense bonus if defense>attack
            I1 = 0.05 * (attacker.getAttack() - defender.getDefense());
            R1 = 0;
        }
        else{
            I1 = 0;
            R1 = 0.025*(defender.getDefense() - attacker.getAttack());
        }

        if (attacker.isRanged()){ //Bonus damage according to trainer proficiencies. Ranged/Melee bonuses
            if (attackingTrainer.getArcheryLevel() == 0)
                I2 = 0;
            else
                if (attackingTrainer.getArcheryLevel() == 1)
                    I2 = 0.1;
                else
                    if (attackingTrainer.getArcheryLevel() == 2)
                        I2 = 0.25;
                    else
                        I2 = 0.5;

            if (attackingTrainer.isArcherySpecialty())
                I3 = 0.05 * attackingTrainer.getLevel() * I2;
            else
                I3 = 0;

        }
        else{
            if (attackingTrainer.getMeleeLevel() == 0)
                I2 = 0;
            else
                if (attackingTrainer.getMeleeLevel() == 1)
                    I2 = 0.1;
                else
                    if (attackingTrainer.getMeleeLevel() == 2)
                        I2 = 0.25;
                    else
                        I2 = 0.5;

            if (attackingTrainer.isMeleeSpecialty())
                I3 = 0.05 * attackingTrainer.getLevel() * I2;
            else
                I3 = 0;
        }


        if (Math.random()*100+1 > 95) //5% chance for a lucky strike which increases damage by a lot
            I4 = 1;
        else
            I4 = 0;

        if (defendingTrainer.getArmorLevel() == 0) //Increase defenses according to trainer defense proficiency, similar to the attack
            R2 = 0;
        else
            if (defendingTrainer.getArmorLevel() == 1)
                R2 = 0.05;
            else
                if (defendingTrainer.getArmorLevel() == 2)
                    R2 = 0.1;
                else
                    R2 = 0.15;

        if (defendingTrainer.isArmorSpecialty())
            R3 = 0.05 * R2 * defendingTrainer.getLevel();
        else
            R3 = 0;

        R4 = defender.getShielded()/100; //If defender has a magic shield then reduces damage

        System.out.println("Base Damage=" + dmgB +" I1=" + I1 +" I2=" + I2 +" I3=" + I3 +" I4=" + I4 + " R1=" + R1 + " R2=" + R2 + " R3=" + R3 + " R4=" + R4);

        double damage = dmgB*(1+I1+I2+I3+I4)*(1-R1)*(1-R2-R3)*(1-R4); //Damage to be dealt
        int totalHealth = defender.getCurrentHealth() + defender.getMaxHealth()*(defender.getUnitsInStack()-1); //Total health a unit stack has

        System.out.println("Damage " + damage);
        System.out.println("Total Health " + totalHealth);

        if (damage == 0) { //Minimal damage is one in case the attack is weak or the enemy has a lot of defense
            damage = 1;
            System.out.println("Damage set to 1");
        }

        totalHealth -= damage; //Remove damage from totalHealth

        if (totalHealth <= 0){ //If totalHealth is a negative or 0 then the unit is dead.
            System.out.println(defender.getUnitName() + " is dead");
            defender.setUnitsInStack(0);
            defender.setCurrentHealth(0);
            return;
        }

        defender.setUnitsInStack(totalHealth/defender.getMaxHealth()+1); //Units left in stack
        defender.setCurrentHealth(totalHealth%defender.getMaxHealth()); //Current health of top unit

        if (defender.getCurrentHealth() == 0){ //Handle if the defender takes exactly the amount of damage left in the top unit
            defender.setUnitsInStack(defender.getUnitsInStack()-1);
            defender.setCurrentHealth(defender.getMaxHealth());
        }

        System.out.println(defender.getUnitName() + " has " + defender.getUnitsInStack() + " units");
        System.out.println(defender.getUnitName() + " has " + defender.getCurrentHealth() + " current health");

    }

    public static void setTeam(Trainer trainer, NodeList roster, int x, int BOARDWIDTH, boolean teamB){
        int column = x/(BOARDWIDTH/17);
        Node node;
        Element element;
        for (int i = 0; i<7; i++){
            node = roster.item(column*7+i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) node;
                trainer.addUnit(new Unit(element.getElementsByTagName("unitName").item(0).getTextContent(), 10, teamB));
            }
        }

    }

    public static Pair<Color, Image> rosterColorNeeded(int i){

        Pair<Color, Image> values;

        Color clr;
        String fileLoc = "Images/TypeSymbols/";
        ImageIcon icon;

        if (i==0) {
            clr = new Color(122, 199, 76);
            icon = new ImageIcon(fileLoc + "Grass.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==1) {
            clr =  new Color(115, 87, 151);
            icon = new ImageIcon(fileLoc + "Ghost.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==2) {
            clr =  new Color(169, 143, 243);
            icon = new ImageIcon(fileLoc + "Flying.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==3) {
            clr =  new Color(238, 129, 48);
            icon = new ImageIcon(fileLoc + "Fire.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==4){
            clr =  new Color(194, 46, 40);
            icon = new ImageIcon(fileLoc + "Fighting.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==5) {
            clr =  new Color(247, 208, 44);
            icon = new ImageIcon(fileLoc + "Electric.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==6) {
            clr =  new Color(111, 53, 252);
            icon = new ImageIcon(fileLoc + "Dragon.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==7){
            clr =  new Color(112, 87, 70);
            icon = new ImageIcon(fileLoc + "Dark.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==8) {
            clr =  new Color(166, 185, 26);
            icon = new ImageIcon(fileLoc + "Bug.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==9) {
            clr =  new Color(99, 144, 240);
            icon = new ImageIcon(fileLoc + "Water.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==10) {
            clr =  new Color(183, 183, 206);
            icon = new ImageIcon(fileLoc + "Steel.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==11){
            clr =  new Color(182, 161, 54);
            icon = new ImageIcon(fileLoc + "Rock.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==12) {
            clr =  new Color(249, 85, 135);
            icon = new ImageIcon(fileLoc + "Psychic.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==13) {
            clr =  new Color(163, 62, 161);
            icon = new ImageIcon(fileLoc + "Poison.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==14) {
            clr =  new Color(168, 167, 122);
            icon = new ImageIcon(fileLoc + "Normal.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }
        if (i==15) {
            clr =  new Color(150, 217, 214);
            icon = new ImageIcon(fileLoc + "Ice.png");
            return new Pair<Color, Image>(clr, icon.getImage());
        }

        clr =  new Color(226, 191, 101);
        icon = new ImageIcon(fileLoc + "Ground.png");
        return new Pair<Color, Image>(clr, icon.getImage());
    }

    public static Cursor makeCursor(boolean canAttack, boolean canFly){ //Changes cursor according to situation.
        Cursor c = null;
        
        return c;
    }

}
