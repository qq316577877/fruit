/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.meta;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-17
 * Project        : www.fruit.com
 * File Name      : MetadataProvider.java
 */
@Service
public class MetadataProvider
{
    private Map<Integer, MetaCountry> countryIndexMap;

    private Map<Integer, MetaCountry> countryOnlyIndexMap;

    private Map<Integer, MetaProvince> provinceIndexMap;

    private Map<Integer, MetaCity> cityIndexMap;

    private Map<Integer, MetaArea> areaIndexMap;


    public MetaCountry getCountry(int countryId)
    {
        return this.countryIndexMap.get(countryId);
    }

    public String getCountryName(int countryId)
    {
        MetaCountry country = this.getCountry(countryId);
        return country != null ? country.getName() : "";
    }

    public MetaProvince getProvince(int provinceId)
    {
        return this.provinceIndexMap.get(provinceId);
    }

    public String getProvinceName(int provinceId)
    {
        MetaProvince province =  this.getProvince(provinceId);
        return province != null ? province.getName() : "";
    }

    public MetaCity getCity(int cityId)
    {
        return this.cityIndexMap.get(cityId);
    }

    public String getCityName(int cityId)
    {
        MetaCity city = this.getCity(cityId);
        return city != null ? city.getName() : "";
    }

    public List<Integer> getCityIds(int provinceId)
    {
        List<Integer> cityIds = new ArrayList<Integer>();
        MetaProvince metaProvince = this.provinceIndexMap.get(provinceId);
        if(metaProvince != null)
        {
            for(MetaCity metaCity: metaProvince.getCities())
            {
                cityIds.add(metaCity.getId());
            }
        }
        return cityIds;
    }

    public MetaArea getArea(int areaId)
    {
        return this.areaIndexMap.get(areaId);
    }

    public String getAreaName(int areaId)
    {
        MetaArea area = this.getArea(areaId);
        return area != null ? area.getName() : "";
    }


    public Map<Integer, MetaCountry> getCountryIndexMap()
    {
        return countryIndexMap;
    }

    protected void setCountryIndexMap(Map<Integer, MetaCountry> countryIndexMap)
    {
        this.countryIndexMap = countryIndexMap;
    }

    public Map<Integer, MetaCountry> getCountryOnlyIndexMap()
    {
        return countryOnlyIndexMap;
    }

    protected void setCountryOnlyIndexMap(Map<Integer, MetaCountry> countryOnlyIndexMap)
    {
        this.countryOnlyIndexMap = countryOnlyIndexMap;
    }

    public Map<Integer, MetaProvince> getProvinceIndexMap()
    {
        return provinceIndexMap;
    }

    protected void setProvinceIndexMap(Map<Integer, MetaProvince> provinceIndexMap)
    {
        this.provinceIndexMap = provinceIndexMap;
    }

    public Map<Integer, MetaCity> getCityIndexMap()
    {
        return cityIndexMap;
    }

    protected void setCityIndexMap(Map<Integer, MetaCity> cityIndexMap)
    {
        this.cityIndexMap = cityIndexMap;
    }

    public Map<Integer, MetaArea> getAreaIndexMap()
    {
        return areaIndexMap;
    }

    protected void setAreaIndexMap(Map<Integer, MetaArea> areaIndexMap)
    {
        this.areaIndexMap = areaIndexMap;
    }
}
