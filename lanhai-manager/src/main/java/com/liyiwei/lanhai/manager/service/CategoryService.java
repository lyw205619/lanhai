package com.liyiwei.lanhai.manager.service;

import com.liyiwei.lanhai.model.entity.product.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {


    List<Category> findCategoryList(Long id);


    void exportData(HttpServletResponse response);


    void importData(MultipartFile file);
}
