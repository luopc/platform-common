package com.luopc.platform.web.example.service;


import com.luopc.platform.web.example.entity.SysUser;

import java.util.List;

/**
 * <p>
 *     系统用户服务接口
 * </p>
 *
 * @author liujixiang
 * @since 2018/12/28
 */
public interface SysUserService {

    Boolean addSysUser(SysUser sysUser);

    Boolean updateSysUser(SysUser sysUser);

    Boolean deleteSysUser(Long id);

    SysUser getSysUser(Long id);

    List<SysUser> getSysUserList();

}
