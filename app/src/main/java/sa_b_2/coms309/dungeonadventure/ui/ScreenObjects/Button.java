package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * Button class that creates an onscreen button
 */

public class Button implements ScreenObject {

    @NonNull
    final RectF rect;
    String text;
    boolean down = false;

    /**
     * Creates a button that is within rect, and displays the text
     *
     * @param rect boundaries of the button
     * @param text text labeling the button
     */
    public Button(@NonNull Rect rect, String text) {
        rect.sort();
        this.rect = new RectF(rect.left, rect.top, rect.right, rect.bottom);
        this.text = text;
    }

    /**
     * Creates a button that is within rect, and displays the text
     *
     * @param rect boundaries of the button
     * @param text text labeling the button
     */
    public Button(@NonNull RectF rect, String text) {
        rect.sort();
        this.rect = new RectF(rect.left, rect.top, rect.right, rect.bottom);
        this.text = text;
    }

    /**
     * Changes the text of the button
     *
     * @param text New text on the button
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint paint = new Paint();

        //Sets Color
        if (down)
            paint.setARGB(255, 102, 204, 0);
        else
            paint.setARGB(255, 153, 255, 51);

        //Draws button
        canvas.drawRoundRect(new RectF(rect), rect.width(), rect.height() * 7, paint);

        if (text == null)
            return;

        //Draws text
        paint = new Paint();
        float textSize = (float) (rect.height() * .35);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Constants.font);
        canvas.drawText(text, rect.centerX(), rect.centerY() + (float) (textSize / 2.0), paint);
    }

    /**
     * Handles the button being pressed, returns if the button was pressed
     *
     * @param event Motion event from android api
     * @return if the button was pressed
     */
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        if (rect.contains((int) event.getX(), (int) event.getY())) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_POINTER_DOWN)
                down = true;
            else if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) && down) {
                down = false;
                return true;
            }
        } else
            down = false;
        return false;
    }

    @NonNull
    public RectF getRect() {
        return new RectF(rect);
    }

    public boolean isDown(){
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
