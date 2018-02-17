package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * Slider for users to change settings
 */
public class Slider implements ScreenObject {

    @NonNull
    private final RectF rect;
    private final String text;
    private int percent;
    private boolean down = false;

    public Slider(@NonNull Rect rect, String text, int start) {
        rect.sort();
        this.rect = new RectF(rect.left, rect.top, rect.right, rect.bottom);
        this.percent = start;
        this.text = text;
    }

    public void draw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setARGB(255, 153, 255, 51);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(rect), rect.width(), rect.height() * 7, paint);

        //Draws background
        paint = new Paint();
        paint.setARGB(255, 102, 204, 0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) ((rect.height()) / 10.0));
        canvas.drawRoundRect(new RectF(rect), rect.width(), rect.height() * 7, paint);

        //Draws slider
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        double padding = ((rect.height()) / 2.5) + (rect.height()) / 10.0;
        canvas.drawCircle((float) (rect.left + padding + (rect.width() - padding * 2) * percent / 100.0), rect.centerY(), (float) ((rect.height()) / 2.5), paint);

        //Draws Text
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.RIGHT);
        float textSize = (float) (rect.height() * .35);
        paint.setTextSize(textSize);
        paint.setTypeface(Constants.font);
        canvas.drawText(text, rect.left - rect.width() / 10, rect.centerY() + (float) (textSize / 2.0), paint);

    }

    public int getSpot() {
        return percent;
    }

    public void setSpot(int spot) {
        percent = spot;
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {

        double padding = ((rect.height()) / 2.5) + (rect.height()) / 10.0;

        RectF containsRect = new RectF(rect.left - rect.width() / 3, rect.top, rect.right + rect.width() / 3, rect.bottom);
        if (containsRect.contains((int) event.getX(), (int) event.getY())) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_POINTER_DOWN) {
                down = true;

                percent = (int) (((-rect.left + event.getX() + padding) / (rect.width() + padding * 2)) * 100);
                if (percent > 100)
                    percent = 100;
                else if (percent < 0)
                    percent = 0;
            } else if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) && down) {
                down = false;
                return true;
            }
        } else
            down = false;
        return false;
    }
}
