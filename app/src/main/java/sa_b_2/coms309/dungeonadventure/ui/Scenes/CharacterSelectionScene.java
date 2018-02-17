package sa_b_2.coms309.dungeonadventure.ui.Scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import sa_b_2.coms309.dungeonadventure.R;
import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.game.PlayableCharacter;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.Button;

/**
 * Scene displayed where you select your character before a game
 */
public class CharacterSelectionScene implements Scene {

    private final SceneManager sceneManager;
    private final Button backButton, previousButton, nextButton, startGameButton;
    private final Bitmap dungeonImage = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.dungeon);
    private int current = 0;
    private List<Bitmap> characterImages;

    CharacterSelectionScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;

        int buttonWidth = Constants.SCREENWIDTH / 12;
        int buttonHeight = Constants.SCREENHEIGHT / 25;

        backButton = new Button(new Rect(Constants.SCREENWIDTH / 35, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENWIDTH / 35 + buttonWidth, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 + buttonHeight), "Back");
        nextButton = new Button(new Rect(Constants.SCREENWIDTH - Constants.SCREENHEIGHT / 35 - buttonWidth * 2, Constants.SCREENHEIGHT / 2 - buttonHeight, Constants.SCREENWIDTH - Constants.SCREENHEIGHT / 35, Constants.SCREENHEIGHT / 2 + buttonHeight), "Next");
        previousButton = new Button(new Rect(Constants.SCREENWIDTH / 35, Constants.SCREENHEIGHT / 2 - buttonHeight, Constants.SCREENWIDTH / 35 + buttonWidth * 2, Constants.SCREENHEIGHT / 2 + buttonHeight), "Previous");
        startGameButton = new Button(new Rect((Constants.SCREENWIDTH / 2) - buttonWidth, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 9) - buttonHeight, (Constants.SCREENWIDTH / 2) + buttonWidth, Constants.SCREENHEIGHT - (Constants.SCREENHEIGHT / 9) + buttonHeight), "Start Game");

        characterImages = new ArrayList<>();
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.slime));
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.demon));
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.goblin));
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.wolf));
        characterImages.add(BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.ghost));
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //Draws background
        canvas.drawBitmap(dungeonImage, null, new Rect(0, 0, Constants.SCREENWIDTH, Constants.SCREENHEIGHT), null);
        backButton.draw(canvas);
        nextButton.draw(canvas);
        previousButton.draw(canvas);
        startGameButton.draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTypeface(Constants.font);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Constants.SCREENHEIGHT / 15);

        canvas.drawText(PlayableCharacter.values()[current].toString(), Constants.SCREENWIDTH / 2, Constants.SCREENHEIGHT / 7, paint);

        Bitmap image = characterImages.get(current);

        if (image != null) {
            Rect dest = new Rect(Constants.SCREENWIDTH / 2 - Constants.SCREENWIDTH / 6, Constants.SCREENHEIGHT / 2 - (Constants.SCREENHEIGHT / 4), Constants.SCREENWIDTH / 2 + Constants.SCREENWIDTH / 6, Constants.SCREENHEIGHT / 2 + Constants.SCREENHEIGHT / 4);
            canvas.drawBitmap(image, null, dest, null);
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVESCENE = SceneList.MainMenu;
        sceneManager.scenes.set(SceneList.CharacterSelection.ordinal(), null);
    }

    @Override
    public void receiveTouch(@NonNull MotionEvent event) {
        if (backButton.onTouchEvent(event))
            terminate();
        else if (nextButton.onTouchEvent(event)) {
            current = fixNum(++current);
        } else if (previousButton.onTouchEvent(event)) {
            current = fixNum(--current);
        } else if (startGameButton.onTouchEvent(event)) {
            sceneManager.scenes.set(SceneList.Gameplay.ordinal(), new GameplayScene(sceneManager, PlayableCharacter.values()[current]));
            SceneManager.ACTIVESCENE = SceneList.Gameplay;
            sceneManager.scenes.set(SceneList.CharacterSelection.ordinal(), null);
        }
    }

    private int fixNum(int i) {
        if (i == PlayableCharacter.values().length)
            return 0;
        else if (i == -1)
            return PlayableCharacter.values().length - 1;
        return i;
    }
}