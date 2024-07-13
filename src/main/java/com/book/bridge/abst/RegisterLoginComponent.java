package com.book.bridge.abst;

import com.book.bridge.function.RegisterLoginFuncInterface;
import com.book.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

public class RegisterLoginComponent extends AbstractRegisterLoginComponent {
    public RegisterLoginComponent(RegisterLoginFuncInterface funcInterface) {
        super(funcInterface);
    }
//直接通过桥梁，调用右路Implementor方法，把具体实现交给右路的实现类
    @Override
    protected String login(String userName, String password) {
        return funcInterface.login(userName,password);
    }

    @Override
    protected String register(UserInfo userInfo) {
        return funcInterface.register(userInfo);
    }

    @Override
    protected boolean checkUserExists(String userName) {
        return funcInterface.checkUserExists(userName);
    }

    @Override
    protected String login3rd(HttpServletRequest request) {
        return funcInterface.login3rd(request);
    }
}
