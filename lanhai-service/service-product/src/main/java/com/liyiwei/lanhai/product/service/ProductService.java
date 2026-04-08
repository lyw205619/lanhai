package com.liyiwei.lanhai.product.service;

import com.liyiwei.lanhai.model.dto.h5.ProductSkuDto;
import com.liyiwei.lanhai.model.entity.product.ProductSku;
import com.liyiwei.lanhai.model.vo.h5.ProductItemVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {


    List<ProductSku> selectProductSkuBySale();


    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);


    ProductItemVo item(Long skuId);


    ProductSku getBySkuId(Long skuId);

    /**
     * 订单支付成功后按 SKU 累加销量
     */
    void incrementSkuSaleNum(Long skuId, Integer delta);
}
