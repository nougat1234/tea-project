package com.app.moudles.service;

import com.app.common.util.session.SessionUtil;
import com.app.moudles.mapper.UserMapper;
import com.common.common.config.property.IOProperty;
import com.common.common.exception.ServiceException;
import com.common.common.util.ImgFileUtils;
import com.common.common.util.weixin.WeixinUtil;
import com.common.entity.app.User;
import com.common.entity.app.form.LoginByWeixinForm;
import com.common.entity.app.form.UpdateUserForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl {
    @Resource
    private UserMapper userMapper;

    @Resource
    private IOProperty ioProperty;

    // 通过微信登录，没有就注册
    @Transactional
    public User loginByWeixin(LoginByWeixinForm form) throws ServiceException {
        log.warn("登录传入的参数为: " + form.toString());
        String wxOpenid = WeixinUtil.getWeiXinOpenid(form.getCode());
        if (wxOpenid != null) {
            User user = userMapper.selectById(wxOpenid);
            if (user == null) { // 没有就注册
                user = new User();
                user.setWxOpenid(wxOpenid);
                user.setStatus(true);
                userMapper.insert(user);
            }
            if (!user.getStatus()) { // 检查账号是否冻结
                throw ServiceException.CONST_user_is_forbidden;
            }

            user.setWxAvatar(this.saveUserAvatar(form.getWxAvatar())); // 微信头像
            SessionUtil.setUserSession(user);
            userMapper.updateWxAvatar(user.getWxAvatar(), wxOpenid);
            log.info("[通过微信登录] {}", user);
            return user;
        } else {
            throw ServiceException.CONST_wx_login_failed;
        }
    }

    public User getUser(String userId) {
        return userMapper.selectById(userId);
    }

    public int updateUser(UpdateUserForm form, String wxOpenid) throws Exception {
        User user = new User();
        user.setWxOpenid(wxOpenid);
        user.setName(form.getName());
        user.setAddress(form.getAddress());
        user.setWxAvatar(form.getWxAvatar());
        user.setPhone(form.getPhone());
        user.setSex(form.getSex());
        return userMapper.updateById(user);
    }


    /**
     * 保存图片
     * @param
     * @return
     */
    private String saveUserAvatar(String avaterUrl){
        if(StringUtils.isEmpty(avaterUrl)){
            return null;
        }
        File dir = new File(ioProperty.getImageFileRootPath());
        if (!dir.exists()) { // 不存在该目录就创建
            dir.mkdir();
        }
        Random random = new Random();
        // 图片的扩展名
        String extension = ".jpg"; // JPEG格式
        String userAvatar = "userAvatar-" + random.nextInt(1000000) + "-" + extension;
        ImgFileUtils.saveImg(avaterUrl, dir + File.separator + userAvatar);
        return userAvatar;
    }
}
