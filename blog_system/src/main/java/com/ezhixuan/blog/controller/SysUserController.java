package com.ezhixuan.blog.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.UserEditDTO;
import com.ezhixuan.blog.domain.dto.UserLoginDTO;
import com.ezhixuan.blog.domain.dto.UserRegisterDTO;
import com.ezhixuan.blog.domain.vo.UserInfoVO;
import com.ezhixuan.blog.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@Tag(name = "SysUserController", description = "提供用户相关的操作")
public class SysUserController {

  private final SysUserService userService;

  @PostMapping("/register")
  @Operation(summary = "用户注册")
  public BaseResponse<String> doRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
    userService.doRegister(userRegisterDTO);
    return R.success();
  }

  @PostMapping("/login")
  @Operation(summary = "用户登录")
  public BaseResponse<SaTokenInfo> doLogin(@RequestBody UserLoginDTO userLoginDTO) {
    userService.doLogin(userLoginDTO);
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

  @SaCheckLogin
  @GetMapping("/info")
  @Operation(summary = "获取登录用户信息")
  public BaseResponse<UserInfoVO> getLoginUserInfo() {
    try {
      StpUtil.checkLogin();
      return R.success(userService.getLoginUserInfoVO());
    } catch (Exception exception) {
      return R.success();
    }
  }

  @GetMapping("/admin")
  @Operation(summary = "获取管理员用户信息")
  public BaseResponse<UserInfoVO> getAdminUserInfo() {
    return R.success(userService.getAdminUserInfoVO());
  }

  @SaCheckLogin
  @PatchMapping
  @Operation(summary = "用户信息修改")
  public BaseResponse<UserInfoVO> editUserInfo(@RequestBody UserEditDTO userEditDTO) {
    userService.updateUserInfo(userEditDTO);
    return R.success(userService.getUserInfoVo(StpUtil.getLoginIdAsLong()));
  }
}
