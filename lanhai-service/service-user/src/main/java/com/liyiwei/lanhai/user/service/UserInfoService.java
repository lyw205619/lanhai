package com.liyiwei.lanhai.user.service;

import com.liyiwei.lanhai.model.dto.h5.UserLoginDto;
import com.liyiwei.lanhai.model.dto.h5.UserRegisterDto;
import com.liyiwei.lanhai.model.vo.h5.UserInfoVo;

public interface UserInfoService {

    //注册
    void register(UserRegisterDto userRegisterDto);

    //登录
    String login(UserLoginDto userLoginDto);

    //获取当前登录用户信息
    UserInfoVo getCurrentUserInfo(String token);
}
