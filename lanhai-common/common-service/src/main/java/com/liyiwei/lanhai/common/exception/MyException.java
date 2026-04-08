package com.liyiwei.lanhai.common.exception;

import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import lombok.Data;

@Data
public class MyException extends RuntimeException{

    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public MyException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
