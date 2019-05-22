package pokemonHeroes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class NetworkRead implements Runnable{
    Socket socket;
    Scene scene;

    public NetworkRead(Socket socket, Scene scene){
        this.socket=socket;
        this.scene=scene;
    }

    public void run(){
        try{

            BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream())); //What we use to get messages from the server
            String serverString; //Create the string we'll be using

            while ((serverString=in.readLine())!=null) //Stay in the while so long as we don't get a null message
            {
                System.out.println("serverString=" + serverString); //For debugging

                if(serverString.startsWith("Bye")) { //If the message has bye it means we're quitting

                    scene.endGame(); //Go to end game function where the game is shut down
                    break;
                }
                else //If we're not quitting then send the message to scene where the variables we get will be used
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