package pokemonHeroes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This class allows us to get and read messages from the server
 * Implements runnable to access the run function
 */
class NetworkRead implements Runnable{
    /**
     * Has the socket through which we've connected to the server
     * Has the scene so that we can send it the messages
     */
    Socket socket;
    Scene scene;

    /**
     * Constructor of NetworkRead
     * @param socket The socket which is connected to the server
     * @param scene The game's scene
     */
    public NetworkRead(Socket socket, Scene scene){
        this.socket=socket;
        this.scene=scene;
    }

    /**
     * A function that constantly runs and checks if we've received a message from the server.
     */
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