package com.ling.generator.controller;

import com.google.common.base.CaseFormat;
import com.ling.generator.model.Db;
import com.ling.generator.model.RespBean;
import com.ling.generator.model.TableClass;
import com.ling.generator.utils.DbUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangling 2021/4/2 9:40
 */
@RestController
public class DbController {

    @PostMapping("/connect")
    public RespBean connect(@RequestBody Db db) {
        Connection con = DbUtils.initDb(db);
        if (con != null) {
            return RespBean.ok("数据库连接成功");
        }
        return RespBean.error("数据库连接失败");
    }


    /**
     * 根据传入的包名，生成对应的Java类对象
     * @param map
     * @return
     */
    @PostMapping("/config")
    public RespBean config(@RequestBody Map<String, String> map) {
        try {
            String packageName = map.get("packageName");
            Connection connection = DbUtils.getConnection();
            // 获取数据库信息
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取数据库所有表的信息
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, null);
            List<TableClass> tableClassList = new ArrayList<>();

            // 遍历数据库所有表，生成对应的java类名
            while (tables.next()) {
                TableClass tableClass = new TableClass();
                tableClass.setPackageName(packageName);
                String table_name = tables.getString("TABLE_NAME");
                // 将数据库表名转换成陀峰表示形式
                String modelName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table_name);
                tableClass.setTableName(table_name);
                tableClass.setModelName(modelName);
                tableClass.setControllerName(modelName + "Controller");
                tableClass.setServiceName(modelName + "Service");
                tableClass.setMapperName(modelName + "Mapper");
                tableClassList.add(tableClass);
            }
            return RespBean.ok("数据库信息读取成功", tableClassList);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return RespBean.error("数据库信息读取失败");
    }
}
