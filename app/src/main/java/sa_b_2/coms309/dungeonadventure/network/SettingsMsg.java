package sa_b_2.coms309.dungeonadventure.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import sa_b_2.coms309.dungeonadventure.game.Constants;
import sa_b_2.coms309.dungeonadventure.user.UserSettings;

/**
 * Object to be sent to server containing the user's settings
 */
public class SettingsMsg {
    private float controllerSize;
    private float musicVolumePercent;
    private float gameVolumePercent;
    private int mute;
    private int showStats;
    private String username;
    private int accountID;

    public SettingsMsg() {

    }

    /**
     * Sets the users current settings to the given SettingsMsg object
     *
     * @param sm given settings
     */
    static void setSettings(@NonNull SettingsMsg sm) {
        UserSettings.showStats = sm.showStats == 1;
        UserSettings.controllerSize = sm.controllerSize;
        UserSettings.gameVolumePercent = sm.gameVolumePercent;
        UserSettings.musicVolumePercent = sm.musicVolumePercent;
        UserSettings.mute = sm.mute == 1;
    }

    /**
     * Sets the SettingsMsg object variables to the current user's settings
     */
    public void getSettings() {
        this.username = Constants.currentUser != null ? Constants.currentUser.getUsername() : "";
        this.showStats = UserSettings.showStats ? 1 : 0;
        this.mute = UserSettings.mute ? 1 : 0;
        this.gameVolumePercent = UserSettings.gameVolumePercent;
        this.musicVolumePercent = UserSettings.musicVolumePercent;
        this.controllerSize = UserSettings.controllerSize;
    }

    float getControllerSize() {
        return controllerSize;
    }

    float getMusicVolumePercent() {
        return musicVolumePercent;
    }

    float getGameVolumePercent() {
        return gameVolumePercent;
    }

    boolean isMute() {
        return mute == 1;
    }

    boolean isShowStats() {
        return showStats == 1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        SettingsMsg sm = (SettingsMsg) obj;

        return this.username.equals(sm.username) &&
                this.musicVolumePercent == sm.musicVolumePercent &&
                this.gameVolumePercent == sm.gameVolumePercent &&
                this.controllerSize == sm.controllerSize &&
                this.mute == sm.mute &&
                this.showStats == sm.showStats;
    }

    //Used for debugging
    @NonNull
    @Override
    public String toString() {
        return username + ": cS = " + controllerSize + ", gV = " + gameVolumePercent + ", mV = " + musicVolumePercent + ", sS = " + showStats + ", m = " + mute;
    }
}