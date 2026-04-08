package com.liyiwei.lanhai.product.service;

import com.liyiwei.lanhai.model.entity.product.Brand;

import java.util.List;

public interface BrandService {

    //获取全部品牌
    List<Brand> findAll();
}
