package com.admin.modules.app.controller;

import com.admin.modules.app.service.GoodsAdminService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.entity.app.dto.GoodsDTO;
import com.admin.modules.app.mapper.GoodsAdminMapper;
import com.common.common.annotation.NeedPermission;
import com.common.common.exception.ServiceException;
import com.common.common.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "系统：商铺管理")
@RestController
@RequestMapping("/goodsAdmin")
public class GoodsAdminController {

    @Resource
    private GoodsAdminService goodsAdminService;
    @Resource
    private GoodsAdminMapper goodsAdminMapper;


    @ApiOperation("分页查询")
    @NeedPermission
    @GetMapping("/page")
    public Result<Page<GoodsDTO>> getGoodsAdminByPage(@RequestParam(defaultValue = "1") int pageNo,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(goodsAdminService.getGoodsAdminByPage(pageNo, pageSize));
    }

    @ApiOperation("查询一个商品")
    @NeedPermission
    @GetMapping("/{goodsId}")
    public Result<GoodsDTO> getGoodsById(@PathVariable Integer goodsId) {
        return Result.ok(goodsAdminService.getGoodsById(goodsId));
    }

    @ApiOperation("增加")
    @NeedPermission
    @PostMapping("")
    public Result<Integer> add(@RequestBody GoodsDTO goodsAdmin) {
        return Result.ok(goodsAdminService.addGoodsAdmin(goodsAdmin));
    }

    @ApiOperation("批量删除")
    @NeedPermission
    @DeleteMapping("/batch")
    public Result<Integer> delete(@RequestBody List<Integer> idList) {
        return Result.ok(goodsAdminService.deleteGoodsAdminBatchIds(idList));
    }

    @ApiOperation("修改")
    @NeedPermission
    @PutMapping("")
    public Result<Integer> update(@RequestBody GoodsDTO goodsAdmin) {
        return Result.ok(goodsAdminService.updateGoodsAdmin(goodsAdmin));
    }

    @ApiOperation("下架或上架商品")
    @NeedPermission
    @PutMapping("/updateSellStatus/{goodsId}")
    public Result<Integer> updateSellStatus(@PathVariable Integer goodsId) {
        return Result.ok(goodsAdminMapper.updateSellStatus(goodsId));
    }

    @ApiOperation("更新商品的图片")
    @NeedPermission
    @RequestMapping("/image")
    public Result<Integer> updateGoodsImage(@RequestParam Integer goodsId, @RequestParam MultipartFile file) throws ServiceException { // file的名字要和前端input里的name一致
        return Result.ok(goodsAdminService.updateGoodsImage(goodsId, file));
    }
}
