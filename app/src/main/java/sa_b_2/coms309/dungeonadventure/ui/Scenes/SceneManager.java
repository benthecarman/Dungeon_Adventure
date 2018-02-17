package sa_b_2.coms309.dungeonadventure.ui.Scenes;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Handles which scene is being displayed and updated currently
 */
public class SceneManager {
    @NonNull
    public static SceneList ACTIVESCENE = SceneList.MainMenu;
    public final List<Scene> scenes;

    public SceneManager() {
        scenes = new LinkedList<>();
        scenes.add(SceneList.MainMenu.ordinal(), new MainMenuScene(this));
        scenes.add(SceneList.Gameplay.ordinal(), null);
        scenes.add(SceneList.Account.ordinal(), null);
        scenes.add(SceneList.Settings.ordinal(), null);
        scenes.add(SceneList.CharacterSelection.ordinal(), null);
        scenes.add(SceneList.Lobby.ordinal(), null);
        scenes.add(SceneList.Multiplayer.ordinal(), null);
    }

    public void update() {
        scenes.get(ACTIVESCENE.ordinal()).update();
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVESCENE.ordinal()).draw(canvas);
    }

    public void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVESCENE.ordinal()).receiveTouch(event);
    }
}
