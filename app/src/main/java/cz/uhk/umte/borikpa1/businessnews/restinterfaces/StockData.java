package cz.uhk.umte.borikpa1.businessnews.restinterfaces;

import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import cz.uhk.umte.borikpa1.businessnews.model.StockItemTimeSeries;
import cz.uhk.umte.borikpa1.businessnews.model.StockSymbol;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StockData {
    //e.g. https://api.iextrading.com/1.0/stock/aapl/quote
    @GET("stock/{symbol}/quote")
    Call<StockItem> getStockData(@Path("symbol") String symbol);

    //e.g. https://api.iextrading.com/1.0/stock/market/batch?symbols=aapl,fb&types=quote
    @GET("stock/market/batch?displayPercent=true")
    Call<List<StockItem>> getBatchedStockData(@Query("symbols") String symbols, @Query("types") String types);

    @GET("stock/market/list/gainers?displayPercent=true")
    Call<StockItem[]> getGainers();

    @GET("stock/market/list/losers?displayPercent=true")
    Call<StockItem[]> getLosers();

    @GET("stock/{symbol}/chart/{range}?chartInterval=5")
    Call<StockItemTimeSeries[]> getTimeSeries(@Path("symbol") String symbol, @Path("range") String range);
}
