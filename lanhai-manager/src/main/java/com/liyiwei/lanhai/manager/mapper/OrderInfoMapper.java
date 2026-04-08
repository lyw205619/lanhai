package com.liyiwei.lanhai.manager.mapper;

import com.liyiwei.lanhai.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper {
    //统计前一天交易金额
    OrderStatistics selectStatisticsByDate(String createDate);
}
