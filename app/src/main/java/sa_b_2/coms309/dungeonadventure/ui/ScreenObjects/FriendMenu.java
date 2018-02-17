package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.network.InviteToGame;
import sa_b_2.coms309.dungeonadventure.network.RemoveFriend;
import sa_b_2.coms309.dungeonadventure.ui.Scenes.LobbyScene;
import sa_b_2.coms309.dungeonadventure.ui.Scenes.SceneList;
import sa_b_2.coms309.dungeonadventure.ui.Scenes.SceneManager;

/**
 * Menu Display for showing options on a particular friend
 */
public class FriendMenu extends ScreenMessage {

    private String username;
    private Button inviteButton;

    FriendMenu(String username) {
        super(username);
        this.username = username;

        rect.inset(rectF.width() / 4, 0);
        rect.offset(-rectF.width() / 4, 0);
        inviteButton = new Button(rect, "Invite to Game");
        rect.offset(rectF.width() / 2, 0);
        okayButton = new Button(rect, "Remove Friend");
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (okayButton.onTouchEvent(event)) {
            RemoveFriend.removeFriend(username);
            return true;
        } else if (inviteButton.onTouchEvent(event)) {
            InviteToGame.inviteToGame(username);

            Constants.sceneManager.scenes.set(SceneList.Lobby.ordinal(), new LobbyScene(Constants.sceneManager, true));
            SceneManager.ACTIVESCENE = SceneList.Lobby;
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        inviteButton.draw(canvas);
    }
}
