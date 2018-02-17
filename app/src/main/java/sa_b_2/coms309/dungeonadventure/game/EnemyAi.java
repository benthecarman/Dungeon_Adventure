package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.PointF;

import java.util.List;
import java.util.Random;


/**
 * Created by Armand on 9/18/2017.
 */

public class EnemyAi {
    private Enemy enemy;
    private int fps=30;
    private Player target;
    private Random rand;
    private int count=0;


    public EnemyAi(Enemy enemy)
    {
        this.enemy=enemy;
        target=enemy.getTarget();
        rand=new Random();
    }

    /**
     * Changes the position of the enemy
     * @param speed ai to set to enemy
     */
    public void wander(int speed){

        PointF point = new PointF(enemy.getLocation().x,enemy.getLocation().y);
        if (enemy.getDirectionY()==null)
            enemy.setYDirection(randY(rand.nextInt(2)));       //gives a random y direction if there is no direction

        if (enemy.getDirectionX()==null)
            enemy.setXDirection(randX(rand.nextInt(2)));     //gives a random x direction if there is no direction

        if (point.x + enemy.getRect().width() / 2 + (speed / fps) > Constants.GAMEWIDTH && !enemy.playerCollide(target)) {   //changes direction if enemy runs into wall
            enemy.setXDirection(Direction.WEST);
        }
        else if(point.x - enemy.getRect().width()/2-(speed / fps) < 0&&!enemy.playerCollide(target)){
            enemy.setXDirection(Direction.EAST);
        }

        if (point.y + enemy.getRect().height() / 2 + (speed / fps) > Constants.GAMEHEIGHT && !enemy.playerCollide(target)) {
            enemy.setYDirection(Direction.NORTH);
        }
        else if(point.y - enemy.getRect().height()/2 -(speed / fps)< 0&&!enemy.playerCollide(target)){
            enemy.setYDirection(Direction.SOUTH);
        }

        linearX(speed); //moves player to x
        linearY(speed); //moves player to y
    }

    /**
     * Changes the position of the enemy
     * @param speed ai to set to enemy
     */
    public void chase(int speed){

        int tolerance=3;
        PointF tPoint = new PointF(target.getLocation().x,target.getLocation().y);  //gets target location
        PointF ePoint = new PointF(enemy.getLocation().x,enemy.getLocation().y);    //gets enemy location

        if(tPoint.x>ePoint.x&&!(tPoint.x+tolerance>=ePoint.x&&tPoint.x-tolerance<=ePoint.x)&&!enemy.playerCollide(target)){  //changes direction of enemy depending on where the target is
            enemy.setXDirection(Direction.EAST);
            linearX(speed);
        }
        else if (tPoint.x<ePoint.x&&!(tPoint.x+tolerance>=ePoint.x&&tPoint.x-tolerance<=ePoint.x)&&!enemy.playerCollide(target)){
            enemy.setXDirection(Direction.WEST);
            linearX(speed);
        }

        if(tPoint.y>ePoint.y&&!(tPoint.y+tolerance>=ePoint.y&&tPoint.y-tolerance<=ePoint.y)&&!enemy.playerCollide(target)){
            enemy.setYDirection(Direction.SOUTH);
            linearY(speed);
        }
        else if (tPoint.y<ePoint.y&&!(tPoint.y+tolerance>=ePoint.y&&tPoint.y-tolerance<=ePoint.y)&&!enemy.playerCollide(target)){
            enemy.setYDirection(Direction.NORTH);
            linearY(speed);
        }

    }

    /**
     * Changes the position of the enemy
     * @param speed ai to set to enemy
     */
    public Shot keepDistance(int speed) {
        int tolerance = 3;
        PointF tPoint = new PointF(target.getLocation().x,target.getLocation().y);  //gets target location
        PointF ePoint = new PointF(enemy.getLocation().x,enemy.getLocation().y);    //gets enemy location
        int distance = 500;
        Direction direction;
        List<Shot> s;

        if (tPoint.x > ePoint.x && !(distance - tolerance <= tPoint.x - ePoint.x && tPoint.x - ePoint.x <= distance + tolerance)&&!enemy.playerCollide(target)) {   //handles x direction depending on how close the enemy is to target
            if (tPoint.x - ePoint.x > distance) {
                enemy.setXDirection(Direction.EAST);
            }
            else if (tPoint.x - ePoint.x < distance) {
                enemy.setXDirection(Direction.WEST);
            }
            linearX(speed);

        } else if (tPoint.x < ePoint.x && !(distance - tolerance <= ePoint.x - tPoint.x && ePoint.x - tPoint.x <= distance + tolerance)&&!enemy.playerCollide(target)) {
            if (ePoint.x - tPoint.x > distance) {
                enemy.setXDirection(Direction.WEST);
            }
            else if (ePoint.x - tPoint.x < distance) {
                enemy.setXDirection(Direction.EAST);
            }
            linearX(speed);
        }

        if (tPoint.y > ePoint.y && !(distance - tolerance <= tPoint.y - ePoint.y && tPoint.y - ePoint.y <= distance + tolerance)&&!enemy.playerCollide(target)) {//handles y direction depending on how close the enemy is to target
            if (tPoint.y - ePoint.y > distance) {
                enemy.setYDirection(Direction.SOUTH);
            }
            else if (tPoint.y - ePoint.y < distance) {
                enemy.setYDirection(Direction.NORTH);
            }
            linearY(speed);
        } else if (tPoint.y < ePoint.y && !(distance - tolerance <= ePoint.y - tPoint.y && ePoint.y - tPoint.y <= distance + tolerance)&&!enemy.playerCollide(target)) {
            if (ePoint.y - tPoint.y > distance) {
                enemy.setYDirection(Direction.NORTH);
            }
            else if (ePoint.y - tPoint.y < distance) {
                enemy.setYDirection(Direction.SOUTH);
            }
            linearY(speed);
        }
        if(Math.abs(tPoint.y-ePoint.y)>Math.abs(tPoint.x-ePoint.x)){    //gets direction of the shot depending on position of target
            if (tPoint.y > ePoint.y) {
                direction = Direction.SOUTH;
            } else {
                direction = Direction.NORTH;
            }

        } else {

            if (tPoint.x > ePoint.x) {
                direction = Direction.EAST;
            } else {
                direction = Direction.WEST;
            }
        }
        s = enemy.shoot(direction);
        return s.get(0);
    }

