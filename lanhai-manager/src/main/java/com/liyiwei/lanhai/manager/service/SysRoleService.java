package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.dto.system.SysRoleDto;
import com.liyiwei.lanhai.model.entity.system.SysRole;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface SysRoleService {


    PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit);


    void saveSysRole(SysRole sysRole);


    void updateSysRole(SysRole sysRole);


    void deleteById(Long roleId);


    Map<String, Object> findAll(Long userId);
}
