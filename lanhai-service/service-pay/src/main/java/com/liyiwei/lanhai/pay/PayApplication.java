package com.liyiwei.lanhai.pay;

import com.liyiwei.lanhai.common.anno.EnableUserLoginAuthInterceptor;
import com.liyiwei.lanhai.pay.utils.AlipayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties(value = {AlipayProperties.class})
@EnableUserLoginAuthInterceptor
@EnableFeignClients(basePackages = {"com.liyiwei.lanhai"})
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class , args) ;
    }

}
