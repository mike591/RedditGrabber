package com.michaelallan.android.redditgrabber;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Myko on 3/14/2016.
 */
public class RedditGrabberFragment extends Fragment {

    public static RedditGrabberFragment newInstance() {
        return new RedditGrabberFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public class RedditGrabberViewHolder extends RecyclerView.ViewHolder {
        public RedditGrabberViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RedditGrabberAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

}
