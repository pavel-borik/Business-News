package cz.uhk.umte.borikpa1.businessnews.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
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

    private LineChart lineChart;
    private LineDataSet dataSet;
    private LineData lineData;
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
        lineChart = findViewById(R.id.lineChartStockTimeSeries);

        StockData stockClient  = RetrofitServiceGenerator.createService(StockData.class);
        Call<StockItemTimeSeries[]> call = stockClient.getTimeSeries(SYMBOL, "1d");

        call.enqueue(new Callback<StockItemTimeSeries[]>() {
            @Override
            public void onResponse(Call<StockItemTimeSeries[]> call, Response<StockItemTimeSeries[]> response) {
                StockItemTimeSeries[] stockItemTimeSeries = response.body();
                List<String> xLabels = new ArrayList<>();
                int i = 0;
                for(StockItemTimeSeries s : stockItemTimeSeries) {
                    float value = (float) Math.round(s.getAverage().doubleValue() * 100) / 100;
                    if (value > 0) {
                        entries.add(new Entry((float)i++, value));
                        xLabels.add(s.getLabel());
                    }

                }
                dataSet = new LineDataSet(entries, "Price");
                dataSet.setColor(Color.parseColor("#007e4c"));
                dataSet.setDrawCircles(false);
                dataSet.setDrawFilled(true);
                dataSet.setFillColor(Color.parseColor("#007e4c"));

                lineData = new LineData(dataSet);
                lineData.setDrawValues(false);


                lineChart.setData(lineData);
                styleLineChart(lineChart);
                lineChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(xLabels));
                lineChart.invalidate();
            }

            @Override
            public void onFailure(Call<StockItemTimeSeries[]> call, Throwable t) {

            }
        });
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
            return xLabels.get((int)value);
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
