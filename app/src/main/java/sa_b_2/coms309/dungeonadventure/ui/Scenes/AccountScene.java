package sa_b_2.coms309.dungeonadventure.ui.Scenes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.R;
import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.ui.Activities.ChangeEmailActivity;
import sa_b_2.coms309.dungeonadventure.ui.Activities.ChangePasswordActivity;
import sa_b_2.coms309.dungeonadventure.ui.Activities.ChangeUsernameActivity;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.Button;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.ScreenMessage;

/**
 * Scene displayed where you manage your account
 */
public class AccountScene implements Scene {

    private final SceneManager sceneManager;
    private final Button backButton, changePasswordButton, changeEmailButton, changeUsernameButton;
    private final Bitmap dungeonImage = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.dungeon);

    AccountScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;

        int buttonWidth = Constants.SCREENWIDTH / 6;
        int buttonHeight = Constants.SCREENHEIGHT / 20;

        changePasswordButton = new Button(new Rect((Constants.SCREENWIDTH / 2) - buttonWidth, (Constants.SCREENHEIGHT / 3) - buttonHeight, (Constants.SCREENWIDTH / 2) + buttonWidth, (Constants.SCREENHEIGHT / 3) + buttonHeight), "Change Password");
        changeEmailButton = new Button(new Rect((Constants.SCREENWIDTH / 2) - buttonWidth, Constants.SCREENHEIGHT / 2 - buttonHeight, (Constants.SCREENWIDTH / 2) + buttonWidth, Constants.SCREENHEIGHT / 2 + buttonHeight), "Change Email");
        changeUsernameButton = new Button(new Rect((Constants.SCREENWIDTH / 2) - buttonWidth, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 3) - buttonHeight, (Constants.SCREENWIDTH / 2) + buttonWidth, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 3) + buttonHeight), "Change Username");

        buttonWidth = Constants.SCREENWIDTH / 12;
        buttonHeight = Constants.SCREENHEIGHT / 25;

        backButton = new Button(new Rect(Constants.SCREENWIDTH / 35, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENWIDTH / 35 + buttonWidth, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 + buttonHeight), "Done");
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect r = new Rect(0,0,Constants.SCREENWIDTH,Constants.SCREENHEIGHT);

        //Draws the dungeon background
        canvas.drawBitmap(dungeonImage, null, r, null);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTypeface(Constants.font);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Constants.SCREENHEIGHT / 9);
        canvas.drawText(Constants.currentUser.getUsername(), Constants.SCREENWIDTH / 2, Constants.SCREENHEIGHT / 5, paint);

        backButton.draw(canvas);
        changePasswordButton.draw(canvas);
        changeEmailButton.draw(canvas);
        changeUsernameButton.draw(canvas);
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVESCENE = SceneList.MainMenu;
        sceneManager.scenes.set(SceneList.Account.ordinal(), null);
    }

    @Override
    public void receiveTouch(@NonNull MotionEvent event) {
        if (backButton.onTouchEvent(event))
            terminate();
        else if (changePasswordButton.onTouchEvent(event)) {
            Intent intent = new Intent(Constants.context, ChangePasswordActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Constants.context.startActivity(intent);
        } else if (changeEmailButton.onTouchEvent(event)) {
            Intent intent = new Intent(Constants.context, ChangeEmailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Constants.context.startActivity(intent);
        } else if (changeUsernameButton.onTouchEvent(event)) {
            Intent intent = new Intent(Constants.context, ChangeUsernameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Constants.context.startActivity(intent);
            Constants.screenMessages.add(new ScreenMessage("You were logged out."));
            terminate();
        }
    }
}