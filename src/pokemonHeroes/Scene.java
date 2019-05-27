package pokemonHeroes;

import javafx.util.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This class is the scene on which everything is painted and where a lot of info is stored
 */
public class Scene extends JPanel implements MouseListener, ActionListener, MouseMotionListener, KeyListener {
    /**
     * BOARDWIDTH: Width of screen in pixels
     * BOARDHEIGHT: Height of screen in pixels
     */
    private final static int BOARDWIDTH = 1533;
    private final static int BOARDHEIGHT = 845;
    /**
     * The client which we'll connect to the server with
     */
    private Client client;
    /**
     * paused: To tell if the pause menu is open
     * myTurn: To see if it's player's turn or the opponents turn
     * id: Player id to differentiate between them
     */
    private boolean paused;
    private boolean myTurn;
    private int id;
    /**
     * tiles: Number of tiles on board
     * tileLength: Length of each tile
     * queueTileLength: Length of each tile in the queue
     */
    private int tiles = 100;
    private int tileLength = 64;
    private int queueTileLength = 100;
    /**
     * trainerAttackReady: Tells if trainer attack has been readied
     */
    private boolean trainerAttackReady;
    /**
     * Various images we need, first icons to later create images that can be displayed
     */
    private ImageIcon trainerSelectBGIcon = new ImageIcon("Images/TrainerSelectBG.jpg");
    private Image trainerSelectBGImage = trainerSelectBGIcon.getImage();
    private ImageIcon bgIcon = new ImageIcon("Images/Maps/CmBkBch.png");
    private Image bgImage = bgIcon.getImage();
    private ImageIcon shieldIcon = new ImageIcon("Images/ScreenIcons/Defense.png");
    private Image shieldImage = shieldIcon.getImage();
    private ImageIcon trainerAttackIcon = new ImageIcon("Images/ScreenIcons/TrainerAttack.png");
    private Image trainerAttackImage = trainerAttackIcon.getImage();
    private ImageIcon trainerAttackReadyIcon = new ImageIcon("Images/ScreenIcons/TrainerAttackChosen.png");
    private Image trainerAttackReadyImage = trainerAttackReadyIcon.getImage();
    /**
     * Turn queue
     */
    private Unit[] queue;
    /**
     * Array of tiles
     */
    private Tile[][] tilesArr = new Tile[(int)Math.sqrt(tiles)][(int)Math.sqrt(tiles)];
    /**
     * The two player's trainers
     */
    private Trainer trainerOne, trainerTwo;
    /**
     * Time in between ticks. Lower speed-->Things happen faster. Counted in milliseconds.
     */
    private int speed = 50;
    /**
     * Pokemon related graphics
     */
    private ImageIcon pokeIcon;
    private Image pokeImage;
    private BufferedImage pokeQueueImage;
    private Graphics pokeGraphics;
    private BufferedImage pokeFieldImage;
    private ImageIcon targetIcon = new ImageIcon("Images/targetTemp.png");
    private Image target = targetIcon.getImage();
    /**
     * Checks if a Pokemon is currently moving (if yes, then we must wait until it's done
     */
    private boolean inTurn;
    /**
     * canAttack: Checks if there are enemies in range of the pokemon
     * enemiesInRange: Order of this array is like queue, but instead of Units it is booleans of if in range
     */
    private boolean canAttack;
    private boolean[] enemiesInRange;
    /**
     * roster: The whole roster to be displayed during team choosing stage
     * rosterIcon/rosterImage: Used for roster drawing
     * imageLocation: String location of image to make it look cleaner
     * teamsChosen/trainersChosen/unitsPlaced: Used to tell which stage of the game we're in
     * teamOneChosen/trainerOneChosen: Tells which team gets the team/trainer when one is chosen
     */
    private NodeList roster;
    private ImageIcon rosterIcon;
    private Image rosterImage;
    private String imageLocation;
    private boolean teamsChosen, trainersChosen;
    private boolean teamOneChosen, trainerOneChosen;
    private boolean unitsPlaced;
    /**
     * Makes sure Pokemon doesn't move twice in one turn
     */
    private boolean hasMoved = false;
    /**
     * UI elements for special actions
     */
    private Rectangle defenseTile, trainerAttackTile;
    /**
     * Used to know which part of the animation should be played
     */
    private boolean moveOne = false;
    /**
     * The tile that has been chosen
     */
    private Tile chosenTile;

