package sa_b_2.coms309.dungeonadventure.network;

import java.io.Serializable;

/**
 * Lobby object to be received from the server containing the users in the lobby and what characters they have selected
 */
public class Lobby implements Serializable {

    public int character0, character1;
    public String name0, name1;

    public Lobby() {
    }
}
