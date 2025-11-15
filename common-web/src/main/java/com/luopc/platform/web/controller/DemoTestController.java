package com.luopc.platform.web.controller;

import com.luopc.platform.common.core.util.SmartJsonUtil;
import com.luopc.platform.common.core.util.SmartNumIDUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/demo", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "DemoTestController", description = "测试方法")
public class DemoTestController {

    @Operation(summary = "获取单条记录", description = "不需要登录后访问")
    @Parameters({
            @Parameter(name = "name", description = "用户姓名")
    })
    @GetMapping("/get")
    public String getUser(@RequestParam("name") String name) {
        log.info("Received request to get user by name:{}", name);
        Map<String, Object> user = new HashMap<>();
        user.put("id", SmartNumIDUtil.nextPkId());
        user.put("name", StringUtils.isNoneBlank(name) ? name : "Zhang san");
        return SmartJsonUtil.writeJson(user);
    }
}
