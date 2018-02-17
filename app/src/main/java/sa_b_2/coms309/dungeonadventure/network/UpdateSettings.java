package sa_b_2.coms309.dungeonadventure.network;


import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Sets the current settings of a user to the settings saved on the server
 */
public class UpdateSettings {

    /**
     * Sets the current settings of a user to the settings saved on the server
     *
     * @param sm Settings to be set
     * @return if it was successful
     */
    public static boolean updateSettings(SettingsMsg sm) {

        AsyncSettings updateSettings = new AsyncSettings();

        try {
            return updateSettings.execute(sm).get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static class AsyncSettings extends AsyncTask<SettingsMsg, Void, Boolean> {

        @NonNull
        @Override
        protected Boolean doInBackground(SettingsMsg... sm) {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", sm[0].getUsername());
            map.put("showStats", Boolean.toString(sm[0].isShowStats()));
            map.put("controllerSize", "" + sm[0].getControllerSize());
            map.put("musicVolumePercent", "" + sm[0].getMusicVolumePercent());
            map.put("gameVolumePercent", "" + sm[0].getGameVolumePercent());
            map.put("mute", Boolean.toString(sm[0].isMute()));

            String updateSettingsURL = "http://proj-309-sa-b-2.cs.iastate.edu/UpdateSettings.php";
            String result = HttpParse.postRequest(map, updateSettingsURL);
            return Integer.parseInt(result) == 0;
        }
    }
}