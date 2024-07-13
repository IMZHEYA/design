package com.book.bridge.function;

import com.book.bridge.abst.factory.RegisterLoginComponentFactory;
import com.book.pojo.UserInfo;
import com.book.repo.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
@Component
public class RegisterLoginByDefault extends RegisterLoginFunc implements RegisterLoginFuncInterface {
    @Resource
    private UserRepository userRepository;

    @PostConstruct
    private void initFunMap(){
        RegisterLoginComponentFactory.funcMap.put("Default",this);
    }

    public String login(String account, String password) {
      return super.commonLogin(account,password,userRepository);
    }

    public String register(UserInfo userInfo) {
        return super.commonRegister(userInfo,userRepository);
    }
    //根据用户账号名称检查用户是否已注册
    public boolean checkUserExists(String userName){
        return super.commonCheckUserExists(userName,userRepository);
    }
//瑕疵所在，Default类不需要实现login3rd方法
//    @Override
//    public String login3rd(HttpServletRequest request) {
//        return null;
//    }
}
