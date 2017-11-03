/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.meta;

import com.fruit.base.biz.dto.AreaDTO;
import com.fruit.base.biz.dto.CityDTO;
import com.fruit.base.biz.dto.CountryDTO;
import com.fruit.base.biz.dto.ProvinceDTO;
import com.fruit.base.biz.service.AreaService;
import com.fruit.base.biz.service.CityService;
import com.fruit.base.biz.service.CountryService;
import com.fruit.base.biz.service.ProvinceService;
import com.ovfintech.arch.common.spy.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description:
 * 基础数据服务，请使用各种XxxProvider来获取缓存的基础数据<p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-21
 * Project        : fruit
 * File Name      : MetadataService.java
 */
@Service
public class MetadataService
{
    private static final Logger LOG = LoggerFactory.getLogger(MetadataService.class);

    public static final String CITY = "sys-city";

    @Autowired
    private CountryService countryService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private MetadataProvider metadataProvider;

    private String cacheDir = "/data/appdatas/fruit/sys-admin-web/";

    /**
     * 功能描述：加载本地缓存
     * <p/>
     * 前置条件：需要基础信息数据库
     * <p/>
     * 方法影响：重新设置基础信息缓存
     * <p/>
     */
    @Spy
    public synchronized void loadMetadata()
    {
        try
        {
            loadCityData();
            LOG.warn("Finish reloading base data cache at {}", getTimeStamp());
        }
        catch (Exception e)
        {
            LOG.error("Can not load base data cache from pigeon service....", e);
        }
    }

    /**
     * 功能描述：加载城市数据信息
     */
    private synchronized void loadCityData()
    {
        try
        {
            String countryStoreFile = getStoreFile(CITY);
            Map<Integer, MetaCountry> ountryIndexMap  = (Map<Integer, MetaCountry>) deserialize(countryStoreFile);
            if (ountryIndexMap == null) // 文件读取失败，从数据库读取
            {
                this.refreshCityCache();
            }
            else
            {
                Map<Integer, MetaProvince> provinceIndexMap = new LinkedHashMap<Integer, MetaProvince>(36);
                Map<Integer, MetaCity> cityIndexMap = new LinkedHashMap<Integer, MetaCity>(390);
                Map<Integer, MetaArea> areaIndexMap = new LinkedHashMap<Integer, MetaArea>(4000);
                for (Map.Entry<Integer, MetaCountry> entry : ountryIndexMap.entrySet())
                {
                    MetaCountry metaCountry = entry.getValue();
                    List<MetaProvince> provinces = metaCountry.getProvinces();
                    for (MetaProvince province : provinces)
                    {
                        provinceIndexMap.put(province.getId(), province);
                        List<MetaCity> cities = province.getCities();
                        for (MetaCity city : cities)
                        {
                            cityIndexMap.put(city.getId(), city);
                            List<MetaArea> areas = city.getAreas();
                            for (MetaArea area : areas)
                            {
                                areaIndexMap.put(area.getId(), area);
                            }
                        }
                    }
                }
                Map<Integer, MetaCountry> cuntryOnlyIndexMap = this.retrieveCountryOnly(ountryIndexMap);
                this.metadataProvider.setCountryIndexMap(ountryIndexMap);
                this.metadataProvider.setCountryOnlyIndexMap(cuntryOnlyIndexMap);
                this.metadataProvider.setProvinceIndexMap(provinceIndexMap);
                this.metadataProvider.setCityIndexMap(cityIndexMap);
                this.metadataProvider.setAreaIndexMap(areaIndexMap);
            }
            LOG.warn("Finish serialize country & province & city & area data cache at {}", getTimeStamp());
        }
        catch (Exception e)
        {
            LOG.error("Can not handle country & province & city & area datas", e);
        }
    }

    /**
     * 功能描述：刷新城市信息缓存
     * <p/>
     * Author terry, 2017-05-17
     *
     * @throws
     */
    @Spy
    public synchronized void refreshCityCache()
    {
        try
        {
            Map<Integer, MetaCountry> metaCountries = this.retrieveCountry();
            Map<Integer, MetaCountry> metaCountriesOnly = this.retrieveCountryOnly(metaCountries);
            this.metadataProvider.setCountryIndexMap(metaCountries);
            this.metadataProvider.setCountryOnlyIndexMap(metaCountriesOnly);
            LOG.warn("Finish reloading country data cache at {}", getTimeStamp());

            Map<Integer, MetaProvince> metaProvinces = this.retrieveProvince(metaCountries);
            this.metadataProvider.setProvinceIndexMap(metaProvinces);
            LOG.warn("Finish reloading province data cache at {}", getTimeStamp());

            Map<Integer, MetaCity> metaCities = this.retrieveCity(metaProvinces);
            this.metadataProvider.setCityIndexMap(metaCities);
            LOG.warn("Finish reloading city data cache at {}", getTimeStamp());

            Map<Integer, MetaArea> metaAreas = this.retrieveArea(metaCities);
            this.metadataProvider.setAreaIndexMap(metaAreas);
            LOG.warn("Finish reloading area data cache at {}", getTimeStamp());

            serialize(metaCountries, getStoreFile(CITY));
            LOG.warn("Finish serialize country & province & city & area data cache at {}", getTimeStamp());
        }
        catch (Exception e)
        {
            LOG.error("Can not handle country & province & city & area datas", e);
        }
    }

