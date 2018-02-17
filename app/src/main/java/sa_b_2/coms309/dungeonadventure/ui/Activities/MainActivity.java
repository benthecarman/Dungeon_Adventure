package sa_b_2.coms309.dungeonadventure.ui.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.network.GetFriends;
import sa_b_2.coms309.dungeonadventure.network.GetSettings;
import sa_b_2.coms309.dungeonadventure.network.HttpParse;
import sa_b_2.coms309.dungeonadventure.ui.GamePanel;
import sa_b_2.coms309.dungeonadventure.user.User;

/**
 * Main activity where the core game code is ran
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//Force game to be landscape

        Point p = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(p);
        Constants.SCREENHEIGHT = p.y;
        Constants.SCREENWIDTH = p.x;

        Constants.font = Typeface.createFromAsset(getAssets(), "Font.ttf");

        Constants.is = null;
        Constants.eis = null;
        try {
            Constants.is = new InputStreamReader(getAssets().open("ItemList"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Constants.eis = new InputStreamReader(getAssets().open("EnemyList"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constants.context = getApplicationContext();
        Constants.connected = isConnectedToInternet();
        setContentView(new GamePanel(this));

        autoLogin();
        try {
            Constants.fileOutputStream = openFileOutput(Constants.ACCOUNT_FILE, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//Force game to be landscape

        Constants.context = getApplicationContext();
        setContentView(new GamePanel(this));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null)
                return info.isConnected();
        }
        return false;
    }

    private void autoLogin() {
        ArrayList<Byte> bytes = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = openFileInput(Constants.ACCOUNT_FILE);
            while (fis.available() > 0)
                bytes.add((byte) fis.read());
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] b = new byte[bytes.size()];
        for (int z = 0; z < bytes.size(); ++z)
            b[z] = bytes.get(z);

        try {
            String string = new String(b, StandardCharsets.UTF_8);

            String username = string.split(" ", 2)[0];
            String pass = string.split(" ", 2)[1];

            UserLoginTask login = new UserLoginTask(username, pass);
            login.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @NonNull
        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", mUsername);
            map.put("password", mPassword);

            String loginURL = "http://proj-309-sa-b-2.cs.iastate.edu/Login.php";
            String finalResult = HttpParse.postRequest(map, loginURL);

            return Integer.parseInt(finalResult) == 0;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Constants.currentUser = new User(mUsername, GetFriends.getFriends(mUsername));
                GetSettings.getSettings(mUsername);
                Constants.autoLoggedIn = true;
            }
        }
    }
}
