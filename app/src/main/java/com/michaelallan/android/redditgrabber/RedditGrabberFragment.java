package com.michaelallan.android.redditgrabber;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myko on 3/14/2016.
 */
public class RedditGrabberFragment extends Fragment {
    private static final String TAG = "REDDIT_GRABBER_FRAGMENT";

    private RecyclerView mRecyclerview;
    private RedditGrabberAdapter mAdapter;
    private List<RedditLinkItems> mList;

    public static RedditGrabberFragment newInstance() {
        return new RedditGrabberFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reddit_recycler_view, container, false);
        mRecyclerview = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        LinkGenerator linkGenerator = new LinkGenerator();
        try {
            mList = linkGenerator.parseJSON();
        } catch (IOException ioe) {
            Log.d(TAG, "onCreateView: " + ioe);
        } catch (JSONException j) {
            Log.d(TAG, "onCreateView: " + j);
        }

        mAdapter = new RedditGrabberAdapter(mList);
        mRecyclerview.setAdapter(mAdapter);

        return v;
    }

    public class RedditGrabberViewHolder extends RecyclerView.ViewHolder {
        private Button mListButton;

        public RedditGrabberViewHolder(View itemView) {
            super(itemView);
            mListButton = (Button) itemView.findViewById(R.id.title_button_view);
        }

        public void bind(String title) {
            mListButton.setText(title);
        }

    }


    public class RedditGrabberAdapter extends RecyclerView.Adapter<RedditGrabberViewHolder> {

        private List<RedditLinkItems> mNumbers;

        public RedditGrabberAdapter(List<RedditLinkItems> numbers) {
            mNumbers = numbers;
        }

        @Override
        public RedditGrabberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_reddit_items, parent, false);

            return new RedditGrabberViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RedditGrabberViewHolder holder, int position) {
            holder.bind(mNumbers.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return mNumbers.size();
        }
    }
}
