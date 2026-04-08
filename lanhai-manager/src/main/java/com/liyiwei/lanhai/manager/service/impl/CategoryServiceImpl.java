package com.liyiwei.lanhai.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.liyiwei.lanhai.common.exception.MyException;
import com.liyiwei.lanhai.manager.listener.ExcelListener;
import com.liyiwei.lanhai.manager.mapper.CategoryMapper;
import com.liyiwei.lanhai.manager.service.CategoryService;
import com.liyiwei.lanhai.model.entity.product.Category;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import com.liyiwei.lanhai.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findCategoryList(Long id) {

        List<Category> categoryList = categoryMapper.selectCategoryByParentId(id);


        if(!CollectionUtils.isEmpty(categoryList)) {
            categoryList.forEach(category -> {

                int count = categoryMapper.selectCountByParentId(category.getId());
                if(count > 0) {
                    category.setHasChildren(true);
                } else {
                    category.setHasChildren(false);
                }
            });
        }
        return categoryList;
    }

    //导出
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置响应头信息和其他信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类数据", "UTF-8");

            //设置响应头信息 Content-disposition
            response.setHeader("Content-disposition","attachment;filename="+fileName+".xlsx");

            // 调用mapper方法查询所有分类，返回list集合
            List<Category> categoryList = categoryMapper.findAll();

            // 最终数据list集合
            List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();
            // List<Category>  --  List<CategoryExcelVo>
            for(Category category : categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                //把category值获取出来，设置到categoryExcelVo里面

                BeanUtils.copyProperties(category,categoryExcelVo);
                categoryExcelVoList.add(categoryExcelVo);
            }

            // 调用EasyExcel的write方法完成写操作
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据").doWrite(categoryExcelVoList);
        }catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }

    //导入
    @Override
    public void importData(MultipartFile file) {
        //监听器
        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener(categoryMapper);
        try {
            EasyExcel.read(file.getInputStream(),CategoryExcelVo.class,excelListener)
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
