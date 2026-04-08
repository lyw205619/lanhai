package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.entity.product.ProductSpec;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductSpecService {

    //列表
    PageInfo<ProductSpec> findByPage(Integer page, Integer limit);

    //添加
    void save(ProductSpec productSpec);

    //修改
    void updateById(ProductSpec productSpec);

    //删除
    void deleteById(Long id);

    List<ProductSpec> findAll();
}
