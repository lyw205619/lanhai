package com.liyiwei.lanhai.manager.service.impl;

import com.liyiwei.lanhai.manager.mapper.CategoryBrandMapper;
import com.liyiwei.lanhai.manager.service.CategoryBrandService;
import com.liyiwei.lanhai.model.dto.product.CategoryBrandDto;
import com.liyiwei.lanhai.model.entity.product.Brand;
import com.liyiwei.lanhai.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBrandServiceImpl implements CategoryBrandService {

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    //分类品牌条件分页查询
    @Override
    public PageInfo<CategoryBrand> findByPage(Integer page,
                                              Integer limit,
                                              CategoryBrandDto categoryBrandDto) {
        PageHelper.startPage(page,limit);
        List<CategoryBrand> list = categoryBrandMapper.findByPage(categoryBrandDto);
        PageInfo<CategoryBrand> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    //添加
    @Override
    public void save(CategoryBrand categoryBrand) {
        categoryBrandMapper.save(categoryBrand);
    }

    //根据分类id查询对应品牌数据
    @Override
    public List<Brand> findBrandByCategoryId(Long categoryId) {
        return categoryBrandMapper.findBrandByCategoryId(categoryId);
    }

    @Override
    public void update(CategoryBrand categoryBrand) {
        categoryBrandMapper.updateById(categoryBrand);
    }

    @Override
    public void deleteById(Long id) {
        categoryBrandMapper.deleteById(id);
    }
}
