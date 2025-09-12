package com.ezhixuan.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.config.props.BlogProp;
import com.ezhixuan.blog.controller.user.dto.UserEditDTO;
import com.ezhixuan.blog.controller.user.dto.UserLoginDTO;
import com.ezhixuan.blog.controller.user.dto.UserRegisterDTO;
import com.ezhixuan.blog.controller.user.dto.UserUpdatePasswordDTO;
import com.ezhixuan.blog.controller.user.vo.UserInfoVO;
import com.ezhixuan.blog.domain.entity.User;
import com.ezhixuan.blog.domain.enums.RoleEnum;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.mapper.UserMapper;
import com.ezhixuan.blog.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author ezhixuan
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 * @createDate 2025-09-12 17:10:08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Resource private BlogProp blogProp;

  /**
   * 用户注册
   *
   * @param userRegisterDTO 用户注册信息
   */
  @Override
  public void register(UserRegisterDTO userRegisterDTO) {
    userRegisterDTO.check();
    String encryptedPassword = checkAndSupplyEncPwd(userRegisterDTO);

    String userRole = count() == 0 ? RoleEnum.ROLE_ADMIN.getRole() : RoleEnum.ROLE_USER.getRole();
    User user = new User(userRegisterDTO.getAccount(), encryptedPassword, userRole);
    save(user);
  }

  /**
   * 用户登录
   *
   * @param userLoginDTO 用户登录信息
   */
  @Override
  public void login(UserLoginDTO userLoginDTO) {
    userLoginDTO.check();
    String encryptedPassword = checkAndSupplyEncPwd(userLoginDTO);
    User login =
        getOne(
            Wrappers.<User>lambdaQuery()
                .eq(User::getAccount, userLoginDTO.getAccount())
                .eq(User::getPassword, encryptedPassword));
    StpUtil.login(login.getId());
  }

  /**
   * 内部方法 校验并返回加密后的密码
   *
   * @author Ezhixuan
   * @param dto 用户登录/注册信息
   * @return String
   */
  private <T extends UserLoginDTO> String checkAndSupplyEncPwd(T dto) {
    String userAccount = dto.getAccount();
    String password = dto.getPassword();
    // 是否已经存在
    String encPwd = encryptByMd5(password);

    if (dto instanceof UserUpdatePasswordDTO) {
      String confirmPassword = ((UserUpdatePasswordDTO) dto).getConfirmPassword();
      String oldEncPwd = encryptByMd5(confirmPassword);
      boolean exists =
          this.lambdaQuery()
              .eq(User::getAccount, userAccount)
              .eq(User::getPassword, oldEncPwd)
              .exists();
      if (!exists) {
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "密码错误");
      }
    } else if (dto instanceof UserRegisterDTO) {
      boolean exists = this.lambdaQuery().eq(User::getAccount, userAccount).exists();
      if (exists) {
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户名已存在");
      }
    } else {
      boolean exists =
          this.lambdaQuery()
              .eq(User::getAccount, userAccount)
              .eq(User::getPassword, encPwd)
              .exists();
      if (!exists) {
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户名或密码错误");
      }
    }
    return encPwd;
  }

  private String encryptByMd5(String password) {
    String saltPwd = blogProp.getSalt() + password;
    return DigestUtils.md5DigestAsHex(saltPwd.getBytes());
  }

  /**
   * 获取当前登录用户信息VO
   *
   * @return 当前登录用户的脱敏信息
   */
  @Override
  public UserInfoVO getLoginUserVO() {
    try {
      StpUtil.checkLogin();
      long userId = StpUtil.getLoginIdAsLong();
      return getUserVOById(userId);
    } catch (Exception exception) {
      StpUtil.logout();
    }
    return new UserInfoVO();
  }

  /**
   * 根据用户ID获取用户信息VO
   *
   * @param userId 用户ID
   * @return 指定用户的脱敏信息
   */
  @Override
  public UserInfoVO getUserVOById(Long userId) {
    return new UserInfoVO(getUserById(userId));
  }

  /**
   * 根据用户ID获取用户完整信息
   *
   * @param userId 用户ID
   * @return 指定用户的完整信息
   */
  @Override
  public User getUserById(Long userId) {
    return getById(userId);
  }

  /**
   * 更新用户信息
   *
   * @param userEditDTO 用户编辑信息
   */
  @Override
  public void updateUserInfo(UserEditDTO userEditDTO) {
    long userId = StpUtil.getLoginIdAsLong();
    String account = baseMapper.selectAccountById(userId);
    User editUser = userEditDTO.toEntity(userId);
    if (StringUtils.hasText(userEditDTO.getPassword())) {
      UserUpdatePasswordDTO updatePasswordDTO =
          new UserUpdatePasswordDTO(
              account, userEditDTO.getPassword(), userEditDTO.getOldPassword());
      String encPwd = checkAndSupplyEncPwd(updatePasswordDTO);
      editUser.setPassword(encPwd);
    }
    updateById(editUser);
  }

  /**
   * 判断用户是否为管理员
   *
   * @param userId 用户ID
   * @return 是否为管理员
   */
  @Override
  public boolean isAdmin(Long userId) {
    return Objects.equals(userId, 1L)
        || this.lambdaQuery().eq(User::getId, userId).eq(User::getRole, "admin").exists();
  }

  /**
   * 获取管理员用户信息VO
   *
   * @return 管理员用户的脱敏信息
   */
  @Override
  public UserInfoVO getAdminUserVO() {
    User one =
        getOne(Wrappers.<User>lambdaQuery().eq(User::getRole, RoleEnum.ROLE_ADMIN.getRole()));
    return new UserInfoVO(one);
  }
}
