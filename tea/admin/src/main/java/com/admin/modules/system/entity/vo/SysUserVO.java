package com.admin.modules.system.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("视图层的用户对象")
@Data
public class SysUserVO {
    @TableId
    private Integer id;

    private String username;

    private Integer roleId;

    private String roleName;

    @ApiModelProperty("账号激活状态")
    private Boolean status;

    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
