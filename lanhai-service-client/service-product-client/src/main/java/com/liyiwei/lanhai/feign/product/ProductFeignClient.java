package com.liyiwei.lanhai.feign.product;

import com.liyiwei.lanhai.model.entity.product.ProductSku;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("/api/product/getBySkuId/{skuId}")
    public ProductSku getBySkuId(@PathVariable("skuId") Long skuId);

    @PutMapping("/api/product/sku/incrementSaleNum/{skuId}/{delta}")
    void incrementSkuSaleNum(@PathVariable("skuId") Long skuId, @PathVariable("delta") Integer delta);

}
