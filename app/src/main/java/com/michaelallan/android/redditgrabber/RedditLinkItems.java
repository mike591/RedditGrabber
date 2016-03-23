package com.michaelallan.android.redditgrabber;

/**
 * Created by Myko on 3/18/2016.
 */
public class RedditLinkItems {

    private String mTitle;
    private String mUrl;


    public RedditLinkItems(String title){
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
