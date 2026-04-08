package com.liyiwei.lanhai.manager.task;

import cn.hutool.core.date.DateUtil;
import com.liyiwei.lanhai.manager.mapper.OrderInfoMapper;
import com.liyiwei.lanhai.manager.mapper.OrderStatisticsMapper;
import com.liyiwei.lanhai.model.entity.order.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderStatisticsTask {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    //每天凌晨2点，查询前一天日期统计数据，把统计之后数据添加统计结果表里面
    @Scheduled(cron = "0 0 2 * * ?")
    public void orderTotalAmountStatistics() {
        System.out.println(new Date().toInstant());
        // 获取前一天日期
        String createDate =
                DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd");

        // 根据前一天日期进行统计功能，（分组操作）
        //统计前一天交易金额
        OrderStatistics orderStatistics =
                orderInfoMapper.selectStatisticsByDate(createDate);

        // 把统计之后的数据，添加统计结果表里面
        if(orderStatistics != null) {
            orderStatisticsMapper.insert(orderStatistics);
        }

    }

}
