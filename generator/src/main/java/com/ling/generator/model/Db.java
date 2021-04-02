package com.ling.generator.model;

import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;

/**
 * @author zhangling 2021/4/2 9:28
 */
@Data
public class Db {

    private String username;
    private String password;
    private String url;

}
