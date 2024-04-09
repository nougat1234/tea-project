package com.admin.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.admin.modules.system.mapper.RoleMapper;
import com.admin.modules.system.mapper.RolePermissionMapper;
import com.admin.modules.system.entity.form.UpdateRolePermissionForm;
import com.admin.modules.system.service.RoleService;
import com.common.common.annotation.NeedPermission;
import com.common.common.exception.ServiceException;
import com.common.common.util.result.Result;
import com.common.entity.system.Role;
import com.common.entity.system.RolePermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Api(tags = "系统：角色管理")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePermissionMapper sysRolePermissionMapper;

    @Resource
    private RoleService roleService;

    @ApiOperation("查询全部")
    @NeedPermission
    @GetMapping("/list")
    public Result<List<Role>> getAllsysRoles() throws ServiceException {
        return Result.ok(roleMapper.selectList(null));
    }

    @ApiOperation("分页条件查询")
    @NeedPermission
    @GetMapping("/page/{pageNo}/{pageSize}")
    public Result page(@ApiParam("页数") @PathVariable Integer pageNo,
                       @ApiParam("页面大小") @PathVariable Integer pageSize,
                       @ApiParam("查询参数") @RequestParam Map<String, Object> params) throws ServiceException {
        return Result.ok(roleService.listByPageAndCondition(pageNo, pageSize, params));
    }

    @ApiOperation("根据id查询")
    @NeedPermission
    @GetMapping("/{sysRoleId}")
    public Result detail(@PathVariable("sysRoleId") Integer sysRoleId) {
        return Result.ok(roleMapper.selectById(sysRoleId));
    }

    @ApiOperation("角色的所有权限")
    @NeedPermission
    @GetMapping("/perms/{sysRoleId}")
    public Result getRolePermissions(@PathVariable("sysRoleId") Integer sysRoleId) {
        return Result.ok(
                sysRolePermissionMapper.selectObjs(new QueryWrapper<RolePermission>()
                        .select("permission").eq("role_id", sysRoleId)
                )
        );
    }

    @ApiOperation("增")
    @NeedPermission
    @PostMapping("")
    public Result add(@RequestBody Role sysRole) throws ServiceException {
        return Result.ok(roleMapper.insert(sysRole));
    }

    @ApiOperation("改")
    @NeedPermission
    @PutMapping("")
    public Result update(@RequestBody Role sysRole) throws ServiceException {
        return Result.ok(roleMapper.updateById(sysRole));
    }

    @ApiOperation("批量删除角色")
    @NeedPermission
    @DeleteMapping("/batch")
    public Result delete(@RequestBody List<Integer> sysRoleIdList) throws ServiceException {
        return Result.ok(roleMapper.deleteBatchIds(sysRoleIdList));
    }

    @ApiOperation("修改角色的权限")
    @NeedPermission
    @PutMapping("/permission")
    public Result updateRolePermission(@ApiParam("角色权限表单") @RequestBody UpdateRolePermissionForm vo) throws ServiceException {
        roleService.updateRolePermission(vo);
        return Result.ok();
    }
}
