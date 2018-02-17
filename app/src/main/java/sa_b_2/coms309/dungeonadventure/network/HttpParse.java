package sa_b_2.coms309.dungeonadventure.network;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import sa_b_2.coms309.dungeonadventure.game.Constants;

/**
 * Handles http requests
 */
public class HttpParse {

    /**
     * Handles http requests
     * @param Data          Map of data to be added to http url
     * @param HttpUrlHolder The http url
     * @return What the server returns
     */
    public static String postRequest(@NonNull HashMap<String, String> Data, String HttpUrlHolder) {

        String FinalHttpData;

        if (!Constants.connected)
            return "Not connected to the internet";
        try {
            URL url = new URL(HttpUrlHolder);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(14000);

            httpURLConnection.setConnectTimeout(14000);

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoInput(true);

            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            bufferedWriter.write(FinalDataParse(Data));

            bufferedWriter.flush();

            bufferedWriter.close();

            outputStream.close();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                FinalHttpData = bufferedReader.readLine();
                bufferedReader.close();
            } else
                FinalHttpData = "Something Went Wrong";

        } catch (Exception e) {
            e.printStackTrace();
            return "Not connected to the internet";
        }

        return FinalHttpData;
    }

    private static String FinalDataParse(@NonNull HashMap<String, String> hashMap2) throws UnsupportedEncodingException {

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> map_entry : hashMap2.entrySet()) {
            stringBuilder.append("&");
            stringBuilder.append(URLEncoder.encode(map_entry.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(map_entry.getValue(), "UTF-8"));
        }

        return stringBuilder.toString();
    }
}