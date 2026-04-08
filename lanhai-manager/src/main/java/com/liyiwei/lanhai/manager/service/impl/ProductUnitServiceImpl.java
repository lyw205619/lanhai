package com.liyiwei.lanhai.manager.service.impl;

import com.liyiwei.lanhai.manager.mapper.ProductUnitMapper;
import com.liyiwei.lanhai.manager.service.ProductUnitService;
import com.liyiwei.lanhai.model.entity.base.ProductUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {

    @Autowired
    private ProductUnitMapper productUnitMapper ;

    @Override
    public List<ProductUnit> findAll() {
        return productUnitMapper.findAll();
    }
}
