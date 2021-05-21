package com.crazy.phoneauthotpdemo;

public class AppD {
    private String language,phonenum,type;

    public AppD()
    {

    }

    public AppD(String language, String phonenum, String type) {
        this.language = language;
        this.phonenum = phonenum;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }
}
