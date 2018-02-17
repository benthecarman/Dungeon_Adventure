package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.network.AcceptFriendRequest;
import sa_b_2.coms309.dungeonadventure.network.DenyFriendRequest;

/**
 * Displays a screen message for when a friend is selected
 */
public class FriendRequestScreenMessage extends ScreenMessage {

    private final Button denyButton;
    private final String from;

    public FriendRequestScreenMessage(String username) {
        super(username + " wants to be friends!");
        from = username;
        rect.inset(rectF.width()/4,0);
        rect.offset(-rectF.width()/4,0);
        okayButton = new Button(rect,"Accept");
        rect.offset(rectF.width()/2,0);
        denyButton = new Button(rect,"Deny");
    }

    @Override
    public void draw(@NonNull Canvas canvas){
        super.draw(canvas);

        denyButton.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event){
        if (okayButton.onTouchEvent(event))
            return accept();
        else if(denyButton.onTouchEvent(event))
            return deny();
        return super.onTouchEvent(event);
    }

    private boolean accept(){
        return AcceptFriendRequest.acceptFriend(from);
    }

    private boolean deny(){
        return DenyFriendRequest.denyFriend(from);
    }
}
