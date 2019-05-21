package pokemonHeroes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@SuppressWarnings("WeakerAccess")

public class Unit {

    private int maxHealth;
    private int currentHealth;
    private int attack;
    private int defense;
    private int movement;  //Amount of tiles a Pokemon can move during its turn
    private int initiative; //Affects the Pokemon's spot in Queue. Higher initiative means it'll be higher in the queue.
    private int CD;  //How long unit must wait between uses of special

    private int minDamage; //The minimum amount of damage a unit can deal
    private int maxDamage; //The maximum amount of damage a unit can deal

    private Move move; //Special ability each Pokemon has

    private boolean team; //Tells which team this unit belongs to
    private String specialPic;  //Some Pokemon images have special dimensions. This is here to tell me how to deal with those special images.
    private boolean ranged; //Tells if the Pokemon can attack from afar
    private boolean flying; //Tells if a Pokemon is able to fly and ignore hazards while moving

    private int shielded; //0-100 int value that tells how shielded the unit is. Only applied by magic.
    private boolean defended; //True if unit defended during turn, otherwise false.

    private int unitsInStack;  //Number of units currently in a stack. Might need to move somewhere else
    private String unitName;

    private int x; //Actual X, Y coords for place on board
    private int y;

    private int tileX; //X, Y of tile they're on
    private int tileY;

    private String direction;

    public int getMinDamage() {
        return minDamage;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getShielded() {
        return shielded;
    }

    public void setShielded(int shielded) {
        this.shielded = shielded;
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

    public void setCD(int CD) {
        this.CD = CD;
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

    public int getCD() {
        return CD;
    }

    public String getSpecialPic() {
        return specialPic;
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

    public boolean isRanged() {
        return ranged;
    }

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }

    public boolean isDefended() {
        return defended;
    }

    public void setDefended(boolean defended) {
        this.defended = defended;
    }



    public Unit(String unitName, int unitsInStack, boolean team){
        this.unitsInStack=unitsInStack;
        this.unitName=unitName;
        this.team=team;

        if (team) //Make sure the units on each team are facing the correct direction
            setDirection("Right");
        else
            setDirection("Left");
    }

    public static NodeList forRoster(){
        try {
            File inputFile = new File("Resources/unitInventory.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc.getElementsByTagName("Unit");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public static void fillStats(Unit[] queue){

        try {
            File inputFile = new File("Resources/unitInventory.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Unit");

            for (Unit unit : queue) {
                unit.tileX = -1;
                unit.tileY = -1;
                unit.x = -200;
                for (int j = 0; j < nList.getLength(); j++) {
                    Node nNode = nList.item(j);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (unit.getUnitName().equals(eElement.getElementsByTagName("unitName").item(0).getTextContent())){
                            System.out.println("\nCurrent Element :" + nNode.getNodeName());
                            System.out.println("Unit Name : " + eElement.getElementsByTagName("unitName").item(0).getTextContent());
                            System.out.println("Max HP : " + eElement.getElementsByTagName("MaxHP").item(0).getTextContent());
                            System.out.println("Attack : " + eElement.getElementsByTagName("Attack").item(0).getTextContent());
                            System.out.println("Defense : " + eElement.getElementsByTagName("Defense").item(0).getTextContent());
                            System.out.println("Min Damage : " + eElement.getElementsByTagName("MinDamage").item(0).getTextContent());
                            System.out.println("Max Damage : " + eElement.getElementsByTagName("MaxDamage").item(0).getTextContent());
                            System.out.println("Movement : " + eElement.getElementsByTagName("Movement").item(0).getTextContent());
                            System.out.println("CD : " + eElement.getElementsByTagName("CD").item(0).getTextContent());
                            System.out.println("SpecialPic : " + eElement.getElementsByTagName("SpecialPic").item(0).getTextContent());
                            System.out.println("Flying : " + eElement.getElementsByTagName("Flying").item(0).getTextContent());
                            System.out.println("Ranged : " + eElement.getElementsByTagName("Ranged").item(0).getTextContent());
                            System.out.println("Move : " + eElement.getElementsByTagName("Move").item(0).getTextContent());
                            unit.maxHealth = Integer.parseInt(eElement.getElementsByTagName("MaxHP").item(0).getTextContent());
                            unit.attack = Integer.parseInt(eElement.getElementsByTagName("Attack").item(0).getTextContent());
                            unit.defense = Integer.parseInt(eElement.getElementsByTagName("Defense").item(0).getTextContent());
                            unit.minDamage = Integer.parseInt(eElement.getElementsByTagName("MinDamage").item(0).getTextContent());
                            unit.maxDamage = Integer.parseInt(eElement.getElementsByTagName("MaxDamage").item(0).getTextContent());
                            unit.movement = Integer.parseInt(eElement.getElementsByTagName("Movement").item(0).getTextContent());
                            unit.CD = Integer.parseInt(eElement.getElementsByTagName("CD").item(0).getTextContent());
                            unit.specialPic = eElement.getElementsByTagName("SpecialPic").item(0).getTextContent();
                            unit.flying = Boolean.parseBoolean(eElement.getElementsByTagName("Flying").item(0).getTextContent());
                            unit.ranged = Boolean.parseBoolean(eElement.getElementsByTagName("Ranged").item(0).getTextContent());
                            unit.currentHealth = unit.maxHealth;
                            //queue[i].move = Integer.parseInt(eElement.getElementsByTagName("MaxHP").item(0).getTextContent());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

