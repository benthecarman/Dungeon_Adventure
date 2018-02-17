package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import sa_b_2.coms309.dungeonadventure.R;

public class Player extends Character {

    private final float startShotSpeed;
    private final float startShotDamage;
    private final short startShotSize;
    private final short startRange;
    private final double startMoveSpeed;
    private int inventorySize;
    private Player(float health, float shotSpeed, float shotDamage, short shotSize, short range, PointF location, String name) {
        super(health, shotSpeed, shotDamage, shotSize, range, location, name);
        isFriendly = true;
        this.setMoveSpeed(15);

        this.startShotSpeed = shotSpeed;
        this.startShotDamage = shotDamage;
        this.startShotSize = shotSize;
        this.startRange = range;
        this.startMoveSpeed = getMoveSpeed();

        this.inventorySize = 3;
    }

    /**
     * Generates a new player object
     *
     * @param characterType Type of character you wish to create
     * @return Player object
     */
    public static Player makePlayer(PlayableCharacter characterType){

        Player p;
        switch (characterType){
            case Slime:
                p = new Player(100, 2, 20, (short) 20, (short) (Constants.GAMEWIDTH / 2), new PointF(Constants.GAMEWIDTH / 2, Constants.GAMEHEIGHT / 2), characterType.toString());
                p.setImage(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.slime));
                return p;
            case Demon:
                p = new Player(100, 2, 20, (short) 20, (short) (Constants.GAMEWIDTH / 2), new PointF(Constants.GAMEWIDTH / 2, Constants.GAMEHEIGHT / 2), characterType.toString());
                p.setImage(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.demon));
                return p;
            case Goblin:
                p = new Player(100, 2, 20, (short) 20, (short) (Constants.GAMEWIDTH / 2), new PointF(Constants.GAMEWIDTH / 2, Constants.GAMEHEIGHT / 2), characterType.toString());
                p.setImage(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.goblin));
                return p;
            case Wolf:
                p = new Player(100, 2, 20, (short) 20, (short) (Constants.GAMEWIDTH / 2), new PointF(Constants.GAMEWIDTH / 2, Constants.GAMEHEIGHT / 2), characterType.toString());
                p.setImage(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.wolf));
                return p;
            case Ghost:
                p = new Player(100, 2, 20, (short) 20, (short) (Constants.GAMEWIDTH / 2), new PointF(Constants.GAMEWIDTH / 2, Constants.GAMEHEIGHT / 2), characterType.toString());
                p.setImage(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.ghost));
                return p;
        }
        return null;
    }

    /**
     * Returns maximum items player can hold
     * @return inventorySize
     */
    public int getInventorySize() {
        return inventorySize;
    }

    /**
     * Sets new maximum playerInventory size, as long as it is less than 10
     * @param size size to set inventory to
     */
    public void setInventorySize(int size) {
        if (size < 10) {
            this.inventorySize = size;
        }
    }

    /**
     * Goes through all items in inventory, and adds those values to player stats
     */
    public void processItemList() {
        //float tempHealth = 0;
        float tempShotSpeed = startShotSpeed;
        float tempShotDamage = startShotDamage;
        short tempShotSize = startShotSize;
        short tempRange = startRange;
        double tempMoveSpeed = startMoveSpeed;
        for (Item it : playerItems) {
            //tempHealth += it.getHealth();
            tempShotSpeed += it.getShotSpeed();
            tempShotDamage += it.getShotDamage();
            tempShotSize += it.getShotSize();
            tempRange += it.getRange();
            tempMoveSpeed += it.getMoveSpeed();
        }
        setShotSpeed(tempShotSpeed);
        setShotDamage(tempShotDamage);
        setShotSize(tempShotSize);
        setRange(tempRange);
        setMoveSpeed(tempMoveSpeed);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        Bitmap image = this.getImage();
        doDrawRect();
        if(image != null){
            if(!isInvincible())
                c.drawBitmap(image, null, this.getDrawRect(), null);
        }
        else{
            Paint paint = new Paint();
            paint.setColor(Color.CYAN);
            c.drawRect(this.getDrawRect(),paint);
        }
    }
}
