package pokemonHeroes;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;;

public class Scene extends JPanel implements MouseListener, ActionListener {

    private final static int BOARDWIDTH = 1533;
    private final static int BOARDHEIGHT = 845;

    private int tiles = 100; //Number of tiles on board
    private int tileLength = 64; //Length of each tile
    private int queueTileLength = 100; //Length of each tile in the queue

    private ImageIcon tile = new ImageIcon("Images/grassTile.jpg"); //The image with which a tile will be shown
    private Image tileImage = tile.getImage();

    private ImageIcon bgIcon = new ImageIcon("Images/tempBG.png"); //Background image
    private Image bgImage = bgIcon.getImage();

    private Unit[] queue; //Turn queue

    private Tile[][] tilesArr = new Tile[(int)Math.sqrt(tiles)][(int)Math.sqrt(tiles)];

    private Trainer trainerOne, trainerTwo; //The two player's trainers

    private int speed = 50; //Time in between ticks. Lower speed-->Things happen faster. Counted in milliseconds.

    private ImageIcon pokeIcon; //Pokemon related graphics
    private Image pokeImage;
    private BufferedImage pokeQueueImage;
    private Graphics pokeGraphics;
    private BufferedImage pokeFieldImage;

    private boolean inTurn;

    public Scene(){

        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("Images/Cursors/AttackCursor.png").getImage(), new Point(0, 0), "custom cursor"));

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

        trainerOne = new Trainer("Cynthia", true); //Creates first trainer. Temporary until player can choose
        trainerTwo = new Trainer("Cyrus", false); //Same as above

        trainerOne.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Wobbuffet", 1, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Xatu", 2, false));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Yanmega", 3, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Zapdos", 4, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Togekiss", 100, false));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Torterra", 6, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Toxicroak", 7, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Gyarados", 7, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Tyranitar", 9, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Ursaring", 10, true));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Vespiquen", 11, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Grumpig", 12, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Walrein", 13, true));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 5, 0, false, 0, "Empoleon", 7, false));

        queue = SceneFunctions.createQueue(trainerOne, trainerTwo); //Creates the queue according to player's teams

        queue[0].setTileX(0);queue[0].setTileY(0);
        queue[1].setTileX(0);queue[1].setTileY(1);
        queue[2].setTileX(0);queue[2].setTileY(2);
        queue[3].setTileX(0);queue[3].setTileY(3);
        queue[4].setTileX(0);queue[4].setTileY(4);
        queue[5].setTileX(0);queue[5].setTileY(5);
        queue[6].setTileX(0);queue[6].setTileY(6);
        queue[7].setTileX(9);queue[7].setTileY(0);
        queue[8].setTileX(9);queue[8].setTileY(1);
        queue[9].setTileX(9);queue[9].setTileY(2);
        queue[10].setTileX(9);queue[10].setTileY(3);
        queue[11].setTileX(9);queue[11].setTileY(4);
        queue[12].setTileX(9);queue[12].setTileY(5);
        queue[13].setTileX(9);queue[13].setTileY(6);

        int tileStart = (int) Math.round((BOARDWIDTH - tileLength * Math.sqrt(tiles) + Math.sqrt(tiles) * 5) / 2) - 50; //Starts drawing tiles closer to center instead of on the left side of the screen
