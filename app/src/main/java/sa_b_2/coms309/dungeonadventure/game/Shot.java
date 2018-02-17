package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawX;
import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawY;

/**
 * Object that holds the information of a character's shot
 */
public class Shot implements GameObject {

    private Circle circle;
    private Circle drawCircle;
    private Character owner;
    private float damage, baseDamage;
    private short range, speed;
    private int distanceTraveled;
    private Direction direction;
    private Bitmap image = null;
    private boolean outOfRange;
    private boolean bounced = false;
    private boolean exploded = false;

    Shot(Character owner, short speed, float damage, short size, short range, Direction direction) {
        this.owner = owner;
        this.speed = speed;
        this.damage = damage;
        this.baseDamage = damage;
        this.range = range;
        this.direction = direction;
        this.circle = new Circle(owner.getLocation(), size);
        this.image = owner.getShotImage();
        this.outOfRange = false;
    }

    /**
     * Returns the shot's damage
     *
     * @return
     */
    public float getDamage() {
        return damage;
    }

    /**
     * Returns if the object is out of range
     *
     * @return If the object is out of range
     */
    public boolean isOutOfRange() {
        return outOfRange;
    }

    @Override
    public void draw(Canvas c) {
        Paint paint = new Paint();
        updateDrawCircle();
        if (image == null) {
            paint.setColor(owner.isFriendly() ? Color.BLUE : Color.RED);
            c.drawCircle(drawCircle.getX(), drawCircle.getY(), drawCircle.getRadius(), paint);
        } else {
            c.drawBitmap(image, null, new RectF(drawCircle.getX() - drawCircle.getRadius(), drawCircle.getY() - drawCircle.getRadius(), drawCircle.getX() + drawCircle.getRadius(), drawCircle.getY() + drawCircle.getRadius()), paint);
        }
    }

    private void updateDrawCircle() {
        this.drawCircle = new Circle(new PointF(gameToDrawX(circle.getLoc().x), gameToDrawY(circle.getLoc().y)), (short) gameToDrawX(circle.getRadius()));
    }

    @Override
    public void update() {
        //Moves the shot
        switch (direction) {
            case NORTH:
                circle = new Circle(circle.getX(), circle.getY() - speed, circle.getRadius());
                break;
            case NORTHEAST:
                circle = new Circle(circle.getX() + speed, circle.getY() - speed, circle.getRadius());
                break;
            case NORTHWEST:
                circle = new Circle(circle.getX() - speed, circle.getY() - speed, circle.getRadius());
                break;
            case SOUTH:
                circle = new Circle(circle.getX(), circle.getY() + speed, circle.getRadius());
                break;
            case SOUTHEAST:
                circle = new Circle(circle.getX() + speed, circle.getY() + speed, circle.getRadius());
                break;
            case SOUTHWEST:
                circle = new Circle(circle.getX() - speed, circle.getY() + speed, circle.getRadius());
                break;
            case EAST:
                circle = new Circle(circle.getX() + speed, circle.getY(), circle.getRadius());
                break;
            case WEST:
                circle = new Circle(circle.getX() - speed, circle.getY(), circle.getRadius());
                break;
            default:
                circle = new Circle(circle.getX(), circle.getY(), circle.getRadius());
                break;
        }
        distanceTraveled += Math.abs(speed);

        if (owner.hasItem("Rocket"))
            damage = (float) (0.75 + (1.25 * distanceTraveled / (Constants.GAMEWIDTH / 2))) * baseDamage;

        //Determines if it is our of range or outside the game dimensions
        boolean outOfBounds = circle.getX() > Constants.GAMEWIDTH || circle.getY() > Constants.GAMEHEIGHT || circle.getX() < 0 || circle.getY() < 0;

        if (distanceTraveled >= range || ((!owner.hasItem("Rubber Shots") || bounced) && outOfBounds))
            outOfRange = true;
        else if ((owner.hasItem("Rubber Shots") && !bounced) && outOfBounds) {
            speed = (short) -speed;
            bounced = true;
        }
    }

    @Override
    public PointF getLocation() {
        return circle.getLoc();
    }

    /**
     * Returns if the shot collided with the character
     * @param c Character to check if the shot collided with
     * @return if the shot collided with the character
     */
    public boolean shotCollide(Character c) {
        if (circle.intersects(c.getRect()) && !c.equals(owner)) {
            if (owner.hasItem("Grenade Shot") && !exploded) {
                outOfRange = false;
                exploded = true;
                speed = 0;
                circle = new Circle(circle.getX(), circle.getY(), (short) (circle.getRadius() * 3));
                return false;
            } else {
                outOfRange = true;//This is so it will be deleted
                return true;
            }
        }

        return false;
    }

    /**
     * Tests if 2 shots collided and have rubber shots to allow shot collisions
     * @param s Shot to test if it collided
     */
    public void shotCollide(Shot s) {
        if (this.circle.intersects(s.circle)) {
            if (owner.hasItem("Rubber Shots") && s.owner != owner)//May need testing
                speed = (short) -speed;
            if (owner.hasItem("No item"))
                outOfRange = true;
        }
    }

    /**
     * Returns if the shot collided with the orbital
     * @param o orbital to test
     * @return if they collided
     */
    public boolean shotCollide(Orbital o) {
        return circle.intersects(o.getRect());
    }

    /**
     * Returns owner of the shot
     * @return owner of the shot
     */
    public Character getOwner() {
        return owner;
    }

    /**
     * Circle class to handle all aspects of the shot that pertain to it being a circle
     */
    private class Circle {

        private PointF loc;
        private short radius;

        private Circle(PointF loc, short radius) {
            this.loc = loc;
            this.radius = radius;
        }

        private Circle(float x, float y, short radius) {
            this.loc = new PointF(x, y);
            this.radius = radius;
        }

        private short getRadius() {
            return radius;
        }

        private PointF getLoc() {
            return loc;
        }

        private float getX() {
            return loc.x;
        }

        private float getY() {
            return loc.y;
        }

        private boolean intersects(Circle circle) {
            return (Math.pow(radius - circle.getRadius(), 2) <= Math.pow(loc.x - circle.getX(), 2) + Math.pow(loc.y - circle.getY(), 2));
        }

        private boolean intersects(RectF rect) {
            double x = Math.abs(loc.x - (rect.centerX()));
            double y = Math.abs(loc.y - rect.centerY());

            if (x > (rect.width() / 2 + radius)) {
                return false;
            }
            if (y > (rect.height() / 2 + radius)) {
                return false;
            }

            if (x <= (rect.width() / 2)) {
                return true;
            }
            if (y <= (rect.height() / 2)) {
                return true;
            }

            double z = Math.pow(x - rect.width() / 2, 2) + Math.pow(y - rect.height() / 2, 2);

            return (z <= (Math.pow(radius, 2)));
        }
    }
}
