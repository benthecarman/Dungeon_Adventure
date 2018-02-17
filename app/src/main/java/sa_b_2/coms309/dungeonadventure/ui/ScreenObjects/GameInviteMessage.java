package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;


import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.network.RemoveGameInvite;
import sa_b_2.coms309.dungeonadventure.ui.Scenes.LobbyScene;
import sa_b_2.coms309.dungeonadventure.ui.Scenes.SceneList;
import sa_b_2.coms309.dungeonadventure.ui.Scenes.SceneManager;

/**
 * A screen message for the current user showing a game invite
 */
public class GameInviteMessage extends ScreenMessage {

    private final Button denyButton;
    private final String username;

    public GameInviteMessage(String username) {
        super(username + "invited you to a game!");

        this.username = username;
        rect.inset(rectF.width() / 4, 0);
        rect.offset(-rectF.width() / 4, 0);
        okayButton = new Button(rect, "Accept");
        rect.offset(rectF.width() / 2, 0);
        denyButton = new Button(rect, "Deny");
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        denyButton.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (okayButton.onTouchEvent(event)) {
            Constants.sceneManager.scenes.set(SceneList.Lobby.ordinal(), new LobbyScene(Constants.sceneManager, false));
            Constants.sceneManager.scenes.set(SceneManager.ACTIVESCENE.ordinal(), null);
            SceneManager.ACTIVESCENE = SceneList.Lobby;
            RemoveGameInvite.removeGameInvite(username);
            return true;
        } else if (denyButton.onTouchEvent(event)) {
            RemoveGameInvite.removeGameInvite(username);
            return true;
        }
        return super.onTouchEvent(event);
    }
}
