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

    public LinkGenerator() {

    }

    public byte[] getURL() throws IOException {

        URL url = new URL("https://www.reddit.com/r/all.json");
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
            while((bytesRead = in.read(URLBuffer)) > 0) {
                out.write(URLBuffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            urlConnection.disconnect();
        }
    }

    public List<RedditLinkItems> parseJSON() throws JSONException, IOException{
        byte[] bytes = getURL();
        JSONObject jBody = new JSONObject(bytes.toString());
        JSONObject jTitle = jBody.getJSONObject("data");
        JSONArray jsonArray = jTitle.getJSONArray("data");
        List<RedditLinkItems> links = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject titleObject = jsonArray.getJSONObject(i);
            RedditLinkItems title = new RedditLinkItems(titleObject.getString("title"));
            links.add(title);
        }

        return links;
    }


}


