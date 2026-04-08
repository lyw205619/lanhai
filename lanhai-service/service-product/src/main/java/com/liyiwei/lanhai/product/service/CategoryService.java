package com.liyiwei.lanhai.product.service;

import com.liyiwei.lanhai.model.entity.product.Category;

import java.util.List;

public interface CategoryService {

    List<Category> selectOneCategory();


    List<Category> findCategoryTree();
}