    /**
     * Scene constructor
     */
    public Scene(){

        /**
        Get focus for KeyListener to work
         */
        this.setFocusable(true);
        this.requestFocusInWindow();

        /**
         * Sound related code. Starts music when opening the app
         */
        try {
            File soundFile = new File("CynthiaBattleMusic.wav");
            AudioInputStream as = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(as);
            clip.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        this.addMouseListener(this); //Adds ability to change things with mouse
        this.addMouseMotionListener(this); //For dynamic cursor
        this.addKeyListener(this); //For pause menu and shortcuts

        /**
         * Timer according to which an action will be taken during every tick
         */
        Timer timer = new Timer(speed, this);
        timer.start();

        /**
         * Set locations for special action tiles
         */
        defenseTile = new Rectangle(BOARDWIDTH-210, 10, 200, 50);
        trainerAttackTile = new Rectangle(BOARDWIDTH - 210, 70, 200, 50);
        /**
         * Starts false by default since nobody has started moving
         */
        inTurn=false;

        /**
         * Setup booleans. Become true once their task is complete
         */
        teamsChosen = false;
        teamOneChosen = false;
        unitsPlaced = false;

        System.out.println("About to start client");
        /**
         * Creates the client so that we can connect to the server
         */
        client = new Client(this);
    }

    /**
     * Draw the image of a Pokemon on the battlefield from a spritesheet
     * @param numInQueue Current Pokemon's position in the queue
     * @param g Used to draw
     */
    private void drawPokeFieldImage(int numInQueue, Graphics g){
        pokeIcon = new ImageIcon("Images/PokePics/Combat/" + queue[numInQueue].getUnitName() + ".png");
        pokeImage = pokeIcon.getImage();
        pokeFieldImage = new BufferedImage(pokeImage.getWidth(null) / 2, pokeImage.getHeight(null) / 4, BufferedImage.TYPE_INT_ARGB);
        pokeGraphics = pokeFieldImage.getGraphics();
        Unit unit = queue[numInQueue]; //The Pokemon we're drawing

        /**
         * To get the correct part of the spritesheet
         */
        int xPos = 0;
        int yPos = 0;

        /**
         * Some units have unique or special spritesheet sizes, so we must treat them accordingly
         */
        switch (unit.getSpecialPic()) {
            case "G4":
                switch (unit.getDirection()) {
                    case "Up":
                        xPos = 0;
                        break;
                    case "Down":
                        xPos = -65;
                        break;
                    case "Left":
                        xPos = -130;
                        break;
                    default:
                        xPos = -195;
                        break;
                }
                if (moveOne && numInQueue == 0)
                    yPos = 0;
                else
                    yPos = -64;
                pokeFieldImage = new BufferedImage(pokeImage.getWidth(null)/4, pokeImage.getHeight(null)/2, BufferedImage.TYPE_INT_ARGB);
                pokeGraphics = pokeFieldImage.getGraphics();
                pokeGraphics.drawImage(pokeImage, xPos, yPos, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
                pokeGraphics.dispose();
                g.drawImage(pokeFieldImage, unit.getX()-tileLength/3, unit.getY()-tileLength/2, (int)(pokeFieldImage.getWidth()*1.5), (int)(pokeFieldImage.getHeight()*1.5), this);
                break;

            case "Kyo":
                switch (unit.getDirection()) {
                    case "Up":
                        yPos = -36;
                        break;
                    case "Down":
                        yPos = 0;
                        break;
                    case "Left":
                        yPos = -68;
                        break;
                    default:
                        yPos = -100;
                        break;
                }
                if (moveOne && numInQueue == 0)
                    xPos = 0;
                else
                    xPos = -50;
                pokeFieldImage = new BufferedImage(pokeImage.getWidth(null)/2, pokeImage.getHeight(null)/4, BufferedImage.TYPE_INT_ARGB);
                pokeGraphics = pokeFieldImage.getGraphics();
                pokeGraphics.drawImage(pokeImage, xPos, yPos, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
                pokeGraphics.dispose();
                g.drawImage(pokeFieldImage, unit.getX(), unit.getY()+5, (int)(pokeFieldImage.getWidth()*1.5), (int)(pokeFieldImage.getHeight()*1.5), this);
                break;

            case "Wail":
                boolean upDown = false;
                switch (unit.getDirection()) {
                    case "Up":
                        yPos = -38;
                        upDown = true;
                        break;
                    case "Down":
                        yPos = 0;
                        upDown = true;
                        break;
                    case "Left":
                        yPos = -76;
                        break;
                    default:
                        yPos = -100;
                        break;
                }
                if (moveOne && numInQueue == 0)
                    if (!upDown)
                        xPos = 0;
                    else
                        xPos = 10;
                else
                    xPos = -50;
                if(upDown)
                    pokeFieldImage = new BufferedImage(pokeImage.getWidth(null)/2, (int)(pokeImage.getHeight(null)/3.15), BufferedImage.TYPE_INT_ARGB);
                else
                    pokeFieldImage = new BufferedImage(pokeImage.getWidth(null)/2, pokeImage.getHeight(null)/4, BufferedImage.TYPE_INT_ARGB);
                pokeGraphics = pokeFieldImage.getGraphics();
                pokeGraphics.drawImage(pokeImage, xPos, yPos, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
                pokeGraphics.dispose();
                g.drawImage(pokeFieldImage, unit.getX(), unit.getY()+5, (int)(pokeFieldImage.getWidth()*1.5), (int)(pokeFieldImage.getHeight()*1.5), this);
                break;

            default:
                if (unit.getDirection().equals("Right") || unit.getDirection().equals("Left"))
                    xPos = -32;
                if (unit.getDirection().equals("Right") || unit.getDirection().equals("Down"))
                    yPos = -64;
                if (!moveOne && numInQueue == 0)
                    yPos -= 32;
                pokeGraphics.drawImage(pokeImage, xPos, yPos, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
                g.drawImage(pokeFieldImage, unit.getX(), unit.getY(), pokeFieldImage.getWidth() * 2, pokeFieldImage.getHeight() * 2, this);
                break;
        }

    }

    /**
     * Default panel function that allows us to add stuff to the panel
     * @param g
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        /**
         * Functionized draw so that I can have it draw different stuff depending on the situation
         */
        if (paused){
            drawPauseMenu(g);
            return;
        }
        if(teamsChosen) {
            drawBattleground(g);
            g.setFont(new Font("Calibri", 10, 50));
            g.drawString(Boolean.toString(myTurn), 10, 50);
            return;
        }
        if (trainersChosen) {
            drawRoster(g);
            g.setFont(new Font("Calibri", 10, 50));
            g.drawString(Boolean.toString(myTurn), 10, 50);
            return;
        }
        drawTrainerRoster(g);
        g.setFont(new Font("Calibri", 10, 50));
        g.drawString(Boolean.toString(myTurn), 10, 50);
//        delet(g);
    }

    /**
     * Draws the pause menu
     * @param g
     */
    private void drawPauseMenu(Graphics g){
        setBackground(Color.BLACK);
        g.setColor(Color.YELLOW);
        g.drawString("Welcome to the pause menu", 20, 20);
        g.drawString("To return to the game press Escape", 20, 100);
        g.drawString("To exit the game press Backspace", 20, 180);
    }

    /**
     * Draws the battleground
     * @param g
     */
    private void drawBattleground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int tileStart = (int) Math.round((getWidth() - tileLength * Math.sqrt(tiles) + Math.sqrt(tiles) * 5) / 2) - 50; //Starts drawing tiles closer to center instead of on the left side of the screen
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this); //Draw background

        /**
         * All trainer pictures are by default facing left. This flips them.
         */
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-trainerOne.getTrainerImage().getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        BufferedImage trainerOneImage = new BufferedImage(trainerOne.getTrainerImage().getWidth(null), trainerOne.getTrainerImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics trainerGraphics = trainerOneImage.getGraphics();
        trainerGraphics.drawImage(trainerOne.getTrainerImage(), 0, 0, trainerOne.getTrainerImage().getWidth(null), trainerOne.getTrainerImage().getHeight(null), this);
        trainerOneImage = op.filter(trainerOneImage, null); //Flips image
        trainerGraphics.dispose();

        g2d.drawImage(trainerOneImage, 200, getHeight() / 2, 2*trainerOneImage.getWidth(), 2*trainerOneImage.getHeight(), this); //Draw first trainer
        g2d.drawImage(trainerTwo.getTrainerImage(), getWidth() - 250, getHeight() / 2, 2*trainerTwo.getTrainerImage().getWidth(null), 2*trainerTwo.getTrainerImage().getHeight(null), this); //Draw second trainer

        tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-trainerOne.getFriendImage().getWidth(null), 0);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        BufferedImage friendOneImage = new BufferedImage(trainerOne.getFriendImage().getWidth(null), trainerOne.getFriendImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics friendGraphics = friendOneImage.getGraphics();
        friendGraphics.drawImage(trainerOne.getFriendImage(), 0, 0, trainerOne.getFriendImage().getWidth(null), trainerOne.getFriendImage().getHeight(null), this);
        friendOneImage = op.filter(friendOneImage, null);
        friendGraphics.dispose();

        g2d.drawImage(friendOneImage, 200-2*friendOneImage.getWidth(), getHeight()/2, 2*friendOneImage.getWidth(), 2*friendOneImage.getHeight(), this);
        g2d.drawImage(trainerTwo.getFriendImage(), getWidth() - 250 + 2*trainerTwo.getTrainerImage().getWidth(null), getHeight() / 2, 2*trainerTwo.getFriendImage().getWidth(null), 2*trainerTwo.getFriendImage().getHeight(null), this);

        g.setColor(Color.ORANGE);
        g.fillRect(20, getHeight() - (int) (1.5 * queueTileLength), (int) (1.5 * queueTileLength), (int) (1.5 * queueTileLength));
        pokeIcon = new ImageIcon("Images/PokePics/Zoomed/" + queue[0].getUnitName() + ".png");
        pokeImage = pokeIcon.getImage();
        pokeQueueImage = new BufferedImage(pokeImage.getWidth(null) / 2, pokeImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        pokeGraphics = pokeQueueImage.getGraphics();
        pokeGraphics.drawImage(pokeImage, 0, 0, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
        pokeGraphics.dispose();
        g2d.drawImage(pokeQueueImage, 20, getHeight() - (int) (1.5 * queueTileLength), (int) (1.5 * queueTileLength), (int) (1.5 * queueTileLength), this);
        for (int i = 1; i < queue.length; i++) {
            if (queue[i] != null) {
                if (queue[i].isTeam())
                    g.setColor(Color.BLUE);
                else
                    g.setColor(Color.RED);
                g.fillRect((i + 1) * queueTileLength, getHeight() - queueTileLength, queueTileLength, queueTileLength);
                pokeIcon = new ImageIcon("Images/PokePics/Zoomed/" + queue[i].getUnitName() + ".png");
                pokeImage = pokeIcon.getImage();
                pokeQueueImage = new BufferedImage(pokeImage.getWidth(null) / 2, pokeImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                pokeGraphics = pokeQueueImage.getGraphics();
                pokeGraphics.drawImage(pokeImage, 0, 0, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
                pokeGraphics.dispose();
                g2d.drawImage(pokeQueueImage, (i + 1) * queueTileLength, getHeight() - queueTileLength, queueTileLength, queueTileLength, this);


            }
        }

        if (unitsPlaced) {
            if (!trainerAttackReady) {
                for (int i = 0; i < Math.sqrt(tiles); i++) { //Draw tiles
                    for (int j = 0; j < Math.sqrt(tiles); j++) {
                        tilesArr[i][j] = new Tile(i, j, tileStart + i * tileLength + i * 5, j * tileLength + j * 5 + tileLength - 5, tileLength, tileLength);
                        if (SceneFunctions.inRange(i, j, queue[0]) && !inTurn && !SceneFunctions.spotTaken(i, j, queue) && !hasMoved) {
                            g.setColor(new Color(0, 100, 0, 100));
                            g.fillRect(tilesArr[i][j].getX(), tilesArr[i][j].getY(), tileLength, tileLength);
                            g.setColor(new Color(0, 100, 0, 255));
                            g.drawRect(tilesArr[i][j].getX(), tilesArr[i][j].getY(), tileLength, tileLength);
                            //                    g.drawImage(tileImage, tilesArr[j][i].getLeftX(), tilesArr[j][i].getTopY(), tileLength, tileLength, this);
                        }
                    }
                }
            }
        }
        else
            if (queue[0].isTeam()){
                for (int i = 0; i < Math.sqrt(tiles); i++) { //Draw tiles
                    for (int j = 0; j < Math.sqrt(tiles); j++) {
                        tilesArr[i][j] = new Tile(i, j, tileStart + i * tileLength + i * 5,  j * tileLength + j * 5 + tileLength - 5, tileLength, tileLength);
                        if (i < 2 && !SceneFunctions.spotTaken(i, j, queue)) {
                            g.setColor(new Color(0, 100, 0, 100));
                            g.fillRect(tilesArr[i][j].getX(), tilesArr[i][j].getY(), tileLength, tileLength);
                            g.setColor(new Color(0, 100, 0, 255));
                            g.drawRect(tilesArr[i][j].getX(), tilesArr[i][j].getY(), tileLength, tileLength);
                        }
                    }
                }
            }
            else{
                for (int i = 0; i < Math.sqrt(tiles); i++) { //Draw tiles
                    for (int j = 0; j < Math.sqrt(tiles); j++) {
                        tilesArr[i][j] = new Tile(i, j, tileStart + i * tileLength + i * 5, j * tileLength + j * 5 + tileLength - 5, tileLength, tileLength);
                        if (i>7 && !SceneFunctions.spotTaken(i, j, queue)) {
                            g.setColor(new Color(0, 100, 0, 100));
                            g.fillRect(tilesArr[i][j].getX(), tilesArr[i][j].getY(), tileLength, tileLength);
                            g.setColor(new Color(0, 100, 0, 255));
                            g.drawRect(tilesArr[i][j].getX(), tilesArr[i][j].getY(), tileLength, tileLength);
                        }
                    }
                }
            }

            g.setFont(new Font("Calibri", 10, 15));
        for (int k = 0; k < queue.length; k++) {
            drawPokeFieldImage(k, g);
            if (unitsPlaced && !trainerAttackReady)
                if (enemiesInRange[k])
                    g.drawImage(target, queue[k].getX(), queue[k].getY(), tileLength, tileLength, this);

            if(trainerAttackReady)
                if(queue[0].isTeam() != queue[k].isTeam())
                    g.drawImage(target, queue[k].getX(), queue[k].getY(), tileLength, tileLength, this);

            if (queue[k].isTeam())
                g.setColor(Color.BLUE);
            else
                g.setColor(Color.RED);

            g.fillRect(queue[k].getX(), queue[k].getY(), 30, 10);

            g.setColor(Color.BLACK);
            g.drawRect(queue[k].getX(), queue[k].getY(), 30, 10);

            g.drawString(Integer.toString(queue[k].getUnitsInStack()), queue[k].getX(), queue[k].getY()+g.getFont().getSize()-2);

            g.drawImage(shieldImage, defenseTile.getX(), defenseTile.getY(), defenseTile.getWidth(), defenseTile.getHeight(), this);
            if(trainerAttackReady)
                g.drawImage(trainerAttackReadyImage, trainerAttackTile.getX(), trainerAttackTile.getY(), trainerAttackTile.getWidth(), trainerAttackTile.getHeight(), this);
            else
                g.drawImage(trainerAttackImage, trainerAttackTile.getX(), trainerAttackTile.getY(), trainerAttackTile.getWidth(), trainerAttackTile.getHeight(), this);
        }

    }

    /**
     * Draws the roster of Pokemon
     * @param g For graphics
     */
    private void drawRoster(Graphics g){

        Graphics2D g2d = (Graphics2D)g;

        roster = Unit.forRoster();
        BufferedImage pokeRosterImage;
        Graphics rosterGraphics;

        for (int i = 0; i<17; i++){
            Pair<Color, Image> pair = SceneFunctions.rosterColorNeeded(i);
            g.setColor(pair.getKey());
            g.fillRect(i*BOARDWIDTH/17, 0, BOARDWIDTH/17, BOARDHEIGHT);
            g.drawImage(pair.getValue(), i*BOARDWIDTH/17, 0, BOARDWIDTH/17, BOARDHEIGHT/7, this);
            for (int j=0; j<7; j++){
                try {
                    Node node = roster.item(i * 7 + j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        imageLocation = "Images/PokePics/Zoomed/" + element.getElementsByTagName("unitName").item(0).getTextContent() + ".png";
                        rosterIcon = new ImageIcon(imageLocation);
                        rosterImage = rosterIcon.getImage();
                        pokeRosterImage = new BufferedImage(rosterImage.getWidth(null) / 2, rosterImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                        rosterGraphics = pokeRosterImage.getGraphics();
                        rosterGraphics.drawImage(rosterImage, 0, 0, rosterImage.getWidth(null), rosterImage.getHeight(null), this);
                        rosterGraphics.dispose();
                        g2d.drawImage(pokeRosterImage, i * BOARDWIDTH / 17, (j + 1) * BOARDHEIGHT / 8, BOARDWIDTH / 17, BOARDHEIGHT / 8, this);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The actions taken during a turn
     * @param x X coordinate of click
     * @param y Y coordinate of click
     */
    private void turn(int x, int y){

        /**
         * Check if we're in trainer attack mode
         */
        if (trainerAttackReady){
            if(trainerAttackTile.hasBeenClicked(x, y)) //Turn off trainerAttackReady if it's been reclicked
                trainerAttackReady = false;

            /**
             * Check if a viable Pokemon has been clicked on
             */
            for (int i = 0; i < tilesArr.length; i++){
                for (int j = 0; j < tilesArr.length; j++){
                    if (tilesArr[i][j].hasBeenClicked(x, y) && SceneFunctions.spotTaken(i, j, queue)) {
                        int inSpot = SceneFunctions.unitInSpot(queue, i, j);
                        if (queue[inSpot].isTeam() != queue[0].isTeam()){
                            if (queue[0].isTeam() == trainerOne.getTeam()) {
                                SceneFunctions.trainerAttack(trainerOne, trainerTwo, queue[inSpot]);
                                trainerOne.setAttackedThisTurn(true);
                            }
                            else {
                                SceneFunctions.trainerAttack(trainerTwo, trainerOne, queue[inSpot]);
                                trainerTwo.setAttackedThisTurn(true);
                            }
                            trainerAttackReady = false;
                        }
                    }
                }
            }
        }
        else {

            /**
             * Make sure the Pokemon hasn't already moved in this turn
             */
            if (!hasMoved) {
                queue = SceneFunctions.defendButtonPressed(defenseTile, x, y, queue, trainerOne, trainerTwo); //Checks if defendButton was clicked
                /**
                 * Checks if trainer attack button was clicked
                 */
                if (queue[0].isTeam() == trainerOne.getTeam()) {
                    if (trainerAttackTile.hasBeenClicked(x, y) && !trainerOne.isAttackedThisTurn())
                        trainerAttackReady = true;
                }
                else
                    if (trainerAttackTile.hasBeenClicked(x, y) && !trainerTwo.isAttackedThisTurn())
                        trainerAttackReady = true;
            }
            for (int i = 0; i < tilesArr.length; i++) {
                for (int j = 0; j < tilesArr.length; j++) {
                    /**
                     * Moves to tile if it's available
                     */
                    if (!hasMoved && tilesArr[i][j].hasBeenClicked(x, y) && !SceneFunctions.spotTaken(i, j, queue) && SceneFunctions.inRange(i, j, queue[0])) {
                        queue[0].setTileX(i);
                        queue[0].setTileY(j);
                        chosenTile = tilesArr[i][j];
                        inTurn = true;
                    }

                    /**
                     * Spot in queue of unit in chosen spot
                     */
                    int inSpot = SceneFunctions.unitInSpot(queue, i, j);

                    /**
                     * Attack unit if available
                     */
                    if (SceneFunctions.spotTaken(i, j, queue) && enemiesInRange[inSpot] && tilesArr[i][j].hasBeenClicked(x, y)) {
                        System.out.println(queue[0].getUnitName() + " has attacked " + queue[inSpot].getUnitName());
                        if (queue[0].isTeam() == trainerOne.getTeam())
                            SceneFunctions.Attack(queue[0], queue[inSpot], trainerOne, trainerTwo);
                        else
                            SceneFunctions.Attack(queue[0], queue[inSpot], trainerTwo, trainerOne);
                        queue = SceneFunctions.updateQueue(queue, trainerOne, trainerTwo);
                        enemiesInRange = SceneFunctions.enemyInRange(queue);
                        hasMoved = false;
                    }
                }
            }
        }
        repaint();
    }

    /**
     * Draw roster of trainers
     * @param g For graphics
     */
    private void drawTrainerRoster(Graphics g){

        roster = Trainer.forRoster();
        String pokeImageLocation;

        g.drawImage(trainerSelectBGImage, 0, 0, BOARDWIDTH, BOARDHEIGHT, this);

        for (int i=0; i<6; i++){
            for (int j=0; j<4; j++){
                try{
                    Node node = roster.item(i*4+j);
                    if (node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element) node;
                        imageLocation = "Images/TrainerPics/" + element.getElementsByTagName("Name").item(0).getTextContent() + ".png";
                        pokeImageLocation = "Images/TrainerPics/FriendPoke/" + element.getElementsByTagName("friendPoke").item(0).getTextContent() + ".png";
                        rosterIcon = new ImageIcon(imageLocation);
                        rosterImage = rosterIcon.getImage();
                        int wid = rosterImage.getWidth(null), hei = rosterImage.getHeight(null);
                        g.drawImage(rosterImage, i*BOARDWIDTH/6 + wid/2, j*BOARDHEIGHT/4 + hei/2, wid, hei, this);
                        rosterIcon = new ImageIcon(pokeImageLocation);
                        rosterImage = rosterIcon.getImage();
                        g.drawImage(rosterImage, i*BOARDWIDTH/6 + wid + wid/2, j*BOARDHEIGHT/4 + hei/2, rosterImage.getWidth(null), rosterImage.getHeight(null), this);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Send a message to other player client during setup
     * @param x X coordinate of click
     * @param y Y coordinate of click
     */
    private void callSendSetup(int x, int y){
        if (myTurn)
            client.send(id, x, y, false, 0);
        myTurn = !myTurn;
    }

    /**
     *Function for testing spritesheet sizes
     * @param g
     */
    private void delet(Graphics g){
        //Trainer t = new Trainer("Roark", true);
        //g.drawImage(t.getTrainerImage(), 50, 50, t.getTrainerImage().getWidth(null), t.getTrainerImage().getHeight(null), this);
    }

    /**
     * Sends coordinates to appropriate function according to the stage of the game
     * @param x X coordinate of click
     * @param y Y coordinate of click
     */
    private void findStartupOperation(int x, int y){

        if (!trainersChosen) {
            chooseTrainers(x, y);
            callSendSetup(x, y);
            repaint();
            return;
        }
        if (!teamsChosen){
            chooseTeams(x, y);
            repaint();
            return;
        }
        if (!unitsPlaced){
            placeUnits(x, y);
            callSend(x, y);
            repaint();
            return;
        }
        if (!inTurn) {
            turn(x, y);
            callSend(x, y);
            repaint();
        }
    }

    /**
     * Allows players to choose their trainer according to click location
     * @param x X coordinate of click
     * @param y Y coordinate of click
     */
    private void chooseTrainers(int x, int y){
        int rectWidth = BOARDWIDTH/6, rectHeight = BOARDHEIGHT/4;
        int i = 5-(BOARDWIDTH - x)/rectWidth, j = 3-(BOARDHEIGHT - y)/rectHeight;
        int z = i*4+j;
        if (!trainerOneChosen){
            trainerOne = new Trainer(z, true, 1);
            trainerOneChosen = true;
            return;
        }
        trainerTwo = new Trainer(z, false, 1);
        trainersChosen = true;

        repaint();

    }
    /**
     * Allows players to choose their team according to click location
     * @param x X coordinate of click
     * @param y Y coordinate of click
     */
    private void chooseTeams(int x, int y){
        if (!teamOneChosen) {
            SceneFunctions.setTeam(trainerOne, Unit.forRoster(), x, BOARDWIDTH, true);
            teamOneChosen = true;
            callSendSetup(x, y);
            return;
        }
        SceneFunctions.setTeam(trainerTwo, Unit.forRoster(), x, BOARDWIDTH, false);
        teamsChosen = true;
        queue = SceneFunctions.createQueue(trainerOne, trainerTwo);

        callSend(x, y);
        repaint();
    }

    /**
     * Send a message to the other player client
     * @param x X location of click
     * @param y Y location of click
     */
    private void callSend(int x, int y){
        if (myTurn) //Only send message if it's your turn
            client.send(id, x, y, false, 0);
        myTurn = queue[0].isTeam() && id == 0 || !queue[0].isTeam() && id == 1; //Sets turn
    }

    /**
     * Handles a right click which has to do with sending messages to Android
     * @param x X coordinate of click
     * @param y Y coordinate of click
     */
    public void rightClick(int x, int y){
        System.out.println("Right Clicked"); //Allows for debugging
        Tile chosenTile = SceneFunctions.chosenTile(x, y, tilesArr); //Find the tile that was clicked if it exists
        if(chosenTile != null) { //Enters if when the tile exists, otherwise end the function
            int chosenUnitLoc = SceneFunctions.unitInSpot(queue, chosenTile.getTileX(), chosenTile.getTileY()); //Find the unit that was clicked
            Unit chosenUnit = queue[chosenUnitLoc];
            //Send a message to the Android with all the unit's information
            client.sendAndroid(chosenUnit.getUnitName(), chosenUnit.getCurrentHealth(), chosenUnit.getMaxHealth(), chosenUnit.getAttack(), chosenUnit.getDefense(),
                    chosenUnit.getMinDamage(), chosenUnit.getMaxDamage(), chosenUnit.getMovement(), chosenUnit.isFlying(), chosenUnit.isRanged());
        }
    }

    /**
     * Allows us to see the x, y locations of a mouse click and use them
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) { //Check which mouse button was pressed
            case MouseEvent.BUTTON1: //Left mouse button
                if (myTurn && !paused) //Make sure the game isn't paused and that it's your turn
                    findStartupOperation(e.getX(), e.getY()); //Send x, y coordinates of the click to the apporpriate function
                break;
            case MouseEvent.BUTTON3: //Right mouse button
                if(unitsPlaced) //Make sure the units have been placed
                    rightClick(e.getX(), e.getY()); //Go to separate function that handles right clicks
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Allows for dynamic cursor image
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (inTurn) //Don't want cursor to change mid-turn
            return;

        Tile t = null; //Will later become the tile the mouse is on if it exists
        if (unitsPlaced) { //Make sure units have been placed
            boolean foundTile = false; //Set by default
            for (Tile[] tileArr : tilesArr){ //Go through Tile array and see if any of the tiles is being hovered over
                for (Tile tile : tileArr) {
                    if (tile.hasBeenClicked(e.getX(), e.getY())) { //Returns true if the cursor is on a tile
                        t = tile;
                        foundTile = true;
                        break;
                    }
                }
                if (foundTile)
                    break;
            }
        }
        //Create a cursor according to all sorts of variables like location, if anybody is in the tile, etc...
        setCursor(SceneFunctions.makeCursor(e.getX(), e.getY(), t, queue, enemiesInRange, defenseTile, trainerAttackReady));
    }

    /**
     * Allows for shortcuts and pause menu
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
        int key = e.getKeyCode(); //Gives int value so that we can compare the pressed key
        if (key == KeyEvent.VK_ESCAPE) { //If escape key is pressed, pause the game
            System.out.println("Game Paused");
            paused = !paused;
        }
        if (paused && key == KeyEvent.VK_BACK_SPACE) //If paused and backspace is pressed then end the game
            endGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Placing the units on the field before the fight
     * @param x X coordinate of click
     * @param y Y coordinate of click
     */
    private void placeUnits(int x, int y){
        chosenTile = SceneFunctions.chosenTile(x, y, tilesArr); //Get the tiles that was clicked on
        int tileStart = (int) Math.round((BOARDWIDTH - tileLength * Math.sqrt(tiles) + Math.sqrt(tiles) * 5) / 2) - 50; //Starts drawing tiles closer to center instead of on the left side of the screen
        Unit unit = queue[0]; //For easier access to the unit we want
        client.sendAndroid(unit.getUnitName(), unit.getCurrentHealth(), unit.getMaxHealth(), unit.getAttack(), unit.getDefense(), unit.getMinDamage()
        , unit.getMaxDamage(), unit.getMovement(), unit.isFlying(), unit.isRanged()); //Send unit info to Android
        if (chosenTile != null) { //Makes sure a tile was actually clicked on
            if (unit.getTileY() == -1 && unit.getTileX() == -1 && !SceneFunctions.spotTaken(chosenTile.getTileX(), chosenTile.getTileY(), queue)) { //Makes sure the Pokemon is allowed to take the spot and that it is not taked
                if (unit.isTeam()) { //Each team starts on different sides so we must make sure the right side was chosen
                    if (chosenTile.getTileX() < 2) { //Can only be placed within the first two columns on the left for team 1
                        unit.setTileX(chosenTile.getTileX());
                        unit.setTileY(chosenTile.getTileY());
                    } else
                        return;
                } else {
                    if (chosenTile.getTileX() > 7) { //Can only be placed within the first two columns on the right for team 2
                        unit.setTileX(chosenTile.getTileX());
                        unit.setTileY(chosenTile.getTileY());
                    } else
                        return;
                }
                unit.setX(tileStart + unit.getTileX() * tileLength + unit.getTileX() * 5); //If the unit has been succesfully placed on the tile, set their actual x, y coordinates
                unit.setY(50 + unit.getTileY() * tileLength + unit.getTileY() * 5);
                if (queue[1].getTileX() != -1) //Queue moves so when it reaches the end, queue[1] will be the first unit meaning everybody has been placed
                    unitsPlaced = true;
                queue = SceneFunctions.updateQueue(queue, trainerOne, trainerTwo); //Once a unit has been placed, it's time to place the next unit
                enemiesInRange = SceneFunctions.enemyInRange(queue); //Creates the enemies in range for if the last unit is ranged
                repaint(); //Repaint the screen with the new unit placement
            }
        }

    }

    /**
     * Function that runs every tick of the timer
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if(inTurn) { //If a move has already been done it means we need to move the Pokemon and make the proper animations
            //Moves the unit in the correct direction a certain distance allowing for an animation that looks good
            if (queue[0].getX() < chosenTile.getX()) {
                queue[0].setX(queue[0].getX() + 10); //Moves the unit
                queue[0].setDirection("Right"); //Makes sure the unit is facing the right direction
                if(queue[0].getX() >= chosenTile.getX()){ //Make sure they don't overshoot the target
                    queue[0].setX(chosenTile.getX());
                }
            }
            //Moves the unit in the correct direction a certain distance allowing for an animation that looks good
            if (queue[0].getX() > chosenTile.getX()) {
                queue[0].setX(queue[0].getX() - 10); //Moves the unit
                queue[0].setDirection("Left"); //Makes sure the unit is facing the left direction
                if(queue[0].getX() <= chosenTile.getX()){ //Make sure they don't overshoot the target
                    queue[0].setX(chosenTile.getX());
                }
            }
            //Once the unit is on the correct X position, it's time to move them on the Y axis
            if(queue[0].getX() == chosenTile.getX()) {
                //Moves the unit in the correct direction a certain distance allowing for an animation that looks good
                if (queue[0].getY() < chosenTile.getY()) {
                    queue[0].setY(queue[0].getY() + 10); //Moves the unit
                    queue[0].setDirection("Down"); //Makes sure the unit is facing the down direction
                    if (queue[0].getY() >= chosenTile.getY()) { //Make sure they don't overshoot the target
                        queue[0].setY(chosenTile.getY());

                    }
                }

                //Moves the unit in the correct direction a certain distance allowing for an animation that looks good
                if (queue[0].getY() > chosenTile.getY()) {
                    queue[0].setY(queue[0].getY() - 10); //Moves the unit
                    queue[0].setDirection("Up"); //Makes sure the unit is facing the up direction
                    if (queue[0].getY() <= chosenTile.getY()) { //Make sure they don't overshoot the target
                        queue[0].setY(chosenTile.getY());
                    }
                }

            }

            moveOne = !moveOne; //Flip the animation for the spritesheet between the two pictures for each direction

            if (queue[0].getX() == chosenTile.getX() && queue[0].getY() == chosenTile.getY()) { //Make sure we're in the final position
                inTurn = false; //The turn is officially over and we can move over to the next part of the turn
                moveOne = false; //Reset animation
                if (queue[0].isTeam()) //Sets the Pokemon to be looking in the same direction as the rest of their team
                    queue[0].setDirection("Right");
                else
                    queue[0].setDirection("Left");
                enemiesInRange = SceneFunctions.enemyInRange(queue); //Check which enemies are in range after movement
                canAttack = false; //Set false by default
                hasMoved = true; //Make sure they don't move again in the same turn
                for (boolean inRange : enemiesInRange) { //Set to true if there are enemies in range
                    if (inRange) {
                        canAttack = true;
                        break;
                    }
                }
                if (!canAttack) { //If nobody is in range then end turn
                    queue = SceneFunctions.updateQueue(queue, trainerOne, trainerTwo); //Update queue
                    enemiesInRange = SceneFunctions.enemyInRange(queue); //Set enemies in range for the next Pokemon in line
                    hasMoved = false; //Reset hasMoved for the new Pokemon
                }
            }
            repaint(); //Repaint the screen with the new position
        }
    }

    /**
     * Got a message from the server through NetworkRead
     * @param fromServer The String we've gotten from the server
     */
    public void update(String fromServer) {

        if (fromServer.startsWith("Wait")){ //Game has not yet started and not all clients have conneccted
            if (fromServer.contains("0")) { //Means this is the first client to connect, making their id 0 and getting to move first
                myTurn = true;
                id = 0;
            }
            else { //Means this is the second client to connect, making their id 1 and getting to move second
                myTurn = false;
                id = 1;
            }
            Game.setTitle(id); //Allows differentiation between clients if they're on the same computer
            return;
        }
        if (fromServer.startsWith("start") || fromServer.startsWith("Android")) { //Either a message that the game has started or a message meant for Android. Print and ignore
            System.out.println(fromServer);
            return;
        }
        if(fromServer.equals("Bye")) { //Means the other client has disconnected ending the game
            endGame();
            return;
        }

        String[] parts = fromServer.split("&&"); //Splits the String we get from the other client
        int senderID = Integer.parseInt(parts[0]); //Allows us to check which client sent the message

        if (senderID != id){ //Ignore messages you send
            int x = Integer.parseInt(parts[1]); //Get the x coordinate of the click
            int y = Integer.parseInt(parts[2]); //Get the y coordinate of the click

            findStartupOperation(x, y); //Send coordinates to a function that handles them
        }
    }

    /**
     * End game when I want to
     */
    public void endGame(){
        System.exit(1);
    }

}
