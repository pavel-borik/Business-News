package cz.uhk.umte.borikpa1.businessnews.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.activities.SearchActivity;
import cz.uhk.umte.borikpa1.businessnews.activities.StockDetailActivity;
import cz.uhk.umte.borikpa1.businessnews.adapters.StockItemsRecyclerViewAdapter;
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
    private AppDatabase appDatabase;
    private List<StockSymbol> stockSymbols;
    private String symbolsParam;

    public StockDataFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        stockClient  = RetrofitServiceGenerator.createService(StockData.class);
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_data, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> new StockDataLoader().execute());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_stocklist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new StockItemsRecyclerViewAdapter(getContext(), stockItemList, pos -> {
            Intent intent = new Intent(getActivity(), StockDetailActivity.class);
            intent.putExtra("symbol", stockItemList.get(pos).getSymbol());
            startActivity(intent);
        });
        mRecyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        //new StockDataLoader().execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new StockDataLoader().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            break;
        }

        return true;
    }

    private void updateStockData (String symbolsParam, StockData stockClient) {
        if(!isNetworkConnected()) {
            stockItemList = appDatabase.stockItemDao().getAllStockItems();
            adapter.setStockItemList(stockItemList);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        } else {
            Call<List<StockItem>> call = stockClient.getBatchedStockData(symbolsParam, "quote");
            call.enqueue(new Callback<List<StockItem>>() {
                @Override
                public void onResponse(Call<List<StockItem>> call, Response<List<StockItem>> response) {
                    stockItemList = response.body();
                    adapter.setStockItemList(stockItemList);
                    mRecyclerView.setAdapter(adapter);
                    swipeContainer.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    if (stockItemList != null) {
                        new DbStockDataUpdater().execute();
                    }
                }
                @Override
                public void onFailure(Call<List<StockItem>> call, Throwable t) {
                    Toast.makeText(StockDataFragment.super.getContext(), "Failed to fetch the data", Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
            });
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        StockSymbol stockSymbol = appDatabase.stockSymbolDao().getSymbolByTag(stockItemList.get(position).getSymbol());
        if(stockSymbol != null) {
            stockSymbol.setWatched(Boolean.FALSE);
            appDatabase.stockSymbolDao().updateSymbol(stockSymbol);
        }
        new StockDataLoader().execute();
        adapter.removeItem(viewHolder.getAdapterPosition());
        adapter.notifyDataSetChanged();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private class StockDataLoader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringBuilder sb = new StringBuilder();
            stockSymbols = appDatabase.stockSymbolDao().getWatchedSymbols();
            System.out.println("fragment");
            stockSymbols.forEach(s -> System.out.println(s.getSymbol()));
            stockSymbols.forEach(s -> sb.append(s.getSymbol() + ","));
            symbolsParam = sb.toString().replaceAll(", $", "");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateStockData(symbolsParam,stockClient);
        }
    }

    private class DbStockDataUpdater extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (stockItemList != null) {
                appDatabase.runInTransaction(() -> {
                    appDatabase.stockItemDao().deleteAllStockItems();
                    appDatabase.stockItemDao().insertAllStockItems(stockItemList);
                });
            }
            return null;
        }
    }
}
