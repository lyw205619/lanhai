package com.liyiwei.lanhai.manager.mapper;

import com.liyiwei.lanhai.model.entity.system.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysOperLogMapper {

    //保存日志数据
    void insert(SysOperLog sysOperLog);
}
