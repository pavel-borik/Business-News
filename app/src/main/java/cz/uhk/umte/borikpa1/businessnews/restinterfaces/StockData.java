package cz.uhk.umte.borikpa1.businessnews.restinterfaces;

import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StockData {
    //e.g. https://api.iextrading.com/1.0/stock/aapl/quote
    @GET("1.0/stock/{symbol}/quote")
    Call<StockItem> getStockData(@Path("symbol") String symbol);

    //e.g. https://api.iextrading.com/1.0/stock/market/batch?symbols=aapl,fb&types=quote
    @GET("1.0/stock/market/batch?displayPercent=true")
    Call<List<StockItem>> getBatchedStockData(@Query("symbols") String symbols, @Query("types") String types);

    @GET("1.0/stock/market/list/gainers?displayPercent=true")
    Call<StockItem[]> getGainers();

    @GET("1.0/stock/market/list/losers?displayPercent=true")
    Call<StockItem[]> getLosers();
}
