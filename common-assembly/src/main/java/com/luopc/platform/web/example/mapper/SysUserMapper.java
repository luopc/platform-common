package com.luopc.platform.web.example.mapper;

import com.luopc.platform.web.example.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *     系统用户Mapper
 * </p>
 *
 * @author liujixiang
 * @since 2018/12/28
 */
@Repository
public interface SysUserMapper {

    Integer addSysUser(SysUser sysUser);

    Integer updateSysUser(SysUser sysUser);

    Integer deleteSysUser(Long id);

    SysUser getSysUser(Long id);

    List<SysUser> getSysUserList();

}
