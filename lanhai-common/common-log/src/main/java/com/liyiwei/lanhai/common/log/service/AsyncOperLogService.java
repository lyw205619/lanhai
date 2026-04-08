package com.liyiwei.lanhai.common.log.service;

import com.liyiwei.lanhai.model.entity.system.SysOperLog;

public interface AsyncOperLogService {

    public abstract void saveSysOperLog(SysOperLog sysOperLog) ;
}
