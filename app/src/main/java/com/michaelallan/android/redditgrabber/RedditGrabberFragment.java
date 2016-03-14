package com.michaelallan.android.redditgrabber;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myko on 3/14/2016.
 */
public class RedditGrabberFragment extends Fragment {
    ArrayList<String> numbers;
    private RecyclerView mRecyclerview;
    private RedditGrabberAdapter mAdapter;

    public static RedditGrabberFragment newInstance() {
        return new RedditGrabberFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numbers = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            numbers.add("Number " + i);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reddit_recycler_view, container, false);
        mRecyclerview = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RedditGrabberAdapter(numbers);
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

        private List<String> mNumbers;

        public RedditGrabberAdapter(ArrayList<String> numbers) {
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
            holder.bind(mNumbers.get(position));
        }

        @Override
        public int getItemCount() {
            return mNumbers.size();
        }
    }
}
