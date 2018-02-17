package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * Same as a button except it is red when turned off
 */

public class SettingsButton extends Button {

    private boolean on;

    public SettingsButton(@NonNull Rect rect, String text, boolean on) {
        super(rect, text);
        this.on = on;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint paint = new Paint();

        //Sets Color
        if (on) {
            paint.setARGB(255, 153, 255, 51);
            if (down)
                paint.setARGB(255, 102, 204, 0);
        } else {
            paint.setARGB(255, 204, 0, 0);
            if (down)
                paint.setARGB(255, 153, 0, 0);
        }
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

    public boolean getOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
