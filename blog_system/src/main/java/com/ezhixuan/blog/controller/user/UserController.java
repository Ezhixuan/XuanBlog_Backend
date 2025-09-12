package com.ezhixuan.blog.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.controller.user.dto.UserEditDTO;
import com.ezhixuan.blog.controller.user.dto.UserLoginDTO;
import com.ezhixuan.blog.controller.user.dto.UserRegisterDTO;
import com.ezhixuan.blog.controller.user.vo.UserInfoVO;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.service.UserService;
import com.ezhixuan.blog.utils.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@Tag(name = "UserController", description = "提供用户相关的操作")
public class UserController {

  private final UserService userService;
  private final RedisUtil redisUtil;

  @PostMapping("/register")
  @Operation(summary = "用户注册")
  public BaseResponse<String> doRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
    userService.register(userRegisterDTO);
    return R.success();
  }

  @PostMapping("/login")
  @Operation(summary = "用户登录")
  public BaseResponse<SaTokenInfo> doLogin(@RequestBody UserLoginDTO userLoginDTO) {
    userService.login(userLoginDTO);
    return R.success(StpUtil.getTokenInfo());
  }

  @SaCheckLogin
  @PostMapping("/logout")
  @Operation(summary = "用户登出")
  public BaseResponse<String> doLogout() {
    StpUtil.checkLogin();
    StpUtil.logout();
    return R.success();
  }

  @Cache(key = RedisKeyConstant.USER_INFO_KEY, expireTime = 60L * 60)
  @SaCheckLogin
  @GetMapping("/info")
  @Operation(summary = "获取登录用户信息")
  public BaseResponse<UserInfoVO> getLoginUserInfo() {
      return R.success(userService.getLoginUserVO());
  }

  @Cache(key = RedisKeyConstant.ADMIN_INFO_KEY, expireTime = 60L * 60 * 7)
  @GetMapping("/admin")
  @Operation(summary = "获取管理员用户信息")
  public BaseResponse<UserInfoVO> getAdminUserInfo() {
    return R.success(userService.getAdminUserVO());
  }

  @SaCheckLogin
  @PatchMapping
  @Operation(summary = "用户信息修改")
  public BaseResponse<UserInfoVO> editUserInfo(@RequestBody UserEditDTO userEditDTO) {
    userService.updateUserInfo(userEditDTO);
    long userId = StpUtil.getLoginIdAsLong();
    if (userService.isAdmin(userId)) {
      redisUtil.cleanCache(RedisKeyConstant.ADMIN_INFO_KEY);
    }
    redisUtil.cleanCache(RedisKeyConstant.USER_INFO_KEY);
    return R.success(userService.getUserVOById(userId));
  }
}
