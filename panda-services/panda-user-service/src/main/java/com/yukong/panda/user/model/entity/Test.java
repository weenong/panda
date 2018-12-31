package com.yukong.panda.user.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 测试
 *
 * @author lumiing
 * @date 2018-12-31 03:16:15
 */
@Data
@Accessors(chain = true)
@TableName(value = "test")
public class Test implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    @TableField(value = "name",exist = true)
    private String name;
    /**
     * 
     */
    @TableField(value = "test_field",exist = true)
    private String testField;

}