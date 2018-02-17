package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * Display for the lobby that shows your current friends
 */
public class FriendOverlay implements ScreenObject {

    private final Button cancelButton;
    private final FriendsListDisplay friendsListDisplay;

    public FriendOverlay() {
        friendsListDisplay = new FriendsListDisplay(new RectF(Constants.SCREENWIDTH / 10, Constants.SCREENHEIGHT / 10, Constants.SCREENWIDTH - Constants.SCREENWIDTH / 10, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 9), Constants.currentUser.getFriends(), 7, 0);

        float buttonWidth = Constants.SCREENWIDTH / 12;
        float buttonHeight = Constants.SCREENHEIGHT / 25;

        cancelButton = new Button(new RectF(Constants.SCREENWIDTH / 2 - buttonWidth, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENWIDTH / 2 + buttonWidth, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 + buttonHeight), "Close");
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        friendsListDisplay.onTouchEvent(event);

        return cancelButton.onTouchEvent(event);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        friendsListDisplay.draw(canvas);
        cancelButton.draw(canvas);
    }
}