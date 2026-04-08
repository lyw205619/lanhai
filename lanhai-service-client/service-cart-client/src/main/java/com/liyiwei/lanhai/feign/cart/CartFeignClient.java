package com.liyiwei.lanhai.feign.cart;

import com.liyiwei.lanhai.model.entity.h5.CartInfo;
import com.liyiwei.lanhai.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "service-cart")
public interface CartFeignClient {

    @GetMapping("/api/order/cart/auth/deleteChecked")
    public Result deleteChecked();

    @GetMapping(value = "/api/order/cart/auth/getAllCkecked")
    public List<CartInfo> getAllCkecked();
}
