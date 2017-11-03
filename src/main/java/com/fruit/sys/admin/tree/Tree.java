package com.fruit.sys.admin.tree;

import com.fruit.sys.admin.utils.CollectionTools;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * 树形结构
 *
 * @param <T>
 * @author Ciffer
 */
//忽略parent属性，防止json转换嵌套
@JsonIgnoreProperties(value = {"parent"})
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Tree<T> implements Serializable
{
    //关系
    private List<Tree<T>> nodeList = new ArrayList<Tree<T>>();

    private Tree<T> parent;

    //数据
    private T data;        //数据

    private Map<String, String> dataMapping;        //键值对,只有root有 <value,lineal>

    //UI展现
    private String id;

    private String name;

    private String pid;

    private String value;

    private String type;   //节点类型 folder /  item

    private String checked;

    private String lineal; //直系路径

    private String separator = "/";

    private boolean isRoot = false;

    public static <T> Tree<T> createTree(List<T> dataList, TreeCreateParam param)
    {

        if (dataList == null)
        {
            dataList = Collections.EMPTY_LIST;
        }

        return createTree(CollectionTools.list2Map4Sort(dataList, param.getFieldName_id()), param);
    }

    public static <T> Tree<T> createTree(Map<String, T> dataMap, TreeCreateParam param)
    {

        //将数据集转化成Tree类型
        Map<String, Tree<T>> treeMap = new LinkedHashMap<String, Tree<T>>();

        //键值对
        Map<String, String> dataMapping = new HashMap<String, String>();

        //若数据集中不含有跟节点，则创建root
        if (dataMap.get(param.getRoot_id()) == null)
        {
            Tree<T> t = new Tree<T>();
            t.isRoot = true;
            t.id = param.getRoot_id();
            t.name = param.getRoot_name();
            t.value = param.getRoot_value();
            t.separator = param.getSeparator();
            treeMap.put(t.id, t);
            t.dataMapping = dataMapping;

            if (t.value != null)
            {
                dataMapping.put(t.getValue(), t.getName());
            }
        }

        for (Entry<String, T> o : dataMap.entrySet())
        {
            Tree<T> node = treeMap.get(o.getKey());
            //转化
            if (treeMap.get(o.getKey()) == null)
            {
                node = change(o.getValue(), param);
            }
            if (node == null)
            {
                continue;
            }

            treeMap.put(node.getId(), node);

            //root节点不寻找父节点
            if (node.getId().equals(param.getRoot_id()))
            {
                continue;
            }

            //添加父节点
            Tree<T> p = treeMap.get(node.getPid());

            //转化
            if (p == null)
            {
                p = change(dataMap.get(node.getPid()), param);
            }
            //父节点不存在，使用根节点
            if (p == null)
            {
                p = treeMap.get(param.getRoot_id());
            }
            p.addNode(node);
            treeMap.put(p.getId(), p);
        }

        //获取键值对
        for (Tree<T> t : treeMap.values())
        {
            if (t.getValue() != null)
            {
                dataMapping.put(t.getValue(), t.getLineal());
            }
        }

        Tree<T> root = treeMap.get(param.getRoot_id());
        if (root != null)
        {
            root.isRoot = true;
            return root;
        }

        return null;
    }

    /**
     * 数据转化成Tree node
     */
    private static <T> Tree<T> change(T data, TreeCreateParam param)
    {
        if (data == null)
        {
            return null;
        }
        try
        {
            Tree<T> node = new Tree<T>();
            node.setData(data);
            if (StringUtils.isNotBlank(param.getFieldName_id()))
            {
                node.setId(BeanUtils.getProperty(data, param.getFieldName_id()));
            }
            if (StringUtils.isNotBlank(param.getFieldName_name()))
            {
                node.setName(BeanUtils.getProperty(data, param.getFieldName_name()));
            }
            if (StringUtils.isNotBlank(param.getFieldName_pid()))
            {
                node.setPid(BeanUtils.getProperty(data, param.getFieldName_pid()));
            }
            if (StringUtils.isNotBlank(param.getFieldName_value()))
            {
                node.setValue(BeanUtils.getProperty(data, param.getFieldName_value()));
            }
            if (StringUtils.isNotBlank(param.getFieldName_checked()))
            {
                node.setChecked(BeanUtils.getProperty(data, param.getFieldName_checked()));
            }
            node.setSeparator(param.getSeparator());
            return node;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected void addNode(Tree<T> m)
    {
        if (nodeList == null)
        {
            nodeList = new ArrayList<Tree<T>>();
        }
        nodeList.add(m);

        m.setParent(this);
    }

    public String getLineal()
    {
        if (!StringUtils.isNotBlank(lineal))
        {
            StringBuffer lineal_sb = new StringBuffer();
            setLineal(lineal_sb, this);
            lineal = org.apache.commons.lang.StringUtils.removeEnd(lineal_sb.toString(), separator);
        }
        return lineal;
    }

    public void setLineal(StringBuffer lineal, Tree<T> t)
    {
        if (t.parent != null && !t.parent.isRoot)
        {
            setLineal(lineal, t.getParent());
        }

        lineal.append(t.getName() + separator);
    }

    public String getType()
    {
        if (!StringUtils.isNotBlank(type))
        {
            type = TreeConstant.type(nodeList);
        }
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<Tree<T>> getNodeList()
    {
        return nodeList;
    }

    @JsonIgnore
    public Tree<T> getParent()
    {
        return parent;
    }

    public void setParent(Tree<T> parent)
    {
        this.parent = parent;
    }

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

    public void setLineal(String lineal)
    {
        this.lineal = lineal;
    }

    public String getSeparator()
    {
        return separator;
    }

    public void setSeparator(String separator)
    {
        this.separator = separator;
    }

    public boolean isRoot()
    {
        return isRoot;
    }

    public void setRoot(boolean isRoot)
    {
        this.isRoot = isRoot;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Map<String, String> getDataMapping()
    {
        return dataMapping;
    }

    public void setDataMapping(Map<String, String> dataMapping)
    {
        this.dataMapping = dataMapping;
    }

    public String getChecked()
    {
        return checked;
    }

    public void setChecked(String checked)
    {
        this.checked = checked;
    }

}