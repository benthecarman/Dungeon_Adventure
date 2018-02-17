package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import static sa_b_2.coms309.dungeonadventure.game.Constants.SCREENHEIGHT;
import static sa_b_2.coms309.dungeonadventure.game.Constants.SCREENWIDTH;
import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawX;
import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawY;

/**
 * This class is for items
 * Created by Tanner on 9/17/2017.
 */

public class Item implements GameObject{

    private RectF rectangle;
    private RectF drawRect;

    private String name;
    private String desc = null;
    private int value;
    private Bitmap image;

    private int health;
    private float shotSpeed;
    private float shotDamage;
    private short shotSize;
    private short range;
    private double moveSpeed;

    /**
     * Constructs item with location info and stats info
     *
     * @param left       used for creating rectangle
     * @param top        used for creatng rectangle
     * @param name       name of item
     * @param value      value/rarity of item
     * @param image      bitmap file associated with item
     * @param shotSpeed  shotSpeed value of item
     * @param shotDamage shotDamage value of item
     * @param shotSize   shotSize value of item
     * @param range      range value of item
     * @param moveSpeed  moveSpeed value of item
     * @param health     health value of item
     */
    public Item(int left, int top, String name, int value, Bitmap image, float shotSpeed, float shotDamage, short shotSize, short range, double moveSpeed, int health) {
        this.name = name;
        this.value = value;
        this.image = image;

        this.shotSpeed = shotSpeed;
        this.shotDamage = shotDamage;
        this.shotSize = shotSize;
        this.range = range;
        this.moveSpeed = moveSpeed;
        this.health = health;

        //l,t,r,b
        int x = 60;
        rectangle = new RectF(left, top, left + x, top + x);
    }

    /**
     * Returns rectangle where the item is located
     *
     * @return rectangle object corresponding with item
     */
    public RectF getRectangle() {
        return rectangle;
    }

    /**
     * Returns bitmap associated with item
     *
     * @return bitmap image for item
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * sets rectangle to be drawn
     */
    private void doDrawRect() {
        drawRect = new RectF(gameToDrawX(rectangle.left), gameToDrawY(rectangle.top), gameToDrawX(rectangle.right), gameToDrawY(rectangle.bottom));
    }

    /**
     * returns item description
     * @return item description as String
     */
    public String getDesc() {
        return desc;
    }

    /**
     * sets item description
     *
     * @param s description of item
     */
    public void setDesc(String s) {
        desc = s;
    }

    /**
     * Returns name of item
     * @return name of item as String
     */
    public String getName() {
        return name;
    }

    /**
     * returns health value of item
     * @return health value as String
     */
    public int getHealth() {
        return health;
    }

    /**
     * returns shotSpeed value of item
     * @return shotSpeed value as int
     */
    public float getShotSpeed() {
        return shotSpeed;
    }

    /**
     * returns shotDamage value of item
     * @return shotDamage value as float
     */
    public float getShotDamage() {
        return shotDamage;
    }

    /**
     * returns shotSize value of item
     * @return shotSize value as short
     */
    public short getShotSize() {
        return shotSize;
    }

    /**
     * returns range value of item
     * @return range value as short
     */
    public short getRange() {
        return range;
    }

    /**
     * returns moveSpeed value of item
     * @return moveSpeed value as double
     */
    public double getMoveSpeed() {
        return moveSpeed;
    }

    /**
     * returns value value of item
     * @return value value as int
     */
    public int getValue() {
        return value;
    }

    /**
     * Checks if item object's rectangle intersects with player rectangle
     * @param player player object to be checked
     * @return true if intersect, else false
     */
    public boolean playerCollide(Player player) {
        return RectF.intersects(new RectF(rectangle), player.getRect());
    }

    /**
     * Sets rectangle of item object
     * @param left left coordinate
     * @param top top coordinate
     * @param width right coordinate
     * @param height bottom coordinate
     */
    public void setRectangle(int left, int top, int width, int height){
        rectangle = new RectF(left, top, width, height);
        drawRect = new RectF(gameToDrawX(rectangle.left), gameToDrawY(rectangle.top), gameToDrawX(rectangle.right), gameToDrawY(rectangle.bottom));
    }

    /**
     * Displays item name and value on pickup
     * @param canvas canvas to be drawn on
     */
    public void drawPickup(Canvas canvas){
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        int x = SCREENWIDTH/2;
        int y = SCREENHEIGHT / 4;

        canvas.drawText(name, x,y, paint);

        paint.setTextSize(75);

        if (shotSpeed != 0) {
            y += 100;
            canvas.drawText("Shot Speed: +" + shotSpeed, x, y, paint);
        }
        if (shotDamage != 0) {
            y += 100;
            canvas.drawText("Shot Damage: +" + shotDamage, x, y, paint);
        }
        if (shotSize != 0) {
            y += 100;
            canvas.drawText("Shot Size: +" + shotSize, x, y, paint);
        }
        if (range != 0) {
            y += 100;
            canvas.drawText("Range: +" + range, x, y, paint);
        }
        if (moveSpeed != 0) {
            y += 100;
            canvas.drawText("Move Speed: +" + moveSpeed, x, y, paint);
        }
        if (desc != null) {
            y += 100;
            canvas.drawText(desc, x, y, paint);
        }
    }

    /**
     * draws the item object
     * @param canvas canvas to be drawn on
     */
    public void draw(Canvas canvas) {
        Bitmap image = this.getImage();
        doDrawRect();
        if(image != null) {
            canvas.drawBitmap(image, null, this.getDrawRect(), null);
        } else {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawRect(this.getDrawRect(),paint);
        }
    }

    /**
     * not used
     */
    public void update() {

    }

    /**
     * returns location of rectangle
     * @return PointF value of rectangle
     */
    @Override
    public PointF getLocation() {
        return new PointF(rectangle.centerX(), rectangle.centerY());
    }

    /**
     * returns the rectangle to be drawn
     * @return rectangle to be drawn
     */
    public RectF getDrawRect() {
        return drawRect;
    }

    /**
     * Checks if object equals this item
     * @param obj object to be compared to
     * @return true if equals, else false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        Item i = (Item) obj;

        return i.name.equals(this.name) && i.value == this.value;
    }
}