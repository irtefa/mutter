package com.mutter.mirtefa.mutter;


import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.etsy.android.grid.ExtendableListView;
import com.etsy.android.grid.StaggeredGridView;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    StaggeredGridView mFeedGridView;
    SwipeRefreshLayout swipeLayout;
    public static FeedAdapter adapter = null;
    public static ArrayList<ParseObject> stories;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stories = new ArrayList<ParseObject>();
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat2, container, false);
        mFeedGridView = (StaggeredGridView) v.findViewById(R.id.grid_view);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        // Inflate the layout for this fragment
        adapter = new FeedAdapter(getActivity(), stories);
        mFeedGridView.setAdapter(adapter);
        mFeedGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                loadData();
            }
        });
        return v;
    }

    public void loadData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Story");
        query.orderByDescending("createdAt");
        query.setLimit(30);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    ParseObject.unpinAllInBackground();
                    stories.clear();
                    stories.addAll(parseObjects);
                    adapter.notifyDataSetChanged();
                    ParseObject.pinAllInBackground(parseObjects);
                    swipeLayout.setRefreshing(false);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    class ViewHolder {
        TextView feedItemTitle;

        public ViewHolder(View view) {
            feedItemTitle = (TextView) view.findViewById(R.id.feedItemTitle);
        }
    }

    //Adapter
    public class FeedAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<ParseObject> mStories;

        public FeedAdapter(Context c, ArrayList<ParseObject> items) {
            inflater = ( LayoutInflater )c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mStories = items;
        }

        public int getCount() {
            return mStories.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = inflater.inflate(R.layout.feed_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder.feedItemTitle.setText(mStories.get(position).get("Title").toString());
            return convertView;
        }
    }
}
