package com.liyiwei.lanhai.product.controller;

import com.liyiwei.lanhai.model.entity.product.Category;
import com.liyiwei.lanhai.model.entity.product.ProductSku;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.model.vo.h5.IndexVo;
import com.liyiwei.lanhai.product.service.CategoryService;
import com.liyiwei.lanhai.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "首页接口管理")
@RestController
@RequestMapping(value="/api/product/index")
//@CrossOrigin //跨域
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result index() {

        List<Category> categoryList = categoryService.selectOneCategory();

        // 根据销量排序，获取前10条记录
        List<ProductSku> productSkuList = productService.selectProductSkuBySale();


        IndexVo indexVo = new IndexVo();
        indexVo.setCategoryList(categoryList);
        indexVo.setProductSkuList(productSkuList);
        return Result.build(indexVo, ResultCodeEnum.SUCCESS);
    }

}
