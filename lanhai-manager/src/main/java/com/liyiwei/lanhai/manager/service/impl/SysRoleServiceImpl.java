package com.liyiwei.lanhai.manager.service.impl;

import com.liyiwei.lanhai.manager.mapper.SysRoleMapper;
import com.liyiwei.lanhai.manager.mapper.SysRoleUserMapper;
import com.liyiwei.lanhai.manager.service.SysRoleService;
import com.liyiwei.lanhai.model.dto.system.SysRoleDto;
import com.liyiwei.lanhai.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;


    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit) {

        PageHelper.startPage(current,limit);

        List<SysRole> list = sysRoleMapper.findByPage(sysRoleDto);

        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.save(sysRole);
    }


    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
    }


    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.delete(roleId);
    }


    public Map<String, Object> findAll(Long userId) {

        List<SysRole> roleList =  sysRoleMapper.findAll();


        List<Long> roleIds = sysRoleUserMapper.selectRoleIdsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("allRolesList",roleList);
        map.put("sysUserRoles",roleIds);

        return map;
    }
}
