package com.fruit.sys.admin.utils;


import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MathUtil {

    /**
     * double 转 BigDecima ,保留两位小数
     * @param value
     * @return
     */
    public static BigDecimal doubleToBigDecima(Double value){
        DecimalFormat df2 =new DecimalFormat("#.00");
        BigDecimal bigDecimal = null;
        if(value!=null){
            String str2 =df2.format(value);
            bigDecimal = new BigDecimal(str2);
        }
        return bigDecimal;
    }


    /**
     * String 转 BigDecima ,保留两位小数
     * @param value
     * @return
     */
    public static BigDecimal stringToBigDecima(String value){
        Double doubleValue = null;
        if(StringUtils.isNotEmpty(value)){
            doubleValue = Double.valueOf(value);
        }
        DecimalFormat df2 =new DecimalFormat("#.00");
        BigDecimal bigDecimal = null;
        if(doubleValue!=null){
            String str2 =df2.format(doubleValue);
            bigDecimal = new BigDecimal(str2);
        }
        return bigDecimal;
    }


    /**
     * String 转 BigDecima ,保留scale位小数
     * @param value
     * @param scale 保留小数位数
     * @return
     */
    public static BigDecimal stringToBigDecima(String value,int scale){
        Double doubleValue = null;
        if(StringUtils.isNotEmpty(value)){
            doubleValue = Double.valueOf(value);
        }

        BigDecimal bigDecimal = null;
        if(doubleValue!=null){
            bigDecimal = new BigDecimal(doubleValue);
            bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        return bigDecimal;
    }


    /**
     * double 转 BigDecima ,保留scale位小数
     * @param value
     * @param scale 保留小数位数
     * @return
     */
    public static BigDecimal doubleToBigDecima(Double value,int scale){
        BigDecimal bigDecimal = null;
        if(value!=null){
            bigDecimal = new BigDecimal(value);
            bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        return bigDecimal;
    }




}