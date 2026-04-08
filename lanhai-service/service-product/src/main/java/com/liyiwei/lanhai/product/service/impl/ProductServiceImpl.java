package com.liyiwei.lanhai.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.liyiwei.lanhai.model.dto.h5.ProductSkuDto;
import com.liyiwei.lanhai.model.entity.product.Product;
import com.liyiwei.lanhai.model.entity.product.ProductDetails;
import com.liyiwei.lanhai.model.entity.product.ProductSku;
import com.liyiwei.lanhai.model.vo.h5.ProductItemVo;
import com.liyiwei.lanhai.product.mapper.ProductDetailsMapper;
import com.liyiwei.lanhai.product.mapper.ProductMapper;
import com.liyiwei.lanhai.product.mapper.ProductSkuMapper;
import com.liyiwei.lanhai.product.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    // 根据销量排序，获取前10条记录
    @Override
    public List<ProductSku> selectProductSkuBySale() {
        return productSkuMapper.selectProductSkuBySale();
    }


    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit,
                                           ProductSkuDto productSkuDto) {
        PageHelper.startPage(page,limit);
        List<ProductSku> list = productSkuMapper.findByPage(productSkuDto);
        return new PageInfo<>(list);
    }

    //商品详情接口
    @Override
    public ProductItemVo item(Long skuId) {

        ProductItemVo productItemVo = new ProductItemVo();

        // 根据skuId获取sku信息
        ProductSku productSku = productSkuMapper.getById(skuId);


        Long productId = productSku.getProductId();
        Product product = productMapper.getById(productId);


        ProductDetails productDetails = productDetailsMapper.getByProductId(productId);


        Map<String,Object> skuSpecValueMap = new HashMap<>();
        //根据商品id获取商品所有sku列表
        List<ProductSku> productSkuList = productSkuMapper.findByProductId(productId);
        productSkuList.forEach(item ->{
            skuSpecValueMap.put(item.getSkuSpec(),item.getId());
        });


        productItemVo.setProduct(product);
        productItemVo.setProductSku(productSku);
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);


        productItemVo.setDetailsImageUrlList(Arrays.asList(productDetails.getImageUrls().split(",")));


        productItemVo.setSliderUrlList(Arrays.asList(product.getSliderUrls().split(",")));


        productItemVo.setSpecValueList(JSON.parseArray(product.getSpecValue()));
        return productItemVo;
    }

    @Override
    public ProductSku getBySkuId(Long skuId) {
        ProductSku productSku = productSkuMapper.getById(skuId);
        return productSku;
    }

    @Override
    public void incrementSkuSaleNum(Long skuId, Integer delta) {
        if (skuId == null || delta == null || delta <= 0) {
            return;
        }
        productSkuMapper.incrementSaleNum(skuId, delta);
    }

}
