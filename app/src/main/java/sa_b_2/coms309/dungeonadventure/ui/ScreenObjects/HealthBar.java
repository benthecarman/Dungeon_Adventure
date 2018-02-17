package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.game.Character;

import static sa_b_2.coms309.dungeonadventure.game.Constants.SCREENHEIGHT;
import static sa_b_2.coms309.dungeonadventure.game.Constants.SCREENWIDTH;

/**
 * Health bar to show the current health of a player, enemy, or boss
 */
public class HealthBar implements ScreenObject {

    private final Character character;
    private final HealthBarType type;
    private double percent = 1;

    public HealthBar(Character character, HealthBarType type) {
        this.character = character;
        this.type = type;
        update();
    }

    public void update() {
        percent = character.getCurrentHealth() / character.getHealth();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //Determines which type of health bar to draw
        switch (type) {
            case Player:
                playerDraw(canvas);
                break;
            case Enemy:
                enemyDraw(canvas);
                break;
            case Boss:
                bossDraw(canvas);
                break;
        }
    }

    //Draws health bar for player
    private void playerDraw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        int x = SCREENWIDTH / 45;
        int y = SCREENHEIGHT / 45;
        RectF rect = new RectF(x, y, x + SCREENWIDTH / 4, y * 3);
        canvas.drawRect(rect, paint);
        paint.setColor(Color.RED);
        rect = new RectF(rect.left, rect.top, x + (float) ((SCREENWIDTH / 4) * percent), rect.bottom);
        rect.inset(rect.height() / 7, rect.height() / 7);
        canvas.drawRect(rect, paint);
    }

    //Draws health bar over enemy
    private void enemyDraw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        RectF draw = character.getDrawRect();
        int buffer = SCREENHEIGHT / 45;

        RectF rect = new RectF(draw.left, draw.top - (buffer * 2), draw.right, draw.top - buffer);
        canvas.drawRect(rect, paint);

        paint.setColor(Color.RED);
        rect = new RectF(rect.left, rect.top, rect.left + (float) (rect.width() * percent), rect.bottom);

        canvas.drawRect(rect, paint);
    }

    //Draws health bar for boss
    private void bossDraw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        int x = SCREENWIDTH / 3;
        int y = SCREENHEIGHT - (SCREENHEIGHT / 15) - SCREENHEIGHT / 45;
        RectF rect = new RectF(x, y, x + SCREENWIDTH / 3, y + SCREENHEIGHT / 15);
        canvas.drawRect(rect, paint);
        paint.setColor(Color.RED);
        rect = new RectF(rect.left, rect.top, x + (float) ((SCREENWIDTH / 3) * percent), rect.bottom);
        rect.inset(rect.height() / 7, rect.height() / 7);
        canvas.drawRect(rect, paint);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return false;
    }

    public enum HealthBarType {
        Player,
        Enemy,
        Boss
    }
}
