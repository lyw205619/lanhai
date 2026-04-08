package com.liyiwei.lanhai.manager.mapper;

import com.liyiwei.lanhai.model.entity.base.ProductUnit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductUnitMapper {
    List<ProductUnit> findAll();
}
