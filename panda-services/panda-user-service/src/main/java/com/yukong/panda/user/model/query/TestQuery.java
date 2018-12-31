package com.yukong.panda.user.model.query;

import com.yukong.panda.user.model.entity.Test;
import lombok.Data;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author lumiing
 */
@Data
public class TestQuery extends Page<Test> {

    /**
     * id
     */
    private Integer id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 
     */
    private String testField;

}
