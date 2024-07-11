package com.book.service;

import com.book.pojo.UserInfo;
import com.book.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserService {
    @Resource
    private UserRepository userRepository;


    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account, password);
        if(userInfo == null){
            return "account / password error";
        }
        return "login success";
    }

    public String register(UserInfo userInfo) {
        if(checkUserExists(userInfo.getUserName())){
            throw new RuntimeException("user already registered");
        }
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "register success!";
    }
    //根据用户账号名称检查用户是否已注册
    public boolean checkUserExists(String userName){
        UserInfo user = userRepository.findByUserName(userName);
        if(user == null){
            return false;
        }
        return true;
    }
}
