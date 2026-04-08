package com.liyiwei.lanhai.product.mapper;

import com.liyiwei.lanhai.model.dto.h5.ProductSkuDto;
import com.liyiwei.lanhai.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductSkuMapper {
    //2 根据销量排序，获取前10条记录
    List<ProductSku> selectProductSkuBySale();

    //分页查询
    List<ProductSku> findByPage(ProductSkuDto productSkuDto);

    //2 根据skuId获取sku信息
    ProductSku getById(Long skuId);

    //根据商品id获取商品所有sku列表
    List<ProductSku> findByProductId(Long productId);

    /**
     * 支付成功后累加 SKU 销量（delta 一般为订单项购买数量）
     */
    int incrementSaleNum(@Param("skuId") Long skuId, @Param("delta") Integer delta);
}
