package pokemonHeroes;

import javax.swing.*;
import java.awt.*;

public class Game {

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame(); //Opens up the game's frame
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Terminates app when closing window so that it doesn't run in the background
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Sets width and height of frame depending on screen size
                //frame.setUndecorated(true);    //Add this later. Removes borders allowing fullscreen.

                Scene scene = new Scene(); //Panel we draw on

                frame.add(scene);

                frame.setVisible(true); //Makes frame visible
            }
        });

    }

}
