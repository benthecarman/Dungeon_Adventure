package sa_b_2.coms309.dungeonadventure.ui.Scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import sa_b_2.coms309.dungeonadventure.R;
import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.game.Direction;
import sa_b_2.coms309.dungeonadventure.game.Door;
import sa_b_2.coms309.dungeonadventure.game.Dungeon;
import sa_b_2.coms309.dungeonadventure.game.Enemy;
import sa_b_2.coms309.dungeonadventure.game.EnemyManager;
import sa_b_2.coms309.dungeonadventure.game.Floor;
import sa_b_2.coms309.dungeonadventure.game.ItemManager;
import sa_b_2.coms309.dungeonadventure.game.PlayableCharacter;
import sa_b_2.coms309.dungeonadventure.game.Player;
import sa_b_2.coms309.dungeonadventure.game.Room;
import sa_b_2.coms309.dungeonadventure.game.Shot;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.AnalogStick;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.Button;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.HealthBar;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.ItemsButton;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.PauseButton;
import sa_b_2.coms309.dungeonadventure.user.UserSettings;

/**
 * Scene displayed during a single player game
 */
public class GameplayScene implements Scene {

    private final List<AnalogStick> sticks;
    private final List<Shot> shots;
    private final List<Enemy> enemies;
    private final ItemManager itemManager;
    private final EnemyManager enemyManager;
    private final PauseButton pauseButton;
    private final ItemsButton itemsButton;
    private final SceneManager sceneManager;
    @Nullable
    private Player player;
    private Button resumeButton, returnButton, settingsButton, restartButton, itemResumeButton;
    private Button inv0, inv1, inv2, inv3, inv4, inv5, inv6, inv7, inv8;
    private boolean isPaused = false;
    private boolean isItemPaused = false;
    private RectF rectF;
    private RectF rectItem;
    private boolean gameOver = false;
    private HealthBar healthBar;
    private Bitmap floorImage = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.floor);
    private Dungeon dungeon;
    private Floor currentFloor;
    private Room currentRoom;
    private int exitTime = 10;
    private int count = 0;
    private Point currentRoomCoords;

    private boolean isObject = false;

    GameplayScene(SceneManager sceneManager, PlayableCharacter playableCharacter) {
        this.sceneManager = sceneManager;
        sticks = new LinkedList<>();
        shots = new LinkedList<>();
        enemies = new LinkedList<>();
        itemManager = new ItemManager(Constants.is);
        enemyManager = new EnemyManager(Constants.eis, itemManager);
        pauseButton = new PauseButton((short) (Constants.SCREENHEIGHT / 20));
        itemsButton = new ItemsButton((short) (Constants.SCREENHEIGHT / 20));
        int size = (int) (Constants.SCREENHEIGHT / (UserSettings.controllerSize));
        sticks.add(0, new AnalogStick(new Point(size + 5, Constants.SCREENHEIGHT - (size + 5)), size));
        sticks.add(1, new AnalogStick(new Point(Constants.SCREENWIDTH - (size + 5), Constants.SCREENHEIGHT - (size + 5)), size));
        player = Player.makePlayer(playableCharacter);
        healthBar = new HealthBar(player, HealthBar.HealthBarType.Player);
        makeButtons();
        //makeItemButtons();
        dungeon = new Dungeon(enemyManager);
        currentFloor = dungeon.getFloor(0);
        currentRoom = currentFloor.getStart();
        currentRoomCoords = currentRoom.getCoords();
    }

    private void makeButtons() {
        rectF = new RectF(Constants.SCREENWIDTH / 4, Constants.SCREENHEIGHT / 7, Constants.SCREENWIDTH - (Constants.SCREENWIDTH / 4), Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 7);

        RectF rect = new RectF((float) (rectF.left + (rectF.width() * .9)), rectF.top + (rectF.height() / 6), (float) (rectF.right - (rectF.width() * .9)), rectF.top + (rectF.height() / 3));
        settingsButton = new Button(rect, "Settings");
        rect.offset(0, rect.height() + 5);
        returnButton = new Button(rect, "Return to main menu");
        restartButton = new Button(rect, "Restart");
        rect.offset(0, rect.height() + 5);
        resumeButton = new Button(rect, "Resume");
    }

    /**
     * Buttons for inventory menu
     */
    private void makeItemButtons() {
        rectItem = new RectF(Constants.SCREENWIDTH / 4, (float) (Constants.SCREENHEIGHT * .05), Constants.SCREENWIDTH - (Constants.SCREENWIDTH / 4), (float) (Constants.SCREENHEIGHT - Constants.SCREENHEIGHT * .05));
        RectF rect = new RectF((float) (rectItem.left + (rectItem.width() * .7)), (float) (rectItem.top + (rectItem.height() * .8)), (float) (rectItem.right - (rectItem.width() * .7)), (float) (rectItem.top + (rectItem.height() * .95)));
        itemResumeButton = new Button(rect, "Resume");
        rect = new RectF((float) (rectItem.left + (rectItem.width() * .1)), (float) (rectItem.top + (rectItem.height() * .1)), (float) (rectItem.left + (rectItem.width() * .3)), (float) (rectItem.top + (rectItem.height() * .28)));
        inv0 = new Button(rect, "");
        rect.offset(0, (float) (rect.height() + rectItem.height() * .065));
        inv3 = new Button(rect, "");
        rect.offset(0, (float) (rect.height() + rectItem.height() * .065));
        inv6 = new Button(rect, "");

        rect = new RectF((float) (rectItem.centerX() - (rectItem.width() * .1)), (float) (rectItem.top + (rectItem.height() * .1)), (float) (rectItem.centerX() + (rectItem.width() * .1)), (float) (rectItem.top + (rectItem.height() * .28)));
        inv1 = new Button(rect, "");
        rect.offset(0, (float) (rect.height() + rectItem.height() * .065));
        inv4 = new Button(rect, "");
        rect.offset(0, (float) (rect.height() + rectItem.height() * .065));
        inv7 = new Button(rect, "");

        rect = new RectF((float) (rectItem.right - (rectItem.width() * .3)), (float) (rectItem.top + (rectItem.height() * .1)), (float) (rectItem.right - (rectItem.width() * .1)), (float) (rectItem.top + (rectItem.height() * .28)));
        inv2 = new Button(rect, "");
        rect.offset(0, (float) (rect.height() + rectItem.height() * .065));
        inv5 = new Button(rect, "");
        rect.offset(0, (float) (rect.height() + rectItem.height() * .065));
        inv8 = new Button(rect, "");

    }

    private void resetSticks() {
        int size = (int) (Constants.SCREENHEIGHT / (UserSettings.controllerSize));
        sticks.set(0, new AnalogStick(new Point(size + 5, Constants.SCREENHEIGHT - (size + 5)), size));
        sticks.set(1, new AnalogStick(new Point(Constants.SCREENWIDTH - (size + 5), Constants.SCREENHEIGHT - (size + 5)), size));
    }

    @Override
    public void update() {
        Constants.currentTimeMillis = System.currentTimeMillis();
//        currentRoom=floor.getCurrRoom();

        boolean gotShot = false;


        if (isPaused || isItemPaused || player == null)
            return;

        player.update();
        enemyManager.update(player, currentRoom.getRoomEnemies(), currentRoom);

        if (currentRoom.getRoomEnemies().size() == 0) {
            currentRoom.setCleared(true);
        }

        for (Shot s : shots) {
            s.update();
            for (Enemy e : currentRoom.getRoomEnemies()) {
                if (s.shotCollide(player) && s.getOwner() == e) {
                    gotShot = true;
                } else if (s.shotCollide(e) && s.getOwner() == player) {
                    e.hit(s.getDamage());
                }
            }
        }

        for (Shot s1 : shots)
            for (Shot s2 : shots) {
                if (s1 != s2)
                    s1.shotCollide(s2);
            }


        enemyManager.playerCollide(player, gotShot, currentRoom.getRoomEnemies());
        itemManager.playerCollide(player);

        healthBar.update();

        ListIterator<Shot> shotListIterator = shots.listIterator();
        while (shotListIterator.hasNext())
            if (shotListIterator.next().isOutOfRange())
                shotListIterator.remove();

        if (!player.isAlive()) {
            player = null;
            gameOver = true;
        }
        if (currentRoom.isCleared() && currentRoom.isExit()) {
            if (currentRoom.getStairs().playerCollide(player) && currentFloor.getFloorNumber() < dungeon.getFloors().length - 1) {
                currentFloor = dungeon.getFloor(currentFloor.getFloorNumber() + 1);
                currentRoom = currentFloor.getStart();
                currentRoomCoords = currentRoom.getCoords();
            } else if (currentRoom.getStairs().playerCollide(player) && currentFloor.getFloorNumber() == dungeon.getFloors().length - 1) {
                terminate();
            }
        }
        for (Door d : currentRoom.getDoors()) {

            if (d.playerCollide(player) && currentRoom.isCleared() && count >= exitTime) {

                currentRoomCoords.y += d.nextRoom().y;
                currentRoomCoords.x += d.nextRoom().x;
                currentFloor.setRoom(currentRoomCoords);
                currentRoom = currentFloor.getCurrRoom();
                player.moveTo(d.getOppositeLocation());
                count = 0;

            } else if (!d.playerCollide(player)) {
                if (count < exitTime)
                    count++;
            }
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        List<Shot> eShots;
//        Room currentRoom=floor.getCurrRoom();
        currentRoom.draw(canvas);
//        Rect r = new Rect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT);
//        //Draws the dungeon floor background
//        canvas.drawBitmap(floorImage, null, r, null);
        itemManager.draw(canvas);
        enemyManager.draw(canvas, currentRoom.getRoomEnemies());


        if (player != null)
            player.draw(canvas);

        for (Enemy e : currentRoom.getRoomEnemies()) {
            eShots = e.getShots();
            for (Shot s : eShots) {
                if (s != null) {
                    shots.add(s);
                }
            }
        }

        for (Shot s : shots)
            s.draw(canvas);
        if (!isObject)
            for (AnalogStick a : sticks)
                a.draw(canvas);

        pauseButton.draw(canvas);
        itemsButton.draw(canvas);
        drawStats(canvas);

        healthBar.draw(canvas);
        if (player.hasItem("X-Ray Goggles"))
            for (Enemy e : enemyManager.enemies)
                new HealthBar(e, HealthBar.HealthBarType.Enemy).draw(canvas);


        if (isPaused) {
            Paint paint = new Paint();
            //Darkens screen when paused
            paint.setARGB(100, 0, 0, 0);
            canvas.drawRect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT, paint);

            //Creates menu
            paint.setARGB(255, 216, 142, 67);
            canvas.drawRoundRect(rectF, Constants.SCREENWIDTH / 20, Constants.SCREENHEIGHT / 20, paint);

            //Draws buttons
            returnButton.draw(canvas);
            resumeButton.draw(canvas);
            settingsButton.draw(canvas);
        }

        if (isItemPaused) {
            Paint paint = new Paint();

            paint.setARGB(100, 0, 0, 0);
            canvas.drawRect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT, paint);

            paint.setARGB(255, 216, 142, 67);
            canvas.drawRoundRect(rectItem, Constants.SCREENWIDTH / 20, Constants.SCREENHEIGHT / 20, paint);

            itemResumeButton.draw(canvas);

            if (player.getInventorySize() >= 1) {
                inv0.draw(canvas);
                if (player.getPlayerItems().get(0) != null) {
                    Bitmap item = player.getPlayerItems().get(0).getImage();
                    canvas.drawBitmap(item, null, inv0.getRect(), null);
                }
            }

            if (player.getInventorySize() >= 2) {
                inv1.draw(canvas);
                if (player.getPlayerItems().get(1) != null) {
                    Bitmap item = player.getPlayerItems().get(1).getImage();
                    canvas.drawBitmap(item, null, inv1.getRect(), null);
                }
            }

            if (player.getInventorySize() >= 3) {
                inv2.draw(canvas);
                if (player.getPlayerItems().get(2) != null) {
                    Bitmap item = player.getPlayerItems().get(2).getImage();
                    canvas.drawBitmap(item, null, inv2.getRect(), null);
                }
            }

            if (player.getInventorySize() >= 4) {
                inv3.draw(canvas);
                if (player.getPlayerItems().get(3) != null) {
                    Bitmap item = player.getPlayerItems().get(3).getImage();
                    canvas.drawBitmap(item, null, inv3.getRect(), null);
                }
            }

            if (player.getInventorySize() >= 5) {
                inv4.draw(canvas);
                if (player.getPlayerItems().get(4) != null) {
                    Bitmap item = player.getPlayerItems().get(4).getImage();
                    canvas.drawBitmap(item, null, inv4.getRect(), null);
                }
            }

            if (player.getInventorySize() >= 6) {
                inv5.draw(canvas);
                if (player.getPlayerItems().get(5) != null) {
                    Bitmap item = player.getPlayerItems().get(5).getImage();
                    canvas.drawBitmap(item, null, inv5.getRect(), null);
                }
            }

            if (player.getInventorySize() >= 7) {
                inv6.draw(canvas);
                if (player.getPlayerItems().get(6) != null) {
                    Bitmap item = player.getPlayerItems().get(6).getImage();
                    canvas.drawBitmap(item, null, inv6.getRect(), null);
                }
            }

            if (player.getInventorySize() >= 8) {
                inv7.draw(canvas);
                if (player.getPlayerItems().get(7) != null) {
                    Bitmap item = player.getPlayerItems().get(7).getImage();
                    canvas.drawBitmap(item, null, inv7.getRect(), null);
                }
            }

            if (player.getInventorySize() >= 9) {
                inv8.draw(canvas);
                if (player.getPlayerItems().get(8) != null) {
                    Bitmap item = player.getPlayerItems().get(8).getImage();
                    canvas.drawBitmap(item, null, inv8.getRect(), null);
                }
            }
        }

        if (gameOver) {
            Paint paint = new Paint();
            //Darkens screen
            paint.setARGB(100, 0, 0, 0);
            canvas.drawRect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT, paint);
            //Creates menu
            paint.setARGB(255, 216, 142, 67);
            canvas.drawRoundRect(rectF, Constants.SCREENWIDTH / 20, Constants.SCREENHEIGHT / 20, paint);
            restartButton.draw(canvas);
        }
    }

    /**
     * Draws the players stats in side bar
     *
     * @param canvas canvas to be drawn to
     */
    private void drawStats(@NonNull Canvas canvas) {
        if (!UserSettings.showStats || player == null)
            return;

        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);

        int x = Constants.SCREENWIDTH / 45;
        int y = Constants.SCREENHEIGHT / 15;
        int textSize = Constants.SCREENHEIGHT / 25;

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(textSize);
        String temp = "Name: ";
        temp += player.getName();
        canvas.drawText(temp, x, y * 2, paint);
        temp = "Health: ";
        temp += player.getCurrentHealth() + "/" + player.getHealth();
        canvas.drawText(temp, x, y * 3, paint);
        temp = "Shot Speed: ";
        temp += player.getShotSpeed();
        canvas.drawText(temp, x, y * 4, paint);
        temp = "Shot Damage: ";
        temp += player.getShotDamage();
        canvas.drawText(temp, x, y * 5, paint);
        temp = "Shot Size: ";
        temp += player.getShotSize();
        canvas.drawText(temp, x, y * 6, paint);
        temp = "Range: ";
        temp += player.getRange();
        canvas.drawText(temp, x, y * 7, paint);
        temp = "Move Speed: ";
        temp += player.getMoveSpeed();
        canvas.drawText(temp, x, y * 8, paint);
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVESCENE = SceneList.MainMenu;
        sceneManager.scenes.set(SceneList.Gameplay.ordinal(), null);
    }

    @Override
    public void receiveTouch(@NonNull MotionEvent event) {
        boolean pause = pauseButton.onTouchEvent(event);
        boolean itemPause = itemsButton.onTouchEvent(event);
        if (!isPaused && !isItemPaused) {
            doSticks(event);
            if (pause)
                pause();
            else if (itemPause) {
                itemPause();
            } else if (gameOver) {
                if (restartButton.onTouchEvent(event))
                    restart();
            }
        } else if (isPaused) {
            if (resumeButton.onTouchEvent(event))
                unPause();
            if (returnButton.onTouchEvent(event))
                terminate();
            if (settingsButton.onTouchEvent(event)) {
                sceneManager.scenes.set(SceneList.Settings.ordinal(), new SettingsScene(sceneManager, this.getClass()));
                SceneManager.ACTIVESCENE = SceneList.Settings;
            }
            if (pause)
                unPause();
        } else if (isItemPaused) {
            if (itemResumeButton.onTouchEvent(event))
                unPause();
        }
    }

    private void pause() {
        isPaused = true;
    }

    private void itemPause() {
        isItemPaused = true;
        makeItemButtons();
    }

    private void unPause() {
        resetSticks();
        isPaused = false;
        isItemPaused = false;
    }

    private void restart() {
        terminate();
    }

    /**
     * Handles stick movement
     *
     * @param event MotionEvent from android api
     */
    private void doSticks(@NonNull MotionEvent event) {
        AnalogStick stick1 = sticks.get(1);
        stick1.onTouchEvent(event);
        AnalogStick stick2 = sticks.get(0);
        stick2.onTouchEvent(event);

        if (player == null)
            return;

        if (stick1.isTouched()) {
            Direction direction = stick1.getDirection4();
            //When item added
            /*if(hasItem statement)
                direction = stick1.getDirection8();*/
            if (direction != null) {
                List<Shot> s = player.shoot(direction);
                if (!s.isEmpty())
                    shots.addAll(s);
            }
        }

        if (stick2.isTouched()) {
            double angle = Math.toRadians(stick2.getAngle());
            double percentDist = stick2.getDistance() / stick2.getRadius();
            percentDist = (percentDist > 1) ? 1 : percentDist;

            int moveDistance = (int) (percentDist * player.getMoveSpeed());

            PointF newPos = new PointF((int) (-moveDistance * Math.sin(angle)) + player.getLocation().x, (int) (moveDistance * Math.cos(angle)) + player.getLocation().y);

            player.moveTo(newPos);
        }
    }

    public void setIsObject(boolean c) {
        isObject = c;
    }
}
