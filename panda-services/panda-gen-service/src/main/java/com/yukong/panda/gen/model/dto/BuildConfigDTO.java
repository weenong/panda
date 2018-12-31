package com.yukong.panda.gen.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: yukong
 * @date: 2018/11/8 15:52
 */
@Data
public class BuildConfigDTO {

    private String genType;

    private List<String> tableName;

    /**
     * 包名
     */
    private String modelPackageName;

    /**
     * query类的包名
     */
//    private String queryPackageName;

    /**
     * controller类包名
     */
    private String controllerPackageName;
    /**
     * service类包名
     */
    private String servicePackageName;

    /**
     * serviceApi类包名
     */
    private String serviceApiPackageName;

    /**
     * dao的包名
     */
    private String daoPackageName;

    /**
     * 作者名称
     */
    private String authorName;

}
