package com.admin.modules.system.controller;

import com.common.entity.system.SysUser;
import com.admin.modules.system.mapper.RolePermissionMapper;
import com.admin.modules.system.mapper.SysUserMapper;
import com.admin.modules.system.entity.vo.SysUserVO;
import com.common.common.annotation.NeedPermission;
import com.common.common.exception.ServiceException;
import com.common.common.util.GeneratorUtil;
import com.common.common.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Api(tags = "系统：系统管理员")
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @ApiOperation("查询全部")
    @NeedPermission
    @GetMapping("/list")
    public Result<List<SysUserVO>> getAllSysUsers() throws ServiceException {
        return Result.ok(sysUserMapper.getAllUserVOs());
    }

    @ApiOperation("根据id查询")
    @NeedPermission
    @GetMapping("/{sysUserId}")
    public Result detail(@PathVariable("sysUserId") Integer sysUserId) {
        return Result.ok(sysUserMapper.selectById(sysUserId));
    }

    @ApiOperation("增")
    @NeedPermission
    @PostMapping("")
    public Result add(@RequestBody SysUser sysUser) throws ServiceException {
        sysUser.setPassword(GeneratorUtil.md5Base64("123456"));
        sysUser.setCreateTime(new Date());
        return Result.ok(sysUserMapper.insert(sysUser));
    }

    @ApiOperation("改")
    @NeedPermission
    @PutMapping("")
    public Result update(@RequestBody SysUser sysUser) throws ServiceException {
        sysUser.setPassword(null);
        return Result.ok(sysUserMapper.updateById(sysUser));
    }

    @ApiOperation("批量删")
    @NeedPermission
    @DeleteMapping("/{sysUserId}")
    public Result deleteBatch(@RequestBody Integer sysUserId) throws ServiceException {
        if ( rolePermissionMapper.selectPermissionByUserId(sysUserId).contains("*"))
            throw ServiceException.CONST_can_not_change_role_of_super_system_admin_user;
        return Result.ok(sysUserMapper.deleteById(sysUserId));
    }

    @ApiOperation("更新账号激活状态")
    @NeedPermission
    @PutMapping("/status/{sysUserId}/{status}")
    public Result updateStatus(@ApiParam("sysUserId") @PathVariable Integer sysUserId,
                               @ApiParam("status") @PathVariable Boolean status) throws ServiceException {
        SysUser sysUser = new SysUser();
        sysUser.setId(sysUserId);
        sysUser.setStatus(status);
        return Result.ok(sysUserMapper.updateById(sysUser));
    }

    @ApiOperation("重置密码为123456")
    @NeedPermission
    @PutMapping("/resetPassword/{sysUserId}")
    public Result resetPassword(@ApiParam("sysUserId") @PathVariable Integer sysUserId) throws ServiceException {
        SysUser sysUser = new SysUser();
        sysUser.setId(sysUserId);
        sysUser.setPassword(GeneratorUtil.md5Base64("123456"));
        return Result.ok(sysUserMapper.updateById(sysUser));
    }
}
