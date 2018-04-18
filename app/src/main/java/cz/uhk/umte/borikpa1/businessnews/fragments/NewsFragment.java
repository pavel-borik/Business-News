package cz.uhk.umte.borikpa1.businessnews.fragments;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.adapters.NewsItemsRecyclerViewAdapter;
import cz.uhk.umte.borikpa1.businessnews.model.NewsItem;
import cz.uhk.umte.borikpa1.businessnews.utils.XmlDownloadHelper;


public class NewsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private NewsItemsRecyclerViewAdapter adapter;
    private List<NewsItem> newsItemList;
    private static final String ARG_URL = "url";
    private String mUrl;
    private SwipeRefreshLayout swipeContainer;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(String url) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }

        new RssFeedRetriever().execute(mUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_news, container, false);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RssFeedRetriever().execute(mUrl);
            }
        });

        adapter = new NewsItemsRecyclerViewAdapter(newsItemList, pos -> {
            String url = newsItemList.get(pos).getLink();
            if(url != null) {
                if(URLUtil.isValidUrl(url)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_newslist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }

    private class RssFeedRetriever extends AsyncTask<String, Void, List<NewsItem>> {
        @Override
        protected List<NewsItem> doInBackground(String... urls) {
            try {
                return XmlDownloadHelper.loadXmlFromNetwork(urls[0]);
            } catch (IOException | XmlPullParserException e) {
                Log.e("Error ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {
            if (newsItems != null) {
                newsItemList = newsItems;
                adapter.setNewsItemList(newsItems);
                mRecyclerView.setAdapter(adapter);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "Failed to fetch the data", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        }
    }
}
