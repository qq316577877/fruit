package com.fruit.sys.admin.tree;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

public class TreeConstant
{
    public static String type(Collection<?> c)
    {
        return !CollectionUtils.isEmpty(c) ? "folder" : "item";
    }
}
