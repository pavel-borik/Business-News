package cz.uhk.umte.borikpa1.businessnews.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import cz.uhk.umte.borikpa1.businessnews.restinterfaces.StockData;
import cz.uhk.umte.borikpa1.businessnews.utils.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvGainerSymbol = view.findViewById(R.id.gainerStockSymbol);
        TextView tvGainerCompany = view.findViewById(R.id.gainerStockCompany);
        TextView tvGainerPrice = view.findViewById(R.id.gainerStockPrice);
        TextView tvGainerChange = view.findViewById(R.id.gainerStockChange);
        TextView tvGainerChangePct = view.findViewById(R.id.gainerStockChangePct);

        TextView tvLoserSymbol = view.findViewById(R.id.loserStockSymbol);
        TextView tvLoserCompany = view.findViewById(R.id.loserStockCompany);
        TextView tvLoserPrice = view.findViewById(R.id.loserStockPrice);
        TextView tvLoserChange = view.findViewById(R.id.loserStockChange);
        TextView tvLoserChangePct = view.findViewById(R.id.loserStockChangePct);

        StockData stockClient  = RetrofitServiceGenerator.createService(StockData.class);

        Call<StockItem[]> callGainers = stockClient.getGainers();
        callGainers.enqueue(new Callback<StockItem[]>() {
            @Override
            public void onResponse(Call<StockItem[]> call, Response<StockItem[]> response) {
                StockItem[] stockItems = response.body();
                tvGainerSymbol.setText(stockItems[0].getSymbol());
                tvGainerCompany.setText(stockItems[0].getCompanyName());
                tvGainerPrice.setText(String.valueOf(stockItems[0].getLatestPrice()));
                tvGainerChange.setText(String.valueOf((double)Math.round(stockItems[0].getChange()*100) / 100));
                tvGainerChangePct.setText(String.valueOf((double)Math.round(stockItems[0].getChangePercent()*100) / 100));
            }

            @Override
            public void onFailure(Call<StockItem[]> call, Throwable t) {
                Toast.makeText( HomeFragment.super.getContext(),"Failed to fetch the data", Toast.LENGTH_SHORT).show();
                Log.i("FAIL", t.getMessage());
            }
        });

        Call<StockItem[]> callLosers = stockClient.getLosers();
        callLosers.enqueue(new Callback<StockItem[]>() {
            @Override
            public void onResponse(Call<StockItem[]> call, Response<StockItem[]> response) {
                StockItem[] stockItems = response.body();
                tvLoserSymbol.setText(stockItems[0].getSymbol());
                tvLoserCompany.setText(stockItems[0].getCompanyName());
                tvLoserPrice.setText(String.valueOf(stockItems[0].getLatestPrice()));
                tvLoserChange.setText(String.valueOf((double)Math.round(stockItems[0].getChange()*100) / 100));
                tvLoserChangePct.setText(String.valueOf((double)Math.round(stockItems[0].getChangePercent()*100) / 100));
            }

            @Override
            public void onFailure(Call<StockItem[]> call, Throwable t) {
                Toast.makeText( HomeFragment.super.getContext(),"Failed to fetch the data", Toast.LENGTH_SHORT).show();
                Log.i("FAIL", t.getMessage());
            }
        });
        return view;
    }
}
