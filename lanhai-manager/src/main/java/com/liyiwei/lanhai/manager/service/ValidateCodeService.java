package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.vo.system.ValidateCodeVo;

public interface ValidateCodeService {

    //生成图片验证码
    ValidateCodeVo generateValidateCode();
}
