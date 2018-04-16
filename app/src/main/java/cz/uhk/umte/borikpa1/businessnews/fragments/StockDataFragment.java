package cz.uhk.umte.borikpa1.businessnews.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import cz.uhk.umte.borikpa1.businessnews.activities.StockDetailActivity;
import cz.uhk.umte.borikpa1.businessnews.adapters.StockItemsRecyclerViewAdapter;
import cz.uhk.umte.borikpa1.businessnews.dao.StockSymbolDao;
import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import cz.uhk.umte.borikpa1.businessnews.model.StockSymbol;
import cz.uhk.umte.borikpa1.businessnews.restinterfaces.StockData;
import cz.uhk.umte.borikpa1.businessnews.utils.AppDatabase;
import cz.uhk.umte.borikpa1.businessnews.utils.RecyclerItemTouchHelper;
import cz.uhk.umte.borikpa1.businessnews.utils.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockDataFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private List<StockItem> stockItemList;
    private RecyclerView mRecyclerView;
    private StockItemsRecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private StockData stockClient;
    private String symbolsParam;
    private AppDatabase appDatabase;

    public StockDataFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        stockItemList = new ArrayList<>();
        stockClient  = RetrofitServiceGenerator.createService(StockData.class);
        symbolsParam = createSymbolsParam();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_data, container, false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateStockData(symbolsParam, stockClient);
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_stocklist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new StockItemsRecyclerViewAdapter(stockItemList, pos -> {
            Intent intent = new Intent(getActivity(), StockDetailActivity.class);
            intent.putExtra("symbol", stockItemList.get(pos).getSymbol());
            startActivity(intent);
            Toast.makeText(getActivity(),stockItemList.get(pos).getSymbol(),Toast.LENGTH_LONG).show();
        });
        mRecyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        updateStockData(symbolsParam,stockClient);

        return view;
    }

    private void updateStockData (String symbolsParam, StockData stockClient) {
        Call<List<StockItem>> call = stockClient.getBatchedStockData(symbolsParam,"quote");
        call.enqueue(new Callback<List<StockItem>>() {
            @Override
            public void onResponse(Call<List<StockItem>> call, Response<List<StockItem>> response) {
                stockItemList = response.body();
                adapter.setStockItemList(stockItemList);
                mRecyclerView.setAdapter(adapter);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<StockItem>> call, Throwable t) {
                Toast.makeText( StockDataFragment.super.getContext(),"Failed to fetch the data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        StockSymbol stockSymbol = appDatabase.stockSymbolDao().getSymbolByTag(stockItemList.get(position).getSymbol());
        if(stockSymbol != null) {
            stockSymbol.setWatched(Boolean.FALSE);
            appDatabase.stockSymbolDao().updateSymbol(stockSymbol);
        }
        symbolsParam = createSymbolsParam();
        adapter.removeItem(viewHolder.getAdapterPosition());
        adapter.notifyDataSetChanged();
    }

    private String createSymbolsParam() {
        String res;
        StringBuilder sb = new StringBuilder();
        List<StockSymbol> stockSymbols = appDatabase.stockSymbolDao().getWatchedSymbols();
        stockSymbols.forEach(s -> sb.append(s.getSymbol() + ","));
        res = sb.toString();
        res.replaceAll(", $", "");
        return res;
    }
}
