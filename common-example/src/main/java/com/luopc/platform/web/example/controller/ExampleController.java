package com.luopc.platform.web.example.controller;

import com.luopc.platform.common.core.exception.BusinessException;
import com.luopc.platform.common.core.exception.PlatformErrorCode;
import com.luopc.platform.common.core.util.SmartJsonUtil;
import com.luopc.platform.common.core.util.SmartNumIDUtil;
import com.luopc.platform.web.example.request.UserReq;
import com.luopc.platform.web.result.PageParam;
import com.luopc.platform.web.result.PageResult;
import com.luopc.platform.web.result.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/example", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "ExampleController", description = "样例模版")
public class ExampleController {

    @Operation(summary = "获取单条记录", description = "不需要登录后访问")
    @Parameters({
            @Parameter(name = "name", description = "用户姓名")
    })
    @GetMapping("/get")
    public String getUser(@RequestParam("name") String name) {
        log.info("Received request to list users by name:{}", name);
        Map<String, Object> user = new HashMap<>();
        user.put("id", SmartNumIDUtil.nextPkId());
        user.put("name", StringUtils.isNoneBlank(name) ? name : "Zhang san");
        return SmartJsonUtil.writeJson(user);
    }

    @Operation(summary = "获取多条记录", description = "不需要登录后访问")
    @PostMapping("/list")
    public ResponseEntity<Object> getUserList(@Parameter(name = "userReq", description = "查询条件") @RequestBody UserReq userReq) {
        log.info("Received request to list users by name:{}, pageParam:{}", userReq.getName(), userReq.getPageParam());
        List<Map<String, Object>> userList = queryUsers(userReq);
        PageParam pageParam = userReq.getPageParam();
        return ResponseEntity.ok(PageResult.of(pageParam.getPageNum(), pageParam.getPageSize(), 2, userList));
    }

    @Operation(summary = "测试失败的结果", description = "不需要登录后访问")
    @GetMapping("/failure")
    public ResponseEntity<Object> getFailure() {
        return ResponseEntity.internalServerError().body(new BusinessException(PlatformErrorCode.BUSINESS_ERROR));
    }

    @Operation(summary = "测试异常", description = "不需要登录后访问")
    @GetMapping("/throw")
    public ResponseEntity<Object> getThrow() {
        throw new BusinessException(PlatformErrorCode.NETWORK_ERROR);
    }

    @Operation(summary = "获取Message类型的数据", description = "不需要登录后访问")
    @PostMapping("/message")
    public ResponseMessage<Object> getUserListMessage(@Parameter(name = "userReq", description = "查询条件") @RequestBody UserReq userReq) {
        log.info("Received request to list users by name:{}, pageParam:{}", userReq.getName(), userReq.getPageParam());
        List<Map<String, Object>> userList = queryUsers(userReq);
        PageParam pageParam = userReq.getPageParam();
        return ResponseMessage.success(PageResult.of(pageParam.getPageNum(), pageParam.getPageSize(), 2, userList));
    }

    @Operation(summary = "测试错误", description = "不需要登录后访问")
    @GetMapping("/error")
    public ResponseMessage<Object> getErrorMessage() {
        return ResponseMessage.error(String.valueOf(PlatformErrorCode.SERVICE_UNAVAILABLE.getCode()), PlatformErrorCode.SERVICE_UNAVAILABLE.getMessage());
    }


    private List<Map<String, Object>> queryUsers(UserReq userReq) {
        List<Map<String, Object>> userList = new ArrayList<>();

        Map<String, Object> user = new HashMap<>();
        user.put("id", SmartNumIDUtil.nextPkId());
        user.put("name", StringUtils.isNoneBlank(userReq.getName()) ? userReq.getName() : "Zhang san");
        userList.add(user);

        user = new HashMap<>();
        user.put("id", SmartNumIDUtil.nextPkId());
        user.put("name", StringUtils.isNoneBlank(userReq.getName()) ? userReq.getName() : "Li si");
        userList.add(user);
        return userList;
    }
}
