package pokemonHeroes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class NetworkRead implements Runnable{

    private Socket socket;
    private Scene scene;

    public NetworkRead(Socket socket, Scene scene){
        this.socket = socket;
        this.scene = scene;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverString;

            while ((serverString=in.readLine())!=null){
                System.out.println("serverString= "+serverString);

                if (serverString.startsWith("Bye!")){
                    //scene.endGame(1); Need to add function for quitting/finishing game
                    break;
                }
                else {
                    if (serverString.startsWith("Bye from opponent")){
                        //scene.endGame(2); Add function for when opponent has quit the game
                        break;
                    }
                    else {
                        //scene.fresh(serverString); Add function to get string from server
                    }
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Disconnected from Server");
        System.exit(0);
    }
}
