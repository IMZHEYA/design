package com.book.adapter;

import com.alibaba.fastjson.JSONObject;
import com.book.pojo.UserInfo;
import com.book.service.UserService;
import com.book.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Date;

//适配器模式实现第三方登录
@Component
public class Login3rdAdapter extends UserService implements Login3rdTarget {
    @Value("${gitee.state}")
    private String giteeState;

    @Value("${gitee.token.url}")
    private String giteeTokenUrl;

    @Value("${gitee.user.url}")
    private String giteeUserUrl;

    @Value("${gitee.user.prefix}")
    private String giteeUserPrefix;

    @Override
    public String loginByGitee(String code, String state) {
        //进行state判断，state值是前端后端商定
        if(!giteeState.equals(state)){
            throw new UnsupportedOperationException("invalid state!");
        }
        //请求gitee平台token，并携带code
        String tokenUrl = giteeTokenUrl.concat(code);
        JSONObject tokenResponse = HttpClientUtils.execute(tokenUrl, HttpMethod.POST);
        String token = String.valueOf(tokenResponse.get("access_token"));
        //请求用户信息，并携带token
        String userUrl = giteeUserUrl.concat(token);
        JSONObject userInfoResponse = HttpClientUtils.execute(userUrl, HttpMethod.GET);
        //获取用户信息，userName前缀GITEE@，密码保持与userName一致
        String userName = giteeUserPrefix.concat(String.valueOf(userInfoResponse.get("name")));
        String password = userName;
        //自动注册和登录功能，此处体现了方法的复用
        return autoRegister3rdAndLogin(userName,password);
    }
    //自动登录和注册
    private String autoRegister3rdAndLogin(String userName,String password){
        //如果第三方账号登录过.直接登录，此时用户名有前缀了，不用担心重复
        if(super.checkUserExists(userName)){
            return super.login(userName,password);
        }
        //如果是第一次登录，则先注册,再登录
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(password);
        userInfo.setCreateDate(new Date());
        super.register(userInfo);
        return super.login(userName,password);
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
