package pokemonHeroes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedWriter out;

    public Client(Scene scene) {
        try {
            String ip = "10.0.0.21";
            int port = 12345;
            //ip = JOptionPane.showInputDialog("Input server IP");

            socket = new Socket(ip, port);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            NetworkRead readThread = new NetworkRead(socket, scene);

            Thread read = new Thread(readThread);
            read.start();

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to server.");
            System.exit(1);
        }
    }


    //Send a message to the other player client
    public void send(int id, int x, int y, boolean quit, double damage) {
        String line; //Create the string we plan to send
        if (quit) //If we want to send a quit message then the string must be Bye so that the other clients and the server know to shut down too
            line = "Bye";
        else //If we're not quitting then we want to send the id of the sender and the coordinates of the click
            line = id + "&&" + x + "&&" + y;
        if (damage != 0) //We wanna make sure the damage is the same for both players since it's randomized. If damage is 0 then no attack was commenced
            line += "&&" + damage;
        System.out.println("ding"); //Debugging
        try { //We use try to make sure there's no crash in case of an error when sending the message
            out.write(line + "\n"); //Add the string to the stream
            out.flush(); //Sends the prepared message

        } catch (IOException e) {
            System.err.println("Couldn't read or write");
            System.exit(1);
        }
    }

    //Send a message to the Android client
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