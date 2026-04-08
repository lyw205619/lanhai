package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.dto.system.AssginRoleDto;
import com.liyiwei.lanhai.model.dto.system.LoginDto;
import com.liyiwei.lanhai.model.dto.system.SysUserDto;
import com.liyiwei.lanhai.model.entity.system.SysUser;
import com.liyiwei.lanhai.model.vo.system.LoginVo;
import com.github.pagehelper.PageInfo;

public interface SysUserService {


    LoginVo login(LoginDto loginDto);


    SysUser getUserInfo(String token);


    void logout(String token);


    PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);


    void saveSysUser(SysUser sysUser);


    void updateSysUser(SysUser sysUser);


    void deleteById(Long userId);

    //分配角色
    void doAssign(AssginRoleDto assginRoleDto);
}
