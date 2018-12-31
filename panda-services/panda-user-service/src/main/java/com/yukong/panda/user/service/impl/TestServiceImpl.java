package com.yukong.panda.user.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yukong.panda.user.mapper.TestMapper;
import com.yukong.panda.user.model.entity.Test;
import com.yukong.panda.user.model.query.TestQuery;
import com.yukong.panda.user.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * @author lumiing
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

    public void test(){
//        this.save()
    }

}
