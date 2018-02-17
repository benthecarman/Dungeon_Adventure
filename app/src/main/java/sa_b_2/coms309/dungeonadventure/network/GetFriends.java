package sa_b_2.coms309.dungeonadventure.network;


import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

import sa_b_2.coms309.dungeonadventure.user.Friend;

/**
 * Gets the current friend list from the current user's online friends
 */
public class GetFriends {

    /**
     * Gets the current friend list from the current user's online friends
     *
     * @param username username of the user's who's friends should be retrieved
     * @return List of friend objects contain all the friends
     */
    public static List<Friend> getFriends(String username) {

        AsyncFriends getFriends = new AsyncFriends();

        String string;
        try {
            string = getFriends.execute(username).get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        String[] friends = string.split(",");

        List<String> x = new LinkedList<>(Arrays.asList(friends));
        x.remove(0);
        x.remove(0);
        x.remove(null);

        List<Friend> z = new LinkedList<>();

        for (String s : x) {
            if (s.equals(""))
                continue;
            Friend f = new Friend(Integer.parseInt(s), false);//TODO: check online
            f.setUsername(FriendRequestMsg.GetUser(f.getUserID()));
            if (!z.contains(f))
                z.add(f);
        }

        List<Friend> i = new LinkedList<>();

        for(Friend f: z)
            if(f.isOnline()) {
                i.add(f);
            }

        ListIterator<Friend> iterator = z.listIterator();
        while (iterator.hasNext())
            if (i.contains(iterator.next()))
                iterator.remove();

        i.addAll(z);

        return i;
    }

    private static class AsyncFriends extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> map = new HashMap<>();
            map.put("username", strings[0]);

            String getFriendsURL = "http://proj-309-sa-b-2.cs.iastate.edu/GetFriends.php";
            return HttpParse.postRequest(map, getFriendsURL);
        }
    }
}