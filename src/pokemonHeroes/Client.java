package pokemonHeroes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedWriter out;

    public Client(Scene scene){
        try {
            socket = new Socket("localhost", 123456);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            NetworkRead readThread = new NetworkRead(socket, scene);
            Thread read = new Thread(readThread);
            read.start();
        }
        catch (IOException e){
            System.err.println("Couldn't get I/O for the connection to server.");
            System.exit(1);
        }
    }

    public void send(String line){
        try{
            out.write(line+ " \n");
            out.flush();
        }
        catch (IOException e){
            System.err.println("Couldn't read or write");
            System.exit(1);
        }
    }

}
