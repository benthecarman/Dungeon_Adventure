package sa_b_2.coms309.dungeonadventure.ui.Scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;


public interface Scene {

    /**
     * Updates the items in the scene in response to time and user input
     */
    void update();

    /**
     * Method draws the scene on to the user's screen
     *
     * @param canvas Android's canvas that allows us to draw
     */
    void draw(Canvas canvas);

    /**
     * Terminates Scene, goes back to the main menu
     */
    void terminate();

    /**
     * Handles the touch input from the user
     * @param event Android's MotionEvent
     */
    void receiveTouch(MotionEvent event);

}