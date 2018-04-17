package cz.uhk.umte.borikpa1.businessnews.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.model.NewsItem;
import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import cz.uhk.umte.borikpa1.businessnews.restinterfaces.StockData;
import cz.uhk.umte.borikpa1.businessnews.utils.NewsItemsXmlParser;
import cz.uhk.umte.borikpa1.businessnews.utils.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ImageView cardNewsImage;
    TextView cardNewsTitle;
    TextView cardNewsDescription;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvGainerSymbol = view.findViewById(R.id.gainerStockSymbol);
        TextView tvGainerCompany = view.findViewById(R.id.gainerStockCompany);
        TextView tvGainerPrice = view.findViewById(R.id.gainerStockPrice);
        TextView tvGainerChange = view.findViewById(R.id.gainerStockChange);
        TextView tvGainerChangePct = view.findViewById(R.id.gainerStockChangePct);

        TextView tvLoserSymbol = view.findViewById(R.id.loserStockSymbol);
        TextView tvLoserCompany = view.findViewById(R.id.loserStockCompany);
        TextView tvLoserPrice = view.findViewById(R.id.loserStockPrice);
        TextView tvLoserChange = view.findViewById(R.id.loserStockChange);
        TextView tvLoserChangePct = view.findViewById(R.id.loserStockChangePct);

        cardNewsImage = view.findViewById(R.id.cardNewsItemImage);
        cardNewsTitle = view.findViewById(R.id.cardNewsItemTitle);
        cardNewsDescription = view.findViewById(R.id.cardNewsItemDescription);

        new RssFeedRetriever().execute("http://feeds.bbci.co.uk/news/world/rss.xml");

        StockData stockClient  = RetrofitServiceGenerator.createService(StockData.class);

        Call<StockItem[]> callGainers = stockClient.getGainers();
        callGainers.enqueue(new Callback<StockItem[]>() {
            @Override
            public void onResponse(Call<StockItem[]> call, Response<StockItem[]> response) {
                StockItem[] stockItems = response.body();
                if(stockItems != null) {
                    StockItem stockItem = stockItems[0];
                    tvGainerSymbol.setText(stockItem.getSymbol());
                    tvGainerCompany.setText(stockItem.getCompanyName());
                    try {
                        tvGainerPrice.setText(String.valueOf(stockItem.getLatestPrice()));
                        tvGainerChange.setText(String.valueOf((double)Math.round(stockItem.getChange()*100) / 100));
                        tvGainerChangePct.setText(String.valueOf((double)Math.round(stockItem.getChangePercent()*100) / 100));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StockItem[]> call, Throwable t) {
                Toast.makeText( HomeFragment.super.getContext(),"Failed to fetch the data", Toast.LENGTH_SHORT).show();
                Log.i("FAIL", t.getMessage());
            }
        });

        Call<StockItem[]> callLosers = stockClient.getLosers();
        callLosers.enqueue(new Callback<StockItem[]>() {
            @Override
            public void onResponse(Call<StockItem[]> call, Response<StockItem[]> response) {
                StockItem[] stockItems = response.body();
                if(stockItems != null) {
                    StockItem stockItem = stockItems[0];
                    tvLoserSymbol.setText(stockItem.getSymbol());
                    tvLoserCompany.setText(stockItem.getCompanyName());
                    try {
                        tvLoserPrice.setText(String.valueOf(stockItem.getLatestPrice()));
                        tvLoserChange.setText(String.valueOf((double) Math.round(stockItem.getChange() * 100) / 100));
                        tvLoserChangePct.setText(String.valueOf((double) Math.round(stockItem.getChangePercent() * 100) / 100));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StockItem[]> call, Throwable t) {
                Toast.makeText( HomeFragment.super.getContext(),"Failed to fetch the data", Toast.LENGTH_SHORT).show();
                Log.i("FAIL", t.getMessage());
            }
        });
        return view;
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
                NewsItem newsItem= newsItems.get(0);
                //Render image using Picasso library
                if (!TextUtils.isEmpty(newsItem.getThumbnailUrl())) {
                    Picasso.get().load(newsItem.getThumbnailUrl())
                            //.error(R.drawable.placeholder)
                            //.placeholder(R.drawable.placeholder)
                            .into(cardNewsImage);
                }
                cardNewsTitle.setText(newsItem.getTitle());
                cardNewsDescription.setText(newsItem.getDescription());
            } else {
                Toast.makeText(getActivity(), "Failed to fetch the data", Toast.LENGTH_SHORT).show();
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
