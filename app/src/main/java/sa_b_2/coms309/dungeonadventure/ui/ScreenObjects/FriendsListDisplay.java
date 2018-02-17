package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.List;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.user.Friend;

/**
 * Displays all friends of the current user
 */
public class FriendsListDisplay implements ScreenObject {

    private final RectF rectF;
    private final int numSegments;
    private final float startHeight;
    private List<Friend> list;
    private int spot = 0;
    private float initial = 0;
    private boolean scrolling = false;

    public FriendsListDisplay(RectF rectF, List<Friend> list, int numSegments, float startHeight) {
        this.rectF = rectF;
        this.list = list;
        this.numSegments = numSegments;
        this.startHeight = startHeight;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        float segmentSize = (rectF.height() - startHeight) / numSegments;

        if (rectF.contains(event.getX(), event.getY())) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                initial = event.getY();
                scrolling = false;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                //Scrolling
                if (initial > event.getY() + segmentSize / 2) {
                    spot = ++spot >= list.size() - numSegments ? list.size() - numSegments : spot;
                    initial = event.getY();
                    scrolling = true;
                } else if (initial < event.getY() - segmentSize / 2) {
                    spot = --spot < 0 ? 0 : spot;
                    initial = event.getY();
                    scrolling = true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!scrolling) {
                    //Determines which friend you are touching
                    float y = event.getY() - (rectF.top + startHeight);

                    int x = (int) (y / segmentSize);

                    try {
                        Constants.screenMessages.add(new FriendMenu(list.get(spot + x).getUsername()));
                    } catch (Exception ignored) {
                    }
                }
                initial = 0;
                scrolling = false;
            }

            return true;
        }

        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //Draws background
        Paint paint = new Paint();
        //paint.setARGB(255, 153, 255, 51);
        paint.setColor(Color.CYAN);
        canvas.drawRect(rectF, paint);

        //Draws Segments
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(7);
        float height = rectF.height() - startHeight;
        int i = spot;
        float buffer;
        for (buffer = startHeight + height / numSegments; buffer <= rectF.height() + 1; buffer += height / numSegments) {
            canvas.drawLine(rectF.left, rectF.top + buffer, rectF.right, rectF.top + buffer, paint);
            if (i < list.size()) {
                //Draw Friend's name and online status
                float b = buffer + height / numSegments;
                Paint paint1 = new Paint();
                paint1.setTypeface(Constants.font);
                paint1.setTextAlign(Paint.Align.LEFT);
                paint1.setTextSize(rectF.height() / (numSegments * 4));
                Friend friend = list.get(i++);
                if (!friend.isOnline())
                    paint1.setColor(Color.RED);
                else
                    paint1.setColor(Color.BLACK);
                canvas.drawText(friend.getUsername(), rectF.left + rectF.width() / 10, b - (height / (numSegments * 2)), paint1);
                paint1.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(friend.isOnline() ? "Online" : "Offline", rectF.right - rectF.width() / 10, b - (height / (numSegments * 2)), paint1);
            }
        }
    }

    public void setList(List<Friend> list) {
        this.list = list;
    }

    /**
     * Resets the friends list for when being displayed next time
     */
    public void reset() {
        spot = 0;
        initial = 0;
    }
}
