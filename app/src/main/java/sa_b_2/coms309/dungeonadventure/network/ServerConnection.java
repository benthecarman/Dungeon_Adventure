package sa_b_2.coms309.dungeonadventure.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

/**
 * Creates a connection to the game server
 */
public class ServerConnection {

    public static final int GAME_PORT = 9000;
    public static final int LOBBY_PORT = 9000;

    private int port;
    private Object objectToSend = null;

    /**
     * @param port Which port to connect to the server
     */
    public ServerConnection(int port) {
        this.port = port;
    }

    /**
     * Sends object to the server
     *
     * @param o Object to be sent
     */
    public void sendObject(Object o) {
        objectToSend = o;
        new Sender().execute(0);
    }

    /**
     * Receives object from the server
     * @return object received
     */
    public Object receiveObject() {
        try {
            return new Sender().execute(1).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Sender extends AsyncTask<Integer, Void, Object> {

        @Override
        protected Object doInBackground(Integer... integers) {
            try {
                Socket socket = new Socket(InetAddress.getByName("10.64.89.249"), port);
                //Socket socket = new Socket(InetAddress.getByName("10.25.70.25"), port);

                String is = socket.isConnected() ? "" : "Not ";
                Log.d("Server", is + "Connected");

                Object o = null;

                ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

                if (integers[0] == 0) {
                    outToServer.writeObject(objectToSend);
                    objectToSend = null;
                } else if (integers[0] == 1) {
                    if (inFromServer.available() > 0)
                        o = inFromServer.readObject();
                }

                inFromServer.close();
                outToServer.close();
                inFromServer.close();

                return o;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
