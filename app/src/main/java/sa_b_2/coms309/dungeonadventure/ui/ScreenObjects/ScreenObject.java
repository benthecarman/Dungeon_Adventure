package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

/**
 * Object to be displayed on screen
 */
interface ScreenObject {

    /**
     * Handles when the object is touched
     *
     * @param event Android api motion event object
     * @return if the object was touched
     */
    boolean onTouchEvent(@NonNull MotionEvent event);

    /**
     * Draws the current object on the canvas
     * @param canvas Android's canvas object
     */
    void draw(@NonNull Canvas canvas);

}
