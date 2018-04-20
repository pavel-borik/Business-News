package cz.uhk.umte.borikpa1.businessnews.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.adapters.SearchStockItemsRecyclerViewAdapter;
import cz.uhk.umte.borikpa1.businessnews.model.StockLogo;
import cz.uhk.umte.borikpa1.businessnews.model.StockSymbol;
import cz.uhk.umte.borikpa1.businessnews.restinterfaces.StockData;
import cz.uhk.umte.borikpa1.businessnews.utils.AppDatabase;
import cz.uhk.umte.borikpa1.businessnews.utils.RetrofitServiceGenerator;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SearchStockItemsRecyclerViewAdapter mAdapter;
    private List<StockSymbol> stockSymbolList;
    private AppDatabase appDatabase;
    private StockData retrofitStockClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        retrofitStockClient = RetrofitServiceGenerator.createService(StockData.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        EditText editTextSearch = findViewById(R.id.editTextSearchSymbol);
        ImageButton btnSearch = findViewById(R.id.btnSearchSymbol);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryParam = editTextSearch.getText().toString();
                if(queryParam.length() > 0) {
                    new SymbolDataRetriever().execute(queryParam);
                }
            }
        });
        mRecyclerView = findViewById(R.id.recyclerViewFoundSymbols);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mAdapter = new SearchStockItemsRecyclerViewAdapter(getApplicationContext(), stockSymbolList, pos -> {
            Intent intent = new Intent(this, StockDetailActivity.class);
            intent.putExtra("symbol", stockSymbolList.get(pos).getSymbol());
            startActivity(intent);
        });
        mAdapter.setStockSymbolList(stockSymbolList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SymbolDataRetriever extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... queryParams) {

            stockSymbolList = appDatabase.stockSymbolDao().getSymbolsByTagOrName(queryParams[0]);
            StringBuilder sb = new StringBuilder();
            stockSymbolList.forEach(s -> sb.append(s.getSymbol() + ","));
            String symbolsParam = sb.toString().replaceAll(", $", "");
            try {
                List<StockLogo> stockLogoList = retrofitStockClient.getLogoUrl(symbolsParam, "logo").execute().body();
                if(stockLogoList != null) {
                    for(int i = 0; i < stockSymbolList.size(); i++) {
                        stockSymbolList.get(i).setLogoUrl(stockLogoList.get(i).getUrl());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.setStockSymbolList(stockSymbolList);
            mAdapter.notifyDataSetChanged();
        }
    }
}
