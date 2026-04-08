package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.dto.system.AssginMenuDto;

import java.util.Map;

public interface SysRoleMenuService {


    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);


    void doAssign(AssginMenuDto assginMenuDto);
}
