package pokemonHeroes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;

/**
 * This class represents the player's trainer
 */
public class Trainer {

    /**
     * Trainer's name to get the correct image
     * Team differentiates between the players
     * Trainer's level to calculate damage
     * AttackedThisTurn as boolean since you can only use a trainer attack once per round
     */
    private String name;
    private boolean team;
    private int level;
    private boolean attackedThisTurn;

    /**
     * Friend Pokemon's name to get the correct image
     */
    private String friendPoke;
    /**
     * Integers and booleans for damage calculations
     */
    private int archeryLevel;
    private int meleeLevel;
    private boolean archerySpecialty;
    private boolean meleeSpecialty;
    private int armorLevel;
    private boolean armorSpecialty;

    /**
     * Array containing the trainer's units.
     */
    private Unit[] units = new Unit[7];
    /**
     * ImageIcons and Images to later display them
     */
    private ImageIcon friendIcon;
    private ImageIcon trainerIcon;
    private Image trainerImage;
    private Image friendImage;

    /**
     * Constructor for trainer
     * @param numInRoster The trainer's spot in the trainer roster
     * @param team The trainer's team
     * @param level The trainer's level
     */
    protected Trainer(int numInRoster, boolean team, int level){
        this.team = team;
        this.level = level;

        /**
         * Sets the trainer's information
         */
        NodeList roster = forRoster();
        Node node = roster.item(numInRoster);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element element = (Element) node;
            this.name =  element.getElementsByTagName("Name").item(0).getTextContent();
            this.friendPoke = element.getElementsByTagName("friendPoke").item(0).getTextContent();
            this.meleeLevel = Integer.parseInt(element.getElementsByTagName("meleeLevel").item(0).getTextContent());
            this.meleeSpecialty = Boolean.parseBoolean(element.getElementsByTagName("meleeSpecialty").item(0).getTextContent());
            this.archeryLevel = Integer.parseInt(element.getElementsByTagName("archeryLevel").item(0).getTextContent());
            this.archerySpecialty = Boolean.parseBoolean(element.getElementsByTagName("archerySpecialty").item(0).getTextContent());
            this.armorLevel = Integer.parseInt(element.getElementsByTagName("armorLevel").item(0).getTextContent());
            this.armorSpecialty = Boolean.parseBoolean(element.getElementsByTagName("armorSpecialty").item(0).getTextContent());
        }

        /**
         * Set images for later painting
         */
        this.trainerIcon = new ImageIcon("Images/TrainerPics/"+name+".png");
        this.trainerImage = trainerIcon.getImage();
        this.friendIcon = new ImageIcon("Images/TrainerPics/FriendPoke/"+friendPoke+".png");
        this.friendImage = friendIcon.getImage();
    }

    /**
     * Returns a NodeList of all the trainers from the XML to display them during the selection stage
     * @return
     */
    public static NodeList forRoster(){
        try {
            File inputFile = new File("Resources/trainerInventory.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc.getElementsByTagName("Trainer");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the trainer's level
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the trainer's name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the trainer's armor level for damage calculations
     * @return
     */
    public int getArmorLevel() {
        return armorLevel;
    }

    /**
     * Returns the trainer's armor specialty for damage calculations
     * @return
     */
    public boolean isArmorSpecialty() {
        return armorSpecialty;
    }

    /**
     * Returns the trainer's melee level for damage calculations
     * @return
     */
    public int getMeleeLevel() {
        return meleeLevel;
    }

    /**
     * Returns the trainer's archery specialty for damage calculations
     * @return
     */
    public boolean isArcherySpecialty() {
        return archerySpecialty;
    }

    /**
     * Returns the trainer's melee specialty for damage calculations
     * @return
     */
    public boolean isMeleeSpecialty() {
        return meleeSpecialty;
    }

    /**
     * Returns whether or not the trainer has attacked this turn
     * @return
     */
    public boolean isAttackedThisTurn() {
        return attackedThisTurn;
    }

    /**
     * Sets the trainer's attackedThisTurn boolean
     * @return
     */
    public void setAttackedThisTurn(boolean attackedThisTurn) {
        this.attackedThisTurn = attackedThisTurn;
    }

    /**
     * Adds a unit to the trainer's group in the next empty spot
     * @param unit
     */
    public void addUnit(Unit unit){
        for(int i=0; i<units.length; i++){
            if(units[i] == null){
                units[i] = unit;
                break;
            }
        }
    }

    /**
     * Returns the trainers unit array
     * @return
     */
    public Unit[] getUnits(){return units;}

    /**
     * Returns the trainer's image
     * @return
     */
    protected Image getTrainerImage(){return trainerImage;}
    /**
     * Returns the trainer's archery level for damage calculations
     * @return
     */
    public int getArcheryLevel() { return archeryLevel; }
    /**
     * Returns the trainer's team
     * @return
     */
    public boolean getTeam(){return team;}

    /**
     * Returns the trainer's friend Pokemon image
     * @return
     */
    protected Image getFriendImage(){ return friendImage; }
}
