package com.liyiwei.lanhai.manager.controller;

import com.liyiwei.lanhai.common.log.annotation.Log;
import com.liyiwei.lanhai.common.log.enums.OperatorType;
import com.liyiwei.lanhai.manager.service.BrandService;
import com.liyiwei.lanhai.model.entity.product.Brand;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/admin/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    //查询所有品牌
    @GetMapping("/findAll")
    public Result findAll() {
        List<Brand> list = brandService.findAll();
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    //列表
    @Log(title = "品牌管理:列表",businessType = 0,operatorType = OperatorType.OTHER)
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable Integer page,
                       @PathVariable Integer limit) {
        PageInfo<Brand> pageInfo = brandService.findByPage(page,limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //添加
    @PostMapping("/save")
    public Result save(@RequestBody Brand brand) {
        brandService.save(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Log(title = "品牌管理:修改", businessType = 2, operatorType = OperatorType.OTHER)
    @PutMapping("/update")
    public Result update(@RequestBody Brand brand) {
        brandService.update(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Log(title = "品牌管理:删除", businessType = 3, operatorType = OperatorType.OTHER)
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        brandService.deleteById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
