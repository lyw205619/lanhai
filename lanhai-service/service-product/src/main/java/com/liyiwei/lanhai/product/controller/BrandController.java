package com.liyiwei.lanhai.product.controller;

import com.liyiwei.lanhai.model.entity.product.Brand;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.product.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Operation(summary = "获取全部品牌")
    @GetMapping("findAll")
    public Result findAll() {
        List<Brand> list = brandService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
