package com.liyiwei.lanhai.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.liyiwei.lanhai.common.exception.MyException;
import com.liyiwei.lanhai.common.log.annotation.Log;
import com.liyiwei.lanhai.manager.mapper.SysRoleUserMapper;
import com.liyiwei.lanhai.manager.mapper.SysUserMapper;
import com.liyiwei.lanhai.manager.service.SysUserService;
import com.liyiwei.lanhai.model.dto.system.AssginRoleDto;
import com.liyiwei.lanhai.model.dto.system.LoginDto;
import com.liyiwei.lanhai.model.dto.system.SysUserDto;
import com.liyiwei.lanhai.model.entity.system.SysUser;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.model.vo.system.LoginVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Override
    public LoginVo login(LoginDto loginDto) {


        String captcha = loginDto.getCaptcha();
        String key = loginDto.getCodeKey();


        String redisCode = redisTemplate.opsForValue().get("user:validate" + key);


        if(StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode,captcha)) {

            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);

        }


        redisTemplate.delete("user:validate" + key);


        String userName = loginDto.getUserName();


        SysUser sysUser = sysUserMapper.selectUserInfoByUserName(userName);


        if(sysUser == null) {
//            throw new RuntimeException("用户名不存在");
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }


        String database_password = sysUser.getPassword();
        String input_password =
                DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());

        if(!input_password.equals(database_password)) {
//            throw new RuntimeException("密码不正确");
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }


        String token = UUID.randomUUID().toString().replaceAll("-","");

        redisTemplate.opsForValue()
                .set("user:login"+token,
                        JSON.toJSONString(sysUser),
                        7,
                        TimeUnit.DAYS);

        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }


    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login" + token);
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }


    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login" + token);
    }


    @Override
    public PageInfo<SysUser> findByPage(Integer pageNum,
                                        Integer pageSize,
                                        SysUserDto sysUserDto) {
        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> list = sysUserMapper.findByPage(sysUserDto);
        PageInfo<SysUser>  pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


    @Override
    public void saveSysUser(SysUser sysUser) {

        String userName = sysUser.getUserName();
        SysUser dbSysUser = sysUserMapper.selectUserInfoByUserName(userName);
        if(dbSysUser != null) {
            throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }


        String md5_password = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(md5_password);


        sysUser.setStatus(1);

        sysUserMapper.save(sysUser);
    }


    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.update(sysUser);
    }


    @Override
    public void deleteById(Long userId) {
        sysUserMapper.delete(userId);
    }


    @Log(title = "用户分配角色",businessType = 0)
    @Transactional
    @Override
    public void doAssign(AssginRoleDto assginRoleDto) {

        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId());

        List<Long> roleIdList = assginRoleDto.getRoleIdList();

        for(Long roleId:roleIdList) {
            sysRoleUserMapper.doAssign(assginRoleDto.getUserId(),roleId);
        }
    }
}
