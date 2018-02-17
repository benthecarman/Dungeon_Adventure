package sa_b_2.coms309.dungeonadventure.ui.Scenes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import java.util.List;

import sa_b_2.coms309.dungeonadventure.R;
import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.network.GetGameInvites;
import sa_b_2.coms309.dungeonadventure.network.GetRequests;
import sa_b_2.coms309.dungeonadventure.ui.Activities.AccountCreationActivity;
import sa_b_2.coms309.dungeonadventure.ui.Activities.AddFriendActivity;
import sa_b_2.coms309.dungeonadventure.ui.Activities.LoginActivity;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.Button;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.FriendsListDisplay;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.ScreenMessage;
import sa_b_2.coms309.dungeonadventure.user.Friend;

import static sa_b_2.coms309.dungeonadventure.user.User.logout;

/**
 * Scene displayed at the main menu
 */

public class MainMenuScene implements Scene {

    private final Button playButton;
    private final Button multiplayerButton;
    private final Button createAccountButton;
    private final Button loginButton;
    private final Button logoutButton;
    private final Button accountButton;
    private final Button settingsButton;
    private final Button friendsButton;
    private final Button addFriendButton;
    private final SceneManager sceneManager;
    private final Bitmap dungeonImage = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.dungeon);
    private FriendsListDisplay friendsList;
    @Nullable
    private ScreenMessage activeMessage = null;
    private boolean showFriends = false;
    private long lastCall = 0;

    MainMenuScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;

        int buttonWidth = Constants.SCREENWIDTH / 6;
        int buttonHeight = Constants.SCREENHEIGHT / 20;

        playButton = new Button(new Rect((Constants.SCREENWIDTH / 2) - buttonWidth, (Constants.SCREENHEIGHT / 2) - buttonHeight, (Constants.SCREENWIDTH / 2) + buttonWidth, (Constants.SCREENHEIGHT / 2) + buttonHeight), "Single Player Game");
        multiplayerButton = new Button(new Rect((Constants.SCREENWIDTH / 2) - buttonWidth, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 3) - buttonHeight, (Constants.SCREENWIDTH / 2) + buttonWidth, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 3) + buttonHeight), "Multiplayer Game");
        loginButton = new Button(new Rect(Constants.SCREENWIDTH - (Constants.SCREENWIDTH / 3) - buttonWidth + 5, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 3) - buttonHeight, Constants.SCREENWIDTH - (Constants.SCREENWIDTH / 3) + buttonWidth, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 3) + buttonHeight), "Login");
        createAccountButton = new Button(new Rect((Constants.SCREENWIDTH / 3) - buttonWidth, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 3) - buttonHeight, (Constants.SCREENWIDTH / 3) + buttonWidth - 5, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 3) + buttonHeight), "Create Account");

        buttonHeight *= .75;
        buttonWidth *= .75;

        Rect r = new Rect(Constants.SCREENHEIGHT / 35, Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENHEIGHT / 35 + buttonWidth * 2, Constants.SCREENHEIGHT / 15 + buttonHeight);

        accountButton = new Button(r, "");

        buttonWidth = Constants.SCREENWIDTH / 12;

        logoutButton = new Button(new Rect(Constants.SCREENHEIGHT / 35, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENHEIGHT / 35 + buttonWidth * 2, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 + buttonHeight), "Logout");
        settingsButton = new Button(new Rect(Constants.SCREENWIDTH - Constants.SCREENHEIGHT / 35 - buttonWidth * 2, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENWIDTH - Constants.SCREENHEIGHT / 35, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 + buttonHeight), "Settings");

        r.offsetTo(Constants.SCREENWIDTH - r.width() - Constants.SCREENWIDTH/35,r.top);

        friendsButton = new Button(r, "Friends");

        RectF rec = new RectF(r.left - r.width() / 2, r.top, r.left - r.width() / 20, r.bottom);
        addFriendButton = new Button(rec, "Add");

        RectF rect = friendsButton.getRect();
        RectF rectF = new RectF(rect.left,rect.top + rect.height()/2,rect.right,Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 4);

        friendsList = Constants.currentUser == null? new FriendsListDisplay(rectF,null, 7, rect.height()/2):new FriendsListDisplay(rectF,Constants.currentUser.getFriends(), 7, rect.height()/2);
    }

    @Override
    public void update() {
        if (Constants.autoLoggedIn) {
            Constants.autoLoggedIn = false;

            List<Friend> x = Constants.currentUser.getFriends();
            friendsList.setList(x);
        }
        if (Constants.currentUser != null)
            accountButton.setText(Constants.currentUser.getUsername());

        if (!Constants.screenMessages.isEmpty() && activeMessage == null)
            activeMessage = Constants.screenMessages.get(0);

        //Every 10 seconds check for friend request
        if (Constants.currentTimeMillis - lastCall >= 10000 && Constants.currentUser != null) {
            GetRequests.getRequests();
            GetGameInvites.getInvites();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //Draws background
        canvas.drawBitmap(dungeonImage, null, new Rect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT), null);

        playButton.draw(canvas);
        settingsButton.draw(canvas);
        int x = Constants.SCREENHEIGHT / 35;
        int y = Constants.SCREENHEIGHT / 15;
        int textSize = Constants.SCREENHEIGHT / 25;
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(Color.RED);
        if (Constants.currentUser != null) {
            accountButton.draw(canvas);
            logoutButton.draw(canvas);
            multiplayerButton.draw(canvas);
            if(showFriends)
                drawFriendsList(canvas);
            friendsButton.draw(canvas);
        } else {
            paint.setTypeface(Constants.font);
            canvas.drawText("Not currently logged in", x, y, paint);
            loginButton.draw(canvas);
            createAccountButton.draw(canvas);
        }

        //If the user has messages
        if (activeMessage != null) {
            //Darken Screen
            paint.setARGB(100, 0, 0, 0);
            canvas.drawRect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT, paint);
            //Draw message
            activeMessage.draw(canvas);
        }
    }

    private void drawFriendsList(@NonNull Canvas canvas){
        friendsButton.setDown(true);
        friendsList.draw(canvas);
        addFriendButton.draw(canvas);
    }

    @Override
    public void terminate() {
        sceneManager.scenes.set(SceneList.MainMenu.ordinal(),new MainMenuScene(sceneManager));
    }

    @Override
    public void receiveTouch(@NonNull MotionEvent event) {
        if (activeMessage != null) {
            if (activeMessage.onTouchEvent(event)) {
                Constants.screenMessages.remove(0);
                activeMessage = !Constants.screenMessages.isEmpty() ? Constants.screenMessages.get(0) : null;
            }
            return;
        }

        if (playButton.onTouchEvent(event)) {
            sceneManager.scenes.set(SceneList.CharacterSelection.ordinal(), new CharacterSelectionScene(sceneManager));
            SceneManager.ACTIVESCENE = SceneList.CharacterSelection;
        } else if (settingsButton.onTouchEvent(event)) {
            sceneManager.scenes.set(SceneList.Settings.ordinal(), new SettingsScene(sceneManager, this.getClass()));
            SceneManager.ACTIVESCENE = SceneList.Settings;
        }

        if (Constants.currentUser == null) {
            if (createAccountButton.onTouchEvent(event)) {
                Intent intent = new Intent(Constants.context, AccountCreationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Constants.context.startActivity(intent);
            } else if (loginButton.onTouchEvent(event)) {
                Intent intent = new Intent(Constants.context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Constants.context.startActivity(intent);
            }
        } else if (logoutButton.onTouchEvent(event)) {
            logout();
        } else if (accountButton.onTouchEvent(event)) {
            sceneManager.scenes.set(SceneList.Account.ordinal(), new AccountScene(sceneManager));
            SceneManager.ACTIVESCENE = SceneList.Account;
        } else if (multiplayerButton.onTouchEvent(event)) {
            sceneManager.scenes.set(SceneList.Lobby.ordinal(), new LobbyScene(sceneManager, true));
            SceneManager.ACTIVESCENE = SceneList.Lobby;
        } else if(friendsButton.onTouchEvent(event)){
            showFriends = !showFriends;
            if(!showFriends)
                friendsList.reset();
        } else if (showFriends) {
            friendsList.onTouchEvent(event);
            if (addFriendButton.onTouchEvent(event)) {
                Intent intent = new Intent(Constants.context, AddFriendActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Constants.context.startActivity(intent);
            }
        }
    }
}