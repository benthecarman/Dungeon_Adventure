package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawX;
import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawY;

public class Door implements GameObject{

    private PointF location;
    private RectF rect;
    private int size = 60;
    private int offset = 50;
    private Direction direction;
    /**
     * Constructs a door to get to other rooms
     * @param direction direction door leads in the floor
     */
    public Door(Direction direction){
        this.direction=direction;
        location=new PointF();
        setLocation(direction);
        rect = new RectF(location.x - size, location.y - size, location.x + size, location.y + size);
    }

    /**
     * Returns the direction
     * @return direction door leads
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Checks for player collision
     * @param player player to check
     *@return true if intersects, else false
     */
    public boolean playerCollide(Player player) {
        return RectF.intersects(new RectF(this.rect), player.getRect());
    }

    /**
     * Draws the doors
     * @param c canvas to draw on
     */
    @Override
    public void draw(Canvas c) {
        RectF drawRect = new RectF(gameToDrawX(rect.left), gameToDrawY(rect.top), gameToDrawX(rect.right), gameToDrawY(rect.bottom));
//        rect = new RectF(location.x - size, location.y - size, location.x + size, location.y + size);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        c.drawRect(drawRect, paint);
    }

    /**
     * Not used
     */
    @Override
    public void update() {

    }

    /**
     * Returns the location of the door
     * @return  location of door
     */
    @Override
    public PointF getLocation() {
        return location;
    }

    /**
     * Sets the location of the door
     * @param direction direction door leads to in floor
     */
    public void setLocation(Direction direction){
        switch (direction) {
            case NORTH:
                location.y = -offset;
                location.x = Constants.GAMEWIDTH / 2;
                break;
            case SOUTH:
                location.y = Constants.GAMEHEIGHT + offset;
                location.x = Constants.GAMEWIDTH / 2;
                break;
            case EAST:
                location.y = Constants.GAMEHEIGHT / 2;
                location.x = Constants.GAMEWIDTH + offset;
                break;
            case WEST:
                location.y = Constants.GAMEHEIGHT / 2;
                location.x = -offset;
                break;
            default:

                break;
        }
    }

    /**
     * Sets the location of the door
     * @return exitLocation the opposite of the location of the door
     */
    public PointF getOppositeLocation() {
        PointF exitLocation = new PointF();
        switch (direction) {
            case NORTH:
                exitLocation.y = Constants.GAMEHEIGHT - size;
                exitLocation.x = Constants.GAMEWIDTH / 2;
                break;
            case SOUTH:
                exitLocation.y = size;
                exitLocation.x = Constants.GAMEWIDTH / 2;
                break;
            case EAST:
                exitLocation.y = Constants.GAMEHEIGHT / 2;
                exitLocation.x = size;
                break;
            case WEST:
                exitLocation.y = Constants.GAMEHEIGHT / 2;
                exitLocation.x = Constants.GAMEWIDTH - size;
                break;
            default:

                break;
        }
        return exitLocation;
    }

    /**
     * Returns the point that is added to the current room location to get to the next floor
     * @return nr the point is added to the current point in the floor
     */
    public Point nextRoom() {
        Point nr;
        switch (direction) {
            case NORTH:
                nr = new Point(0, -1);
                break;
            case SOUTH:
                nr = new Point(0, 1);
                break;
            case EAST:
                nr = new Point(1, 0);
                break;
            case WEST:
                nr = new Point(-1, 0);
                break;
            default:
                nr = new Point(0, 0);
                break;
        }
        return nr;
    }
}
