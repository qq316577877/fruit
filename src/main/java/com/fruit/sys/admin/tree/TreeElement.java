package com.fruit.sys.admin.tree;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 树形元素
 *
 * @author Ciffer
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TreeElement<T> implements Serializable
{

    //数据
    private T data;        //数据

    //UI展现
    private String id;

    private String name;

    private String pid;

    private String value;

    private String type;   //节点类型 folder /  item

    private String lineal; //直系路径

    private boolean isRoot = false;

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getLineal()
    {
        return lineal;
    }

    public void setLineal(String lineal)
    {
        this.lineal = lineal;
    }

    public boolean isRoot()
    {
        return isRoot;
    }

    public void setRoot(boolean isRoot)
    {
        this.isRoot = isRoot;
    }

}