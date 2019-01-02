package pokemonHeroes;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

@SuppressWarnings("WeakerAccess")

public class Unit {

    private int maxHealth;
    private int currentHealth;
    private int attack;
    private int defense;
    private int movement;  //Amount of tiles a Pokemon can move during its turn
    private int initiative; //Affects the Pokemon's spot in Queue. Higher initiative means it'll be higher in the queue.
    private int PP;  //Tells how many times the Pokemon can use their special attack

    private Move move; //Special ability each Pokemon has

    private boolean team; //Tells which team this unit belongs to
    private boolean large;  //True if a Pokemon is large (takes up four spaces) or false if small (takes up one space)
    private boolean ranged; //Tells if the Pokemon can attack from afar
    private boolean flying; //Tells if a Pokemon is able to fly and ignore hazards while moving

    private int shielded; //0-100 int value that tells how shielded the unit is. Only applied by magic.

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

    public void setPP(int PP) {
        this.PP = PP;
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

    public int getPP() {
        return PP;
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

    public boolean isRanged() {
        return ranged;
    }

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
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
            File inputFile = new File("src/pokemonHeroes/unitInventory.xml");
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

    public static void fillStats(Unit[] queue){

        System.out.println(queue[0].getUnitName());

        try {
            File inputFile = new File("src/pokemonHeroes/unitInventory.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Unit");

            for (Unit unit : queue) {
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
                            System.out.println("Movement : " + eElement.getElementsByTagName("Movement").item(0).getTextContent());
                            System.out.println("Initiative : " + eElement.getElementsByTagName("Initiative").item(0).getTextContent());
                            System.out.println("PP : " + eElement.getElementsByTagName("PP").item(0).getTextContent());
                            System.out.println("Large : " + eElement.getElementsByTagName("Large").item(0).getTextContent());
                            System.out.println("Flying : " + eElement.getElementsByTagName("Flying").item(0).getTextContent());
                            System.out.println("Ranged : " + eElement.getElementsByTagName("Ranged").item(0).getTextContent());
                            System.out.println("Move : " + eElement.getElementsByTagName("Move").item(0).getTextContent());
                            unit.maxHealth = Integer.parseInt(eElement.getElementsByTagName("MaxHP").item(0).getTextContent());
                            unit.attack = Integer.parseInt(eElement.getElementsByTagName("Attack").item(0).getTextContent());
                            unit.defense = Integer.parseInt(eElement.getElementsByTagName("Defense").item(0).getTextContent());
                            unit.movement = Integer.parseInt(eElement.getElementsByTagName("Movement").item(0).getTextContent());
                            unit.initiative = Integer.parseInt(eElement.getElementsByTagName("Initiative").item(0).getTextContent());
                            unit.PP = Integer.parseInt(eElement.getElementsByTagName("PP").item(0).getTextContent());
                            unit.large = Boolean.parseBoolean(eElement.getElementsByTagName("Large").item(0).getTextContent());
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

