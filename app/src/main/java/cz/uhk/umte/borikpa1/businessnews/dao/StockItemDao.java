package cz.uhk.umte.borikpa1.businessnews.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.model.StockItem;

@Dao
public interface StockItemDao {
    @Query("Select * from StockItem")
    List<StockItem> getAllStockItems();

    @Query("Select * from StockItem where symbol like :symbolParam limit 1")
    StockItem getStockItemBySymbol(String symbolParam);

    @Insert
    void insertAllStockItems(List<StockItem> stockItems);

    @Update
    void updateStockItem(StockItem stockItem);

    @Query("Delete from StockItem")
    void deleteAllStockItems();

    @Query("Delete from StockItem where symbol = :symbol")
    void deleteStockItemBySymbol(String symbol);
}
