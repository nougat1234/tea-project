package com.app.moudles.service;

import com.app.moudles.mapper.GoodsCategoryMapper;
import com.app.moudles.mapper.GoodsPropertyMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.app.moudles.mapper.GoodsMapper;
import com.common.common.enums.GoodsPropertyCategory;
import com.common.entity.app.Goods;
import com.common.entity.app.GoodsCategory;
import com.common.entity.app.GoodsProperty;
import com.common.entity.app.vo.GoodsMenuVO;
import com.common.entity.app.vo.GoodsVO;
import com.common.entity.app.vo.SameCategoryPropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;


@Slf4j
@Service
public class GoodsServiceImpl {
    @Resource
    private GoodsCategoryMapper goodsCategoryMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private GoodsPropertyMapper goodsPropertyMapper;

    public List<GoodsMenuVO> getGoodsMenuDetailList() {

        List<GoodsCategory> goodsCategoryList = goodsCategoryMapper.selectList(null);
        // 使用2个map去关联关系
        HashMap<String, List<Goods>> sameCategoryGoodsMap = new HashMap<>(goodsCategoryList.size());
        HashMap<String, GoodsMenuVO> goodsMenuVOMap = new HashMap<>(goodsCategoryList.size());
        for (GoodsCategory goodsCategory : goodsCategoryList) {
            sameCategoryGoodsMap.put(goodsCategory.getName(), new ArrayList<>());
            GoodsMenuVO goodsMenuVO = new GoodsMenuVO();
            goodsMenuVO.setGoodsCategoryName(goodsCategory.getName());
            goodsMenuVO.setGoodsCategoryName(goodsCategory.getName());
            goodsMenuVO.setDisplayOrder(goodsCategory.getDisplayOrder());
            goodsMenuVO.setGoodsCategoryShow(goodsCategory.getShowStatus());
            goodsMenuVOMap.put(goodsCategory.getName(), goodsMenuVO);
        }

        List<Goods> allGoods = goodsMapper.selectList(null);
        // 将所有商品分类
        for (Goods good : allGoods)
            if (sameCategoryGoodsMap.containsKey(good.getGoodsCategoryName()))
                sameCategoryGoodsMap.get(good.getGoodsCategoryName()).add(good);

        Random random = new Random(10000);
        // 关联同类商品排好序后放到到对应的类别里
        for (Map.Entry<String, GoodsMenuVO> goodsMenuVOEntry : goodsMenuVOMap.entrySet()) {
            List<Goods> sameCategoryGoodsList = sameCategoryGoodsMap.get(goodsMenuVOEntry.getKey());
            sameCategoryGoodsList.sort((o1, o2) -> o1.getDisplayOrder() - o2.getDisplayOrder());
            List<GoodsVO> goodsVos = new ArrayList<>();
            // 填充商品的属性
            for (Goods goods : sameCategoryGoodsList) {
                List<GoodsProperty> goodsPropertyList = goodsPropertyMapper.selectList(
                        new QueryWrapper<GoodsProperty>().eq("goods_id", goods.getId()));
                HashMap<String, List<GoodsProperty>> propertyMap = new HashMap<>();
                for (GoodsProperty goodsProperty : goodsPropertyList) {
                    if (propertyMap.containsKey(goodsProperty.getCategory())) {
                        propertyMap.get(goodsProperty.getCategory()).add(goodsProperty);
                    } else {
                        propertyMap.put(goodsProperty.getCategory(), new ArrayList<GoodsProperty>() {{
                            add(goodsProperty);
                        }});
                    }
                    if (goodsProperty.getIsDefault() && GoodsPropertyCategory.ENUM_size.value.equals(goodsProperty.getCategory()))
                        goods.setDefaultPrice(goodsProperty.getRebasePrice()); // 重新设置商品的默认价格
                }
                List<SameCategoryPropertyVO> goodsPropertyVos = new ArrayList<>();
                for (Map.Entry<String, List<GoodsProperty>> entry : propertyMap.entrySet()) {
                    // 属性类别->属性列表  转换成对象
                    SameCategoryPropertyVO goodsPropertyVo = new SameCategoryPropertyVO();
                    goodsPropertyVo.setCategory(entry.getKey());
                    goodsPropertyVo.setRequired(!GoodsPropertyCategory.ENUM_jia_liao.value.equalsIgnoreCase(entry.getKey())); // 除了加料其他全部必选
                    goodsPropertyVo.setPropertyList(entry.getValue());
                    goodsPropertyVos.add(goodsPropertyVo);
                }
                GoodsVO goodsVo = new GoodsVO();
                BeanUtils.copyProperties(goods, goodsVo);
                goodsVo.setGoodsPropertyVos(goodsPropertyVos);
                goodsVo.setRealPrice(goods.getDefaultPrice());
                goodsVo.setImage(goodsVo.getImage() + "?random=" + random.nextInt()); // 动态刷新小程序里的图片
                goodsVos.add(goodsVo);
            }
            goodsMenuVOEntry.getValue().setGoodsList(goodsVos);
        }

        List<GoodsMenuVO> goodsMenuVOList = new ArrayList<>(goodsMenuVOMap.values());
        // 最后对菜单进行排序
        goodsMenuVOList.sort((o1, o2) -> o1.getDisplayOrder() - o2.getDisplayOrder());
        return goodsMenuVOList;
    }
}
