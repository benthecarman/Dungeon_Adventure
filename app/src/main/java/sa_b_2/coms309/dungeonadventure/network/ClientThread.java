package sa_b_2.coms309.dungeonadventure.network;

import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Creates a thread that connects to the game server
 */

public class ClientThread implements Runnable {

    private Lobby lobby = null;

    @Override
    public void run() {
        try {
            Socket socket = new Socket(InetAddress.getByName("10.25.70.25"), 9000);

            ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

            outToServer.writeObject("Hello");

            while (inFromServer.available() == 0) {
                Thread.sleep(10);
            }

            Log.d("Kappa", "received");
            lobby = (Lobby) inFromServer.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Lobby getLobby() {
        return lobby;
    }
}