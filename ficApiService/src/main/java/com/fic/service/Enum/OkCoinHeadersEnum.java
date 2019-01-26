package com.fic.service.Enum;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
public enum OkCoinHeadersEnum {

    OK_ACCESS_KEY("OK-ACCESS-KEY"),
    OK_ACCESS_SIGN("OK-ACCESS-SIGN"),
    OK_ACCESS_TIMESTAMP("OK-ACCESS-TIMESTAMP"),
    OK_ACCESS_PASSPHRASE("OK-ACCESS-PASSPHRASE"),

    OK_FROM("OK-FROM"),
    OK_TO("OK-TO"),
    OK_LIMIT("OK-LIMIT");

    private String header;

    OkCoinHeadersEnum(String header) {
        this.header = header;
    }

    public String header() {
        return header;
    }
}
