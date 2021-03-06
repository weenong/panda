package com.yukong.panda.gen.util;

import com.yukong.panda.gen.model.config.ColumnInfoConfig;
import com.yukong.panda.gen.model.config.TableInfoConfig;
import com.yukong.panda.gen.model.dto.BuildConfigDTO;
import com.yukong.panda.gen.model.entity.ColumnInfo;
import com.yukong.panda.gen.model.entity.TableInfo;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author: yukong
 * @date: 2018/11/8 14:09
 */
public class GenUtil {

    private static Boolean hasBigDecimal = false;

    public static List<String> getTemplates(String genType) {
        List<String> templates = new ArrayList<String>();
        templates.add("templates/Entity.java.vm");
        templates.add("templates/query.java.vm");
        templates.add("templates/controller.java.vm");
        templates.add("templates/service.java.vm");
        templates.add("templates/serviceImpl.java.vm");
        templates.add("templates/mapper.java.vm");
        templates.add("templates/Mapper.xml.vm");
        templates.add("templates/vue_element.vue.vm");
        templates.add("templates/api.js.vm");

//        if(GenTypeEnum.IBATIS.getKey().equals(genType)) {
//            templates.add("templates/dao.java.vm");
//            templates.add("templates/daoImpl.java.vm");
//            templates.add("templates/IBatis_sql_map.xml.vm");
//            templates.add("templates/query.java.vm");
//            templates.add("templates/service.java.vm");
//            templates.add("templates/serviceImpl.java.vm");
//        } else {
//            templates.add("templates/mapper.java.vm");
//            templates.add("templates/Mapper.xml.vm");
//        }
        return templates;
    }

