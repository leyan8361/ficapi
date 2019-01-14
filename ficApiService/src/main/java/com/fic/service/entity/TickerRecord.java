package com.fic.service.entity;

public class TickerRecord {
    private Integer id;

    private String instrumentId;

    private String bestAsk;

    private String bestBid;

    private String open24h;

    private String high24h;

    private String low24h;

    private String timestamp;

    private String last;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(String bestAsk) {
        this.bestAsk = bestAsk;
    }

    public String getBestBid() {
        return bestBid;
    }

    public void setBestBid(String bestBid) {
        this.bestBid = bestBid;
    }

    public String getOpen24h() {
        return open24h;
    }

    public void setOpen24h(String open24h) {
        this.open24h = open24h;
    }

    public String getHigh24h() {
        return high24h;
    }

    public void setHigh24h(String high24h) {
        this.high24h = high24h;
    }

    public String getLow24h() {
        return low24h;
    }

    public void setLow24h(String low24h) {
        this.low24h = low24h;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}