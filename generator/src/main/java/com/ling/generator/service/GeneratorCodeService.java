package com.ling.generator.service;

/**
 * @author zhangling 2021/4/2 11:47
 */

import com.google.common.base.CaseFormat;
import com.ling.generator.model.ColumnClass;
import com.ling.generator.model.RespBean;
import com.ling.generator.model.TableClass;
import com.ling.generator.utils.DbUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeneratorCodeService {

    // 加载 Freemarker模板
    Configuration config = null;

    {
        config = new Configuration(Configuration.VERSION_2_3_31);
        config.setTemplateLoader(new ClassTemplateLoader(GeneratorCodeService.class, "/templates"));
        config.setDefaultEncoding("UTF-8");
    }

    /**
     * @param tableClasses
     * @param realPath     根路径
     * @return
     */
    public RespBean generatorCode(List<TableClass> tableClasses, String realPath) {
        try {
            Template modelTemplate = config.getTemplate("Model.java.ftl");
            Template controllerTemplate = config.getTemplate("Controller.java.ftl");
            Template serviceTemplate = config.getTemplate("Service.java.ftl");
            Template mapperJavaTemplate = config.getTemplate("Mapper.java.ftl");
            Template mapperXmlTemplate = config.getTemplate("Mapper.xml.ftl");

            Connection connection = DbUtils.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            for (TableClass tableClass : tableClasses) {
                ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableClass.getTableName(), null);
                ResultSet primaryKeys = metaData.getPrimaryKeys(connection.getCatalog(), null, tableClass.getTableName());
                List<ColumnClass> columnClassList = new ArrayList<>();
                while (columns.next()) {
                    String column_name = columns.getString("COLUMN_NAME");
                    String type_name = columns.getString("TYPE_NAME");
                    String remarks = columns.getString("REMARKS");
                    ColumnClass columnClass = new ColumnClass();
                    columnClass.setColumnName(column_name);
                    columnClass.setType(type_name);
                    columnClass.setRemark(remarks);
                    // 转换成陀峰
                    columnClass.setPropertyName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, column_name));

                    // 遍历一次后需要移动指针到首位置
                    primaryKeys.first();
                    while (primaryKeys.next()) {
                        String pkName = primaryKeys.getString("COLUMN_NAME");
                        if (column_name.equals(pkName)) {
                            columnClass.setIsPrimary(true);
                        }
                    }
                    columnClassList.add(columnClass);
                }
                tableClass.setColumns(columnClassList);

                // 生成代码
                //String path = realPath + "/"+tableClass.getPackageName().replace(".","/");     // 临时文件夹路径
                String path = GeneratorCodeService.class.getResource("/").getPath() + tableClass.getPackageName().replace(".", "/");
                generate(modelTemplate, tableClass, path + "/model/");
                generate(controllerTemplate, tableClass, path + "/controller/");
                generate(serviceTemplate, tableClass, path + "/service/");
                generate(mapperJavaTemplate, tableClass, path + "/mapper/");
                generate(mapperXmlTemplate, tableClass, path + "/mapper/");
            }
            return RespBean.ok("代码已生成", realPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("代码生成失败");
    }

    /**
     * 根据模板生成代码
     *
     * @param template   Freemarker生成的模板
     * @param tableClass 需要生成模板的数据库表
     * @param path       存放生成代码路径
     * @throws IOException
     * @throws TemplateException
     */
    private void generate(Template template, TableClass tableClass, String path) throws IOException, TemplateException {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = path + "/" + tableClass.getModelName() + template.getName().replace(".ftl", "").replace("Model", "");
        FileOutputStream fos = new FileOutputStream(fileName);
        OutputStreamWriter out = new OutputStreamWriter(fos);
        template.process(tableClass, out);
        fos.close();
        out.close();

    }
}
