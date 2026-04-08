package com.liyiwei.lanhai.product.service.impl;

import com.liyiwei.lanhai.model.entity.product.Brand;
import com.liyiwei.lanhai.product.mapper.BrandMapper;
import com.liyiwei.lanhai.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    //获取全部品牌
    @Override
    public List<Brand> findAll() {
        return brandMapper.findAll();
    }
}
