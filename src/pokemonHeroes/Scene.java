package pokemonHeroes;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Scene extends JPanel{

    private int tiles = 100; //Number of tiles on board
    private int tileLength = 64; //Length of each tile
    private int queueTileLength = 100; //Length of each tile in the queue

    private ImageIcon tile = new ImageIcon("temp.jpg"); //The image with which a tile will be shown
    private Image tileImage = tile.getImage();

    private ImageIcon bgIcon = new ImageIcon("tempBG.png"); //Background image
    private Image bgImage = bgIcon.getImage();

    private Unit[] queue; //Turn queue

    public static void main(String[] args){
        JFrame frame = new JFrame(); //Opens up the game's frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Terminates app when closing window so that it doesn't run in the background
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Sets width and height of frame depending on screen size
        //frame.setUndecorated(true);    //Add this later. Removes borders allowing fullscreen.

        Scene scene = new Scene(); //Panel we draw on

        frame.add(scene); //Adds panel to frame

        frame.setVisible(true); //Makes frame visible
    }

    protected void paintComponent(Graphics g){ //Default panel function that allows us to add stuff to the panel
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        int tileStart = (int)Math.round((getWidth()-tileLength*Math.sqrt(tiles)+Math.sqrt(tiles)*5)/2)-50; //Starts drawing tiles closer to center instead of on the left side of the screen
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this); //Draw background
        for(int i = 0; i<Math.sqrt(tiles); i++){ //Draw tiles
            for(int j = 0; j<Math.sqrt(tiles); j++){
                g.drawImage(tileImage, tileStart+j*tileLength+j*5, 50+i*tileLength+i*5, tileLength, tileLength, this);
            }
        }

        Trainer trainerOne = new Trainer("Cynthia", true); //Creates first trainer. Temporary until player can choose
        Trainer trainerTwo = new Trainer("Cyrus", false); //Same as above

        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); //All pictures are by default facing left. This flips them.
        tx.translate(-trainerOne.getTrainerImage().getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        BufferedImage trainerOneImage = new BufferedImage(trainerOne.getTrainerImage().getWidth(null), trainerOne.getTrainerImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics trainerGraphics = trainerOneImage.getGraphics();
        trainerGraphics.drawImage(trainerOne.getTrainerImage(), 0, 0, trainerOne.getTrainerImage().getWidth(null), trainerOne.getTrainerImage().getHeight(null), this);
        trainerOneImage = op.filter(trainerOneImage, null); //Flips image
        trainerGraphics.dispose();

        g2d.drawImage(trainerOneImage, 200, getHeight()/2, 100, 200, this); //Draw first trainer
        g2d.drawImage(trainerTwo.getTrainerImage(), getWidth()-200, getHeight()/2, 100, 200, this); //Draw second trainer

        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Wobbuffet", 1, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Xatu", 2, false));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Yanmega", 3, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Zapdos", 4, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Togekiss", 100, false));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Torterra", 6, true));
        trainerOne.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Toxicroak", 7, false));
        trainerTwo.addUnit(null);
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Tyranitar", 9, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Ursaring", 10, true));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Vespiquen", 11, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Wailord", 12, false));
        trainerTwo.addUnit(new Unit(0, 0, 0, 0, 0, 0, false, 0, "Walrein", 13, true));
        trainerTwo.addUnit(null);

        queue = SceneFunctions.createQueue(trainerOne, trainerTwo);

        g.setColor(Color.ORANGE);
        g.fillRect(20, getHeight()-(int)(1.5*queueTileLength), (int)(1.5*queueTileLength), (int)(1.5*queueTileLength));
        ImageIcon pokeIcon = new ImageIcon("PokePics/Zoomed/"+queue[0].getUnitName()+".png");
        Image pokeImage = pokeIcon.getImage();
        BufferedImage pokeQueueImage = new BufferedImage(pokeImage.getWidth(null)/2, pokeImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics pokeGraphics = pokeQueueImage.getGraphics();
        pokeGraphics.drawImage(pokeImage, 0, 0, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
        pokeGraphics.dispose();
        g2d.drawImage(pokeQueueImage, 20, getHeight()-(int)(1.5*queueTileLength), (int)(1.5*queueTileLength), (int)(1.5*queueTileLength), this);
        for(int i=1; i<14; i++) {
            if (queue[i] != null) {
                if(queue[i].isTeam())
                    g.setColor(Color.BLUE);
                else
                    g.setColor(Color.RED);
                g.fillRect((i+1)*queueTileLength, getHeight()-queueTileLength, queueTileLength, queueTileLength);
                pokeIcon = new ImageIcon("PokePics/Zoomed/"+queue[i].getUnitName()+".png");
                pokeImage = pokeIcon.getImage();
                pokeQueueImage = new BufferedImage(pokeImage.getWidth(null)/2, pokeImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                pokeGraphics = pokeQueueImage.getGraphics();
                pokeGraphics.drawImage(pokeImage, 0, 0, pokeImage.getWidth(null), pokeImage.getHeight(null), this);
                pokeGraphics.dispose();
                g2d.drawImage(pokeQueueImage, (i+1)*queueTileLength, getHeight()-queueTileLength, queueTileLength, queueTileLength, this);
            }
        }

    }

}
