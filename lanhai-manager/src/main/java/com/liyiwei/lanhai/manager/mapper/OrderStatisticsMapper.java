package com.liyiwei.lanhai.manager.mapper;

import com.liyiwei.lanhai.model.dto.order.OrderStatisticsDto;
import com.liyiwei.lanhai.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderStatisticsMapper {

    //3 把统计之后的数据，添加统计结果表里面
    void insert(OrderStatistics orderStatistics);

    List<OrderStatistics> selectList(OrderStatisticsDto orderStatisticsDto);
}