//        System.out.println(tileStart);

        queue[0].setX(tileStart+queue[0].getTileX()*tileLength+queue[0].getTileX()*5);queue[0].setY(50+queue[0].getTileY()*tileLength+queue[0].getTileY()*5);
        queue[1].setX(tileStart+queue[1].getTileX()*tileLength+queue[1].getTileX()*5);queue[1].setY(50+queue[1].getTileY()*tileLength+queue[1].getTileY()*5);
        queue[2].setX(tileStart+queue[2].getTileX()*tileLength+queue[2].getTileX()*5);queue[2].setY(50+queue[2].getTileY()*tileLength+queue[2].getTileY()*5);
        queue[3].setX(tileStart+queue[3].getTileX()*tileLength+queue[3].getTileX()*5);queue[3].setY(50+queue[3].getTileY()*tileLength+queue[3].getTileY()*5);
        queue[4].setX(tileStart+queue[4].getTileX()*tileLength+queue[4].getTileX()*5);queue[4].setY(50+queue[4].getTileY()*tileLength+queue[4].getTileY()*5);
        queue[5].setX(tileStart+queue[5].getTileX()*tileLength+queue[5].getTileX()*5);queue[5].setY(50+queue[5].getTileY()*tileLength+queue[5].getTileY()*5);
        queue[6].setX(tileStart+queue[6].getTileX()*tileLength+queue[6].getTileX()*5);queue[6].setY(50+queue[6].getTileY()*tileLength+queue[6].getTileY()*5);
        queue[7].setX(tileStart+queue[7].getTileX()*tileLength+queue[7].getTileX()*5);queue[7].setY(50+queue[7].getTileY()*tileLength+queue[7].getTileY()*5);
        queue[8].setX(tileStart+queue[8].getTileX()*tileLength+queue[8].getTileX()*5);queue[8].setY(50+queue[8].getTileY()*tileLength+queue[8].getTileY()*5);
        queue[9].setX(tileStart+queue[9].getTileX()*tileLength+queue[9].getTileX()*5);queue[9].setY(50+queue[9].getTileY()*tileLength+queue[9].getTileY()*5);
        queue[10].setX(tileStart+queue[10].getTileX()*tileLength+queue[10].getTileX()*5);queue[10].setY(50+queue[10].getTileY()*tileLength+queue[10].getTileY()*5);
        queue[11].setX(tileStart+queue[11].getTileX()*tileLength+queue[11].getTileX()*5);queue[11].setY(50+queue[11].getTileY()*tileLength+queue[11].getTileY()*5);
        queue[12].setX(tileStart+queue[12].getTileX()*tileLength+queue[12].getTileX()*5);queue[12].setY(50+queue[12].getTileY()*tileLength+queue[12].getTileY()*5);
        queue[13].setX(tileStart+queue[13].getTileX()*tileLength+queue[13].getTileX()*5);queue[13].setY(50+queue[13].getTileY()*tileLength+queue[13].getTileY()*5);

        Timer timer = new Timer(speed, this);
        timer.start();

        inTurn=false;
    }

    protected void paintComponent(Graphics g){ //Default panel function that allows us to add stuff to the panel
        super.paintComponent(g);
        drawBattleground(g); //Functionized draw so that I can have it draw different stuff depending on the situation
    }

    protected void drawBattleground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int tileStart = (int) Math.round((getWidth() - tileLength * Math.sqrt(tiles) + Math.sqrt(tiles) * 5) / 2) - 50; //Starts drawing tiles closer to center instead of on the left side of the screen
