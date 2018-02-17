package sa_b_2.coms309.dungeonadventure.game;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Enemy extends Character {

    private EnemyAi ai;
    private Direction directionX;
    private Direction directionY;
    private Random rand=new Random();
    private Player player;
    private List<Shot> shots;
    private boolean isEnemy = true;
    private Bitmap image;

    /**
     * Constructs enemy with location info and stats info
     * @param health health value of enemy
     * @param shotSpeed speed value of enemies shots
     * @param shotDamage damage value of enemy
     * @param shotSpeed shotSpeed value of enemy
     * @param shotDamage shotDamage value of enemy
     * @param shotSize shotSize value of enemy
     * @param range range value of enemy shots
     * @param location location of enemy
     * @param name name of enemy
     * @param image bitmap file associated with enemy
     */
    public Enemy(float health, float shotSpeed, float shotDamage, short shotSize, short range, PointF location, String name, Bitmap image) {
        super(health, shotSpeed, shotDamage, shotSize, range, location, name);
//        directionY=Direction.values()[rand.nextInt(2)];
//        directionX=Direction.values()[rand.nextInt(2)+2];
        shots = new LinkedList<>();
//        ai = new EnemyAi(this,);
        player=null;
        this.image = image;
    }

    public Enemy() {
        this(100, 2, 20, (short) 20, (short) (Constants.GAMEWIDTH / 2), new PointF(Constants.GAMEWIDTH / 2, Constants.GAMEHEIGHT / 2), "Knight", null);
    }

    /**
     * Sets enemy ai
     * @param ai ai to set to enemy
     */
    public void setEnemyAi(EnemyAi ai) {
        this.ai = ai;
    }   //sets enemy ai

    /**
     * Sets enemy x direction
     * @param directionX X Direction to set to enemy
     */
    public void setXDirection(Direction directionX){this.directionX=directionX;}    //sets x direction

    /**
     * Sets enemy y direction
     * @param directionY X Direction to set to enemy
     */
    public void setYDirection(Direction directionY){this.directionY=directionY;}    //sets y direction

    /**
     * Returns image of enemy
     * @return image
     */
    public Bitmap getImage()   {
        return image;
    }   //returns enemy image

    /**
     * Returns list of shots
     * @return shots
     */
    public List<Shot> getShots() {
        return shots;
    }   //returns list of shots of an enemy

    /**
     * updates positions of enemy
     */
    @Override
    public void update() {

        if(this.getTarget()!=null) {
//            shots.add(ai.keepDistance(200));

            ai.wander(100);
            switch (this.getName()) {       //determines which ai to use based on enemy names
                case "Knight":
                    ai.chase(130);
                    break;
                case "Drunk":
                    ai.wander(100);
                    break;
                case "Archer":
                    shots.add(ai.keepDistance(200));
                    break;
                case "Assassin":
                    ai.zigzag(200);
                    break;
                default:
                    ai.wander(100);
                    break;
            }
        }
    }

    /**
     * Returns x direction of enemy
     * @return directionX
     */
    public Direction getDirectionX(){return directionX;}    //gets x direction

    /**
     * Returns y direction of enemy
     * @return directionY
     */
    public Direction getDirectionY(){return directionY;}    //gets y direction

    /**
     * Returns enemies target
     * @return player
     */
    public Player getTarget(){return player;}       //returns the target of enemy

    /**
     * Sets enemies target
     * @param player player for enemy to target
     */
    public void setTarget(Player player) {
        this.player = player;
    }   //sets target

    /**
     * Returns true if player collides with enemy
     * @param player player object to be checked
     * @return true if intersect, else false
     */
    public boolean playerCollide(Player player){
        return RectF.intersects(new RectF(this.getRect()), player.getRect());   //returns true if enemy collides into player
    }

    /**
     * draws the enemy
     * @param c canvas to be drawn on
     */
    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if(image != null) {
            c.drawBitmap(image, null, this.getDrawRect(), null);
        }
        else{
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            c.drawRect(this.getDrawRect(),paint);
        }
    }
}