package com.liyiwei.lanhai.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.liyiwei.lanhai.common.exception.MyException;
import com.liyiwei.lanhai.model.dto.h5.UserLoginDto;
import com.liyiwei.lanhai.model.dto.h5.UserRegisterDto;
import com.liyiwei.lanhai.model.entity.user.UserInfo;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.model.vo.h5.UserInfoVo;
import com.liyiwei.lanhai.user.mapper.UserInfoMapper;
import com.liyiwei.lanhai.user.service.UserInfoService;
import com.liyiwei.lanhai.utils.AuthContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //注册
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        //1 userRegisterDto获取数据
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String nickName = userRegisterDto.getNickName();
        String code = userRegisterDto.getCode();

        //2 验证码校验
        //2.1 从redis获取发送验证码
        String redisCode = redisTemplate.opsForValue().get(username);
        //2.2 获取输入的验证码，进行比对
        if(!redisCode.equals(code)) {
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //3 校验用户名不能重复
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        if(userInfo != null) { //存在相同用户名
            throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        //4 封装添加数据，调用方法添加数据库
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");

        userInfoMapper.save(userInfo);

        //5 从redis删除发送的验证码
        redisTemplate.delete(username);
    }

    //登录
    @Override
    public String login(UserLoginDto userLoginDto) {
        //1 dto获取用户名和密码
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();

        //2 根据用户名查询数据库，得到用户信息
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        if(userInfo == null) {
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        //3 比较密码是否一致
        String database_password = userInfo.getPassword();
        String md5_password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!database_password.equals(md5_password)) {
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        //4 校验用户是否禁用
        if(userInfo.getStatus() == 0) {
            throw new MyException(ResultCodeEnum.ACCOUNT_STOP);
        }

        //5 生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        //6 把用户信息放到redis里面
        redisTemplate.opsForValue().set("user:lanhai:"+token,
                              JSON.toJSONString(userInfo),
                                30, TimeUnit.DAYS);

        //7 返回token
        return token;
    }

    //获取当前登录用户信息
    @Override
    public UserInfoVo getCurrentUserInfo(String token) {

        UserInfo userInfo = AuthContextUtil.getUserInfo();
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,userInfoVo);
        return userInfoVo;
    }
}
