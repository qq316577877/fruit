package com.fruit.sys.admin.tree;

/**
 * 构建树形的参数
 *
 * @author Ciffer
 */
public class TreeCreateParam
{

    private String fieldName_id;    //NODE_ID 对应的字段名

    private String fieldName_pid;   //NODE_PID 对应的字段名

    private String fieldName_name;    //NODE_NAME 对应的字段名

    private String fieldName_value;    //NODE_value 对应的字段名

    private String fieldName_checked;    //NODE_checked 对应的字段名

    private String root_id = "";

    private String root_name;

    private String root_value; //root 的值

    private String separator = "/"; //直系路径的分隔符

    public String getFieldName_id()
    {
        return fieldName_id;
    }

    public void setFieldName_id(String fieldNameId)
    {
        fieldName_id = fieldNameId;
    }

    public String getFieldName_pid()
    {
        return fieldName_pid;
    }

    public void setFieldName_pid(String fieldNamePid)
    {
        fieldName_pid = fieldNamePid;
    }

    public String getFieldName_name()
    {
        return fieldName_name;
    }

    public void setFieldName_name(String fieldNameName)
    {
        fieldName_name = fieldNameName;
    }

    public String getRoot_id()
    {
        return root_id;
    }

    public void setRoot_id(String rootId)
    {
        root_id = rootId;
    }

    public String getRoot_name()
    {
        return root_name;
    }

    public void setRoot_name(String rootName)
    {
        root_name = rootName;
    }

    public String getRoot_value()
    {
        return root_value;
    }

    public void setRoot_value(String rootValue)
    {
        root_value = rootValue;
    }

    public String getSeparator()
    {
        return separator;
    }

    public void setSeparator(String separator)
    {
        this.separator = separator;
    }

    public String getFieldName_value()
    {
        return fieldName_value;
    }

    public void setFieldName_value(String fieldNameValue)
    {
        fieldName_value = fieldNameValue;
    }

    public String getFieldName_checked()
    {
        return fieldName_checked;
    }

    public void setFieldName_checked(String fieldNameChecked)
    {
        fieldName_checked = fieldNameChecked;
    }
}