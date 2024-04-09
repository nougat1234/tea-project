package com.admin.modules.app.service;

import com.admin.modules.app.mapper.GoodsCategoryAdminMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.entity.app.GoodsCategory;
import com.common.entity.app.dto.GoodsDTO;
import com.admin.modules.app.mapper.GoodsAdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class GoodsCategoryAdminService {

    @Resource
    private GoodsCategoryAdminMapper goodsCategoryAdminMapper;
    @Resource
    private GoodsAdminMapper goodsAdminMapper;


    /**
     * 查询所有
     */
    public List<GoodsCategory> getAllGoodsCategoryAdmins() {
        return goodsCategoryAdminMapper.selectList(
                new QueryWrapper<GoodsCategory>().orderByAsc("display_order")
        );
    }

    // 增加
    @Transactional(rollbackFor = Exception.class)
    public int addGoodsCategoryAdmin(GoodsCategory goodsCategory) {
        return goodsCategoryAdminMapper.insert(goodsCategory);
    }

    // 删除
    @Transactional(rollbackFor = Exception.class)
    public int deleteGoodsCategoryAdminByName(String categoryName) {
        goodsAdminMapper.delete(new QueryWrapper<GoodsDTO>().eq("goods_category_name", categoryName));
        return goodsCategoryAdminMapper.deleteById(categoryName);
    }

    // 修改
    @Transactional(rollbackFor = Exception.class)
    public int updateGoodsCategoryAdmin(String oldName, GoodsCategory goodsCategory) {
        goodsAdminMapper.updateGoodsCategory(oldName, goodsCategory.getName());
        return goodsCategoryAdminMapper.updateGoodsCategoryAdmin(
                oldName, goodsCategory.getName(), goodsCategory.getDisplayOrder());
    }
}
