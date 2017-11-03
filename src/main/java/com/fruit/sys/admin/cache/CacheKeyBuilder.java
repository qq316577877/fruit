/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.cache;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2014-11-28
 * Project        : promotion
 * File Name      : CacheKeyBuilder.java
 */
public interface CacheKeyBuilder
{
    public Object buildKey(Object... args);
}
