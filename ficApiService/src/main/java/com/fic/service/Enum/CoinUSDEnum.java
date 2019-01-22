package com.fic.service.Enum;


public enum CoinUSDEnum {

    BTC("BTC-USD"),
    ETH("ETH-USD"),
    USDT("USDT-USD"),
    BCH("BCH-USD");

    private String code;

    CoinUSDEnum(String code) {
        this.code = code;
    }

    public String code(){
        return code;
    }

}
