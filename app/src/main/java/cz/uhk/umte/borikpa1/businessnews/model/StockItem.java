package cz.uhk.umte.borikpa1.businessnews.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class StockItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("symbol")
    @Expose
    public String symbol;
    @SerializedName("companyName")
    @Expose
    public String companyName;
    @SerializedName("primaryExchange")
    @Expose
    public String primaryExchange;
    @SerializedName("sector")
    @Expose
    public String sector;
    @SerializedName("calculationPrice")
    @Expose
    public String calculationPrice;
    @SerializedName("open")
    @Expose
    public Double open;
    @SerializedName("openTime")
    @Expose
    public Long openTime;
    @SerializedName("close")
    @Expose
    public Double close;
    @SerializedName("closeTime")
    @Expose
    public Long closeTime;
    @SerializedName("high")
    @Expose
    public Double high;
    @SerializedName("low")
    @Expose
    public Double low;
    @SerializedName("latestPrice")
    @Expose
    public Double latestPrice;
    @SerializedName("latestSource")
    @Expose
    public String latestSource;
    @SerializedName("latestTime")
    @Expose
    public String latestTime;
    @SerializedName("latestUpdate")
    @Expose
    public Long latestUpdate;
    @SerializedName("latestVolume")
    @Expose
    public Long latestVolume;
    @SerializedName("iexRealtimePrice")
    @Expose
    public Double iexRealtimePrice;
    @SerializedName("iexRealtimeSize")
    @Expose
    public Long iexRealtimeSize;
    @SerializedName("iexLastUpdated")
    @Expose
    public Long iexLastUpdated;
    @SerializedName("delayedPrice")
    @Expose
    public Double delayedPrice;
    @SerializedName("delayedPriceTime")
    @Expose
    public Long delayedPriceTime;
    @SerializedName("previousClose")
    @Expose
    public Double previousClose;
    @SerializedName("change")
    @Expose
    public Double change;
    @SerializedName("changePercent")
    @Expose
    public Double changePercent;
    @SerializedName("iexMarketPercent")
    @Expose
    public Double iexMarketPercent;
    @SerializedName("iexVolume")
    @Expose
    public Long iexVolume;
    @SerializedName("avgTotalVolume")
    @Expose
    public Long avgTotalVolume;
    @SerializedName("iexBidPrice")
    @Expose
    public Double iexBidPrice;
    @SerializedName("iexBidSize")
    @Expose
    public Long iexBidSize;
    @SerializedName("iexAskPrice")
    @Expose
    public Double iexAskPrice;
    @SerializedName("iexAskSize")
    @Expose
    public Long iexAskSize;
    @SerializedName("marketCap")
    @Expose
    public Long marketCap;
    @SerializedName("peRatio")
    @Expose
    public Double peRatio;
    @SerializedName("week52High")
    @Expose
    public Double week52High;
    @SerializedName("week52Low")
    @Expose
    public Double week52Low;
    @SerializedName("ytdChange")
    @Expose
    public Double ytdChange;


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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPrimaryExchange() {
        return primaryExchange;
    }

    public void setPrimaryExchange(String primaryExchange) {
        this.primaryExchange = primaryExchange;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCalculationPrice() {
        return calculationPrice;
    }

    public void setCalculationPrice(String calculationPrice) {
        this.calculationPrice = calculationPrice;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getLatestSource() {
        return latestSource;
    }

    public void setLatestSource(String latestSource) {
        this.latestSource = latestSource;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    public Long getLatestUpdate() {
        return latestUpdate;
    }

    public void setLatestUpdate(Long latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    public Long getLatestVolume() {
        return latestVolume;
    }

    public void setLatestVolume(Long latestVolume) {
        this.latestVolume = latestVolume;
    }

    public Double getIexRealtimePrice() {
        return iexRealtimePrice;
    }

    public void setIexRealtimePrice(Double iexRealtimePrice) {
        this.iexRealtimePrice = iexRealtimePrice;
    }

    public Long getIexRealtimeSize() {
        return iexRealtimeSize;
    }

    public void setIexRealtimeSize(Long iexRealtimeSize) {
        this.iexRealtimeSize = iexRealtimeSize;
    }

    public Long getIexLastUpdated() {
        return iexLastUpdated;
    }

    public void setIexLastUpdated(Long iexLastUpdated) {
        this.iexLastUpdated = iexLastUpdated;
    }

    public Double getDelayedPrice() {
        return delayedPrice;
    }

    public void setDelayedPrice(Double delayedPrice) {
        this.delayedPrice = delayedPrice;
    }

    public Long getDelayedPriceTime() {
        return delayedPriceTime;
    }

    public void setDelayedPriceTime(Long delayedPriceTime) {
        this.delayedPriceTime = delayedPriceTime;
    }

    public Double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(Double previousClose) {
        this.previousClose = previousClose;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }

    public Double getIexMarketPercent() {
        return iexMarketPercent;
    }

    public void setIexMarketPercent(Double iexMarketPercent) {
        this.iexMarketPercent = iexMarketPercent;
    }

    public Long getIexVolume() {
        return iexVolume;
    }

    public void setIexVolume(Long iexVolume) {
        this.iexVolume = iexVolume;
    }

    public Long getAvgTotalVolume() {
        return avgTotalVolume;
    }

    public void setAvgTotalVolume(Long avgTotalVolume) {
        this.avgTotalVolume = avgTotalVolume;
    }

    public Double getIexBidPrice() {
        return iexBidPrice;
    }

    public void setIexBidPrice(Double iexBidPrice) {
        this.iexBidPrice = iexBidPrice;
    }

    public Long getIexBidSize() {
        return iexBidSize;
    }

    public void setIexBidSize(Long iexBidSize) {
        this.iexBidSize = iexBidSize;
    }

    public Double getIexAskPrice() {
        return iexAskPrice;
    }

    public void setIexAskPrice(Double iexAskPrice) {
        this.iexAskPrice = iexAskPrice;
    }

    public Long getIexAskSize() {
        return iexAskSize;
    }

    public void setIexAskSize(Long iexAskSize) {
        this.iexAskSize = iexAskSize;
    }

    public Long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    public Double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(Double peRatio) {
        this.peRatio = peRatio;
    }

    public Double getWeek52High() {
        return week52High;
    }

    public void setWeek52High(Double week52High) {
        this.week52High = week52High;
    }

    public Double getWeek52Low() {
        return week52Low;
    }

    public void setWeek52Low(Double week52Low) {
        this.week52Low = week52Low;
    }

    public Double getYtdChange() {
        return ytdChange;
    }

    public void setYtdChange(Double ytdChange) {
        this.ytdChange = ytdChange;
    }
}