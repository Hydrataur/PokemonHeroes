package pokemonHeroes;

import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

public class Client {
    Socket socket;
    BufferedWriter out;

    public Client(Scene scene) {
        try {
            String ip = "localhost";
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


    public void send(int x, int y, boolean quit) {
        String line;
        if (quit)
            line = "Bye";
        else
            line = x + "&&" + y;
        System.out.println("ding");
        try {
            out.write(line + "\n");
            out.flush();

        } catch (IOException e) {
            System.err.println("Couldn't read or write");
            System.exit(1);
        }
    }
}