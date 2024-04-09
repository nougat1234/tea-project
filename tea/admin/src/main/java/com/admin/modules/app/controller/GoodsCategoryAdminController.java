package com.admin.modules.app.controller;

import com.common.entity.app.GoodsCategory;
import com.admin.modules.app.mapper.GoodsCategoryAdminMapper;
import com.admin.modules.app.service.GoodsCategoryAdminService;
import com.common.common.annotation.NeedPermission;
import com.common.common.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "系统：商品类别管理")
@RestController
@RequestMapping("/goodsCategoryAdmin")
public class GoodsCategoryAdminController {

    @Resource
    private GoodsCategoryAdminService goodsCategoryAdminService;
    @Resource
    private GoodsCategoryAdminMapper goodsCategoryAdminMapper;

    @ApiOperation("查询全部")
    @NeedPermission
    @GetMapping("/list")
    public Result<List<GoodsCategory>> getAllGoodsCategoryAdmins() {
        return Result.ok(goodsCategoryAdminService.getAllGoodsCategoryAdmins());
    }

    @ApiOperation("增加")
    @NeedPermission
    @PostMapping("")
    public Result<Integer> add(@RequestBody GoodsCategory goodsCategoryAdmin) {
        return Result.ok(goodsCategoryAdminService.addGoodsCategoryAdmin(goodsCategoryAdmin));
    }

    @ApiOperation("批量删除")
    @NeedPermission
    @DeleteMapping("/{categoryName}")
    public Result<Integer> delete(@PathVariable String categoryName) {
        return Result.ok(goodsCategoryAdminService.deleteGoodsCategoryAdminByName(categoryName));
    }

    @ApiOperation("修改")
    @NeedPermission
    @PutMapping("/{oldName}")
    public Result<Integer> update(@PathVariable String oldName, @RequestBody GoodsCategory goodsCategoryAdmin) {
        return Result.ok(goodsCategoryAdminService.updateGoodsCategoryAdmin(oldName, goodsCategoryAdmin));
    }

    @ApiOperation("显示或隐藏该类商品")
    @NeedPermission
    @PutMapping("/showStatus/{name}")
    public Result<Integer> updateCategoryShowStatus(@PathVariable String name) {
        return Result.ok(goodsCategoryAdminMapper.updateCategoryShowStatus(name));
    }


}
