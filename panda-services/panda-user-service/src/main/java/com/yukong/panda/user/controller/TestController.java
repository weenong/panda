package com.yukong.panda.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yukong.panda.common.annotation.SysLog;
import com.yukong.panda.common.constants.PandaServiceNameConstants;
import com.yukong.panda.common.util.ApiResult;
import com.yukong.panda.user.model.entity.Test;
import com.yukong.panda.user.model.query.SysUserVoQuery;
import com.yukong.panda.user.model.query.TestQuery;
import com.yukong.panda.user.service.TestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lumiing
 */
@RestController
@RequestMapping(value = "/test",produces="application/json;charset=UTF-8")
@Api(value = "测试controller", tags = {"测试操作接口"})
public class TestController {

    @Autowired
    private TestService testService;

    @SysLog(serviceId = PandaServiceNameConstants.PANDA_USER_SERVICE, moduleName = "测试操作", actionName = "分页查询")
    @ApiOperation(value = "分页查询", notes = "分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "query", value = "查询条件", required = false, dataType = "TestQuery")
    @GetMapping(value = "/page",produces="application/json;charset=UTF-8")
    public Object pageByQuery(Page pageInfo, Test test){
        testService.page(pageInfo,null);
        return new ApiResult(pageInfo );
    }
}
