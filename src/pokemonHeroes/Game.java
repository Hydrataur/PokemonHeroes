package pokemonHeroes;

import javax.swing.*;
import java.awt.*;

/**
 * This is the main class of the game which creates the frame
 */
public class Game {

    /**
     * The JFrame we'll be drawing our Scene on
     */
    private static JFrame frame;

    /**
     * function that creates the frame and a Scene which starts the game
     * @param args
     */
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() { //Delays functions in order to allow for everything to load
            @Override
            public void run() {
                frame = new JFrame(); //Opens up the game's frame
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Terminates app when closing window so that it doesn't run in the background
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Sets width and height of frame depending on screen size
                //frame.setUndecorated(true);    //Add this later. Removes borders allowing fullscreen.

                Scene scene = new Scene(); //Panel we draw on

                frame.add(scene); //Insert our scene into the frame

                frame.setVisible(true); //Makes frame visible
            }
        });

    }

    /**
     * Allows us to change the title of the frame
     * @param id
     */
    public static void setTitle(int id){
        frame.setTitle("Player ID = " + id);
    }

}