    /**
     * 生成代码
     * @param tableInfo 
     * @param columns
     * @param zip
     */
    public static void generatorCode(BuildConfigDTO buildConfigDTO, TableInfo tableInfo, List<ColumnInfo> columns,
                                     ZipOutputStream zip) {
        hasBigDecimal = false;
        // 配置信息
        Configuration config = getConfig();
        // 表信息
        TableInfoConfig tableConfig = new TableInfoConfig();

        //  配置 包名等配置
        buildTableConfig(buildConfigDTO ,config, tableConfig);


        BeanUtils.copyProperties(tableInfo, tableConfig);

        //  构建表基本信息
        buildTableInfo(config, tableConfig,columns);

        // 生成代码
        gen(buildConfigDTO, tableConfig, zip);



    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[] { '_' }).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template,TableInfoConfig tableInfoConfig) {
        String modelPackagePath = "main" + File.separator + "java" + File.separator
                + tableInfoConfig.getModelPackageName().replace(".", File.separator);
        String resourcesPath = "main" + File.separator + "resources";
        String frontPath = "main" + File.separator + "front";
        String className = tableInfoConfig.getClassName();
        if (template.contains("Entity.java.vm")) {
            return modelPackagePath + File.separator + "entity" + File.separator + className
                    + ".java";
        }
        if (template.contains("dao.java.vm")) {
            String path = "main" + File.separator + "java" + File.separator
                    + tableInfoConfig.getDaoPackageName().replace(".", File.separator);
            return path + File.separator+ className
                    + "Dao.java";
        }

        if (template.contains("mapper.java.vm")) {
            String path = "main" + File.separator + "java" + File.separator
                    + tableInfoConfig.getDaoPackageName().replace(".", File.separator);
            return path + File.separator+ className
                    + "Mapper.java";
        }

        if (template.contains("daoImpl.java.vm")) {
            String path = "main" + File.separator + "java" + File.separator
                    + tableInfoConfig.getDaoPackageName().replace(".", File.separator);
            return path + File.separator+ "impl" + File.separator + className
                    + "DaoImpl.java";
        }
        if (template.contains("service.java.vm")) {
            String path = "main" + File.separator + "java" + File.separator
                    + tableInfoConfig.getServicePackageName().replace(".", File.separator);
            return path + File.separator+ className
                    + "Service.java";
        }
        if (template.contains("serviceImpl.java.vm")) {
            String path = "main" + File.separator + "java" + File.separator
                    + tableInfoConfig.getServicePackageName().replace(".", File.separator);
            return path + File.separator + "impl" + File.separator + className
                    + "ServiceImpl.java";
        }
        if (template.contains("controller.java.vm")) {
            String path = "main" + File.separator + "java" + File.separator
                    + tableInfoConfig.getControllerPackageName().replace(".", File.separator);
            return path + File.separator+ className
                    + "Controller.java";
        }
        if (template.contains("query.java.vm")) {
            String path = "main" + File.separator + "java" + File.separator
                    + tableInfoConfig.getModelPackageName().replace(".", File.separator);
            return path + File.separator + "query" + File.separator + className
                    + "Query.java";
        }

        if (template.contains("IBatis_sql_map.xml.vm")) {
            return resourcesPath + File.separator+ className
                    + "_sql_map.xml";
        }

        if (template.contains("Mapper.xml.vm")) {
            return resourcesPath + File.separator + "mapper" + File.separator + className
                    + "Mapper.xml";
        }

        if(template.contains("vue_element.vue.vm")){
            return frontPath + File.separator + "vue" + File.separator + "index.vue";
        }

        if(template.contains("api.js.vm")){
            return frontPath + File.separator + "api" + File.separator + tableInfoConfig.getLowerClassName() + ".js";
        }
        return null;
    }


    /**
     * 构建表配置信息  包名等
     * @param buildConfigDTO
     * @param configuration
     * @param tableInfoConfig
     */
    public static void buildTableConfig(BuildConfigDTO buildConfigDTO,Configuration configuration,TableInfoConfig tableInfoConfig) {

        BeanUtils.copyProperties(buildConfigDTO, tableInfoConfig);

        if(StringUtils.isEmpty(tableInfoConfig.getModelPackageName())) {
            String modelPackageName = configuration.getString("modelPackageName", "com.yukong.panda.gen.model");
            tableInfoConfig.setModelPackageName(modelPackageName);
        }
        tableInfoConfig.setPackageName(tableInfoConfig.getModelPackageName() + ".entity");
        tableInfoConfig.setQueryPackageName(tableInfoConfig.getModelPackageName() + ".query");

        if(StringUtils.isEmpty(tableInfoConfig.getDaoPackageName())) {
            tableInfoConfig.setDaoPackageName(configuration.getString("daoPackageName", "com.yukong.panda.gen.mapper"));

        }

        if(StringUtils.isEmpty(tableInfoConfig.getAuthorName())) {
            tableInfoConfig.setAuthorName(configuration.getString("authorName", "yukong"));
        }

        if(StringUtils.isEmpty(tableInfoConfig.getServiceApiPackageName())) {
            tableInfoConfig.setServiceApiPackageName(configuration.getString("serviceApiPackageName", "com.yukong.panda.gen.service.api"));
        }

        if(StringUtils.isEmpty(tableInfoConfig.getServicePackageName())) {
            tableInfoConfig.setServicePackageName(configuration.getString("servicePackageName", "com.yukong.panda.gen.service"));
        }

        if(StringUtils.isEmpty(tableInfoConfig.getControllerPackageName())) {
            tableInfoConfig.setControllerPackageName(configuration.getString("controllerPackageName", "com.yukong.panda.gen.controller"));
        }

//
//        if(StringUtils.isEmpty(tableInfoConfig.getQueryPackageName())) {
//            tableInfoConfig.setQueryPackageName(configuration.getString("queryPackageName", "com.yukong.panda.gen.mapper"));
//        }

    }

    /**
     * 构建表基本数据信息
     * @param config
     * @param tableConfig
     * @param columns
     */
    public static void  buildTableInfo(Configuration config,TableInfoConfig tableConfig,List<ColumnInfo> columns) {
        // 表名转换成Java类名
        String className = tableToJava(tableConfig.getTableName(), config.getString("tablePrefix"));
        tableConfig.setClassName(className);
        tableConfig.setLowerClassName(StringUtils.uncapitalize(className));

        // 列信息
        List<ColumnInfoConfig> columnInfoConfigs = new ArrayList<>();
        for (ColumnInfo column : columns) {


            ColumnInfoConfig columnEntity = new ColumnInfoConfig();
            BeanUtils.copyProperties(column, columnEntity);

            // 列名转换成Java属性名
            String upAttrName = columnToJava(column.getColumnName());
            columnEntity.setUpAttrName(upAttrName);
            String temp = columnEntity.getUpAttrName();
            temp = (new StringBuilder()).append(Character.toLowerCase(temp.charAt(0)))
                    .append(temp.substring(1)).toString();
            columnEntity.setAttrName(temp);
            // 列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && StringUtils.equals("BigDecimal", column.getDataType())) {
                hasBigDecimal = true;
            }
            // 是否主键
            if ( tableConfig.getPk() == null && StringUtils.equalsIgnoreCase("PRI", column.getColumnKey()) ) {
                tableConfig.setPk(columnEntity);
            }

            columnInfoConfigs.add(columnEntity);
        }
        tableConfig.setColumnInfo(columnInfoConfigs);

        // 没主键，则第一个字段为主键
        if (tableConfig.getPk() == null) {
            tableConfig.setPk(tableConfig.getColumnInfo().get(0));
        }
    }

    /**
     * 生成代码
     * @param buildConfigDTO
     * @param tableConfig
     * @param zip
     */
    public static void gen(BuildConfigDTO buildConfigDTO,TableInfoConfig tableConfig, ZipOutputStream zip) {
        // 设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        LocalDateTime now = LocalDateTime.now();
        // 封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableInfo", tableConfig);
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("datetime", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        VelocityContext context = new VelocityContext(map);

        // 获取模板列表
        List<String> templates = getTemplates(buildConfigDTO.getGenType());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableConfig)));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException("渲染模板失败，表名：" + tableConfig.getTableName(), e);
            }
        }
    }
}
