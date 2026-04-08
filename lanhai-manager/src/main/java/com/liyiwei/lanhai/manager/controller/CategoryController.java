package com.liyiwei.lanhai.manager.controller;

import com.liyiwei.lanhai.manager.service.CategoryService;
import com.liyiwei.lanhai.model.entity.product.Category;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value="/admin/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //导入
    @PostMapping("/importData")
    public Result importData(MultipartFile file) {
        //获取上传文件
        categoryService.importData(file);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //导出
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        categoryService.exportData(response);
    }

    //分类列表，每次查询一层数据
    @GetMapping("/findCategoryList/{id}")
    public Result findCategoryList(@PathVariable Long id) {
        List<Category> list = categoryService.findCategoryList(id);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
