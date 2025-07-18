package com.ezhixuan.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.domain.dto.UserEditDTO;
import com.ezhixuan.blog.domain.dto.UserLoginDTO;
import com.ezhixuan.blog.domain.dto.UserRegisterDTO;
import com.ezhixuan.blog.domain.vo.UserInfoVO;
import com.ezhixuan.blog.service.SysUserService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class SysUserController {

    final SysUserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<String> doRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        userService.doRegister(userRegisterDTO);
        return R.success();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<SaTokenInfo> doLogin(@RequestBody UserLoginDTO userLoginDTO) {
        userService.doLogin(userLoginDTO);
        return R.success(StpUtil.getTokenInfo());
    }

    @Operation(summary = "获取登录用户信息")
    @GetMapping("/info")
    @SaCheckLogin
    public BaseResponse<UserInfoVO> getLoginUserInfo() {
        StpUtil.checkLogin();
        return R.success(userService.getLoginUserInfoVO());
    }

    @Operation(summary = "获取管理员用户信息")
    @GetMapping("/admin")
    public BaseResponse<UserInfoVO> getAdminUserInfo() {
        return R.success(userService.getAdminUserInfoVO());
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    @SaCheckLogin
    public BaseResponse<String> doLogout() {
        StpUtil.checkLogin();
        StpUtil.logout();
        return R.success();
    }

    @Operation(summary = "用户信息修改")
    @PostMapping("/edit")
    @SaCheckLogin
    public BaseResponse<UserInfoVO> editUserInfo(@RequestBody UserEditDTO userEditDTO) {
        userService.updateUserInfo(userEditDTO);
        return R.success(userService.getUserInfoVo(StpUtil.getLoginIdAsLong()));
    }
}
