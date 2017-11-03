package com.fruit.sys.admin.utils;


public enum DateStyle {

    YYYY_MM_DD("yyyy-MM-dd"),
    YYYYMMDD("yyyyMMdd"),

    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"),

    YYYY_MM_DD_CN("yyyy年MM月dd日"),
    YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"),

    HH_MM_SS("HH:mm:ss"),
    HHMMSS("HHmss");

    private String value;

    DateStyle(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}