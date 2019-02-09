package pokemonHeroes;

import javax.swing.*;
import java.awt.*;

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

    public int getArcheryLevel() {
        return archeryLevel;
    }

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

    private Unit[] units = new Unit[7]; //Array containing the trainers units. Order doesn't actually matter.

    protected Trainer(String name, boolean team){ //Creates icon and image depending on the name
        this.name = name;
        this.team = team;
        this.trainerIcon = new ImageIcon("Images/TrainerPics/"+name+".png");
        this.trainerImage = trainerIcon.getImage();
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

    public boolean getTeam(){
        return team;
    }

}
