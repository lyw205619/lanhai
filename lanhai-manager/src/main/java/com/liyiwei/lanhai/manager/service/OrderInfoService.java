package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.dto.order.OrderStatisticsDto;
import com.liyiwei.lanhai.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {

    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
