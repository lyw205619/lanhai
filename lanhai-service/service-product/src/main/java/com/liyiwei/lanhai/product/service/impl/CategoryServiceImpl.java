package com.liyiwei.lanhai.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.liyiwei.lanhai.model.entity.product.Category;
import com.liyiwei.lanhai.product.mapper.CategoryMapper;
import com.liyiwei.lanhai.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //category:one
    //查询所有一级分类
    @Override
    public List<Category> selectOneCategory() {
        //1 查询redis，是否有所有一级分类
        String categoryOneJson = redisTemplate.opsForValue().get("category:one");

        //2 如果redis包含所有一级分类，直接返回
        if(StringUtils.hasText(categoryOneJson)) {
            //categoryOneJson字符串转换List<Category>
            List<Category> existCategoryList = JSON.parseArray(categoryOneJson, Category.class);
            return existCategoryList;
        }

        //3 如果redis没有所有一级分类，查询数据库，把数据库查询内容返回，并且查询内容放到redis里面
        List<Category> categoryList = categoryMapper.selectOneCategory();
        //查询内容放到redis里面
        redisTemplate.opsForValue().set("category:one",
                JSON.toJSONString(categoryList),
                7, TimeUnit.DAYS);

        return categoryList;
    }

    //查询所有分类，树形封装
    @Cacheable(value = "category",key="'all'")
    @Override
    public List<Category> findCategoryTree() {

        List<Category> allCategoryList = categoryMapper.findAll();

        // parentid=0得到所有一级分类
        List<Category> oneCategoryList =
                allCategoryList.stream()
                        .filter(item -> item.getParentId().longValue() == 0)
                        .collect(Collectors.toList());

        // id = parentid，得到一级下面二级分类
        oneCategoryList.forEach(oneCategory ->{
            List<Category> twoCategoryList =
                    allCategoryList.stream()
                            .filter(item -> item.getParentId() == oneCategory.getId())
                            .collect(Collectors.toList());

            oneCategory.setChildren(twoCategoryList);


            twoCategoryList.forEach(twoCategory ->{
                List<Category> threeCategoryList =
                        allCategoryList.stream()
                                .filter(item -> item.getParentId() == twoCategory.getId())
                        .collect(Collectors.toList());

                twoCategory.setChildren(threeCategoryList);
            });
        });

        return oneCategoryList;
    }
}
