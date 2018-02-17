package sa_b_2.coms309.dungeonadventure.ui.Scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import sa_b_2.coms309.dungeonadventure.R;
import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.game.PlayableCharacter;
import sa_b_2.coms309.dungeonadventure.network.ClientThread;
import sa_b_2.coms309.dungeonadventure.network.LobbyMessage;
import sa_b_2.coms309.dungeonadventure.network.ServerConnection;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.Button;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.FriendOverlay;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.ScreenMessage;

/**
 * Scene displayed when in a lobby before a multiplayer game
 */
public class LobbyScene implements Scene {

    private final Bitmap dungeonImage = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.dungeon);
    private final Button backButton, startButton, nextButton, previousButton, inviteButton;
    private final SceneManager sceneManager;
    private final FriendOverlay friendOverlay;
    private String otherUserName = "Loading...";
    private int myPosition = 0;
    private int theirPosition = -1;
    private List<Bitmap> characterImages;
    private boolean playerJoined = false;
    private boolean showOverlay = false;
    private boolean isHost;
    private ServerConnection serverConnection;
    private long lastCall = 0;
    private ClientThread clientThread;

    public LobbyScene(SceneManager sceneManager, boolean isHost) {
        this.sceneManager = sceneManager;
        this.isHost = isHost;

        float buttonWidth = Constants.SCREENWIDTH / 12;
        float buttonHeight = Constants.SCREENHEIGHT / 25;

        clientThread = new ClientThread();
        new Thread(new ClientThread()).start();

        startButton = new Button(new RectF(Constants.SCREENWIDTH - Constants.SCREENHEIGHT / 35 - buttonWidth * 2, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENWIDTH - Constants.SCREENHEIGHT / 35, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 + buttonHeight), "Start Game");
        backButton = new Button(new RectF(Constants.SCREENWIDTH / 35, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENWIDTH / 35 + buttonWidth, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 + buttonHeight), "Cancel");

        RectF r = new RectF(Constants.SCREENWIDTH / 20, Constants.SCREENHEIGHT / 20, Constants.SCREENWIDTH / 2 - Constants.SCREENWIDTH / 100, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 20);
        if (!isHost) {
            playerJoined = true;
            nextButton = new Button(new RectF(r.right - buttonWidth + (Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, r.centerX() + buttonHeight, r.right + (Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, r.centerX() - buttonHeight), "Next");
            previousButton = new Button(new RectF(r.left + (Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, r.centerX() + buttonHeight, r.left + (Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2 + buttonWidth, r.centerX() - buttonHeight), "Previous");
        } else {
            nextButton = new Button(new RectF(r.right - buttonWidth, r.centerX() + buttonHeight, r.right, r.centerX() - buttonHeight), "Next");
            previousButton = new Button(new RectF(r.left, r.centerX() + buttonHeight, r.left + buttonWidth, r.centerX() - buttonHeight), "Previous");
        }
        r.offset((Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, 0);
        inviteButton = new Button(new RectF(r.left + r.width() / 5, r.top + r.height() / 13 - buttonHeight, r.right - r.width() / 5, r.top + r.height() / 13 + buttonHeight), "Invite Friend");

        characterImages = new ArrayList<>();
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.slime));
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.demon));
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.goblin));
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.wolf));
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.ghost));

        friendOverlay = new FriendOverlay();
        // serverConnection = new ServerConnection(ServerConnection.LOBBY_PORT);

        //if (!isHost)
