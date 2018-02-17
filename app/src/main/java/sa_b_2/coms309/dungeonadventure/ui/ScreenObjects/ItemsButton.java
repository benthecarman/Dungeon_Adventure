package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.R;
import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * For item button to enter inventory menu
 */
public class ItemsButton implements ScreenObject {

    private final RectF rectF;
    private final Bitmap image;
    private final Paint paint;

    /**
     * creates button
     *
     * @param size size of button
     */
    public ItemsButton(short size) {
        rectF = new RectF(Constants.SCREENWIDTH - size * 4, 0, Constants.SCREENWIDTH - size * 2, size * 2);
        image = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.itembutton);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Checks if user presses button
     * @param event event object for detecting user manipulation
     * @return true if user presses button, else false
     */
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        for (short i = 0; i < event.getPointerCount(); ++i)
            if (rectF.contains(event.getX(i), event.getY(i))) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
                    return true;
            }

        return false;
    }

    /**
     * Draws the button
     * @param c canvas to be drawn to
     */
    @Override
    public void draw(@NonNull Canvas c) {
        c.drawBitmap(image, null, rectF, paint);
    }
}