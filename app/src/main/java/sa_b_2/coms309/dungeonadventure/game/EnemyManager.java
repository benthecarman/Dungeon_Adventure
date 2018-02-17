package sa_b_2.coms309.dungeonadventure.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;

import static sa_b_2.coms309.dungeonadventure.game.Constants.context;


/**
 * Created by Armand on 10/2/2017.
 */

public class EnemyManager {
    public List<Enemy> enemies;
    ItemManager im;
    Paint paint = new Paint();
    private List<Enemy> enemyList;
    //    private List<Enemy> enemiesInRoom;
    private Enemy enemy;
    private Random rand;
    private int count=0;
    private int invincibility=45;

    /**
     * Makes an enemyManager, used to group enemies and draw them
     * @param is used to make items from a text document
     * @param im used to make enemies drop items
     */
    public EnemyManager(InputStreamReader is, ItemManager im) {
        enemies = new LinkedList<>();
        this.im = im;
        rand = new Random();
        if (Constants.enemyList == null)
            populateEnemies(is);
        enemyList = new LinkedList<>(Constants.enemyList);
        populateRoom();
        paint.setColor(Color.WHITE);
    }

    /**
     * Selects random enemy from list of possible enemies
     * @param enemies list of room items
     * @return random enemy
     */
    public static Enemy randomEnemy(List<Enemy> enemies) {      //returns a random enemy from list of all enemies
        Random rand = new Random();
        int size = enemies.size();
        int index = rand.nextInt(size);
        Enemy randomEnemy = enemies.get(index);
        return randomEnemy;
    }

    /**
     * Populates enemyList for all possible enemies from text document
     * @param is used to connect to document
     */
    public void populateEnemies(InputStreamReader is) { //takes input of Enemy.txt
        Constants.enemyList = new LinkedList<>();

        Scanner scan = new Scanner(is);

        while (scan.hasNext()) {    //scans through the text and sets variables
            String line = scan.nextLine();
            if (line.equals("ENEMY")) {
                line = scan.nextLine();
                String name = "";
                int health = 0;
                int shotSpeed = 0;
                int shotDamage = 0;
                float shotSize = 0;
                int range = 0;
                Bitmap image = null;
                while (!line.equals("END") && scan.hasNext()) {
                    if (line.equals("Name:")) {
                        line = scan.nextLine();
                        name = line;
                    }
                    if (line.equals("Health:")) {
                        line = scan.nextLine();
                        health = Integer.parseInt(line);
                    }
                    if (line.equals("ShotSpeed:")) {
                        line = scan.nextLine();
                        shotSpeed = Integer.parseInt(line);
                    }
                    if (line.equals("ShotDamage:")) {
                        line = scan.nextLine();
                        shotDamage = Integer.parseInt(line);
                    }
                    if (line.equals("ShotSize:")) {
                        line = scan.nextLine();
                        shotSize = Float.parseFloat(line);
                    }
                    if (line.equals("Range:")) {
                        line = scan.nextLine();
                        range = Integer.parseInt(line);
                    }
                    if (line.equals("Image:")) {
                        line = scan.nextLine();
                        int resId = context.getResources().getIdentifier(line, "drawable", context.getPackageName());
                        image = BitmapFactory.decodeResource(context.getResources(), resId);
                    }
                    if (scan.hasNext()) {
                        line = scan.nextLine();
                    }
                }
                boolean valid = false;
                PointF point = new PointF();
                while (!valid) {    //gives random position to enemy
                    point.x = rand.nextInt(Constants.GAMEWIDTH);
                    point.y = rand.nextInt(Constants.GAMEHEIGHT);
                    enemy = new Enemy(health, shotSpeed, shotDamage, (short) shotSize, (short) range, point, name, image);
                    if (!Overlap(enemy) || !(point.x + enemy.getRect().width() > Constants.GAMEWIDTH || point.x - enemy.getRect().width() < Constants.GAMEWIDTH
                            || point.y + enemy.getRect().height() > Constants.GAMEHEIGHT || point.y - enemy.getRect().height() < Constants.GAMEHEIGHT)) {
                        valid = true;
                    }
                }
                Constants.enemyList.add(enemy);     //adds to the list of all enemies
            }
        }
        scan.close();
    }

    /**
     * Checks if player collides with any enemies. If so, gives player damage and gives it invincibility for a period of time
     * @param player player to be checked
     * @param currentRoomEnemies enemies in the room to be checked
     * @return true if player collides, else false
     */
    public boolean playerCollide(Player player, boolean gotShot, List<Enemy> currentRoomEnemies) {  //determines if player was hit
        for (Enemy e : currentRoomEnemies) {
            if ((e.playerCollide(player) || gotShot) && (count == invincibility || count == 0)) {
                player.hit((float) e.getShotDamage());
                count=1;
                return true;
            }
        }
        if(count<invincibility&&count!=0) { //gives player invincibility period
            count++;
            player.setInvincible(!player.isInvincible());
        }

        return false;
    }

    /**
     * Used to ensure no two enemies intersect
     * @param enemy enemy to be compared to
     * @return true if they overlap, else false
     */
    public boolean Overlap(Enemy enemy) {   //returns whether the enemy overlaps with any other enemy
        for (Enemy e : Constants.enemyList) {
            if (e.getRect().intersect(enemy.getRect())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Populates the room with random enemies
     */
    public void populateRoom() {    //populates the room with random enemies
        int ePerRoom = 0;
        while (ePerRoom < 4) {
            Enemy temp = randomEnemy(enemyList);
            enemies.add(temp);
            ePerRoom++;
        }
    }
    /**
     * Used to get a random list of enemies
     * @return list of random enemies
     */
    public List<Enemy> populateRooms() {    //returns a list of enemies to put in a room
       int ePerRoom = 0;
        List<Enemy> enemiesInRoom = new LinkedList<>();
        while (ePerRoom < 4) {
            Enemy temp = randomEnemy(enemyList);
            enemiesInRoom.add(temp);
            ePerRoom++;
        }
        return enemiesInRoom;
    }

    /**
     * Used to draw the enemies in the room
     * @param canvas canvas to be drawn on
     * @param enemiesInRoom enemies to be drawn
     */
    public void draw(Canvas canvas, List<Enemy> enemiesInRoom) {   //draws enemies
        for (Enemy e : enemiesInRoom) {
            e.draw(canvas);
        }
    }

    /**
     * updates the the enemies movements and targets
     * @param player player to target
     * @param enemiesInRoom enemies to be updated
     *
     */
    public void update(Player player, List<Enemy> enemiesInRoom) {

        for (Enemy e : enemiesInRoom) {
            if (e.getTarget() == null) {    //sets targets and ai of enemies
                e.setTarget(player);
                EnemyAi ai = new EnemyAi(e);
                e.setEnemyAi(ai);
            }
            e.update();
            if (!e.isAlive()) {
                dropItem(e);
            }
        }

        ListIterator<Enemy> characterListIterator = enemiesInRoom.listIterator(); //gets rid of enemies if killed
        while (characterListIterator.hasNext())
            if (!characterListIterator.next().isAlive()) {
                characterListIterator.remove();

            }

    }

    /**
     * Used to drop random item when enemy is killed
     * @param e enemy to drop item
     */
    public void dropItem(Enemy e) {     //drops random item from list
        int x = 40;
        Item i = ItemManager.randomItem(im.getItemList());
        i.setRectangle((int) e.getLocation().x, (int) e.getLocation().y, (int) e.getLocation().x + x, (int) e.getLocation().y + x);
        List<Item> il = im.getItems();
        il.add(i);
        im.setItems(il);
    }

}
