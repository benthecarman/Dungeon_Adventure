package sa_b_2.coms309.dungeonadventure.ui.Scenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import sa_b_2.coms309.dungeonadventure.R;
import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.network.SettingsMsg;
import sa_b_2.coms309.dungeonadventure.network.UpdateSettings;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.Button;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.SettingsButton;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.Slider;
import sa_b_2.coms309.dungeonadventure.user.UserSettings;

/**
 * Scene that handles changing the current user's settings
 */
class SettingsScene implements Scene {

    private final SceneManager sceneManager;
    private final SettingsMsg sm;
    private final Class callingClass;
    private final Button backButton;
    private final Button defaultButton;
    private final SettingsButton showStatsButton;
    private final SettingsButton muteButton;
    private final Slider uiSizeSlider;
    private final Slider gameVolumeSlider;
    private final Slider musicVolumeSlider;
    private Bitmap dungeonImage = BitmapFactory.decodeResource(Constants.context.getResources(), R.drawable.dungeon);

    SettingsScene(SceneManager sceneManager, Class callingClass) {

        sm = new SettingsMsg();
        sm.getSettings();

        this.sceneManager = sceneManager;
        this.callingClass = callingClass;

        int buttonWidth = Constants.SCREENWIDTH / 6;
        int buttonHeight = Constants.SCREENHEIGHT / 20;

        Rect rect = new Rect(Constants.SCREENWIDTH / 3 + buttonWidth, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 9 + buttonHeight, Constants.SCREENWIDTH / 3 - buttonWidth, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 9 - buttonHeight);
        showStatsButton = new SettingsButton(rect, "Showing Stats", true);
        rect.offset(Constants.SCREENWIDTH / 3 + 5, 0);
        muteButton = new SettingsButton(rect, "Muted", true);

        rect.offset(0, (int) (-Constants.SCREENHEIGHT / 4.5));
        rect.offsetTo(Constants.SCREENWIDTH / 2 - buttonWidth, rect.top);
        uiSizeSlider = new Slider(rect, "Controller Size: ", (int) ((UserSettings.controllerSize - 10) / (-0.055)));

        rect.offset(0, (int) (-Constants.SCREENHEIGHT / 4.5));
        gameVolumeSlider = new Slider(rect, "Game Volume: ", (int) (UserSettings.gameVolumePercent * 100));

        rect.offset(0, (int) (-Constants.SCREENHEIGHT / 4.5));
        musicVolumeSlider = new Slider(rect, "Music Volume: ", (int) (UserSettings.musicVolumePercent * 100));

        buttonWidth = Constants.SCREENWIDTH / 12;
        buttonHeight = Constants.SCREENHEIGHT / 25;

        backButton = new Button(new Rect(Constants.SCREENWIDTH / 35, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENWIDTH / 35 + buttonWidth, Constants.SCREENHEIGHT - Constants.SCREENHEIGHT / 15 + buttonHeight), "Done");
        defaultButton = new Button(new Rect(Constants.SCREENWIDTH - Constants.SCREENWIDTH / 35 - buttonWidth * 3, Constants.SCREENHEIGHT / 15 - buttonHeight, Constants.SCREENWIDTH - Constants.SCREENWIDTH / 35, Constants.SCREENHEIGHT / 15 + buttonHeight), "Reset back to default");
        setButtons();
    }

    private void setButtons() {
        if (UserSettings.showStats) {
            showStatsButton.setText("Showing Stats");
            showStatsButton.setOn(true);
        } else {
            showStatsButton.setText("Not Showing Stats");
            showStatsButton.setOn(false);
        }
        if (UserSettings.mute) {
            muteButton.setText("Muted");
            muteButton.setOn(false);
        } else {
            muteButton.setText("Mute");
            muteButton.setOn(true);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect r = new Rect(0,0,Constants.SCREENWIDTH,Constants.SCREENHEIGHT);
        canvas.drawBitmap(dungeonImage, null, r, null);
        //canvas.drawColor(Color.WHITE);

        showStatsButton.draw(canvas);
        muteButton.draw(canvas);
        backButton.draw(canvas);
        uiSizeSlider.draw(canvas);
        gameVolumeSlider.draw(canvas);
        musicVolumeSlider.draw(canvas);
        defaultButton.draw(canvas);
    }

    @Override
    public void terminate() {
        saveSettings();
        SettingsMsg temp = new SettingsMsg();
        temp.getSettings();

        if (!temp.equals(sm) && Constants.currentUser != null)
            if (!UpdateSettings.updateSettings(temp))
                throw new RuntimeException();

        if (callingClass == MainMenuScene.class)
            SceneManager.ACTIVESCENE = SceneList.MainMenu;
        else if (callingClass == GameplayScene.class)
            SceneManager.ACTIVESCENE = SceneList.Gameplay;
        else
            return;

        sceneManager.scenes.set(SceneList.Settings.ordinal(), null);
    }

    @Override
    public void receiveTouch(@NonNull MotionEvent event) {
        if (backButton.onTouchEvent(event))
            terminate();
        else if (showStatsButton.onTouchEvent(event)) {
            UserSettings.showStats = !UserSettings.showStats;
            setButtons();
        } else if (muteButton.onTouchEvent(event)) {
            UserSettings.mute = !UserSettings.mute;
            setButtons();
        } else if (uiSizeSlider.onTouchEvent(event))
            UserSettings.controllerSize = (float) (-0.055 * (uiSizeSlider.getSpot())) + 10;
        else if (gameVolumeSlider.onTouchEvent(event))
            UserSettings.gameVolumePercent = gameVolumeSlider.getSpot() / (float) 100.0;
        else if (musicVolumeSlider.onTouchEvent(event))
            UserSettings.musicVolumePercent = musicVolumeSlider.getSpot() / (float) 100.0;
        else if (defaultButton.onTouchEvent(event))
            resetSettings();
    }

    private void resetSettings() {
        UserSettings.controllerSize = 7;
        UserSettings.musicVolumePercent = 1;
        UserSettings.gameVolumePercent = 1;
        UserSettings.mute = false;
        UserSettings.showStats = true;
        setButtons();

        uiSizeSlider.setSpot((int) ((UserSettings.controllerSize - 10) / (-0.055)));
        gameVolumeSlider.setSpot(100);
        musicVolumeSlider.setSpot(100);
    }

    private void saveSettings() {
        UserSettings.controllerSize = (float) (-0.055 * (uiSizeSlider.getSpot())) + 10;
        UserSettings.gameVolumePercent = gameVolumeSlider.getSpot() / (float) 100.0;
        UserSettings.musicVolumePercent = musicVolumeSlider.getSpot() / (float) 100.0;
        UserSettings.mute = !muteButton.getOn();
        UserSettings.showStats = showStatsButton.getOn();
    }
}
