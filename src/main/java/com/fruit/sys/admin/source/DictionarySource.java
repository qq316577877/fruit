package com.fruit.sys.admin.source;

import com.fruit.base.biz.common.DBStatusEnum;
import com.fruit.base.biz.common.EnterpriseTypeEnum;
import com.fruit.base.biz.common.LocationTypeEnum;
import com.fruit.sys.biz.common.AdminMenuType;
import com.fruit.sys.biz.common.AdminStatus;
import com.fruit.sys.biz.common.AdminType;

import java.util.HashMap;
import java.util.Map;

public class DictionarySource
{
    private static Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

    static
    {
        initAdmin();
        initConfigs();
    }

    public static void initAdmin()
    {
        map.put("AdminMenuType", AdminMenuType.map);
        map.put("AdminStatus", AdminStatus.map);
        map.put("AdminType", AdminType.map);
    }

    public static void initConfigs()
    {
        map.put("DBStatus", DBStatusEnum.map);
        map.put("EnterpriseType", EnterpriseTypeEnum.map);
        map.put("LocationType", LocationTypeEnum.map);
    }

    public static Map<String, String> getDic(String key)
    {
        return map.get(key);
    }

    public String parse(String dicCode, String dicKey)
    {
        Map<String, String> tmp = map.get(dicCode);
        if (tmp == null)
        {
            return "";
        }

        if (tmp.get(dicKey) == null)
        {
            return "";
        }
        return tmp.get(dicKey);
    }
}
