package sa_b_2.coms309.dungeonadventure.network;


import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Gets the settings from the given User
 */
public class GetSettings {

    /**
     * Gets the settings from the given User
     *
     * @param username username of who's settings should be retrieved
     */
    public static void getSettings(String username) {

        AsyncSettings getSettings = new AsyncSettings();

        String string;
        try {
            string = getSettings.execute(username).get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        SettingsMsg sm = new Gson().fromJson(string, SettingsMsg.class);
        SettingsMsg.setSettings(sm);
    }

    private static class AsyncSettings extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> map = new HashMap<>();
            map.put("username", strings[0]);

            String getSettingsURL = "http://proj-309-sa-b-2.cs.iastate.edu/GetSettings.php";
            return HttpParse.postRequest(map, getSettingsURL);
        }
    }
}
