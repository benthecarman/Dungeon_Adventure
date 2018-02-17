package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawX;
import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawY;

/**
 * Created by shiqig on 9/28/17.
 */
public class Stairs implements GameObject {

    private PointF location;
    private int size = 60;
    private RectF rect;

    /**
     * Constructs a stairs get to next floor
     */
    public Stairs() {
        location = new PointF(Constants.GAMEWIDTH / 2, Constants.GAMEHEIGHT / 2);
        rect = new RectF(location.x - size, location.y - size, location.x + size, location.y + size);
    }

    /**
     * Returns true if player collides with stairs
     * @return true if intersects, else false
     */
    public boolean playerCollide(Player player) {
        return RectF.intersects(new RectF(this.rect), player.getRect());
    }

    /**
     * Draws the stairs
     * @param c canvas to be drawn on
     */
    @Override
    public void draw(Canvas c) {
        RectF drawRect = new RectF(gameToDrawX(rect.left), gameToDrawY(rect.top), gameToDrawX(rect.right), gameToDrawY(rect.bottom));
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        c.drawRect(drawRect, paint);
    }

    /**
     * not used
     */
    @Override
    public void update() {

    }

    /**
     * Returns stairs location
     * @return location of stairs
     */
    @Override
    public PointF getLocation() {
        return location;
    }
}