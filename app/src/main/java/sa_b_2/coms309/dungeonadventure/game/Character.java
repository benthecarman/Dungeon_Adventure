package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.LinkedList;
import java.util.List;

import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawX;
import static sa_b_2.coms309.dungeonadventure.game.Constants.gameToDrawY;

/**
 * Character class for Players and Enemies
 */
public abstract class Character implements GameObject {

    private final int CHARACTER_SIZE = 50;
    private final String name;
    protected List<Item> playerItems;
    boolean isFriendly = false;
    private List<Orbital> orbitals;
    private float health;
    private float currentHealth;
    private float shotSpeed;
    private float shotDamage;
    private short shotSize;
    private short range;
    private double moveSpeed;
    private boolean isAlive = true;
    private boolean invincible = false;
    private PointF location;
    private PointF prevLocation;
    private Bitmap image;
    private Bitmap shotImage;
    private long lastShot;
    private RectF rect, drawRect;
    private int size = 0;

    Character(float health, float shotSpeed, float shotDamage, short shotSize, short range, PointF location, String name) {

        this.playerItems = new LinkedList<>();
        this.orbitals = new LinkedList<>();
        this.health = health;
        this.currentHealth = health;
        this.shotSpeed = shotSpeed;
        this.shotDamage = shotDamage;
        this.shotSize = shotSize;
        this.range = range;
        this.location = location;
        moveTo(location);
        this.name = name;
        this.moveSpeed = 15;
    }

    /**
     * Moves the character to the Point p
     *
     * @param p Where the character will be moved to
     */
    public void moveTo(PointF p) {

        prevLocation = location;

        if (p.x + CHARACTER_SIZE + size > Constants.GAMEWIDTH)
            p = new PointF(Constants.GAMEWIDTH - CHARACTER_SIZE - size, p.y);
        else if (p.x - CHARACTER_SIZE - size < 0)
            p = new PointF(CHARACTER_SIZE + size, p.y);
        if (p.y + CHARACTER_SIZE + size > Constants.GAMEHEIGHT)
            p = new PointF(p.x, Constants.GAMEHEIGHT - CHARACTER_SIZE - size);
        else if (p.y - CHARACTER_SIZE - size < 0)
            p = new PointF(p.x, CHARACTER_SIZE + size);

        location = new PointF(p.x, p.y);
        doRect();
    }

    /**
     * Updates the rectangles after the player moves
     */
    private void doRect() {
        rect = new RectF(location.x - CHARACTER_SIZE - size, location.y - CHARACTER_SIZE - size, location.x + CHARACTER_SIZE + size, location.y + CHARACTER_SIZE + size);
    }

    /**
     * Updates the rectangle of where the object is drawn
     */
    public void doDrawRect() {
        drawRect = new RectF(gameToDrawX(rect.left), gameToDrawY(rect.top), gameToDrawX(rect.right), gameToDrawY(rect.bottom));
    }

    public RectF getRect() {
        return rect;
    }

    public RectF getDrawRect() {
        return drawRect;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean i) {
        invincible = i;
    }

    /**
     * returns player health
     *
     * @return player health
     */
    public double getHealth() {
        return health;
    }

    /**
     * Adds to both current health and maximum health
     * @param health value to be added to health and currentHealth
     */
    public void setHealth(int health) {
        this.health += health;
        this.currentHealth += health;
    }

    /**
     * returns current health
     * @return current health
     */
    public double getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets currentHealth to said number
     * @param currentHealth number to set currentHealth to
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Return shotSpeed
     * @return shotSpeed
     */
    public double getShotSpeed() {
        return shotSpeed;
    }

    /**
     * set shotSpeed to value
     * @param shotSpeed value to set shotSpeed to
     */
    public void setShotSpeed(float shotSpeed) {
        this.shotSpeed = shotSpeed;
    }

    /**
     * Return shotDamage
     * @return shotDamage
     */
    public double getShotDamage() {
        return shotDamage;
    }

    /**
     * set shotDamage to value
     * @param shotDamage value to set shotDamage to
     */
    public void setShotDamage(float shotDamage) {
        this.shotDamage = shotDamage;
    }

    /**
     * Return shotSize
     * @return shotSize
     */
    public double getShotSize() {
        return shotSize;
    }

    /**
     * set shotSize to value
     * @param shotSize value to set shotSize to
     */
    public void setShotSize(short shotSize) {
        this.shotSize = shotSize;
    }

    /**
     * Return range
     * @return range
     */
    public int getRange() {
        return range;
    }

    /**
     * set range to value
     * @param range value to set range to
     */
    public void setRange(short range) {
        this.range = range;
    }

    /**
     * Return moveSpeed
     * @return moveSpeed
     */
    public double getMoveSpeed() {
        return moveSpeed;
    }

