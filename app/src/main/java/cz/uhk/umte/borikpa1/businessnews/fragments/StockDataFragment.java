package cz.uhk.umte.borikpa1.businessnews.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.adapters.StockItemsRecyclerViewAdapter;
import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import cz.uhk.umte.borikpa1.businessnews.restinterfaces.StockData;
import cz.uhk.umte.borikpa1.businessnews.utils.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockDataFragment extends Fragment {

    private List<StockItem> stockItemList;
    private RecyclerView mRecyclerView;
    private StockItemsRecyclerViewAdapter adapter;
    public StockDataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_data, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_stocklist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new StockItemsRecyclerViewAdapter(stockItemList);
        mRecyclerView.setAdapter(adapter);

        stockItemList = new ArrayList<>();
        StockData stockClient  = RetrofitServiceGenerator.createService(StockData.class);

        List<String> symbols = Arrays.asList("AAPL","SNAP","FB","NFLX","MSFT","TSLA","CSCO","AMZN","WOW3","KO","DIS","ORCL","BA","WMT","EBAY","USGE");
        symbols.sort(String::compareToIgnoreCase);
        StringBuilder sb = new StringBuilder();
        symbols.forEach(str -> sb.append(str + ","));
        String symbolsParam = sb.toString();
        symbolsParam.replaceAll(", $", "");

        Call<List<StockItem>> call = stockClient.getBatchedStockData(symbolsParam,"quote");
        call.enqueue(new Callback<List<StockItem>>() {
            @Override
            public void onResponse(Call<List<StockItem>> call, Response<List<StockItem>> response) {
                stockItemList = response.body();
                adapter.setStockItemList(stockItemList);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<StockItem>> call, Throwable t) {
                Toast.makeText( StockDataFragment.super.getContext(),"Failed to fetch the data", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
