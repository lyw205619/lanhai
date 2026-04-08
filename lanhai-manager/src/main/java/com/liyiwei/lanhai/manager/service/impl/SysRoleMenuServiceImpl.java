package com.liyiwei.lanhai.manager.service.impl;

import com.liyiwei.lanhai.manager.mapper.SysRoleMenuMapper;
import com.liyiwei.lanhai.manager.service.SysMenuService;
import com.liyiwei.lanhai.manager.service.SysRoleMenuService;
import com.liyiwei.lanhai.model.dto.system.AssginMenuDto;
import com.liyiwei.lanhai.model.entity.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {

        List<SysMenu> sysMenuList = sysMenuService.findNodes();

        // 询角色分配过菜单id列表
        List<Long> roleMenuIds = sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId);

        Map<String, Object> map = new HashMap<>();
        map.put("sysMenuList",sysMenuList);
        map.put("roleMenuIds",roleMenuIds);
        return map;
    }


    @Override
    public void doAssign(AssginMenuDto assginMenuDto) {

        sysRoleMenuMapper.deleteByRoleId(assginMenuDto.getRoleId());


        List<Map<String, Number>> menuInfo = assginMenuDto.getMenuIdList();
        if(menuInfo != null && menuInfo.size()>0) { //角色分配了菜单
            sysRoleMenuMapper.doAssign(assginMenuDto);
        }
    }
}
