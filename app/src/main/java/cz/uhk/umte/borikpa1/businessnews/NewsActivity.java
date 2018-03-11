package cz.uhk.umte.borikpa1.businessnews;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.model.NewsItem;
import cz.uhk.umte.borikpa1.businessnews.utils.NewsItemsRecyclerViewAdapter;
import cz.uhk.umte.borikpa1.businessnews.utils.NewsItemsXmlParser;

public class NewsActivity extends AppCompatActivity {

    List<NewsItem> newsItems;
    private RecyclerView mRecyclerView;
    private NewsItemsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_newslist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String url = "http://feeds.bbci.co.uk/news/business/rss.xml";
        new RssFeedRetriever().execute(url);

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private class RssFeedRetriever extends AsyncTask<String, Void, List<NewsItem>> {
        int result;
        @Override
        protected List<NewsItem> doInBackground(String... urls) {
            result = 0;
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException | XmlPullParserException e) {
                Log.e("Error ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {

            if (result == 1) {
                adapter = new NewsItemsRecyclerViewAdapter(NewsActivity.this, newsItems);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(NewsActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }

        private List<NewsItem> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            NewsItemsXmlParser newsItemsXmlParser = new NewsItemsXmlParser();
            List<NewsItem> newsItems;

            try {
                stream = downloadUrl(urlString);
                newsItems = newsItemsXmlParser.parse(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return newsItems;
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int statusCode = conn.getResponseCode();
            if (statusCode == 200) result = 1;
            return conn.getInputStream();
        }
    }
}
