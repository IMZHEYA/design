package com.book.bridge.function;

import com.book.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

public abstract  class RegisterLoginFunc implements RegisterLoginFuncInterface {
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
