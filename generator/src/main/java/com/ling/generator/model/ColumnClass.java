package com.ling.generator.model;

import lombok.Data;

/**
 * 数据库中表的列属性
 * @author zhangling 2021/4/2 10:30
 */
@Data
public class ColumnClass {

    /**
     * 数据库列对应Java属性名
     */
    private String propertyName;
    /**
     * 数据库字段名
     */
    private String columnName;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 备注
     */
    private String remark;

    /**
     * 是否是主键
     */
    private Boolean isPrimary;
}
