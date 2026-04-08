package com.liyiwei.lanhai.ai.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 占位 Mapper，满足 MyBatis 启动；无业务表。
 */
@Mapper
public interface DummyMapper {

    @Select("SELECT 1")
    int ping();
}
