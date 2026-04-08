package com.liyiwei.lanhai.manager.service.impl;

import com.liyiwei.lanhai.common.exception.MyException;
import com.liyiwei.lanhai.manager.mapper.SysMenuMapper;
import com.liyiwei.lanhai.manager.mapper.SysRoleMenuMapper;
import com.liyiwei.lanhai.manager.service.SysMenuService;
import com.liyiwei.lanhai.manager.utils.MenuHelper;
import com.liyiwei.lanhai.model.entity.system.SysMenu;
import com.liyiwei.lanhai.model.entity.system.SysUser;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.model.vo.system.SysMenuVo;
import com.liyiwei.lanhai.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;


    @Override
    public List<SysMenu> findNodes() {

        List<SysMenu> sysMenuList = sysMenuMapper.findAll();
        if(CollectionUtils.isEmpty(sysMenuList)) {
            return null;
        }


        List<SysMenu> treeList = MenuHelper.buildTree(sysMenuList);
        return treeList;
    }


    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.save(sysMenu);

        //新添加子菜单，把父菜单isHalf半开状态 1
        updateSysRoleMenu(sysMenu);
    }

    //新添加子菜单，把父菜单isHalf半开状态 1
    private void updateSysRoleMenu(SysMenu sysMenu) {
        //获取当前添加菜单的父菜单
        SysMenu parentMenu = sysMenuMapper.selectParentMenu(sysMenu.getParentId());
        if(parentMenu != null) {
            //把父菜单isHalf半开状态 1
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId()) ;
            // 递归调用
            updateSysRoleMenu(parentMenu) ;
        }
    }


    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.update(sysMenu);
    }


    @Override
    public void removeById(Long id) {

        int count = sysMenuMapper.selectCountById(id);

        //包含子菜单
        if(count > 0) {
            throw new MyException(ResultCodeEnum.NODE_ERROR);
        }

        //count等于0 ，直接删除
        sysMenuMapper.delete(id);
    }


    @Override
    public List<SysMenuVo> findMenusByUserId() {

        SysUser sysUser = AuthContextUtil.get();
        Long userId = sysUser.getId();


        List<SysMenu> sysMenuList =
                MenuHelper.buildTree(sysMenuMapper.findMenusByUserId(userId));
        List<SysMenuVo> sysMenuVos = this.buildMenus(sysMenuList);
        return sysMenuVos;
    }


    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
