package com.ling.generator.model;

import lombok.Data;

import java.util.List;

/**
 * 表的信息
 * @author zhangling 2021/4/2 10:33
 */
@Data
public class TableClass {
    private String tableName;
    private String modelName;
    private String serviceName;
    private String mapperName;
    /**
     * 生成控制层的名字
     */
    private String controllerName;
    private String packageName;

    private List<ColumnClass> columns;


}