//            serverConnection.sendObject(new LobbyMessage(Constants.currentUser.getUsername(), myPosition, 1));
    }

    @Override
    public void update() {
        if (!isHost && !playerJoined) {
            Constants.screenMessages.add(new ScreenMessage("Host has disbanded the lobby"));
            terminate();
        }

        if (clientThread.getLobby() != null) {
            if (isHost) {
                otherUserName = clientThread.getLobby().name1;
                theirPosition = clientThread.getLobby().character1;
            } else {
                otherUserName = clientThread.getLobby().name0;
                theirPosition = clientThread.getLobby().character0;
            }
        }

       /* if(Constants.currentTimeMillis - lastCall > 10){
            LobbyMessage lobbyMessage = (LobbyMessage) serverConnection.receiveObject();
            if(lobbyMessage != null) {
                theirPosition = lobbyMessage.getCharacter();
                otherUserName = lobbyMessage.getUsername();
            }
        }*/
    }

    @Override
    public void draw(Canvas canvas) {
        //Draw Background
        canvas.drawBitmap(dungeonImage, null, new Rect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT), null);

        //Shade player's sections
        Paint paint = new Paint();
        paint.setARGB(120, 0, 0, 0);
        RectF r = new RectF(Constants.SCREENWIDTH / 20, Constants.SCREENHEIGHT / 20, Constants.SCREENWIDTH / 2 - Constants.SCREENWIDTH / 100, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 20);
        canvas.drawRoundRect(r, 100, 100, paint);
        r.offset((Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, 0);
        canvas.drawRoundRect(r, 100, 100, paint);

        if (isHost)
            hostDraw(canvas);
        else
            memberDraw(canvas);
    }

    private void memberDraw(Canvas canvas) {
        RectF r = new RectF(Constants.SCREENWIDTH / 20, Constants.SCREENHEIGHT / 20, Constants.SCREENWIDTH / 2 - Constants.SCREENWIDTH / 100, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 20);

        //Draw Players Names
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Constants.font);
        paint.setTextSize(Constants.SCREENHEIGHT / 20);
        canvas.drawText(Constants.currentUser.getUsername(), r.centerX() + (Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, r.top + r.height() / 10, paint);
        canvas.drawText(otherUserName, r.centerX(), r.top + r.height() / 10, paint);

        //Draw Character Selection
        RectF f = new RectF(r);
        f.inset(r.width() / 2 - r.width() / 6, r.height() / 2 - r.height() / 6);
        if (playerJoined && theirPosition != -1) {
            canvas.drawBitmap(characterImages.get(theirPosition), null, f, null);
            canvas.drawText(PlayableCharacter.values()[theirPosition].toString(), f.centerX() + (Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, r.bottom - r.height() / 4, paint);
        }
        f.offset((Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, 0);
        canvas.drawBitmap(characterImages.get(myPosition), null, f, null);
        canvas.drawText(PlayableCharacter.values()[myPosition].toString(), f.centerX(), r.bottom - r.height() / 4, paint);

        //Draw Buttons
        backButton.draw(canvas);
        startButton.draw(canvas);
        nextButton.draw(canvas);
        previousButton.draw(canvas);
    }

    private void hostDraw(Canvas canvas) {
        RectF r = new RectF(Constants.SCREENWIDTH / 20, Constants.SCREENHEIGHT / 20, Constants.SCREENWIDTH / 2 - Constants.SCREENWIDTH / 100, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 20);
        r.offset((Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, 0);
        //Draw Players Names
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Constants.font);
        paint.setTextSize(Constants.SCREENHEIGHT / 20);
        canvas.drawText(Constants.currentUser.getUsername(), r.centerX() - (Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / 2, r.top + r.height() / 10, paint);
        if (playerJoined)
            canvas.drawText(otherUserName, r.centerX(), r.top + r.height() / 10, paint);

        //Draw Character Selection
        RectF f = new RectF(r);
        f.inset(r.width() / 2 - r.width() / 6, r.height() / 2 - r.height() / 6);
        if (playerJoined && theirPosition != -1) {
            canvas.drawBitmap(characterImages.get(theirPosition), null, f, null);
            canvas.drawText(PlayableCharacter.values()[theirPosition].toString(), f.centerX(), r.bottom - r.height() / 4, paint);
        }
        f.offset((Constants.SCREENWIDTH - Constants.SCREENWIDTH / 20) / -2, 0);
        canvas.drawBitmap(characterImages.get(myPosition), null, f, null);
        canvas.drawText(PlayableCharacter.values()[myPosition].toString(), f.centerX(), r.bottom - r.height() / 4, paint);

        //Draw Buttons
        backButton.draw(canvas);
        startButton.draw(canvas);
        nextButton.draw(canvas);
        previousButton.draw(canvas);
        if (!playerJoined)
            inviteButton.draw(canvas);

        if (showOverlay) {
            paint.setARGB(100, 0, 0, 0);
            canvas.drawRect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT, paint);

            friendOverlay.draw(canvas);
            if (!Constants.screenMessages.isEmpty())
                Constants.screenMessages.get(0).draw(canvas);
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVESCENE = SceneList.MainMenu;
        sceneManager.scenes.set(SceneList.Lobby.ordinal(), null);
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (!Constants.screenMessages.isEmpty()) {
            Constants.screenMessages.get(0).onTouchEvent(event);
            return;
        }

        if (backButton.onTouchEvent(event))
            terminate();
        else if (startButton.onTouchEvent(event)) {
            serverConnection.sendObject(new LobbyMessage("0", -1, -1));
            sceneManager.scenes.set(SceneList.Multiplayer.ordinal(), new MultiplayerScene(sceneManager, isHost ? 0 : 1));
            SceneManager.ACTIVESCENE = SceneList.Multiplayer;
            sceneManager.scenes.set(SceneList.CharacterSelection.ordinal(), null);
        } else if (previousButton.onTouchEvent(event))
            previousCharacter();
        else if (nextButton.onTouchEvent(event))
            nextCharacter();
        else if (showOverlay && friendOverlay.onTouchEvent(event))
            showOverlay = false;
        else if (!playerJoined && inviteButton.onTouchEvent(event))
            showOverlay = true;
    }

    private void nextCharacter() {
        int i = myPosition;
        i = fixNum(++i);
        if (i == theirPosition)
            i = fixNum(++i);
        myPosition = i;

//        serverConnection.sendObject(new LobbyMessage(Constants.currentUser.getUsername(), myPosition, isHost?0:1));
    }

    private void previousCharacter() {
        int i = myPosition;
        i = fixNum(--i);
        if (i == theirPosition)
            i = fixNum(--i);
        myPosition = i;

        //   serverConnection.sendObject(new LobbyMessage(Constants.currentUser.getUsername(), myPosition, isHost?0:1));
    }

    private int fixNum(int i) {
        if (i == PlayableCharacter.values().length)
            return 0;
        else if (i == -1)
            return PlayableCharacter.values().length - 1;
        return i;
    }
}
