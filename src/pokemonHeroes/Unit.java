package pokemonHeroes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@SuppressWarnings("WeakerAccess")
/**
This class represents a single Pokemon
 */
public class Unit {

    /**
     * Pokemon's stats for damage calculations
     */
    private int maxHealth;
    private int currentHealth;
    private int attack;
    private int defense;
    private int minDamage;
    private int maxDamage;
    /**
     * movement: Amount of tiles a Pokemon can move during its turn
     * ranged: Tells if a Pokemon can attack from afar
     * flying: Tells if a Pokemon is able to fly and ignore hazards while moving
     */
    private int movement;
    private boolean ranged;
    private boolean flying;
    /**
     * move: A Pokemon's special ability
     * CD: How long a unit must wait between uses of its special move
     */
    private Move move;
    private int CD;
    /**
     * team: Tells which team this Pokemon belongs to
     * specialPic: Some Pokemon images have special dimensions. This is here to tell me how to deal with those special images.
     */
    private boolean team;
    private String specialPic;
    /**
     * shielded: 0-100 int value that tells how shielded the unit is. Only applied by magic.
     * defended: True if unit defended during turn, otherwise false.
     */
    private int shielded;
    private boolean defended;
    /**
     * unitsInStack: Number of units currently in a stack. Might need to move somewhere else
     * unitName: Name of the Pokemon
     */
    private int unitsInStack;
    private String unitName;
    /**
     * Actual x, y coordinates for place on board
     */
    private int x;
    private int y;
    /**
     * Tile x, y coordinates of Pokemon
     */
    private int tileX;
    private int tileY;
    /**
     * Direction in which the Pokemon is facing
     */
    private String direction;

    /**
     * Unit constructor
     * @param unitName The Pokemon's name
     * @param unitsInStack The number of units in the stack
     * @param team The team on which the Pokemon is
     */
    public Unit(String unitName, int unitsInStack, boolean team){
        this.unitsInStack=unitsInStack;
        this.unitName=unitName;
        this.team=team;

        if (team) //Make sure the units on each team are facing the correct direction
            setDirection("Right");
        else
            setDirection("Left");
    }

    /**
     * Return a NodeList of all the Pokemon on the roster to display it during the choosing stage
     * @return
     */
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

    /**
     * Set the unit's stats from the XML sheet
     * @param queue
     */
    public static void fillStats(Unit[] queue){
        try {
            File inputFile = new File("Resources/unitInventory.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Unit");

            for (Unit unit : queue) {
                /**
                 * Set default positions
                 */
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

    /**
     * Returns the Pokemon's min damage per attack
     * @return
     */
    public int getMinDamage() {
        return minDamage;
    }

    /**
     * Returns the Pokemon's current direction
     * @return
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the Pokemon's current direction
     * @return
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Get the Pokemon's shielded level
     * @return
     */
    public int getShielded() {
        return shielded;
    }

    /**
     * Sets the Pokemon's shielded level
     * @return
     */
    public void setShielded(int shielded) {
        this.shielded = shielded;
    }

    /**
     * Returns the Pokemon's tile X in the array
     * @return
     */
    public int getTileX() {
        return tileX;
    }

    /**
     * Sets the Pokemon's tile X in the array
     * @param tileX
     */
    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    /**
     * Returns the Pokemon's tile Y in the array
     * @return
     */
    public int getTileY() {
        return tileY;
    }

    /**
     * Sets the Pokemon's tile Y in the array
     * @return
     */
    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    /**
     * Returns the Pokemon's X position on the screen
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the Pokemon's X position on the screen
     * @return
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the Pokemon's Y position on the screen
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the Pokemon's Y position on the screen
     * @return
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the Pokemon's max health per stack
     * @return
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Get the Pokemon's current health
     * @return
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the Pokemon's current health for the top unit in the stack
     * @return
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Get the Pokemon's attack power
     * @return
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Get the Pokemon's defensive ability
     * @return
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Returns the amount of tiles a Pokemon can move per turn
     * @return
     */
    public int getMovement() {
        return movement;
    }

    /**
     * Return the amount of turns in between special attack uses
     * @return
     */
    public int getCD() {
        return CD;
    }

    /**
     * Gets the Pokemon's special pic if it has one
     * @return
     */
    public String getSpecialPic() {
        return specialPic;
    }

    /**
     * Returns the Pokemon's team
     * @return
     */
    public boolean isTeam() { return team; }

    /**
     * Return the amount of units in the Pokemon's stack
     * @return
     */
    public int getUnitsInStack() {
        return unitsInStack;
    }

    /**
     * Set the amount of Pokemon in the stack
     * @param unitsInStack
     */
    public void setUnitsInStack(int unitsInStack) {
        this.unitsInStack = unitsInStack;
    }

    /**
     * Returns the Pokemon's name
     * @return
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Get whether the Pokemon can fly or not
     * @return
     */
    public boolean isFlying() {
        return flying;
    }

    /**
     * Get whether the Pokemon has ranged attacks or not
     * @return
     */
    public boolean isRanged() {
        return ranged;
    }

    /**
     * Get if the Pokemon is defended
     * @return
     */
    public boolean isDefended() {
        return defended;
    }

    /**
     * Set if the Pokemon is defended
     * @return
     */
    public void setDefended(boolean defended) {
        this.defended = defended;
    }

    /**
     * Get the Pokemon's max damage per attack
     * @return
     */
    public int getMaxDamage() {
        return maxDamage;
    }

}

