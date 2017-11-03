package com.fruit.sys.admin.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.*;

public class CollectionTools
{
    /**
     * @param <T>
     * @param list
     * @param key
     * @param value
     * @return 返回第一个满足条件的数据
     */
    public static <T> T searchByField(Collection<T> list, String key, Object value)
    {
        for (T o : list)
        {
            try
            {
                Object v = BeanUtils.getProperty(o, key);
                if (value.equals(v))
                {
                    return o;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param <T>
     * @param list
     * @param key
     * @param value
     * @return 返回所有满足条件的数据
     */
    public static <T> List<T> searchListByField(Collection<T> list, String key, Object value)
    {
        List<T> res = new ArrayList<T>();

        for (T o : list)
        {
            try
            {
                Object v = BeanUtils.getProperty(o, key);
                if (value.equals(v))
                {
                    res.add(o);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * list转成map
     *
     * @param <T>
     * @param list
     * @param key  list属性值
     * @return
     */
    public static <T> Map<String, T> list2Map(Collection<T> list, String key)
    {
        Map<String, T> res = new HashMap<String, T>();
        for (T o : list)
        {
            try
            {
                Object v = BeanUtils.getProperty(o, key);
                if (v != null)
                {
                    res.put(v.toString(), o);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return res;
    }

    public static Map<String, String> list2Map(Collection<?> list, String key, String value)
    {
        Map<String, String> res = new HashMap<String, String>();
        for (Object o : list)
        {
            try
            {
                Object v = BeanUtils.getProperty(o, key);
                if (v != null)
                {
                    res.put(v.toString(), BeanUtils.getProperty(o, value).toString());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return res;
    }

    /**
     * 保留排序
     *
     * @param <T>
     * @param list
     * @param key
     * @return
     */
    public static <T> Map<String, T> list2Map4Sort(Collection<T> list, String key)
    {
        Map<String, T> res = new LinkedHashMap<String, T>();
        for (T o : list)
        {
            try
            {
                Object v = BeanUtils.getProperty(o, key);
                if (v != null)
                {
                    res.put(v.toString(), o);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return res;
    }

    public static Map<String, String> list2Map4Sort(Collection<?> list, String key, String value)
    {
        Map<String, String> res = new LinkedHashMap<String, String>();
        for (Object o : list)
        {
            try
            {
                Object v = BeanUtils.getProperty(o, key);
                if (v != null)
                {
                    res.put(v.toString(), BeanUtils.getProperty(o, value).toString());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return res;
    }

    public static List<Integer> createIntList(String brandIds)
    {
        List<Integer> ids = null;
        if (StringUtils.isNotBlank(brandIds))
        {
            String[] segments = brandIds.split(",");
            ids = new ArrayList<Integer>(segments.length);
            for (String segment : segments)
            {
                ids.add(NumberUtils.toInt(segment));
            }
        }
        return ids;
    }
}
