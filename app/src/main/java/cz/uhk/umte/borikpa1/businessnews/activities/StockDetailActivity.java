package cz.uhk.umte.borikpa1.businessnews.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import cz.uhk.umte.borikpa1.businessnews.model.StockItemTimeSeries;
import cz.uhk.umte.borikpa1.businessnews.model.StockSymbol;
import cz.uhk.umte.borikpa1.businessnews.restinterfaces.StockData;
import cz.uhk.umte.borikpa1.businessnews.utils.AppDatabase;
import cz.uhk.umte.borikpa1.businessnews.utils.RetrofitServiceGenerator;

public class StockDetailActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private LineChart lineChart;
    private LineDataSet dataSet;
    private LineData lineData;
    private StockItem currentStockItem;
    private List<Entry> entries = new ArrayList<>();
    private StockData stockClient;
    private static String SYMBOL;
    private static final String FOLLOW ="follow";
    private static final String UNFOLLOW ="unfollow";
    private String range = "1d";
    private boolean initiallyFollowed;

    private TextView tvStockSymbol;
    private TextView tvStockValue;
    private TextView tvStockDateTime;
    private TextView tvStockDetailChange;
    private TextView tvStockDetailChangePct;
    private ActionBar actionbar;
    private ToggleButton tbFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        stockClient  = RetrofitServiceGenerator.createService(StockData.class);
        SYMBOL = getIntent().getStringExtra("symbol");
        currentStockItem = AppDatabase.getAppDatabase(getApplicationContext()).stockItemDao().getStockItemBySymbol(SYMBOL);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        tvStockSymbol = findViewById(R.id.tvStockDetailSymbol);
        tvStockValue = findViewById(R.id.tvStockDetailValue);
        tvStockDateTime = findViewById(R.id.tvStockDetailDateTime);
        tvStockDetailChange = findViewById(R.id.tvStockDetailChange);
        tvStockDetailChangePct = findViewById(R.id.tvStockDetailChangePct);
        tbFollow = findViewById(R.id.toggleButtonFollow);
        tbFollow.setOnClickListener(v -> {
            if (tbFollow.isChecked() && !initiallyFollowed) {
                new DbSymbolUpdater().execute(FOLLOW);
            }
            else if (!tbFollow.isChecked() && initiallyFollowed) {
                new DbSymbolUpdater().execute(UNFOLLOW);
            }
        });
        lineChart = findViewById(R.id.lineChartStockTimeSeries);

        if(currentStockItem == null) {
            new StockItemLoader().execute(SYMBOL);
            tbFollow.setChecked(false);
            initiallyFollowed = false;
        } else {
            tbFollow.setChecked(true);
            initiallyFollowed = true;
            setupLabels();
        }

        new StockTimeSeriesLoader().execute(SYMBOL,range);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tbFollow.isChecked() && !initiallyFollowed) {
            new DbSymbolUpdater().execute(FOLLOW);
        }
        else if (!tbFollow.isChecked() && initiallyFollowed) {
            new DbSymbolUpdater().execute(UNFOLLOW);
        }
    }

    private class DbSymbolUpdater extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            if(strings[0] == FOLLOW) {
                StockSymbol stockSymbol = appDatabase.stockSymbolDao().getSymbolByTag(SYMBOL);
                stockSymbol.setWatched(true);
                appDatabase.stockSymbolDao().updateSymbol(stockSymbol);
                List<StockSymbol> st = appDatabase.stockSymbolDao().getWatchedSymbols();
                st.forEach(s -> System.out.println(s.getSymbol()));

            } else if (strings[0] == UNFOLLOW) {
                StockSymbol stockSymbol = appDatabase.stockSymbolDao().getSymbolByTag(SYMBOL);
                stockSymbol.setWatched(false);
                appDatabase.stockSymbolDao().updateSymbol(stockSymbol);
                List<StockSymbol> st = appDatabase.stockSymbolDao().getWatchedSymbols();
                st.forEach(s -> System.out.println(s.getSymbol()));
            }
            return null;
        }
    }

    private class StockItemLoader extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                currentStockItem = stockClient.getStockData(strings[0]).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(currentStockItem != null) setupLabels();
        }
    }
    private class StockTimeSeriesLoader extends AsyncTask<String, Void, StockItemTimeSeries[]> {

        @Override
        protected StockItemTimeSeries[] doInBackground(String... strings) {
            try {
                StockItemTimeSeries[] stockItemTimeSeries = stockClient.getTimeSeries(strings[0], strings[1]).execute().body();
                return stockItemTimeSeries;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(StockItemTimeSeries[] stockItemTimeSeries) {
            if(stockItemTimeSeries != null) {
                String label;
                float value;
                styleLineChart(lineChart);
                List<String> xLabels = new ArrayList<>();
                int i = 0;
                for(StockItemTimeSeries s : stockItemTimeSeries) {
                    value = (float) Math.round(s.getAverage().doubleValue() * 100) / 100;
                    label = s.getLabel();
                    if (value > 0 && label.length() > 0) {
                        entries.add(new Entry((float)i++, value));
                        xLabels.add(label);
                    }
                }
                if(entries.size() > 0) {
                    dataSet = new LineDataSet(entries, "Price");
                    dataSet.setColor(Color.parseColor("#007e4c"));
                    dataSet.setDrawCircles(false);
                    dataSet.setDrawFilled(true);
                    dataSet.setFillColor(Color.parseColor("#007e4c"));

                    lineData = new LineData(dataSet);
                    lineData.setDrawValues(false);

                    lineChart.setData(lineData);
                    lineChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(xLabels));
                    lineChart.invalidate();
                }
            }
        }
    }

    private void setupLabels() {

        actionbar.setTitle(currentStockItem.getCompanyName());
        tvStockSymbol.setText(currentStockItem.getSymbol());
        tvStockValue.setText(currentStockItem.getLatestPrice().toString());
        tvStockDateTime.setText(currentStockItem.getLatestTime());

        double change = 0d;
        String changeStr = "";
        String changePctStr = "";
        try {
            change = (double)Math.round(currentStockItem.getChange().doubleValue()*100)/100;
            changeStr = String.valueOf(change);
            changePctStr = String.valueOf((double)Math.round(currentStockItem.getChangePercent().doubleValue()*100)/100);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (change >=0) {
            changeStr = "+" + changeStr;
            changePctStr = "(+" + changePctStr + "%)";
            tvStockDetailChange.setText(changeStr);
            tvStockDetailChangePct.setText(changePctStr);
            tvStockDetailChange.setTextColor(getResources().getColor(R.color.colorTrendingUp, getTheme()));
            tvStockDetailChangePct.setTextColor(getResources().getColor(R.color.colorTrendingUp, getTheme()));

        } else {
            changeStr = "-" + changeStr;
            changePctStr = "(-" + changePctStr + "%)";
            tvStockDetailChange.setText(changeStr);
            tvStockDetailChangePct.setText(changePctStr);
            tvStockDetailChange.setTextColor(getResources().getColor(R.color.colorTrendingDown, getTheme()));
            tvStockDetailChangePct.setTextColor(getResources().getColor(R.color.colorTrendingDown, getTheme()));
        }

    }
    private void styleLineChart(LineChart lineChart) {
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(true);
        lineChart.setExtraRightOffset(25);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                lineChart.highlightValue(null);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
        IMarker marker = new MyMarkerView(this,R.layout.chart_marker);
        lineChart.setMarker(marker);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(6,true);
        xAxis.setXOffset(-10);
        //xAxis.setAvoidFirstLastClipping(true);
        lineChart.getAxisRight().setEnabled(false);
        YAxis yAxisLeft = lineChart.getAxisLeft();
        LimitLine prevClose = new LimitLine(currentStockItem.getClose().floatValue(), "Previous close");
        prevClose.setLineWidth(1f);
        prevClose.enableDashedLine(30f, 10f, 0f);
        prevClose.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        prevClose.setTextSize(8f);
        yAxisLeft.addLimitLine(prevClose);
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

    private class MyXAxisValueFormatter implements IAxisValueFormatter {
        private List<String> xLabels;

        public MyXAxisValueFormatter(List<String> xLabels) {
           this.xLabels = xLabels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if(value >= 0) {
                if(xLabels.size() > (int) value) {
                    return xLabels.get((int)value);
                } else return "";
            } else return "";
        }
    }

    private class MyMarkerView extends MarkerView {
        private TextView tvContent;
        private MPPointF mOffset;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            tvContent.setText("" + e.getY());
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            if(mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
            }
            return mOffset;
        }
    }
}
