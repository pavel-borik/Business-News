package cz.uhk.umte.borikpa1.businessnews.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class StockDataFragment extends Fragment {

    List<StockItem> stockItemList;
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

        stockItemList = new ArrayList<>();
        StockData stockClient  = RetrofitServiceGenerator.createService(StockData.class);
        List<String> symbols = Arrays.asList("AAPL", "SNAP", "FB", "NFLX", "MSFT");

//        Call<List<StockItem>> call = stockClient.getBatchedStockData("AAPL,SNAP","quote");
//        call.enqueue(new Callback<List<StockItem>>() {
//            @Override
//            public void onResponse(Call<List<StockItem>> call, Response<List<StockItem>> response) {
//                stockItemList = response.body();
//                adapter = new StockItemsRecyclerViewAdapter(stockItemList);
//                mRecyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<StockItem>> call, Throwable t) {
//                Log.i("FAIL", t.getMessage());
//
//            }
//        });

        Call<StockItem> call = null;

        for(String s : symbols) {
            call = stockClient.getStockData(s);
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

        return view;
    }

}
