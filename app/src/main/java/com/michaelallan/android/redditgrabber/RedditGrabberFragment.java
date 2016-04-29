package com.michaelallan.android.redditgrabber;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myko on 3/14/2016.
 */
public class RedditGrabberFragment extends Fragment {
    private static final String TAG = "REDDIT_GRABBER_FRAGMENT";
    public static final String LINK_URL = "LINK_URL";

    private RecyclerView mRecyclerview;
    private RedditGrabberAdapter mAdapter;
    private List<RedditLinkItems> mList;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mNavLinearLayout;
    private Button mNextButton;
    private Button mPrevButton;

    private static String subreddit;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    public static RedditGrabberFragment newInstance() {
        return new RedditGrabberFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<RedditLinkItems>();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_reddit_recycler_view, container, false);
        new getRedditInfo().execute();

        mNavLinearLayout = (LinearLayout) v.findViewById(R.id.navigation_button);
        mNextButton = (Button) v.findViewById(R.id.next_button);
        mPrevButton = (Button) v.findViewById(R.id.prev_button);

        mDrawerLayout = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(getActivity(), "Drawer Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText(getActivity(), "Drawer Close", Toast.LENGTH_SHORT).show();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerList = (ListView) v.findViewById(R.id.left_drawer);
        final String[] mDrawerItems = getResources().getStringArray(R.array.subreddit);
        mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, mDrawerItems));
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subreddit = mDrawerItems[position];
                Log.i(TAG, "onItemClick: " + subreddit);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                new getRedditInfo().execute();
                updateUI();
            }
        });

        mRecyclerview = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mLayoutManager);

        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerview.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (totalItemCount <= (firstVisibleItem + visibleItemCount)) {
                    // End has been reached

                    Log.i(TAG, "end called");

                    mRecyclerview.setPadding(0, 0, 0, 150);
                    mNavLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    mNavLinearLayout.setVisibility(View.INVISIBLE);
                    mRecyclerview.setPadding(0, 0, 0, 0);
                }
            }
        });

        updateUI();
        mDrawerToggle.syncState();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_reddit_recycler_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_item_refresh:
                new getRedditInfo().execute();
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (subreddit == null) {
            subreddit = "/r/all";
        }
        actionBar.setTitle(subreddit);

        if (mAdapter == null) {
            mAdapter = new RedditGrabberAdapter(mList);
            mRecyclerview.setAdapter(mAdapter);
        } else {
            mAdapter.updateList(mList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public class RedditGrabberViewHolder extends RecyclerView.ViewHolder {
        private Button mListButton;



        public RedditGrabberViewHolder(View itemView) {
            super(itemView);
            mListButton = (Button) itemView.findViewById(R.id.title_button_view);
            mListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(LINK_URL, mList.get(getAdapterPosition()).getUrl());
                    startActivity(intent);
                }
            });
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

        public void updateList(List<RedditLinkItems> list) {
            mNumbers = list;
        }
    }

    public class getRedditInfo extends AsyncTask<Void, Void, List<RedditLinkItems>> {

        @Override
        protected List<RedditLinkItems> doInBackground(Void... params) {
            if (subreddit == null) {
                return LinkGenerator.parseJSON("/r/all");
            }
            return LinkGenerator.parseJSON(subreddit);
        }

        @Override
        protected void onPostExecute(List<RedditLinkItems> redditLinkItemses) {
            Log.i(TAG, "onPostExecute: redditLinkItemses size is " + redditLinkItemses.size());
            mList = redditLinkItemses;
            Log.i(TAG, "onPostExecute: mList size is " + mList.size());
            updateUI();
        }
    }
}



