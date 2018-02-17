package sa_b_2.coms309.dungeonadventure.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Friend object containing the friends username and ID
 */
public class Friend {
    private final int userID;
    private String username;
    private boolean isOnline;

    public Friend(int userID, boolean isOnline) {
        this.isOnline = isOnline;
        this.userID = userID;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @NonNull
    @Override
    public String toString() {
        return username + ": " + (isOnline?"Online":"Offline");
    }

    public int getUserID() {
        return userID;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        Friend f = (Friend) obj;

        return this.userID == f.userID;
    }
}