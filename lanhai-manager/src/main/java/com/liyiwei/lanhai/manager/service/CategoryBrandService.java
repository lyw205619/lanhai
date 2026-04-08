package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.dto.product.CategoryBrandDto;
import com.liyiwei.lanhai.model.entity.product.Brand;
import com.liyiwei.lanhai.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryBrandService {

    //分类品牌条件分页查询
    PageInfo<CategoryBrand> findByPage(Integer page, Integer limit, CategoryBrandDto categoryBrandDto);

    //添加
    void save(CategoryBrand categoryBrand);

    //根据分类id查询对应品牌数据
    List<Brand> findBrandByCategoryId(Long categoryId);

    void update(CategoryBrand categoryBrand);

    void deleteById(Long id);
}
