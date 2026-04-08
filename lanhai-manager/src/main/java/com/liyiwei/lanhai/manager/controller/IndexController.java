package com.liyiwei.lanhai.manager.controller;

import com.liyiwei.lanhai.manager.service.SysMenuService;
import com.liyiwei.lanhai.manager.service.SysUserService;
import com.liyiwei.lanhai.manager.service.ValidateCodeService;
import com.liyiwei.lanhai.model.dto.system.LoginDto;
import com.liyiwei.lanhai.model.entity.system.SysUser;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.model.vo.system.LoginVo;
import com.liyiwei.lanhai.model.vo.system.SysMenuVo;
import com.liyiwei.lanhai.model.vo.system.ValidateCodeVo;
import com.liyiwei.lanhai.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private SysMenuService sysMenuService;

    //查询用户可以操作菜单
    @GetMapping("/menus")
    public Result menus() {
        List<SysMenuVo> list = sysMenuService.findMenusByUserId();
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    //用户退出
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(name = "token") String token) {
        sysUserService.logout(token);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping(value = "/getUserInfo")
    public Result getUserInfo() {
        return Result.build(AuthContextUtil.get(),ResultCodeEnum.SUCCESS);
    }


    //生成图片验证码
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo,ResultCodeEnum.SUCCESS);
    }

    //用户登录
    @Operation(summary = "登录的方法")
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

}
