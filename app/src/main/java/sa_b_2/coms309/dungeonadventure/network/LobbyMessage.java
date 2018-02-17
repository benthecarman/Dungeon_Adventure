package sa_b_2.coms309.dungeonadventure.network;

import java.io.Serializable;

/**
 * Lobby object to be sent to the server containing the user in the lobby and what character they have selected
 */

public class LobbyMessage implements Serializable {

    private String username;
    private int character;
    private int player;

    public LobbyMessage(String username, int character, int player) {
        this.username = username;
        this.character = character;
        this.player = player;
    }

    public String getUsername() {
        return username;
    }

    public int getCharacter() {
        return character;
    }

    public int getPlayer() {
        return player;
    }
}