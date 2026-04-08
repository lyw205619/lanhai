package com.liyiwei.lanhai.manager.service.impl;

import com.liyiwei.lanhai.common.log.service.AsyncOperLogService;
import com.liyiwei.lanhai.manager.mapper.SysOperLogMapper;
import com.liyiwei.lanhai.model.entity.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    //保存日志数据
    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insert(sysOperLog);
    }
}
