package cz.uhk.umte.borikpa1.businessnews.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import cz.uhk.umte.borikpa1.businessnews.model.StockSymbol;

@Dao
public interface StockSymbolDao {

    @Query("Select * from StockSymbol")
    List<StockSymbol> getAllSymbols();

    @Query("Select * from StockSymbol where symbol like :symbolParam limit 1")
    StockSymbol getSymbolByTag(String symbolParam);

    @Query("Select * from StockSymbol where isWatched = 1")
    List<StockSymbol> getWatchedSymbols();

    @Insert
    void insertAllSymbols(StockSymbol... stockSymbols);

    @Update
    void updateSymbol(StockSymbol stockSymbol);
}
