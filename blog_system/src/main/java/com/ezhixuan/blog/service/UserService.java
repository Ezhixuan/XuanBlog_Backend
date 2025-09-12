package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.controller.user.dto.UserEditDTO;
import com.ezhixuan.blog.controller.user.dto.UserLoginDTO;
import com.ezhixuan.blog.controller.user.dto.UserRegisterDTO;
import com.ezhixuan.blog.domain.entity.User;
import com.ezhixuan.blog.controller.user.vo.UserInfoVO;

/**
* @author ezhixuan
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2025-09-12 17:10:08
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterDTO 用户注册信息
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     * @param userLoginDTO 用户登录信息
     */
    void login(UserLoginDTO userLoginDTO);

    /**
     * 获取当前登录用户信息VO
     * @return 当前登录用户的脱敏信息
     */
    UserInfoVO getLoginUserVO();

    /**
     * 根据用户ID获取用户信息VO
     * @param userId 用户ID
     * @return 指定用户的脱敏信息
     */
    UserInfoVO getUserVOById(Long userId);

    /**
     * 根据用户ID获取用户完整信息
     * @param userId 用户ID
     * @return 指定用户的完整信息
     */
    User getUserById(Long userId);

    /**
     * 更新用户信息
     * @param userEditDTO 用户编辑信息
     */
    void updateUserInfo(UserEditDTO userEditDTO);

    /**
     * 判断用户是否为管理员
     * @param userId 用户ID
     * @return 是否为管理员
     */
    boolean isAdmin(Long userId);

    /**
     * 获取管理员用户信息VO
     * @return 管理员用户的脱敏信息
     */
    UserInfoVO getAdminUserVO();
}
