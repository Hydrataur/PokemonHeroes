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

public class Trainer {

    private String name; //Trainer name used to get correct image
    private boolean team; //Tells which team the trainer is, right or left
    private int level;
    private boolean attackedThisTurn; //Trainer can only attack once per round

    private String friendPoke;

    private int archeryLevel;
    private int meleeLevel;
    private boolean archerySpecialty;
    private boolean meleeSpecialty;
    private int armorLevel;
    private boolean armorSpecialty;

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getArmorLevel() {
        return armorLevel;
    }

    public boolean isArmorSpecialty() {
        return armorSpecialty;
    }

    private ImageIcon friendIcon;

    public int getMeleeLevel() {
        return meleeLevel;
    }

    public boolean isArcherySpecialty() {
        return archerySpecialty;
    }

    public boolean isMeleeSpecialty() {
        return meleeSpecialty;
    }

    public String getFriendPoke() { return friendPoke; }

    public boolean isAttackedThisTurn() {
        return attackedThisTurn;
    }

    public void setAttackedThisTurn(boolean attackedThisTurn) {
        this.attackedThisTurn = attackedThisTurn;
    }

    private ImageIcon trainerIcon;
    private Image trainerImage;
    private Image friendImage;
    protected Trainer(int numInRoster, boolean team, int level){
        this.team = team;
        this.level = level;

        NodeList roster = forRoster();
        System.out.println(roster.getLength() + " " + numInRoster);
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

        this.trainerIcon = new ImageIcon("Images/TrainerPics/"+name+".png");
        this.trainerImage = trainerIcon.getImage();

        this.friendIcon = new ImageIcon("Images/TrainerPics/FriendPoke/"+friendPoke+".png");
        this.friendImage = friendIcon.getImage();
    }

    private Unit[] units = new Unit[7]; //Array containing the trainers units. Order doesn't actually matter.

    protected Trainer(String name, boolean team){ //Creates icon and image depending on the name
        this.name = name;
        this.team = team;
        this.trainerIcon = new ImageIcon("Images/TrainerPics/"+name+".png");
        this.trainerImage = trainerIcon.getImage();
    }

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

    public void addUnit(Unit unit){ //Adds a unit to the trainer's group in the next empty spot. Order doesn't actually matter.
        for(int i=0; i<units.length; i++){
            if(units[i] == null){
                units[i] = unit;
                break;
            }
        }
    }

    public Unit[] getUnits(){return units;}

    protected Image getTrainerImage(){
        return trainerImage;
    }

    public int getArcheryLevel() { return archeryLevel; }

    public boolean getTeam(){
        return team;
    }

    protected Image getFriendImage(){ return friendImage; }
}
