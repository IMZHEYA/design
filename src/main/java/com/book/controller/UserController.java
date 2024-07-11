package com.book.controller;

import com.book.adapter.Login3rdAdapter;
import com.book.pojo.UserInfo;
import com.book.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private Login3rdAdapter login3rdAdapter;

    @PostMapping("/login")
    public String login(String account,String password){
        return userService.login(account,password);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo){
        return userService.register(userInfo);
    }
    //gitee平台回调接口
    @GetMapping("/gitee")
    public String gitee(String code,String state){
        return login3rdAdapter.loginByGitee(code,state);
    }

}
