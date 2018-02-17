package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Objects that can appear in game
 */
interface GameObject {

    /**
     * Draws the GameObject on the canvas
     *
     * @param c canvas to be drawn on
     */
    void draw(Canvas c);

    /**
     * Updates the GameObject
     */
    void update();

    /**
     * Returns the location of the GameObject
     *
     * @return location of the object
     */
    PointF getLocation();
}
