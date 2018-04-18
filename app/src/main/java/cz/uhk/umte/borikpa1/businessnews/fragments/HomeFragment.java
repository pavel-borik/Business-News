package cz.uhk.umte.borikpa1.businessnews.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
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
import cz.uhk.umte.borikpa1.businessnews.utils.XmlDownloadHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private CardView cardView;
    private NewsItem newsItem;
    private ImageView cardNewsImage;
    private TextView cardNewsTitle;
    private TextView cardNewsDescription;
    private TextView tvGainerSymbol;
    private TextView tvGainerCompany;
    private TextView tvGainerPrice;
    private TextView tvGainerChange;
    private TextView tvGainerChangePct;
    private TextView tvLoserSymbol;
    private TextView tvLoserCompany;
    private TextView tvLoserPrice;
    private TextView tvLoserChange;
    private TextView tvLoserChangePct;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardView = view.findViewById(R.id.cardViewHotNews);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = newsItem.getLink();
                if(link != null) {
                    if(URLUtil.isValidUrl(link)) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(browserIntent);
                    }
                }
            }
        });
        
        tvGainerSymbol = view.findViewById(R.id.gainerStockSymbol);
        tvGainerCompany = view.findViewById(R.id.gainerStockCompany);
        tvGainerPrice = view.findViewById(R.id.gainerStockPrice);
        tvGainerChange = view.findViewById(R.id.gainerStockChange);
        tvGainerChangePct = view.findViewById(R.id.gainerStockChangePct);

        tvLoserSymbol = view.findViewById(R.id.loserStockSymbol);
        tvLoserCompany = view.findViewById(R.id.loserStockCompany);
        tvLoserPrice = view.findViewById(R.id.loserStockPrice);
        tvLoserChange = view.findViewById(R.id.loserStockChange);
        tvLoserChangePct = view.findViewById(R.id.loserStockChangePct);

        cardNewsImage = view.findViewById(R.id.cardNewsItemImage);
        cardNewsTitle = view.findViewById(R.id.cardNewsItemTitle);
        cardNewsDescription = view.findViewById(R.id.cardNewsItemDescription);

        StockData stockClient = RetrofitServiceGenerator.createService(StockData.class);
        new RssFeedRetriever().execute("http://feeds.bbci.co.uk/news/world/rss.xml");
        new StockDataRetriever().execute(stockClient);

        return view;
    }

    private class RssFeedRetriever extends AsyncTask<String, Void, List<NewsItem>> {
        int result;

        @Override
        protected List<NewsItem> doInBackground(String... urls) {
            result = 0;
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
                newsItem = newsItems.get(0);
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
    }

    private class StockDataRetriever extends AsyncTask<StockData, Integer, StockItem[]> {

        @Override
        protected StockItem[] doInBackground(StockData... stockData) {
            StockItem best = new StockItem();
            StockItem worst = new StockItem();
            try {
                best = stockData[0].getGainers().execute().body()[0];
                worst = stockData[0].getLosers().execute().body()[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new StockItem[]{best, worst};
        }

        @Override
        protected void onPostExecute(StockItem[] stockItems) {
            StockItem stockItem;
            if(stockItems[0] != null) {
                stockItem = stockItems[0];
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
            if(stockItems[1] != null) {
                stockItem = stockItems[1];
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
    }
}