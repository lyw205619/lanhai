package com.liyiwei.lanhai.manager.mapper;

import com.liyiwei.lanhai.model.dto.product.CategoryBrandDto;
import com.liyiwei.lanhai.model.entity.product.Brand;
import com.liyiwei.lanhai.model.entity.product.CategoryBrand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryBrandMapper {

    //分类品牌条件分页查询
    List<CategoryBrand> findByPage(CategoryBrandDto categoryBrandDto);

    //添加
    void save(CategoryBrand categoryBrand);

    //根据分类id查询对应品牌数据
    List<Brand> findBrandByCategoryId(Long categoryId);

    void updateById(CategoryBrand categoryBrand);

    void deleteById(Long id);
}
