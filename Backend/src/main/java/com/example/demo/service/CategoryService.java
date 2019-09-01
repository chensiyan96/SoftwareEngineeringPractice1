package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.CategoryMapper;
import com.example.demo.model.Category;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> implements IService<Category> {

    /**
     * 根据类别Id数组查询类别数组
     */
    public String renderCategoryArr(String categoryIds, List<Category> categoryList) {
        if (StringUtils.isEmpty(categoryIds)) {
            return "";
        }
        List<String> categoryStrList = new ArrayList<>();
        String[] categoryIdArr = categoryIds.split(",");
        for (String s : categoryIdArr) {
            Integer categoryId = Integer.parseInt(s);
            // 根据Id查找类别名称
            String categoryStr = categoryList
                    .stream()
                    .filter(category -> category.getId().equals(categoryId))
                    .map(Category::getName)
                    .findAny().orElse("类别已被删除");
            categoryStrList.add(categoryStr);
        }
        return String.join(",",categoryStrList);
    }

    public List<Category> listCategory() {
        return baseMapper.queryAll();
    }
}
