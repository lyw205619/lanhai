package com.liyiwei.lanhai.product.mapper;

import com.liyiwei.lanhai.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    //productId，获取商品信息
    Product getById(Long productId);
}
