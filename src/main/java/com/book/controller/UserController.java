package com.book.controller;

import com.book.adapter.Login3rdAdapter;
import com.book.pojo.BusinessLaunch;
import com.book.pojo.UserInfo;
import com.book.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
//
//    @PostMapping("/business/launch")
//    public List<BusinessLaunch> filterBusinessLaunch(@RequestParam("city") String city,@RequestParam("sex") String sex,@RequestParam("product") String product){
//        return userService.filterBusinessLaunch(city,sex,product);
//    }

    @PostMapping("/ticket")
    public Object createTicket(String type,String productId,String content,String title,String bankInfo,String taxId){
        return userService.createTicket(type,productId,content,title,bankInfo,taxId);
    }
}
