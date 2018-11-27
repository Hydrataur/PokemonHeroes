package pokemonHeroes;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Scene extends JPanel implements MouseListener, ActionListener {

    private int tiles = 100; //Number of tiles on board
    private int tileLength = 64; //Length of each tile
    private int queueTileLength = 100; //Length of each tile in the queue

    private ImageIcon tile = new ImageIcon("temp.jpg"); //The image with which a tile will be shown
    private Image tileImage = tile.getImage();

    private ImageIcon bgIcon = new ImageIcon("tempBG.png"); //Background image
    private Image bgImage = bgIcon.getImage();

    private Unit[] queue; //Turn queue

    private Tile[][] tilesArr = new Tile[(int)Math.sqrt(tiles)][(int)Math.sqrt(tiles)];

    private Trainer trainerOne, trainerTwo; //The two player's trainers

    private int speed = 1000; //Time in between ticks. Lower speed-->Things happen faster. Counted in milliseconds.

    public Scene(){
        JFrame frame = new JFrame(); //Opens up the game's frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Terminates app when closing window so that it doesn't run in the background
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Sets width and height of frame depending on screen size
        //frame.setUndecorated(true);    //Add this later. Removes borders allowing fullscreen.

        frame.add(this); //Adds panel to frame

        frame.addMouseListener(this); //Adds ability to change things with mouse

        frame.setVisible(true); //Makes frame visible

        trainerOne = new Trainer("Cynthia", true); //Creates first trainer. Temporary until player can choose
        trainerTwo = new Trainer("Cyrus", false); //Same as above

        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Wobbuffet", 1, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Xatu", 2, false));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Yanmega", 3, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Zapdos", 4, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Togekiss", 100, false));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Torterra", 6, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Toxicroak", 7, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Gyarados", 7, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Tyranitar", 9, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Ursaring", 10, true));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Vespiquen", 11, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Grumpig", 12, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Walrein", 13, true));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Empoleon", 7, false));

        queue = SceneFunctions.createQueue(trainerOne, trainerTwo); //Creates the queue according to player's teams

        queue[0].setX(0);queue[0].setY(0);
        queue[1].setX(0);queue[1].setY(1);
        queue[2].setX(0);queue[2].setY(2);
        queue[3].setX(0);queue[3].setY(3);
        queue[4].setX(0);queue[4].setY(4);
        queue[5].setX(0);queue[5].setY(5);
        queue[6].setX(0);queue[6].setY(6);
        queue[7].setX(9);queue[7].setY(0);
        queue[8].setX(9);queue[8].setY(1);
        queue[9].setX(9);queue[9].setY(2);
        queue[10].setX(9);queue[10].setY(3);
        queue[11].setX(9);queue[11].setY(4);
        queue[12].setX(9);queue[12].setY(5);
        queue[13].setX(9);queue[13].setY(6);

        Timer timer = new Timer(speed, this);
        timer.start();

    }

    protected void paintComponent(Graphics g){ //Default panel function that allows us to add stuff to the panel
        super.paintComponent(g);
        drawBattleground(g); //Functionized draw so that I can have it draw different stuff depending on the situation
    }

    protected void drawBattleground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int tileStart = (int) Math.round((getWidth() - tileLength * Math.sqrt(tiles) + Math.sqrt(tiles) * 5) / 2) - 50; //Starts drawing tiles closer to center instead of on the left side of the screen
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
        ImageIcon pokeIcon = new ImageIcon("PokePics/Zoomed/" + queue[0].getUnitName() + ".png");
        Image pokeImage = pokeIcon.getImage();
        BufferedImage pokeQueueImage = new BufferedImage(pokeImage.getWidth(null) / 2, pokeImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics pokeGraphics = pokeQueueImage.getGraphics();
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
                pokeIcon = new ImageIcon("PokePics/Zoomed/" + queue[i].getUnitName() + ".png");
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
                g.drawImage(tileImage, tileStart + j * tileLength + j * 5, 50 + i * tileLength + i * 5, tileLength, tileLength, this);
                tilesArr[j][i] = new Tile(j, i, tileStart + j * tileLength + j * 5, tileStart + j * tileLength + j * 5+tileLength, 80 + i * tileLength + i * 5, 80 + i * tileLength + i * 5+tileLength);
                for (int k = 0; k < 14; k++) {
                    if (queue[k].getX() == j && queue[k].getY() == i) {
                        pokeIcon = new ImageIcon("PokePics/Combat/" + queue[k].getUnitName() + ".png");
                        pokeImage = pokeIcon.getImage();
                        BufferedImage pokeFieldImage = new BufferedImage(pokeImage.getWidth(null) / 2, pokeImage.getHeight(null) / 4, BufferedImage.TYPE_INT_ARGB);
                        pokeGraphics = pokeFieldImage.getGraphics();
                        if (queue[k].isTeam())
                            pokeGraphics.drawImage(pokeImage, -32, -64, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
                        else
                            pokeGraphics.drawImage(pokeImage, -32, -32, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
                        pokeGraphics.dispose();
                        g2d.drawImage(pokeFieldImage, queue[k].getTileX(), queue[k].getTileY(), pokeFieldImage.getWidth() * 2, pokeFieldImage.getHeight() * 2, this);
                        //queue[k].setTileX(tileStart+j*tileLength+j*5);
                        //queue[k].setTileY(50+i*tileLength+i*5);
                    }
                }
            }
        }
    }

    public void turn(int x, int y){
        for (int i=0; i<tilesArr.length; i++){
            for (int j=0; j<tilesArr.length; j++){
                //System.out.println(e.getX()+" "+e.getY()+" "+ tilesArr[j][i].toString());
                if(SceneFunctions.inTile(x, y, tilesArr[j][i]) && !SceneFunctions.spotTaken(j, i, queue)) {
                    //System.out.println(tilesArr[j][i].getX() + " " + tilesArr[j][i].getY());
                    queue[0].setX(j);
                    queue[0].setY(i);
                    SceneFunctions.updateQueue(queue);
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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

    @Override
    public void actionPerformed(ActionEvent e){
        System.out.println("Test");
    }

}
