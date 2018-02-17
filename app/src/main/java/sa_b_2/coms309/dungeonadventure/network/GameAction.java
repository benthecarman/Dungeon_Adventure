package sa_b_2.coms309.dungeonadventure.network;

import java.util.List;

import sa_b_2.coms309.dungeonadventure.game.Player;
import sa_b_2.coms309.dungeonadventure.game.Shot;

/**
 * Class that holds information to be send to the server containing the user's position and what shots they fired
 */

public class GameAction {

    private int playerID;
    private Player player;
    private List<Shot> shots;

    public GameAction(int playerID, Player player, List<Shot> shots) {
        this.playerID = playerID;
        this.player = player;
        this.shots = shots;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Shot> getShots() {
        return shots;
    }
}
