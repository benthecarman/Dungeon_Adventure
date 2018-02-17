package sa_b_2.coms309.dungeonadventure.ui.ScreenObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.game.Direction;

/**
 * An analog stick that controls the user's in game movements
 */
public class AnalogStick implements ScreenObject {

    private final Point location;
    private final int radius;
    private boolean isTouched = false;
    private float x = 0, y = 0;
    private float adjustedX = 0, adjustedY = 0;
    private float distance = 0, angle = 0;

    /**
     * Creates the analog stick object
     *
     * @param location Where on screen the stick is located
     * @param radius   How big the analog stick is
     */
    public AnalogStick(Point location, int radius) {
        this.location = location;
        this.radius = radius;
    }

    /**
     * Returns the radius of the analog stick
     * @return radius of the analog stick
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Calculates where the analog stick is being touched
     *
     * @param event Motion event from the android api
     */
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        for (short i = 0; i < event.getPointerCount(); ++i) {

            float x = event.getX(i);
            float y = event.getY(i);

            //(x - center_x)^2 + (y - center_y)^2 <= radius^2
            if ((Math.pow(x - location.x, 2) + Math.pow(y - location.y, 2) <= Math.pow(radius, 2)) ||
                    (isTouched && (Math.pow(x - location.x, 2) + Math.pow(y - location.y, 2) <= Math.pow(radius * 2.5, 2)))) {

                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_CANCEL) {
                    isTouched = false;
                    return false;
                }

                this.x = x;
                this.y = y;
                adjustedX = location.x - x;
                adjustedY = location.y - y;
                distance = (float) Math.sqrt(Math.pow(adjustedX, 2) + Math.pow(adjustedY, 2));
                angle = (float) calculateAngle();
                isTouched = true;
                return true;
            }
        }

        return false;
    }

    public boolean isTouched() {
        return isTouched;
    }

    public float getDistance() {
        return distance;
    }

    public float getAngle() {
        return angle + 90 >= 360 ? angle - 270 : angle + 90;
    }

    /**
     * Returns the direction that the stick is being pushed in the 4 cardinal directions
     * @return Which direction the stick is being pushed
     */
    @Nullable
    public Direction getDirection4() {
        if (distance > radius / 10 && isTouched) {
            if (angle >= 225 && angle < 315)
                return Direction.SOUTH;
            else if (angle >= 315 || angle < 45)
                return Direction.WEST;
            else if (angle >= 45 && angle < 135)
                return Direction.NORTH;
            else if (angle >= 135 && angle < 225)
                return Direction.EAST;
        }

        return null;
    }

    /**
     * Returns the direction that the stick is being pushed in the 8 cardinal directions
     * @return Which direction the stick is being pushed
     */
    @Nullable
    public Direction getDirection8() {
        if (distance > radius / 10 && isTouched) {

            float angle = getAngle();

            if (angle >= 337.5 || angle <= 22.5)
                return Direction.SOUTH;
            else if (angle >= 22.5 && angle <= 67.5)
                return Direction.SOUTHEAST;
            else if (angle >= 67.5 && angle <= 112.5)
                return Direction.EAST;
            else if (angle >= 112.5 && angle <= 157.5)
                return Direction.NORTHEAST;
            else if (angle >= 157.5 && angle <= 202.5)
                return Direction.NORTH;
            else if (angle >= 202.5 && angle <= 247.5)
                return Direction.NORTHWEST;
            else if (angle >= 247.5 && angle <= 292.5)
                return Direction.WEST;
            else if (angle >= 292.5 && angle <= 337.5)
                return Direction.SOUTHWEST;
        }

        return null;
    }

    private double calculateAngle() {
        if (adjustedX >= 0 && adjustedY >= 0)
            return Math.toDegrees(Math.atan(adjustedY / adjustedX));
        else if (adjustedX < 0)
            return Math.toDegrees(Math.atan(adjustedY / adjustedX)) + 180;
        else if (adjustedX >= 0 && adjustedY < 0)
            return Math.toDegrees(Math.atan(adjustedY / adjustedX)) + 360;
        return 0;
    }

    @Override
    public void draw(@NonNull Canvas c) {
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setAlpha(100);
        c.drawCircle(location.x, location.y, radius, paint);
        paint.setColor(Color.BLACK);
        paint.setAlpha(50);
        if (isTouched) {
            if ((Math.pow(adjustedX, 2) + Math.pow(adjustedY, 2) > Math.pow(radius, 2))) {
                float newX = x;
                float newY = y;
                if (adjustedX > radius)
                    newX = location.x - radius;
                else if (-adjustedX > radius)
                    newX = location.x + radius;
                if (adjustedY > radius)
                    newY = location.y - radius;
                else if (-adjustedY > radius)
                    newY = location.y + radius;
                c.drawCircle(newX, newY, radius / 2, paint);
            } else c.drawCircle(x, y, radius / 2, paint);
        } else
            c.drawCircle(location.x, location.y, radius / 2, paint);
    }
}