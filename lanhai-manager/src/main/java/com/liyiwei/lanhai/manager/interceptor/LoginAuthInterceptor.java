package com.liyiwei.lanhai.manager.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.liyiwei.lanhai.model.entity.system.SysUser;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();
        if("OPTIONS".equals(method)) {
            return true;
        }

        String token = request.getHeader("token");


        if(StrUtil.isEmpty(token)) {
            responseNoLoginInfo(response);
            return false;
        }


        String userInfoString = redisTemplate.opsForValue().get("user:login" + token);


        if(StrUtil.isEmpty(userInfoString)) {
            responseNoLoginInfo(response);
            return false;
        }

        SysUser sysUser = JSON.parseObject(userInfoString, SysUser.class);
        AuthContextUtil.set(sysUser);


        redisTemplate.expire("user:login" + token,30, TimeUnit.MINUTES);


        return true;
    }


    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

        AuthContextUtil.remove();

    }
}
