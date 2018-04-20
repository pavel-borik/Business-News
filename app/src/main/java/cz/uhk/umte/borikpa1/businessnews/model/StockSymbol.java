package cz.uhk.umte.borikpa1.businessnews.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class StockSymbol {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String symbol;
    private String name;
    private String type;
    private Boolean isWatched;
    private String logoUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getWatched() {
        return isWatched;
    }

    public void setWatched(Boolean watched) {
        isWatched = watched;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
