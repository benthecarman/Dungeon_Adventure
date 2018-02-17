package sa_b_2.coms309.dungeonadventure.game;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;

import sa_b_2.coms309.dungeonadventure.R;


public class Room implements GameObject{
    private List<Enemy> enemies;
    private Player player;
    private Bitmap floorImage = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.floor);
    private ItemManager itemManager;
    private EnemyManager enemyManager;
    private boolean cleared;
    private List<Door> doors;
    private Point coords;
    private boolean exit;
    private Stairs stairs;

    /**
     * Constructs a room to add to the floor
     * @param enemies enemies to be in the room
     * @param coords coordinates of the room on the floor
     * @param exit sets the room to an exit if true
     */
    public Room(List<Enemy> enemies, Point coords, boolean exit) {
        this.enemies=enemies;
        cleared=false;
        doors=new LinkedList<>();
        this.coords = coords;
        this.exit = exit;
        if (exit) {
            stairs = new Stairs();
        }
    }

    /**
     * Constructs a empty room to add to the floor
     */
    public Room() {
        enemies = new LinkedList<>();
        cleared=true;
        doors=new LinkedList<>();
        coords = new Point(7, 7);
    }

    /**
     * Returns the enemies in the room
     * @return enemies in room
     */
    public List<Enemy> getRoomEnemies(){return enemies;}

    /**
     * Returns if the room is cleared
     * @return if the room is cleared
     */
    public boolean isCleared(){return cleared;}

    /**
     * Sets if the room is cleared
     * @param  cleared set to cleared
     */
    public void setCleared(boolean cleared){this.cleared=cleared;}

    /**
     * Returns if the room is an exit
     * @return if the room is an exit
     */
    public boolean isExit() {
        return exit;
    }

    /**
     * Returns the room coordinates
     * @return the room coordinates
     */
    public Point getCoords() {
        return coords;
    }

    /**
     * Returns the doors in the room
     * @return list doors in the room
     */
    public List<Door> getDoors() {
        return doors;
    }

    /**
     * Returns the stairs in the room
     * @return stairs in the room
     */
    public Stairs getStairs() {
        return stairs;
    }

    /**
     * Adds a door to list of doors
     * @return stairs in the room
     */
    public void addDoor(Door door){doors.add(door);}

    /**
     * Not used
     */
    @Override
    public void update(){

    }

    /**
     * Draws room background and doors
     * @param  canvas canvas to be drawn on
     */
    @Override
    public void draw(Canvas canvas){
        Rect r = new Rect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
        canvas.drawBitmap(floorImage, null, r, null);
        //canvas.drawColor(Color.WHITE);
//        itemManager.draw(canvas);
//        enemyManager.draw(canvas);
//
//        if(player != null)
//            player.draw(canvas);
        if (stairs != null) {
            stairs.draw(canvas);
        }
        for(Door d:doors){
            d.draw(canvas);
        }
    }

    /**
     * Not used
     */
    @Override
    public PointF getLocation(){
        return null;
    }
}
