/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.cache;

import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2014-11-28
 * Project        : promotion
 * File Name      : CacheFetcher.java
 */
public abstract class CacheFetcher<T>
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(CacheFetcher.class);

    protected static final String CLAZZ_TYPE = "CacheFetcher";

    private static final String LOAD_FROM_SERVICE = ".LoadFromService";

    private static final String UPDATE_CACHE = ".UpdateCache";

    private static final String LOAD_FROM_CACHE = ".LoadFromCache";

    @Autowired(required = false)
    protected List<EventPublisher> eventPublishers;

    public T get(Cache cache, String action, CacheKeyBuilder cacheKeyBuilder, Object... args)
    {
//        Transaction transaction = Cat.newTransaction(CLAZZ_TYPE, action);
        T result = null;
        try
        {
            Object cacheKey = cacheKeyBuilder.buildKey(args);
            result = this.loadFromCache(cache, action, cacheKey);
            if (result == null)
            {
                result = this.loadData(action, result, args);

                if (result != null)
                {
                    this.updateCache(cache, action, result, cacheKey);
                }
            }
//            transaction.setStatus(Transaction.SUCCESS);
        }
        catch (RuntimeException e)
        {
//            transaction.setStatus(e);
            String argsKey = StringUtils.join(args);
            String message = "[get]Can not " + action  + ": " + argsKey;
            LOGGER.error(message, e);
            EventHelper.triggerEvent(this.eventPublishers, CLAZZ_TYPE + "." + action + "." + argsKey, message,
                    EventLevel.IMPORTANT, e);
        }
        finally
        {
//            transaction.complete();
        }
        return result;
    }

    protected T loadData(String action, T flag, Object[] args)
    {
//        Transaction transaction = Cat.newTransaction(CLAZZ_TYPE, action + LOAD_FROM_SERVICE);
        try
        {
            flag = loadFromService(args);
//            transaction.setStatus(Transaction.SUCCESS);
        }
       catch (RuntimeException e)
       {
//           transaction.setStatus(e);
           String argsKey = StringUtils.join(args);
           String message = "[loadData]Can not " + action + " by " + argsKey;
           LOGGER.error(message, e);
           EventHelper.triggerEvent(this.eventPublishers, CLAZZ_TYPE + "." + action + LOAD_FROM_SERVICE + "." + argsKey, message,
                   EventLevel.IMPORTANT, e);
       }
       finally
       {
//           transaction.complete();
       }
        return flag;
    }

    protected void updateCache(Cache cache, String action, Object value, Object cacheKey)
    {
//        Transaction transaction = Cat.newTransaction(CLAZZ_TYPE,  action + UPDATE_CACHE);
        try
        {
            cache.put(new Element(cacheKey, value));
//            transaction.setStatus(Transaction.SUCCESS);
        }
        catch (RuntimeException e)
        {
//            transaction.setStatus(e);
            String message = "[updateCache]Can not " + action + " by " + cacheKey;
            LOGGER.error(message, e);
            EventHelper.triggerEvent(this.eventPublishers, CLAZZ_TYPE + "." + action + "." + cacheKey, message,
                    EventLevel.IMPORTANT, e);
        }
        finally
        {
//            transaction.complete();
        }
    }

    protected abstract T loadFromService(Object... args);

    protected T loadFromCache(Cache cache, String action,  Object cacheKey)
    {
        T result = null;
//        Transaction transaction = Cat.newTransaction(CLAZZ_TYPE, action + LOAD_FROM_CACHE);
        try
        {
            Element element = cache.get(cacheKey);
            if (element != null)
            {
                result = (T) element.getValue();
            }
//            transaction.setStatus(Transaction.SUCCESS);
        }
        catch (RuntimeException e)
        {
//            transaction.setStatus(e);
            String message = "[loadFromCache]Can not " + action + " by " + cacheKey;
            LOGGER.error(message, e);
            EventHelper.triggerEvent(this.eventPublishers, CLAZZ_TYPE + "." + action + "." + cacheKey, message,
                    EventLevel.IMPORTANT, e);
        }
        finally
        {
//            transaction.complete();
        }
        return result;
    }

}
