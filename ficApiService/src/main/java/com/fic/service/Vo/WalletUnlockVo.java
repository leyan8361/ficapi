package com.fic.service.Vo;


public class WalletUnlockVo {

    private String address;
    private String password;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WalletUnlockVo(String address, String password) {
        this.address = address;
        this.password = password;
    }
}