    private String getTimeStamp()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private Map<Integer, MetaCountry> retrieveCountry()
    {
        List<CountryDTO> models = this.countryService.loadAll();
        Map<Integer, MetaCountry> metas = new LinkedHashMap<Integer, MetaCountry>(models.size());
        for(CountryDTO model : models)
        {
            MetaCountry meta = new MetaCountry();
            BeanUtils.copyProperties(model, meta);
            metas.put(meta.getId(), meta);
        }
        return metas;
    }

    private Map<Integer, MetaCountry> retrieveCountryOnly(Map<Integer, MetaCountry> countries)
    {
        Map<Integer, MetaCountry> metas = new LinkedHashMap<Integer, MetaCountry>(countries.size());
        for(Map.Entry<Integer, MetaCountry> entry : countries.entrySet())
        {
            MetaCountry meta = new MetaCountry();
            BeanUtils.copyProperties(entry.getValue(), meta);
            meta.setProvinces(Collections.EMPTY_LIST);
            metas.put(meta.getId(), meta);
        }
        return metas;
    }

    private Map<Integer, MetaProvince> retrieveProvince(Map<Integer, MetaCountry> metaCountries)
    {
        List<ProvinceDTO> models = this.provinceService.loadAll();
        Map<Integer, MetaProvince> metas = new LinkedHashMap<Integer, MetaProvince>(models.size());
        for(ProvinceDTO model : models)
        {
            MetaProvince meta = new MetaProvince();
            BeanUtils.copyProperties(model, meta);
            metas.put(meta.getId(), meta);
            MetaCountry metaCountry = metaCountries.get(model.getCountryId());
            if (null != metaCountry)
            {
                metaCountry.getProvinces().add(meta);
            }
        }
        return metas;
    }

    private Map<Integer, MetaCity> retrieveCity(Map<Integer, MetaProvince> metaProvinces)
    {
        List<CityDTO> models = this.cityService.loadAll();
        Map<Integer, MetaCity> metas = new LinkedHashMap<Integer, MetaCity>(models.size());
        for(CityDTO model : models)
        {
            MetaCity meta = new MetaCity();
            BeanUtils.copyProperties(model, meta);
            metas.put(meta.getId(), meta);
            MetaProvince metaProvince = metaProvinces.get(model.getProvinceId());
            if (null != metaProvince)
            {
                metaProvince.getCities().add(meta);
            }
        }
        return metas;
    }

    private Map<Integer, MetaArea> retrieveArea(Map<Integer, MetaCity> metaCities)
    {
        List<AreaDTO> models = this.areaService.loadAll();
        Map<Integer, MetaArea> metas = new LinkedHashMap<Integer, MetaArea>(models.size());
        for(AreaDTO model : models)
        {
            MetaArea meta = new MetaArea();
            BeanUtils.copyProperties(model, meta);
            metas.put(model.getId(), meta);
            MetaCity metaCity = metaCities.get(model.getCityId());
            if (null != metaCity)
            {
                metaCity.getAreas().add(meta);
            }
        }
        return metas;
    }

    private String getStoreFile(String name)
    {
        return this.cacheDir.concat(name);
    }

    public static void serialize(Object object, String path)
    {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        File file = new File(path);
        try
        {
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
        }
        catch (IOException e)
        {
            LOG.error("Can not serialize object:" + object.getClass(), e);
        }
        finally
        {
            try
            {
                objectOutputStream.close();
            }
            catch (IOException e)
            {
                LOG.error("Can not close io", e);
            }
        }
    }

    public static Object deserialize(String path)
    {
        Object object = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        File file = new File(path);
        if(!file.exists())
        {
            return null;
        }

        try
        {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();

        }
        catch (IOException e)
        {
            LOG.error("Can not deserialize object to path:" + path, e);
        }
        catch (ClassNotFoundException e)
        {
            LOG.error("Can not deserialize object to path:" + path, e);
        }
        finally
        {
            try
            {
                objectInputStream.close();
                fileInputStream.close();
            }
            catch (IOException e)
            {
                LOG.error("Can not close io", e);
            }
        }
        return object;
    }

}
