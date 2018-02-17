package sa_b_2.coms309.dungeonadventure.ui.Scenes;


import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.game.Direction;
import sa_b_2.coms309.dungeonadventure.game.Player;
import sa_b_2.coms309.dungeonadventure.game.Shot;
import sa_b_2.coms309.dungeonadventure.network.GameAction;
import sa_b_2.coms309.dungeonadventure.network.ServerConnection;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.AnalogStick;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.Button;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.HealthBar;
import sa_b_2.coms309.dungeonadventure.user.UserSettings;

/**
 * Scene that is used during a multiplayer game
 */
public class MultiplayerScene implements Scene {

    private final List<AnalogStick> sticks = new LinkedList<>();
    private Button resumeButton, returnButton, settingsButton, restartButton, itemResumeButton;
    private Button inv0, inv1, inv2, inv3, inv4, inv5, inv6, inv7, inv8;
    private SceneManager sceneManager;
    private Player player;
    private List<Shot> shots;
    private HealthBar healthBar;
    private RectF rectF;
    private RectF rectItem;
    private GameplayScene gameplayScene = null;
    private int playerID;
    private ServerConnection serverConnection;

    public MultiplayerScene(SceneManager sceneManager, int ID) {
        this.sceneManager = sceneManager;
        this.playerID = ID;

        int size = (int) (Constants.SCREENHEIGHT / (UserSettings.controllerSize));
        sticks.add(0, new AnalogStick(new Point(size + 5, Constants.SCREENHEIGHT - (size + 5)), size));
        sticks.add(1, new AnalogStick(new Point(Constants.SCREENWIDTH - (size + 5), Constants.SCREENHEIGHT - (size + 5)), size));

        makeButtons();

        serverConnection = new ServerConnection(ServerConnection.GAME_PORT);
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

    //ltrb
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

    @Override
    public void update() {

        serverConnection.sendObject(new GameAction(playerID, player, shots));
        shots = new LinkedList<>();


        gameplayScene = (GameplayScene) serverConnection.receiveObject();
    }

    @Override
    public void draw(Canvas canvas) {
        gameplayScene.draw(canvas);

        for (AnalogStick a : sticks)
            a.draw(canvas);

    }

    @Override
    public void terminate() {
        SceneManager.ACTIVESCENE = SceneList.MainMenu;
        sceneManager.scenes.set(SceneList.Gameplay.ordinal(), null);
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        doSticks(event);
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
}
