package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawX;
import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawY;

/**
 * An orbital is an item that rotates around the character
 */
public class Orbital implements GameObject {

    private final int spotDuration = 1250;// 1.25 seconds in milliseconds
    private long lastChange = 0;
    private Direction currentSpot = Direction.NORTH;
    /**
     * Character being orbited
     */
    private Character character;
    private RectF rect;
    private boolean backwards = false;

    /**
     * Creates an orbital
     *
     * @param character character to orbited
     */
    Orbital(Character character) {
        this.character = character;
        doRect();
    }

    /**
     * Creates an orbital
     *
     * @param character character to orbited
     * @param backwards if the orbital goes around backwards
     */
    Orbital(Character character, boolean backwards) {
        this.character = character;
        this.backwards = backwards;
        doRect();
    }

    private void doRect() {
        RectF charRect = character.getRect();
        float buffer = character.getSize() / 2;
        switch (currentSpot) {
            case NORTH:
                doHardRect(charRect, Math.toRadians(90));
                break;
            case NORTHEAST:
                doHardRect(charRect, Math.toRadians(135));
                break;
            case EAST:
                doHardRect(charRect, Math.toRadians(180));
                break;
            case SOUTHEAST:
                doHardRect(charRect, Math.toRadians(225));
                break;
            case SOUTH:
                doHardRect(charRect, Math.toRadians(270));
                break;
            case SOUTHWEST:
                doHardRect(charRect, Math.toRadians(315));
                break;
            case WEST:
                doHardRect(charRect, 0);
                break;
            case NORTHWEST:
                doHardRect(charRect, Math.toRadians(45));
                break;
        }
    }

    private void doHardRect(RectF charRect, double angleInRadians) {
        float buffer = character.getSize() / 2;
        float x = buffer * 4 * (float) Math.cos(angleInRadians);
        float y = buffer * 4 * (float) Math.sin(angleInRadians);
        x = charRect.centerX() - x;
        y = charRect.centerY() - y;
        rect = new RectF(x - buffer, y - buffer, x + buffer, y + buffer);
    }

    private void back() {
        int plus = currentSpot.ordinal() >= 7 ? 0 : currentSpot.ordinal() + 1;
        currentSpot = Direction.values()[plus];
    }

    private void next() {
        int minus = currentSpot.ordinal() == 0 ? 7 : currentSpot.ordinal() - 1;
        currentSpot = Direction.values()[minus];
    }

    public boolean collide(Character c) {
        return c.getRect().intersect(rect) && !c.equals(character);
    }

    /**
     * Returns the rectangle object the orbital contains
     * @return the rectangle object the orbital contains
     */
    public RectF getRect() {
        return rect;
    }

    @Override
    public void update() {
        long currentTime = Constants.currentTimeMillis;
        if (currentTime - lastChange >= spotDuration) {
            lastChange = currentTime;
            if (backwards)
                back();
            else
                next();
            doRect();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        RectF drawRect = new RectF(gameToDrawX(rect.left), gameToDrawY(rect.top), gameToDrawX(rect.right), gameToDrawY(rect.bottom));
        canvas.drawRect(drawRect, paint);
    }

    @Override
    public PointF getLocation() {
        return new PointF(rect.centerX(), rect.centerY());
    }
}