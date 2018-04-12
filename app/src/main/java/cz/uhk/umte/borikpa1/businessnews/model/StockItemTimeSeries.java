package cz.uhk.umte.borikpa1.businessnews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockItemTimeSeries {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("minute")
    @Expose
    private String minute;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("high")
    @Expose
    private Double high;
    @SerializedName("low")
    @Expose
    private Double low;
    @SerializedName("average")
    @Expose
    private Double average;
    @SerializedName("volume")
    @Expose
    private Integer volume;
    @SerializedName("notional")
    @Expose
    private Double notional;
    @SerializedName("numberOfTrades")
    @Expose
    private Integer numberOfTrades;
    @SerializedName("marketHigh")
    @Expose
    private Double marketHigh;
    @SerializedName("marketLow")
    @Expose
    private Double marketLow;
    @SerializedName("marketAverage")
    @Expose
    private Double marketAverage;
    @SerializedName("marketVolume")
    @Expose
    private Integer marketVolume;
    @SerializedName("marketNotional")
    @Expose
    private Double marketNotional;
    @SerializedName("marketNumberOfTrades")
    @Expose
    private Integer marketNumberOfTrades;
    @SerializedName("changeOverTime")
    @Expose
    private Double changeOverTime;
    @SerializedName("marketChangeOverTime")
    @Expose
    private Double marketChangeOverTime;
    @SerializedName("open")
    @Expose
    private Double open;
    @SerializedName("close")
    @Expose
    private Double close;
    @SerializedName("unadjustedVolume")
    @Expose
    private Integer unadjustedVolume;
    @SerializedName("change")
    @Expose
    private Double change;
    @SerializedName("changePercent")
    @Expose
    private Double changePercent;
    @SerializedName("vwap")
    @Expose
    private Double vwap;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Double getNotional() {
        return notional;
    }

    public void setNotional(Double notional) {
        this.notional = notional;
    }

    public Integer getNumberOfTrades() {
        return numberOfTrades;
    }

    public void setNumberOfTrades(Integer numberOfTrades) {
        this.numberOfTrades = numberOfTrades;
    }

    public Double getMarketHigh() {
        return marketHigh;
    }

    public void setMarketHigh(Double marketHigh) {
        this.marketHigh = marketHigh;
    }

    public Double getMarketLow() {
        return marketLow;
    }

    public void setMarketLow(Double marketLow) {
        this.marketLow = marketLow;
    }

    public Double getMarketAverage() {
        return marketAverage;
    }

    public void setMarketAverage(Double marketAverage) {
        this.marketAverage = marketAverage;
    }

    public Integer getMarketVolume() {
        return marketVolume;
    }

    public void setMarketVolume(Integer marketVolume) {
        this.marketVolume = marketVolume;
    }

    public Double getMarketNotional() {
        return marketNotional;
    }

    public void setMarketNotional(Double marketNotional) {
        this.marketNotional = marketNotional;
    }

    public Integer getMarketNumberOfTrades() {
        return marketNumberOfTrades;
    }

    public void setMarketNumberOfTrades(Integer marketNumberOfTrades) {
        this.marketNumberOfTrades = marketNumberOfTrades;
    }

    public Double getChangeOverTime() {
        return changeOverTime;
    }

    public void setChangeOverTime(Double changeOverTime) {
        this.changeOverTime = changeOverTime;
    }

    public Double getMarketChangeOverTime() {
        return marketChangeOverTime;
    }

    public void setMarketChangeOverTime(Double marketChangeOverTime) {
        this.marketChangeOverTime = marketChangeOverTime;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Integer getUnadjustedVolume() {
        return unadjustedVolume;
    }

    public void setUnadjustedVolume(Integer unadjustedVolume) {
        this.unadjustedVolume = unadjustedVolume;
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

    public Double getVwap() {
        return vwap;
    }

    public void setVwap(Double vwap) {
        this.vwap = vwap;
    }
}
