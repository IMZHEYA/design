package com.book.bridge.function;

import com.book.pojo.UserInfo;
import com.book.repo.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public abstract  class RegisterLoginFunc implements RegisterLoginFuncInterface {
    protected String commonLogin(String account, String password, UserRepository userRepository){
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account, password);
        if(userInfo == null){
            return "account / password error";
        }
        return "login success";
    }

    protected String commonRegister(UserInfo userInfo,UserRepository userRepository){
        if(commonCheckUserExists(userInfo.getUserName(),userRepository)){
            throw new RuntimeException("user already registered");
        }
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "register success!";
    }

    protected boolean commonCheckUserExists(String userName,UserRepository userRepository){
        UserInfo user = userRepository.findByUserName(userName);
        if(user == null){
            return false;
        }
        return true;
    }

    public String login(String account,String password){
        throw new UnsupportedOperationException();
    }

    public String register(UserInfo userInfo){
        throw new UnsupportedOperationException();
    }

    public boolean checkUserExists(String userName){
        throw new UnsupportedOperationException();
    }

    //第三方账号登录接口
    public String login3rd(HttpServletRequest request){
        throw new UnsupportedOperationException();
    }
}
