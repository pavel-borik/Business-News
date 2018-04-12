package cz.uhk.umte.borikpa1.businessnews.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.model.StockItemTimeSeries;
import cz.uhk.umte.borikpa1.businessnews.restinterfaces.StockData;
import cz.uhk.umte.borikpa1.businessnews.utils.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockDetailActivity extends AppCompatActivity {

    private List<Entry> entries = new ArrayList<>();
    private static String SYMBOL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        SYMBOL = getIntent().getStringExtra("symbol");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(SYMBOL);
        LineChart lineChart = findViewById(R.id.lineChartStockTimeSeries);

        StockData stockClient  = RetrofitServiceGenerator.createService(StockData.class);
        Call<StockItemTimeSeries[]> call = stockClient.getTimeSeries(SYMBOL, "1d");

        call.enqueue(new Callback<StockItemTimeSeries[]>() {
            @Override
            public void onResponse(Call<StockItemTimeSeries[]> call, Response<StockItemTimeSeries[]> response) {
                StockItemTimeSeries[] stockItemTimeSeries = response.body();
                int i = 0;
                for(StockItemTimeSeries s : stockItemTimeSeries) {
                    float value = s.getAverage().floatValue();
                    if (value > 0) entries.add(new Entry((float)i++, value));
                    LineDataSet dataSet = new LineDataSet(entries, "Label");
                    LineData lineData = new LineData(dataSet);
                    lineChart.setData(lineData);
                    lineChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<StockItemTimeSeries[]> call, Throwable t) {

            }
        });
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
}
