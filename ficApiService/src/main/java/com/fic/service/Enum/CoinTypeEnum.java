package com.fic.service.Enum;


public enum CoinTypeEnum {

    TFC(0,"TFC"),//TFC
    ETH(1,"ETH"),//ETH
    BTC(2,"BTC");//BTC

    private Integer code;

    private String value;

    CoinTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
