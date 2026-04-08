package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.entity.system.SysMenu;
import com.liyiwei.lanhai.model.vo.system.SysMenuVo;

import java.util.List;

public interface SysMenuService {
    //菜单列表
    List<SysMenu> findNodes();

    //菜单添加
    void save(SysMenu sysMenu);

    //菜单修改
    void update(SysMenu sysMenu);

    //菜单删除
    void removeById(Long id);

    //查询用户可以操作菜单
    List<SysMenuVo> findMenusByUserId();
}
