package com.ezhixuan.xuanblog_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.web.bind.annotation.*;

import com.ezhixuan.xuanblog_backend.common.BaseResponse;
import com.ezhixuan.xuanblog_backend.common.R;
import com.ezhixuan.xuanblog_backend.domain.dto.UserEditDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.UserLoginDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.UserRegisterDTO;
import com.ezhixuan.xuanblog_backend.domain.vo.UserInfoVO;
import com.ezhixuan.xuanblog_backend.service.SysUserService;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class SysUserController {

    final SysUserService userService;

    @PostMapping("/register")
    public BaseResponse<String> doRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        userService.doRegister(userRegisterDTO);
        return R.success();
    }

    @PostMapping("/login")
    public BaseResponse<SaTokenInfo> doLogin(@RequestBody UserLoginDTO userLoginDTO) {
        userService.doLogin(userLoginDTO);
        return R.success(StpUtil.getTokenInfo());
    }

    @GetMapping("/info")
    @SaCheckLogin
    public BaseResponse<UserInfoVO> getLoginUserInfo() {
        StpUtil.checkLogin();
        return R.success(userService.getLoginUserInfoVO());
    }

    @PostMapping("/logout")
    @SaCheckLogin
    public BaseResponse<String> doLogout() {
        StpUtil.checkLogin();
        StpUtil.logout();
        return R.success();
    }

    @PostMapping("/edit")
    @SaCheckLogin
    public BaseResponse<UserInfoVO> editUserInfo(@RequestBody UserEditDTO userEditDTO) {
        userService.updateUserInfo(userEditDTO);
        return R.success(userService.getUserInfoVo(StpUtil.getLoginIdAsLong()));
    }
}
