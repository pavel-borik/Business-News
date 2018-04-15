package cz.uhk.umte.borikpa1.businessnews.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.model.StockSymbol;

public class DatabaseInitializer {

    public static void populateAsync(@NonNull final AppDatabase db, Context context) {
        new PopulateDbAsync(db, context).execute();
    }

    private static void populateWithSymbolsData(AppDatabase mDb, Context context) throws IOException {
        Gson g = new Gson();
        InputStream is = context.getResources().openRawResource(R.raw.symbols);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();
        StockSymbol[] stockSymbols = g.fromJson(jsonString, StockSymbol[].class);
        mDb.stockSymbolDao().insertAllSymbols(stockSymbols);
        List<String> symbols = Arrays.asList("AAPL","SNAP","FB","NFLX","MSFT","TSLA","CSCO","AMZN","WOW3","KO","DIS","ORCL","BA","WMT","EBAY","USGE");
        StockSymbol stockSymbol;
        for(String symbol : symbols) {
            stockSymbol = mDb.stockSymbolDao().getSymbolByTag(symbol);
            if(stockSymbol != null) {
                stockSymbol.setWatched(Boolean.TRUE);
                mDb.stockSymbolDao().updateSymbol(stockSymbol);
            }
        }
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private final Context context;

        PopulateDbAsync(AppDatabase db, Context context) {
            this.mDb = db;
            this.context = context;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                populateWithSymbolsData(mDb, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
