package sa_b_2.coms309.dungeonadventure.game;

import android.content.Context;
import android.graphics.Typeface;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import sa_b_2.coms309.dungeonadventure.ui.Scenes.SceneManager;
import sa_b_2.coms309.dungeonadventure.ui.ScreenObjects.ScreenMessage;
import sa_b_2.coms309.dungeonadventure.user.User;

/**
 * Constants that are used throughout the game
 */

public class Constants {

    public static final String ACCOUNT_FILE = "account";
    public static int SCREENHEIGHT, SCREENWIDTH;
    public static int GAMEHEIGHT = 900;
    public static int GAMEWIDTH = 1600;
    public static InputStreamReader is;
    public static InputStreamReader eis;
    public static Context context;
    public static boolean connected = false;
    public static User currentUser = null;
    public static List<Item> itemList = null;
    public static List<Enemy> enemyList = null;
    public static FileOutputStream fileOutputStream = null;
    public static Typeface font = Typeface.DEFAULT;
    public static boolean autoLoggedIn = false;
    public static long currentTimeMillis = System.currentTimeMillis();

    public static List<ScreenMessage> screenMessages = new LinkedList<>();
    public static SceneManager sceneManager;

    public static float gameToDrawX(float x){
        return (x / GAMEWIDTH) * SCREENWIDTH;
    }

    public static float gameToDrawY(float y){
        return (y / GAMEHEIGHT) * SCREENHEIGHT;
    }
}