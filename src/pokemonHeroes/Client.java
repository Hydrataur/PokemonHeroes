package pokemonHeroes;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * The client through which we connect to the server
 */
public class Client {

    /**
     * The socket through which we connect to the server
     * BufferedWriter which allows us to send messages to the server
     */
    private Socket socket;
    private BufferedWriter out;

    /**
     * Constructs a new client
     * @param scene The scene which we use to play in order to send messages to it
     */
    public Client(Scene scene) {
        try {
            String ip = "";
            while(ip.length() == 0 || ip == null)
                ip = JOptionPane.showInputDialog("Please enter a valid IP address");
            int port = 12345;

            socket = new Socket(ip, port); //Create the socket out of our ip and port
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //Create the BufferedWriter so that we can send messages
            NetworkRead readThread = new NetworkRead(socket, scene); //Create a NetworkRead so that we can get messages from the server

            Thread read = new Thread(readThread); //The thread through which we get messages from the server
            read.start();

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to server.");
            System.exit(1);
        }
    }


    /**
     *Sends a message to the other player client
     * @param id The sender's id to differentiate between the players
     * @param x The X location of the click
     * @param y The Y location of the click
     * @param quit Whether the player decided to quit or not
     * @param damage If an attack was made then this is the amount of damage dealt. Must be sent since there is randomness in the calculation.
     */
    public void send(int id, int x, int y, boolean quit, double damage) {
        String line; //Create the string we plan to send
        if (quit) //If we want to send a quit message then the string must be Bye so that the other clients and the server know to shut down too
            line = "Bye";
        else //If we're not quitting then we want to send the id of the sender and the coordinates of the click
            line = id + "&&" + x + "&&" + y;
        line += "&&" + damage; //We wanna make sure the damage is the same for both players since it's randomized. If damage is 0 then no attack was commenced
        System.out.println("ding"); //Debugging
        try { //We use try to make sure there's no crash in case of an error when sending the message
            out.write(line + "\n"); //Add the string to the stream
            out.flush(); //Sends the prepared message

        } catch (IOException e) {
            System.err.println("Couldn't read or write");
            System.exit(1);
        }
    }

    /**
     * Send a message to the Android client with a units stats so that they can be displayed
     * @param name Unit's name
     * @param hp Unit's current HP
     * @param maxhp Unit's maximum HP
     * @param attack Unit's attack power
     * @param defense Unit's defensive ability
     * @param mindamage Min damage a unit can deal on attacking
     * @param maxdamage Max damage a unit can deal on attacking
     * @param movement How far a unit can move per turn
     * @param fly Whether the unit can fly
     * @param ranged Whether the unit has ranged attacks
     */
    public void sendAndroid(String name, int hp, int maxhp, int attack

            , int defense, int mindamage, int maxdamage, int movement, boolean fly, boolean ranged){
        String line; //Create the string we plan to send
        line = "Android&&" + name.toLowerCase() + "&&" + hp + "&&" + maxhp + "&&" + attack + "&&" + defense
                + "&&" + mindamage + "&&" + maxdamage + "&&" + movement + "&&" + fly + "&&" + ranged; //Give the string all the info we're sending
        try { //We use try to make sure there's no crash in case of an error when sending the message
            out.write(line + "\n"); //Add the string to the stream
            out.flush(); //Sends the prepared message
        }catch (IOException e){
            System.err.println("Couldn't read or write");
            System.exit(1);
        }
    }
}