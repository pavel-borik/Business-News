package cz.uhk.umte.borikpa1.businessnews.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.adapters.StockItemsRecyclerViewAdapter;
import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import cz.uhk.umte.borikpa1.businessnews.restinterfaces.StockData;
import cz.uhk.umte.borikpa1.businessnews.utils.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {
    List<StockItem> stockItemList;
    private RecyclerView mRecyclerView;
    private StockItemsRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.fragment_news,null, false);
        mainContent.addView(contentView, 0);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_newslist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));

        stockItemList = new ArrayList<>();
        StockData stockClient  = RetrofitServiceGenerator.createService(StockData.class);
        List<String> symbols = Arrays.asList("AAPL", "SNAP", "FB", "NFLX", "MSFT");
        Call<StockItem> call = null;

        for(String s : symbols) {
            call = stockClient.getPreviousStockData(s);
            call.enqueue(new Callback<StockItem>() {
                @Override
                public void onResponse(Call<StockItem> call, Response<StockItem> response) {
                    StockItem s = response.body();
                    stockItemList.add(s);
                    adapter = new StockItemsRecyclerViewAdapter(stockItemList);
                    mRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<StockItem> call, Throwable t) {
                    Log.i("FAIL", t.getMessage());

                }
            });
        }
        adapter = new StockItemsRecyclerViewAdapter(stockItemList);
        mRecyclerView.setAdapter(adapter);
    }
}
