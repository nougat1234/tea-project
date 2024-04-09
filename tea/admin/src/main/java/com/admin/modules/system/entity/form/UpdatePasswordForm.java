package com.admin.modules.system.entity.form;

import lombok.Data;

@Data
public class UpdatePasswordForm {
    private String oldPassword;

    private String newPassword;
}
