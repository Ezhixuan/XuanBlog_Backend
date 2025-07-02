package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.dto.UserEditDTO;
import com.ezhixuan.blog.domain.dto.UserLoginDTO;
import com.ezhixuan.blog.domain.dto.UserRegisterDTO;
import com.ezhixuan.blog.domain.entity.sys.SysUser;
import com.ezhixuan.blog.domain.vo.UserInfoVO;


/**
* @author ezhixuan
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2025-03-26 16:38:17
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户注册
     * @author Ezhixuan
     * @param userRegisterDTO 用户注册信息
     */
    void doRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     * @author Ezhixuan
     * @param userLoginDTO 用户登录信息
     */
    void doLogin(UserLoginDTO userLoginDTO);

    /**
     * 获取当前登录用户信息Vo
     * @author Ezhixuan
     * @return 脱敏后的用户信息
     */
    UserInfoVO getLoginUserInfoVO();

    /**
     * 根据用户id获取用户信息Vo
     * @author Ezhixuan
     * @param userId 用户id
     * @return 脱敏后的用户信息
     */
    UserInfoVO getUserInfoVo(Long userId);

    /**
     * 获取当前登录用户信息
     * @author Ezhixuan
     * @return 用户信息
     */
    SysUser getLoginUserInfo();

    /**
     * 根据用户id获取用户信息
     * @author Ezhixuan
     * @param userId 用户id
     * @return 用户信息
     */
    SysUser getUserInfo(Long userId);

    /**
     * 用户信息编辑
     * @author Ezhixuan
     * @param userEditDTO 用户编辑
     */
    void updateUserInfo(UserEditDTO userEditDTO);

    /**
     * 判断当前用户是否是管理员
     * @param userId 用户id
     * @return 是否是管理员
     */
    boolean isAdmin(Long userId);

}
