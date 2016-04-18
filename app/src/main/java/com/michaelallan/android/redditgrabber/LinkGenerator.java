package com.michaelallan.android.redditgrabber;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myko on 3/18/2016.
 */
public class LinkGenerator {
    private static final String TAG = "LINK_GENERATOR";

    public static byte[] getURL(String subreddit) throws IOException {

        URL url = new URL("https://www.reddit.com/"+subreddit+".json");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(30000);
        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(TAG + " no connection yo!");
        }
        try {
            InputStream in = urlConnection.getInputStream();
            byte[] URLBuffer = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int bytesRead = 0;
            while ((bytesRead = in.read(URLBuffer)) > 0) {
                out.write(URLBuffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String getJsonString(String subreddit) throws IOException {
        return new String(getURL(subreddit));
    }

    public static List<RedditLinkItems> parseJSON(String subreddit) {
        List<RedditLinkItems> links = new ArrayList<>();

        try {
            byte[] bytes = getURL(subreddit);
            JSONObject jBody = new JSONObject(getJsonString(subreddit));
            JSONObject jData = jBody.getJSONObject("data");
            JSONArray JChildren = jData.getJSONArray("children");

            for (int i = 0; i < JChildren.length(); i++) {
                JSONObject redditLinkDataObjects = JChildren.getJSONObject(i).getJSONObject("data");
                RedditLinkItems redditLinkItems = new RedditLinkItems(redditLinkDataObjects.getString("title"));
                redditLinkItems.setUrl("https://m.reddit.com"+redditLinkDataObjects.getString("permalink"));
                links.add(redditLinkItems);
            }
        } catch (IOException ioe) {
            Log.d(TAG, "parseJSON: " + ioe);
        } catch (JSONException j) {
            Log.d(TAG, "parseJSON: " + j);
        }

        Log.i(TAG, "parseJson Size is: " + links.size());
        return links;
    }
}