    /**
     * Changes the position of the enemy
     * @param speed ai to set to enemy
     */
    public void zigzag(int speed){

        PointF tPoint = new PointF(target.getLocation().x,target.getLocation().y);  //gets target location
        PointF ePoint = new PointF(enemy.getLocation().x,enemy.getLocation().y);    //gets enemy location
        if(Math.abs(tPoint.y-ePoint.y)>Math.abs(tPoint.x-ePoint.x)&&!enemy.playerCollide(target)){  //changes the direction if closer in x direction
            if(tPoint.y>ePoint.y){
                enemy.setYDirection(Direction.SOUTH);
                if (enemy.getDirectionX()==null)
                    enemy.setXDirection(randX(rand.nextInt(2)));
            }
            else if(tPoint.y<ePoint.y){
                enemy.setYDirection(Direction.NORTH);
                if (enemy.getDirectionX()==null)
                    enemy.setXDirection(randX(rand.nextInt(2)));
            }
            if(count==15)   //makes enemy zigzag every half second
            {
                count=0;
                if(enemy.getDirectionX()==Direction.EAST){
                    enemy.setXDirection(Direction.WEST);
                }
                else if(enemy.getDirectionX()==Direction.WEST){
                    enemy.setXDirection(Direction.EAST);
                }
            }
            else{
                count++;
            }
            linearY(speed);
            linearX(speed);
        }
        else if(Math.abs(tPoint.y-ePoint.y)<Math.abs(tPoint.x-ePoint.x)&&!enemy.playerCollide(target)){//changes the direction if closer in y direction
            if(tPoint.x>ePoint.x){
                enemy.setXDirection(Direction.EAST);
                if (enemy.getDirectionY()==null)
                    enemy.setYDirection(randY(rand.nextInt(2)));
            }
            else if(tPoint.x<ePoint.x){
                enemy.setXDirection(Direction.WEST);
                if (enemy.getDirectionY()==null)
                    enemy.setYDirection(randY(rand.nextInt(2)));
            }
            if(count==15)   //makes enemy zigzag every half second
            {
                count=0;
                if(enemy.getDirectionY()==Direction.NORTH){
                    enemy.setXDirection(Direction.SOUTH);
                }
                else if(enemy.getDirectionY()==Direction.SOUTH){
                    enemy.setXDirection(Direction.NORTH);
                }
            }
            else{
                count++;
            }
            linearY(speed);
            linearX(speed);
        }
    }
    public void teleport()
    {
        PointF point=new PointF(enemy.getLocation().x,enemy.getLocation().y);
        int randomX=0;
        int randomY=0;

    }

    /**
     * Changes the position of the enemy in the Y direction
     * @param speed ai to set to enemy
     */
    public void linearY(int speed){     //moves player to y position depending on the speed and direction
        PointF point =new PointF(enemy.getLocation().x,enemy.getLocation().y);
        if(enemy.getDirectionY()==Direction.NORTH)
            point.set(point.x, point.y - (speed / fps));
        else if(enemy.getDirectionY()==Direction.SOUTH)
            point.set(point.x , point.y + (speed / fps));

        enemy.moveTo(point);
    }

    /**
     * Changes the position of the enemy in the X direction
     * @param speed ai to set to enemy
     */
    public void linearX(int speed) {    //moves player to y position depending on the speed and direction
        PointF point =new PointF(enemy.getLocation().x,enemy.getLocation().y);

        if(enemy.getDirectionX()==Direction.EAST)
            point.set(point.x + (speed / fps), point.y);
        else if(enemy.getDirectionX()==Direction.WEST)
            point.set(point.x - (speed / fps), point.y );

        enemy.moveTo(point);
    }

    /**
     * Returns a Y direction
     * @return NORTH if num==0, else SOUTH
     */
    public Direction randY(int num) {

        if (num == 0)
            return Direction.NORTH;
        else
            return Direction.SOUTH;
    }

    /**
     * Returns a X direction
     * @return EAST if num==0, else WEST
     */
    public Direction randX(int num) {
        if (num == 0)
            return Direction.EAST;
        else
            return Direction.WEST;
    }
}
