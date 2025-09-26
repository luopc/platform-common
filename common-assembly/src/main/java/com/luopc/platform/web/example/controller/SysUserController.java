package com.luopc.platform.web.example.controller;

import com.luopc.platform.web.example.entity.SysUser;
import com.luopc.platform.web.example.service.SysUserService;
import com.luopc.platform.web.result.ResponseMessage;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log
@RestController
@RequestMapping(value = "/api/sysUser", produces = MediaType.APPLICATION_JSON_VALUE)
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/add")
    public ResponseEntity<String> addSysUser(SysUser sysUser) {
        Boolean flag = sysUserService.addSysUser(sysUser);
        return ResponseEntity.ok(flag ? "保存成功" : "保存失败");
    }

    @RequestMapping("/update")
    public ResponseEntity<String> updateSysUser(SysUser sysUser) {
        Boolean flag = sysUserService.updateSysUser(sysUser);
        return ResponseEntity.ok(flag ? "修改用户" : "修改失败");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> updateSysUser(Long id) {
        Boolean flag = sysUserService.deleteSysUser(id);
        return ResponseEntity.ok(flag ? "删除成功" : "删除失败");
    }

    @GetMapping("/get/{id}")
    public ResponseMessage<SysUser> getSysUser(@PathVariable("id") Long id) {
        SysUser sysUser = sysUserService.getSysUser(id);
        log.info("sysUser:" + sysUser);
        return ResponseMessage.success(sysUser);
    }

    @RequestMapping("/list")
    public ResponseMessage<Object> getSysUserList() {
        List<SysUser> sysUsers = sysUserService.getSysUserList();
        log.info("sysUser:" + sysUsers);
        return ResponseMessage.success(sysUsers);
    }

}
