package cz.uhk.umte.borikpa1.businessnews.restinterfaces;

import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StockData {
    @GET("1.0/stock/{symbol}/previous")
    Call<StockItem> getPreviousStockData(@Path("symbol") String symbol);

}
