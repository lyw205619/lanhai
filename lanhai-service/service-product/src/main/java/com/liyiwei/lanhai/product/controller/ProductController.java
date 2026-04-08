package com.liyiwei.lanhai.product.controller;

import com.liyiwei.lanhai.model.dto.h5.ProductSkuDto;
import com.liyiwei.lanhai.model.entity.product.ProductSku;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.model.vo.h5.ProductItemVo;
import com.liyiwei.lanhai.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    //商品详情接口
    @Operation(summary = "商品详情")
    @GetMapping("item/{skuId}")
    public Result item(@PathVariable Long skuId) {
        ProductItemVo productItemVo = productService.item(skuId);
        return Result.build(productItemVo,ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/{page}/{limit}")
    public Result list(@PathVariable Integer page,
                       @PathVariable Integer limit,
                       ProductSkuDto productSkuDto) {
       PageInfo<ProductSku> pageInfo = productService.findByPage(page,limit,productSkuDto);
       return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //远程调用：根据skuId返回sku信息
    @GetMapping("/getBySkuId/{skuId}")
    public ProductSku getBySkuId(@PathVariable Long skuId) {
        ProductSku productSku = productService.getBySkuId(skuId);
        return productSku;
    }

    /**
     * 远程调用：支付成功后累加 SKU 销量（供 service-pay Feign 调用）
     */
    @PutMapping("/sku/incrementSaleNum/{skuId}/{delta}")
    public void incrementSkuSaleNum(@PathVariable Long skuId, @PathVariable Integer delta) {
        productService.incrementSkuSaleNum(skuId, delta);
    }
}
