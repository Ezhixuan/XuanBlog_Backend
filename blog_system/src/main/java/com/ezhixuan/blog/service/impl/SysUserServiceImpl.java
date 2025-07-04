package com.ezhixuan.blog.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.config.props.BlogProp;
import com.ezhixuan.blog.domain.dto.UserEditDTO;
import com.ezhixuan.blog.domain.dto.UserLoginDTO;
import com.ezhixuan.blog.domain.dto.UserRegisterDTO;
import com.ezhixuan.blog.domain.dto.UserUpdatePasswordDTO;
import com.ezhixuan.blog.domain.entity.sys.SysUser;
import com.ezhixuan.blog.domain.enums.RoleEnum;
import com.ezhixuan.blog.domain.vo.UserInfoVO;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.mapper.SysUserMapper;
import com.ezhixuan.blog.service.SysUserService;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

/**
* @author ezhixuan
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2025-03-26 16:38:17
*/
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    private final BlogProp blogProp;

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册信息
     * @author Ezhixuan
     */
    @Override
    public void doRegister(UserRegisterDTO userRegisterDTO) {
        userRegisterDTO.check();
        String encryptedPassword = checkAndSupplyEncPwd(userRegisterDTO);

        SysUser sysUser = new SysUser();
        sysUser.setUserAccount(userRegisterDTO.getUserAccount());
        sysUser.setPassword(encryptedPassword);
        sysUser.setRole(count() == 0 ? RoleEnum.ROLE_ADMIN.getRole() : RoleEnum.ROLE_USER.getRole());
        sysUser.setUsername(userRegisterDTO.getUserAccount());
        if (Objects.equals(sysUser.getRole(), RoleEnum.ROLE_ADMIN.getRole())) {
            sysUser.setId(1L);
        }
        this.save(sysUser);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @author Ezhixuan
     */
    @Override
    public void doLogin(UserLoginDTO userLoginDTO) {
        userLoginDTO.check();
        String encryptedPassword = checkAndSupplyEncPwd(userLoginDTO);
        SysUser user = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUserAccount, userLoginDTO.getUserAccount()).eq(SysUser::getPassword, encryptedPassword));
        StpUtil.login(user.getId());
    }

    /**
     * 内部方法 校验并返回加密后的密码
     * @author Ezhixuan
     * @param dto 用户登录/注册信息
     * @return String
     */
    private <T extends UserLoginDTO> String checkAndSupplyEncPwd(T dto) {
        String userAccount = dto.getUserAccount();
        String password = dto.getPassword();
        // 是否已经存在
        String encPwd = encryptByMd5(password);

        if (dto instanceof UserUpdatePasswordDTO) {
            String confirmPassword = ((UserUpdatePasswordDTO) dto).getConfirmPassword();
            String oldEncPwd = encryptByMd5(confirmPassword);
            ThrowUtils.throwIf(!this.lambdaQuery().eq(SysUser::getUserAccount, userAccount).eq(SysUser::getPassword, oldEncPwd).exists(), ErrorCode.OPERATION_ERROR, "密码错误");
        } else if (dto instanceof UserRegisterDTO) {
            ThrowUtils.throwIf(this.lambdaQuery().eq(SysUser::getUserAccount, userAccount).exists(), ErrorCode.OPERATION_ERROR, "用户名已存在");
        }else {
            ThrowUtils.throwIf(!this.lambdaQuery().eq(SysUser::getUserAccount, userAccount).eq(SysUser::getPassword, encPwd).exists(), ErrorCode.OPERATION_ERROR, "用户名或密码错误");
        }
        return encPwd;
    }

    private String encryptByMd5(String password) {
        String saltPwd = blogProp.getSalt() + password;
        return DigestUtils.md5DigestAsHex(saltPwd.getBytes());
    }

    /**
     * 获取用户信息Vo
     *
     * @return 脱敏后的用户信息
     * @author Ezhixuan
     */
    @Override
    public UserInfoVO getLoginUserInfoVO() {
        long userId = StpUtil.getLoginIdAsLong();
        return getUserInfoVo(userId);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     * @author Ezhixuan
     */
    @Override
    public SysUser getLoginUserInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        return getUserInfo(userId);
    }

    /**
     * 根据用户id获取用户信息Vo
     *
     * @param userId 用户id
     * @return 脱敏后的用户信息
     * @author Ezhixuan
     */
    @Override
    public UserInfoVO getUserInfoVo(Long userId) {
        return new UserInfoVO(getUserInfo(userId));
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     * @author Ezhixuan
     */
    @Override
    public SysUser getUserInfo(Long userId) {
        ThrowUtils.throwIf(Objects.isNull(userId), ErrorCode.PARAMS_ERROR, "必要参数不能为null");
        return this.getById(userId);
    }

    /**
     * 用户信息编辑
     *
     * @param userEditDTO 用户编辑
     * @author Ezhixuan
     */
    @Override
    public void updateUserInfo(UserEditDTO userEditDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = this.getById(userId);
        ThrowUtils.throwIf(Objects.isNull(user), ErrorCode.NOT_LOGIN_ERROR);

        SysUser sysUser = BeanUtil.copyProperties(userEditDTO, SysUser.class);
        sysUser.setId(userId);
        if (StringUtils.hasText(userEditDTO.getPassword())) {
            UserUpdatePasswordDTO userUpdatePasswordDTO = new UserUpdatePasswordDTO();
            userUpdatePasswordDTO.setUserAccount(user.getUserAccount());
            userUpdatePasswordDTO.setPassword(userEditDTO.getPassword());
            userUpdatePasswordDTO.setConfirmPassword(userEditDTO.getOldPassword());
            String encPwd = checkAndSupplyEncPwd(userUpdatePasswordDTO);
            sysUser.setPassword(encPwd);
        }

        this.updateById(sysUser);
    }

    /**
     * 判断当前用户是否是管理员
     *
     * @param userId 用户id
     * @return 是否是管理员
     */
    @Override
    public boolean isAdmin(Long userId) {
        if (Objects.isNull(userId)) {
            return false;
        }
        return this.lambdaQuery().eq(SysUser::getId, userId).eq(SysUser::getRole, "admin").exists();
    }
}