    /**
     * set moveSpeed to value
     * @param moveSpeed value to set moveSpeed to
     */
    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }


    public boolean isFriendly() {
        return isFriendly;
    }

    @Override
    public PointF getLocation() {
        return location;
    }

    public PointF getPrevLocation() {
        return prevLocation;
    }

    /**
     * Returns bitmap image of character
     * @return bitmap image
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Set character image to value
     * @param image bitmap image for character
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getShotImage() {
        return shotImage;
    }

    public void setShotImage(Bitmap image) {
        this.shotImage = image;
    }

    public String getName() {
        return name;
    }

    public void addSize(int size) {
        this.size += size;
    }

    public int getSize() {
        return CHARACTER_SIZE + size;
    }

    /**
     * Deals damage to the character
     *
     * @param damage how much damage to deal to character
     * @return if the damage killed the character
     */
    public boolean hit(float damage) {
        currentHealth -= damage;

        if (currentHealth <= 0) {
            currentHealth = 0;
            isAlive = false;
        }

        return !isAlive;
    }

    /**
     * Adds item to player inventory
     * @param item item to be added to player inventory
     */
    public void addItem(Item item) {
        playerItems.add(item);
    }

    /**
     * Removes item from player inventory
     * @param item item to be removed from player inventory
     */
    public void removeItem(Item item) {
        playerItems.remove(item);
    }

    /**
     * Uses removeItem and adds item back to itemManager to drop item
     * @param item item to be dropped
     * @param im itemManager to add item to
     */
    public void dropItem(Item item, ItemManager im) {
        removeItem(item);
        if (getRect().top < Constants.SCREENHEIGHT / 2) {
            item.setRectangle((int) getRect().left, (int) getRect().top + 150, (int) getRect().right, (int) getRect().bottom + 150);
        } else {
            item.setRectangle((int) getRect().left, (int) getRect().top - 150, (int) getRect().right, (int) getRect().bottom - 150);
        }
        im.addItem(item);
    }

    /**
     * returns list of player inventory items
     * @return list of player inventory items
     */
    public List<Item> getPlayerItems() {
        return playerItems;
    }

    /**
     * Checks if player has item object in player inventory
     * @param item item to be searched for in player inventory
     * @return true if player has item, else false
     */
    public boolean hasItem(Item item) {
        return playerItems.contains(item);
    }

    /**
     * Checks if player has item object by name in player inventory
     * @param itemName name of item to be searched for
     * @return true if player contains item with the name, else false
     */
    public boolean hasItem(String itemName) {
        /*if(itemName.equals("Rubber Shots"))
            return true;*/
        for (Item i : playerItems)
            if (i.getName().equalsIgnoreCase(itemName))
                return true;
        return false;
    }

    /**
     * Heals the character
     * @param hp how much health is healed
     * @return if it healed the character
     */
    public boolean heal(int hp) {
        if (currentHealth >= health)
            return false;

        currentHealth += hp;
        if (currentHealth > health)
            currentHealth = health;
        return true;
    }

    /**
     * Returns a shot that is to be fired, if it is null the character cannot shoot yet
     *
     * @param direction the direction the shot is going
     * @return if it is null the character cannot shoot yet, otherwise it a shot object that is to be fired
     */
    public List<Shot> shoot(Direction direction) {
        long currentTime = Constants.currentTimeMillis;
        List<Shot> list = new LinkedList<>();
        if (currentTime - lastShot >= (1 / shotSpeed) * 1000) {
            lastShot = currentTime;
            list.add(new Shot(this, (short) (50 + shotSpeed * 3), shotDamage, shotSize, range, direction));
            if (this.hasItem("Shotgun")) {
                int plus = direction.ordinal() == 7 ? 0 : direction.ordinal() + 1;
                int minus = direction.ordinal() == 0 ? 7 : direction.ordinal() - 1;
                list.add(new Shot(this, (short) (shotSpeed * 10), shotDamage, shotSize, range, Direction.values()[plus]));
                list.add(new Shot(this, (short) (shotSpeed * 10), shotDamage, shotSize, range, Direction.values()[minus]));
            }
        }
        return list;
    }

    @Override
    public void draw(Canvas c) {
        Paint paint = new Paint();
        doDrawRect();

        for (Orbital o : orbitals)
            o.draw(c);

        if (this.getClass() != Character.class) {
            return;
        }

        paint.setColor(Color.RED);
        if (invincible) {
            paint.setColor(Color.WHITE);
        }
        c.drawRect(drawRect, paint);
    }

    @Override
    public void update() {
        for (Orbital o : orbitals)
            o.update();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass())
            return false;

        Character c = (Character) o;

        return c.name.equals(this.name) && c.getCurrentHealth() == this.getCurrentHealth();
    }
}