//        System.out.println(tileStart);
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this); //Draw background

        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); //All trainer pictures are by default facing left. This flips them.
        tx.translate(-trainerOne.getTrainerImage().getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        BufferedImage trainerOneImage = new BufferedImage(trainerOne.getTrainerImage().getWidth(null), trainerOne.getTrainerImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics trainerGraphics = trainerOneImage.getGraphics();
        trainerGraphics.drawImage(trainerOne.getTrainerImage(), 0, 0, trainerOne.getTrainerImage().getWidth(null), trainerOne.getTrainerImage().getHeight(null), this);
        trainerOneImage = op.filter(trainerOneImage, null); //Flips image
        trainerGraphics.dispose();

        g2d.drawImage(trainerOneImage, 200, getHeight() / 2, 100, 200, this); //Draw first trainer
        g2d.drawImage(trainerTwo.getTrainerImage(), getWidth() - 200, getHeight() / 2, 100, 200, this); //Draw second trainer

        g.setColor(Color.ORANGE);
        g.fillRect(20, getHeight() - (int) (1.5 * queueTileLength), (int) (1.5 * queueTileLength), (int) (1.5 * queueTileLength));
        pokeIcon = new ImageIcon("Images/PokePics/Zoomed/" + queue[0].getUnitName() + ".png");
        pokeImage = pokeIcon.getImage();
        pokeQueueImage = new BufferedImage(pokeImage.getWidth(null) / 2, pokeImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        pokeGraphics = pokeQueueImage.getGraphics();
        pokeGraphics.drawImage(pokeImage, 0, 0, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
        pokeGraphics.dispose();
        g2d.drawImage(pokeQueueImage, 20, getHeight() - (int) (1.5 * queueTileLength), (int) (1.5 * queueTileLength), (int) (1.5 * queueTileLength), this);
        for (int i = 1; i < 14; i++) {
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

        for (int i = 0; i < Math.sqrt(tiles); i++) { //Draw tiles
            for (int j = 0; j < Math.sqrt(tiles); j++) {
                tilesArr[j][i] = new Tile(j, i, tileStart + j * tileLength + j * 5, tileStart + j * tileLength + j * 5 + tileLength, 50 + i * tileLength + i * 5, 50 + i * tileLength + i * 5 + tileLength);
                if (SceneFunctions.inRange(j, i, queue[0]) && !inTurn && !SceneFunctions.spotTaken(j, i, queue)) {
                    g.drawImage(tileImage, tilesArr[j][i].getLeftX(), tilesArr[j][i].getTopY(), tileLength, tileLength, this);
                }
            }
        }

        for (int k = 0; k < 14; k++) {
            pokeIcon = new ImageIcon("Images/PokePics/Combat/" + queue[k].getUnitName() + ".png");
            pokeImage = pokeIcon.getImage();
            pokeFieldImage = new BufferedImage(pokeImage.getWidth(null) / 2, pokeImage.getHeight(null) / 4, BufferedImage.TYPE_INT_ARGB);
            pokeGraphics = pokeFieldImage.getGraphics();
            drawPokeFieldImage(k);
            pokeGraphics.dispose();
            g.drawImage(pokeFieldImage, queue[k].getX(), queue[k].getY(), pokeFieldImage.getWidth() * 2, pokeFieldImage.getHeight() * 2, this);
//                        queue[k].setX(tileStart+j*tileLength+j*5);
//                        queue[k].setY(50+i*tileLength+i*5);
//                        if(k==0){
//                            System.out.println("X "+queue[k].getX()+" "+ (tileStart+j*tileLength+j*5));
//                            System.out.println("Y "+queue[k].getY()+" "+(50+i*tileLength+i*5));
//                        }

        }

    }

    public void drawPokeFieldImage(int numInQueue){
        if(numInQueue != 0) {
            if (queue[numInQueue].isTeam())
                pokeGraphics.drawImage(pokeImage, -32, -64, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
            else
                pokeGraphics.drawImage(pokeImage, -32, -32, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
            return;
        }
        if (queue[0].getDirection().equals("Right")){
            if (moveOne)
                pokeGraphics.drawImage(pokeImage, -32, -64, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
            else
                pokeGraphics.drawImage(pokeImage, -32, -96, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
//            System.out.println("Right");
            return;
        }
        if (queue[0].getDirection().equals("Left")){
            if (moveOne)
                pokeGraphics.drawImage(pokeImage, -32, 0, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
            else
                pokeGraphics.drawImage(pokeImage, -32, -32, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
//            System.out.println("Left");
            return;
        }
        if (queue[0].getDirection().equals("Up")){
            if (moveOne)
                pokeGraphics.drawImage(pokeImage, 0, 0, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
            else
                pokeGraphics.drawImage(pokeImage, 0, -32, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
//            System.out.println("Up");
            return;
        }
        if (queue[0].getDirection().equals("Down")){
            if (moveOne)
                pokeGraphics.drawImage(pokeImage, 0, -64, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
            else
                pokeGraphics.drawImage(pokeImage, 0, -96, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
//            System.out.println("Down");
            return;
        }
    }

    private Tile chosenTile;

    public void turn(int x, int y){
        for (int i=0; i<tilesArr.length; i++){
            for (int j=0; j<tilesArr.length; j++){
                //System.out.println(e.getX()+" "+e.getY()+" "+ tilesArr[j][i].toString());
                if(SceneFunctions.inTile(x, y, tilesArr[j][i]) && !SceneFunctions.spotTaken(j, i, queue) && SceneFunctions.inRange(j, i, queue[0])) {
                    //System.out.println(tilesArr[j][i].getX() + " " + tilesArr[j][i].getY());
                    queue[0].setTileX(j);
                    queue[0].setTileY(i);
                    chosenTile = tilesArr[j][i];
                    inTurn=true;
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!inTurn)
            turn(e.getX(), e.getY());
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

    private boolean moveOne = false; //Used to know which part of the animation should be played

    @Override
    public void actionPerformed(ActionEvent e){
        if(inTurn) {
            if (queue[0].getX() < chosenTile.getLeftX()) {
                queue[0].setX(queue[0].getX() + 10);
                queue[0].setDirection("Right");
                if(queue[0].getX() >= chosenTile.getLeftX()){
                    queue[0].setX(chosenTile.getLeftX());
                }
            }

            if (queue[0].getX() > chosenTile.getLeftX()) {
                queue[0].setX(queue[0].getX() - 10);
                queue[0].setDirection("Left");
                if(queue[0].getX() <= chosenTile.getLeftX()){
                    queue[0].setX(chosenTile.getLeftX());
                }
            }

            if(queue[0].getX() == chosenTile.getLeftX()) {

                if (queue[0].getY() < chosenTile.getTopY()) {
                    queue[0].setY(queue[0].getY() + 10);
                    queue[0].setDirection("Down");
                    if (queue[0].getY() >= chosenTile.getTopY()) {
                        queue[0].setY(chosenTile.getTopY());

                    }
                }


                if (queue[0].getY() > chosenTile.getTopY()) {
                    queue[0].setY(queue[0].getY() - 10);
                    queue[0].setDirection("Up");
                    if (queue[0].getY() <= chosenTile.getTopY()) {
                        queue[0].setY(chosenTile.getTopY());
                    }
                }

            }

            if(moveOne)
                moveOne=false;
            else
                moveOne=true;

            if (queue[0].getX() == chosenTile.getLeftX() && queue[0].getY() == chosenTile.getTopY()) {
                inTurn = false;
                moveOne = false;
                queue[0].setDirection("Right");
                SceneFunctions.updateQueue(queue);
            }
            repaint();
        }
    }

}
