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
            String ip = "192.168.175.244";
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


    public void send(int id, int x, int y, boolean quit, double damage) {
        String line;
        if (quit)
            line = "Bye";
        else
            line = id + "&&" + x + "&&" + y;
        if (damage != 0)
            line += "&&" + damage;
        System.out.println("ding");
        try {
            out.write(line + "\n");
            out.flush();

        } catch (IOException e) {
            System.err.println("Couldn't read or write");
            System.exit(1);
        }
    }

    public void sendAndroid(String name, int hp, int maxhp, int attack

            , int defense, int mindamage, int maxdamage, int movement, boolean fly, boolean ranged){
        String line;
        line = "Android&&" + name.toLowerCase() + "&&" + hp + "&&" + maxhp + "&&" + attack + "&&" + defense
                + "&&" + mindamage + "&&" + maxdamage + "&&" + movement + "&&" + fly + "&&" + ranged;
        try {
            out.write(line + "\n");
            out.flush();
        }catch (IOException e){
            System.err.println("Couldn't read or write");
            System.exit(1);
        }
    }
}