package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * Gives a dialogue for the current user
 */
public class ScreenMessage implements ScreenObject {

    @NonNull
    final RectF rectF, rect;
    @NonNull
    private final List<String> messages;
    private final Button xButton;
    @NonNull
    Button okayButton;

    public ScreenMessage(String message) {
        if (message == null)
            message = "";

        messages = new LinkedList<>();
        String[] strings = message.split(" ");

        //Separates the message into different lines
        String s = strings[0];
        for (int i = 1; i < strings.length; ++i) {
            if (s.concat(" " + strings[i]).length() > 22) {
                messages.add(s);
                s = "";
                --i;
            } else
                s = s.concat(" " + strings[i]);
        }
        if (messages.size() == 0 || !messages.get(messages.size() - 1).equals(s))
            messages.add(s);

        //Sets buttons
        rectF = new RectF(Constants.SCREENWIDTH / 4, Constants.SCREENHEIGHT / 4, Constants.SCREENWIDTH - (Constants.SCREENWIDTH / 4), Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 4);
        rect = new RectF((float) (rectF.left + (rectF.width() * .9)), rectF.top + rectF.height() - (rectF.height() / 6), (float) (rectF.right - (rectF.width() * .9)), rectF.top + rectF.height() - (rectF.height() / 3));
        okayButton = new Button(rect, "Okay");
        float width = (float) (rectF.width() / 10.0);
        RectF r = new RectF(rectF.right - (width * (float) (3 / 4.0)), rectF.top - width / 4, rectF.right + width / 4, rectF.top + (width * (float) (3 / 4.0)));
        xButton = new Button(r, "X");
    }

    public void draw(@NonNull Canvas canvas) {
        //Draws bg
        Paint paint = new Paint();
        paint.setARGB(240, 216, 142, 67);
        canvas.drawRoundRect(rectF, Constants.SCREENWIDTH / 20, Constants.SCREENHEIGHT / 20, paint);

        paint.setTypeface(Constants.font);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        float textSize = rectF.height() / (10);
        paint.setTextSize(textSize);

        float height = (rect.top + rectF.height() / 10) - rectF.top;

        //Draws message
        for (int i = 0; i < messages.size(); ++i)
            canvas.drawText(messages.get(i), rectF.centerX(), (float) ((rectF.top + height / 2.0) + i * (textSize * 1.5)), paint);

        //Draws buttons
        okayButton.draw(canvas);
        xButton.draw(canvas);
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return okayButton.onTouchEvent(event) || xButton.onTouchEvent(event);
    }
}
