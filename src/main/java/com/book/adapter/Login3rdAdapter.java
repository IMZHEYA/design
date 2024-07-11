package com.book.adapter;

import com.book.service.UserService;
import org.springframework.stereotype.Component;
//适配器模式实现第三方登录
@Component
public class Login3rdAdapter extends UserService implements Login3rdTarget {
    @Override
    public String loginByGitee(String code, String state) {
        return null;
    }

    @Override
    public String loginByWechat(String... params) {
        return null;
    }

    @Override
    public String loginByQQ(String... params) {
        return null;
    }

//    @Autowired
//    private UserService userService;
    //继承是类适配器模式，引入是对象适配器模式
}
