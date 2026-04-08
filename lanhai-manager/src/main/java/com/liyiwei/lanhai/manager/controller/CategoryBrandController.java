package com.liyiwei.lanhai.manager.controller;

import com.liyiwei.lanhai.common.log.annotation.Log;
import com.liyiwei.lanhai.common.log.enums.OperatorType;
import com.liyiwei.lanhai.manager.service.CategoryBrandService;
import com.liyiwei.lanhai.model.dto.product.CategoryBrandDto;
import com.liyiwei.lanhai.model.entity.product.Brand;
import com.liyiwei.lanhai.model.entity.product.CategoryBrand;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/product/categoryBrand")
public class CategoryBrandController {

    @Autowired
    private CategoryBrandService categoryBrandService ;

    //根据分类id查询对应品牌数据
    @GetMapping("/findBrandByCategoryId/{categoryId}")
    public Result findBrandByCategoryId(@PathVariable Long categoryId) {
        List<Brand> list = categoryBrandService.findBrandByCategoryId(categoryId);
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    //添加
    @PostMapping("/save")
    public Result save(@RequestBody CategoryBrand categoryBrand) {
        categoryBrandService.save(categoryBrand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //分类品牌条件分页查询
    @GetMapping("/{page}/{limit}")
    public Result findByPage(@PathVariable Integer page,
                             @PathVariable Integer limit,
                             CategoryBrandDto categoryBrandDto) {
        PageInfo<CategoryBrand> pageInfo =
                categoryBrandService.findByPage(page,limit,categoryBrandDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @Log(title = "分类品牌:修改", businessType = 2, operatorType = OperatorType.OTHER)
    @PutMapping("/update")
    public Result update(@RequestBody CategoryBrand categoryBrand) {
        categoryBrandService.update(categoryBrand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Log(title = "分类品牌:删除", businessType = 3, operatorType = OperatorType.OTHER)
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        categoryBrandService.deleteById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
