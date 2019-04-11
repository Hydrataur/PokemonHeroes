package pokemonHeroes;

import javafx.util.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class SceneFunctions {

    @SuppressWarnings("WeakerAccess")

    protected static Unit[] createQueue(Trainer trainer1, Trainer trainer2) { //Creates the queue according to unit movement
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
                    if (queue[j - 1].getMovement() < queue[j].getMovement()) {
                        temp = queue[j - 1];
                        queue[j - 1] = queue[j];
                        queue[j] = temp;
                    }

            }
        }

        return queue;
    }

    public static Unit[] updateQueue(Unit[] queue, Trainer t1, Trainer t2){

        if (queue[0].getMovement() < queue[1].getMovement()){
            t1.setAttackedThisTurn(false);
            t2.setAttackedThisTurn(false);
        }

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

        if (temp.isTeam())
            temp.setDirection("Right");
        else
            temp.setDirection("Left");

        for (int i=1; i<newQueue.length; i++) //Move entire queue by 1
            newQueue[i-1] = newQueue[i];
        newQueue[newQueue.length-1] = temp;

        newQueue[0].setDefended(false);

        return newQueue;
    }

    public static boolean spotTaken(int X, int Y, Unit[] queue){ //Checks if the spot a unit wants to move to is taken

        for (Unit unit : queue) {
            if (unit.getTileX() == X && unit.getTileY() == Y)
                return true;
        }

        return false;
    }

    public static Tile chosenTile(int x, int y, Tile[][] tilesDouble){
        try {
            for (Tile[] tiles : tilesDouble)
                for (Tile tile : tiles)
                    if (tile.hasBeenClicked(x, y))
                        return tile;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean inRange(int x, int y, Unit poke){ //Returns true if tile is within movement range
        return Math.abs(poke.getTileX() - x) + Math.abs(poke.getTileY() - y) <= poke.getMovement();
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

        dmgB = ThreadLocalRandom.current().nextInt(attacker.getMinDamage(), attacker.getMaxDamage()+1) * attacker.getUnitsInStack();

        double defense = defender.getDefense();
        if (defender.isDefended())
            defense *= 1.2;

        if (attacker.getAttack()>defense) { //Attack bonus if attack>defense, Defense bonus if defense>attack
            I1 = 0.05 * (attacker.getAttack() - defense);
            R1 = 0;
        }
        else{
            I1 = 0;
            R1 = 0.025*(defense - attacker.getAttack());
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

        R4 = defender.getShielded(); //If defender has a magic shield then reduces damage

        System.out.println("Base Damage=" + dmgB +" I1=" + I1 +" I2=" + I2 +" I3=" + I3 +" I4=" + I4 + " R1=" + R1 + " R2=" + R2 + " R3=" + R3 + " R4=" + R4);

        double damage = dmgB*(1+I1+I2+I3+I4)*(1-R1)*(1-R2-R3)*(1-R4); //Damage to be dealt

        dealDamage(defender, damage);
    }

    private static void dealDamage(Unit unit, double dmg){
        int totalHealth = unit.getCurrentHealth() + unit.getMaxHealth()*(unit.getUnitsInStack()-1); //Total health a unit stack has

        System.out.println("Damage " + dmg);
        System.out.println("Total Health " + totalHealth);

        if (dmg == 0) { //Minimal damage is one in case the attack is weak or the enemy has a lot of defense
            dmg = 1;
            System.out.println("Damage set to 1");
        }

        totalHealth -= dmg; //Remove damage from totalHealth

        if (totalHealth <= 0){ //If totalHealth is a negative or 0 then the unit is dead.
            System.out.println(unit.getUnitName() + " is dead");
            unit.setUnitsInStack(0);
            unit.setCurrentHealth(0);
            return;
        }

        unit.setUnitsInStack(totalHealth/unit.getMaxHealth()+1); //Units left in stack
        unit.setCurrentHealth(totalHealth%unit.getMaxHealth()); //Current health of top unit

        if (unit.getCurrentHealth() == 0){ //Handle if the defender takes exactly the amount of damage left in the top unit
            unit.setUnitsInStack(unit.getUnitsInStack()-1);
            unit.setCurrentHealth(unit.getMaxHealth());
        }

        System.out.println(unit.getUnitName() + " has " + unit.getUnitsInStack() + " units");
        System.out.println(unit.getUnitName() + " has " + unit.getCurrentHealth() + " current health");

    }

    public static Unit[] defendButtonPressed(Rectangle dTile, int x, int y, Unit[] queue, Trainer t1, Trainer t2){
        if (dTile.hasBeenClicked(x, y)){
            queue[0].setDefended(true);
            queue = SceneFunctions.updateQueue(queue, t1, t2);
        }
        return queue;
    }

    public static void trainerAttack(Trainer attackingTrainer, Trainer defendingTrainer, Unit unit){
        int unitRank = 0;
        for (int i = 0; i < defendingTrainer.getUnits().length; i++) {
            if (defendingTrainer.getUnits()[i].getUnitName().equals(unit.getUnitName())) {
                unitRank = i + 1;
                break;
            }
        }

        double damage = 0;
        switch (unitRank){
            case 1:
                damage = 2.0 + (attackingTrainer.getLevel() - 1) * 0.333;
                break;
            case 2:
                damage = 1.0 + (attackingTrainer.getLevel() - 1) * 0.267;
                break;
            case 3:
                damage = 0.8 + (attackingTrainer.getLevel() - 1) * 0.190;
                break;
            case 4:
                damage = 0.5 + (attackingTrainer.getLevel() - 1) * 0.133;
                break;
            case 5:
                damage = 0.3 + (attackingTrainer.getLevel() - 1) * 0.09;
                break;
            case 6:
                damage = 0.2 + (attackingTrainer.getLevel() - 1) / 0.06;
                break;
            case 7:
                damage = 0.1 + (attackingTrainer.getLevel() - 1) / 0.047;
                break;
        }

        System.out.println("_________Trainer Attack________");
        System.out.println("Unit: " + unit.getUnitName());
        System.out.println("unitRank: " + unitRank);
        System.out.println("damage: " + damage);
        System.out.println("Trainer level: " + attackingTrainer.getLevel());
        System.out.println("_________________");

        dealDamage(unit, damage);
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

        switch (i) {
            case 0 :
                clr = new Color(122, 199, 76);
                icon = new ImageIcon(fileLoc + "Grass.png");
                break;

            case 1 :
                clr = new Color(115, 87, 151);
                icon = new ImageIcon(fileLoc + "Ghost.png");
                break;

            case 2 :
                clr = new Color(169, 143, 243);
                icon = new ImageIcon(fileLoc + "Flying.png");
                break;

            case 3 :
                clr = new Color(238, 129, 48);
                icon = new ImageIcon(fileLoc + "Fire.png");
                break;

            case 4 :
                clr = new Color(194, 46, 40);
                icon = new ImageIcon(fileLoc + "Fighting.png");
                break;

            case 5 :
                clr = new Color(247, 208, 44);
                icon = new ImageIcon(fileLoc + "Electric.png");
                break;

            case 6 :
                clr = new Color(111, 53, 252);
                icon = new ImageIcon(fileLoc + "Dragon.png");
                break;

            case 7 :
                clr = new Color(112, 87, 70);
                icon = new ImageIcon(fileLoc + "Dark.png");
                break;

            case 8 :
                clr = new Color(166, 185, 26);
                icon = new ImageIcon(fileLoc + "Bug.png");
                break;

            case 9 :
                clr = new Color(99, 144, 240);
                icon = new ImageIcon(fileLoc + "Water.png");
                break;

            case 10 :
                clr = new Color(183, 183, 206);
                icon = new ImageIcon(fileLoc + "Steel.png");
                break;

            case 11 :
                clr = new Color(182, 161, 54);
                icon = new ImageIcon(fileLoc + "Rock.png");
                break;

            case 12 :
                clr = new Color(249, 85, 135);
                icon = new ImageIcon(fileLoc + "Psychic.png");
                break;

            case 13 :
                clr = new Color(163, 62, 161);
                icon = new ImageIcon(fileLoc + "Poison.png");
                break;

            case 14 :
                clr = new Color(168, 167, 122);
                icon = new ImageIcon(fileLoc + "Normal.png");
                break;

            case 15 :
                clr = new Color(150, 217, 214);
                icon = new ImageIcon(fileLoc + "Ice.png");
                break;
            default :
                clr = new Color(226, 191, 101);
                icon = new ImageIcon(fileLoc + "Ground.png");
                break;
        }
        return new Pair<>(clr, icon.getImage());
    }

    public static Cursor makeCursor(int x, int y, Tile tile, Unit[] queue, boolean[] inAttackRange, Rectangle defendButton, boolean trainerAttack){ //Changes cursor according to situation.
        Cursor c;
        Image img;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        String status = "Default";

        if(defendButton.hasBeenClicked(x, y))
            status = "Shield";
        if (trainerAttack)
            status = "Attack";

        boolean unitInPlace;

        if (tile != null && !trainerAttack){
            if (spotTaken(tile.getTileX(), tile.getTileY(), queue)){
                int spot = unitInSpot(queue, tile.getTileX(), tile.getTileY());
                if (inAttackRange[spot] && queue[0].isTeam() != queue[spot].isTeam()) {
                    if (queue[0].isRanged())
                        status = "Shoot";
                    else
                        status = "Attack";
                }
                else
                    status = "Cant";
            }
            else{
                if (inRange(tile.getTileX(), tile.getTileY(), queue[0])){
                    if (queue[0].isFlying())
                        status = "Fly";
                    else
                        status = "Move";
                }
            }
        }

        img = toolkit.getImage("Images/Cursors/" + status + ".png");
        c = toolkit.createCustomCursor(img, new Point(0, 0), "img");
        return c;
    }

}
