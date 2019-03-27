package pokemonHeroes;

import java.awt.Color;
import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

class NetworkRead implements Runnable{
    Socket socket;
    Scene scene;

    public NetworkRead(Socket socket, Scene scene){
        this.socket=socket;
        this.scene=scene;
    }

    public void run(){
        try{

            BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream()));
            String serverString;

            while ((serverString=in.readLine())!=null)
            {
                System.out.println("serverString=" + serverString);

                if(serverString.startsWith("Bye")) {

                    scene.endGame();
                    break;
                }
                else
                    scene.update(serverString);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Disconnected from server");
        System.exit(0);
    }
}