package com.book.bridge.function;

import com.alibaba.fastjson.JSONObject;
import com.book.pojo.UserInfo;
import com.book.repo.UserRepository;
import com.book.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class RegisterLoginByGitee implements RegisterLoginFuncInterface {
    @Value("${gitee.state}")
    private String giteeState;

    @Value("${gitee.token.url}")
    private String giteeTokenUrl;

    @Value("${gitee.user.url}")
    private String giteeUserUrl;

    @Value("${gitee.user.prefix}")
    private String giteeUserPrefix;
    @Resource
    private UserRepository userRepository;


    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account, password);
        if (userInfo == null) {
            return "account / password error";
        }
        return "login success";
    }

    public String register(UserInfo userInfo) {
        if (checkUserExists(userInfo.getUserName())) {
            throw new RuntimeException("user already registered");
        }
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "register success!";
    }

    //根据用户账号名称检查用户是否已注册
    public boolean checkUserExists(String userName) {
        UserInfo user = userRepository.findByUserName(userName);
        if (user == null) {
            return false;
        }
        return true;
    }

    @Override
    public String login3rd(HttpServletRequest request) {
        //入参为HttpServletRequest 这样方法不仅可以为Gitee平台进行功能实现，还能够为其他的第三方平台进行实现
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        //进行state判断，state值是前端后端商定
        if (!giteeState.equals(state)) {
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
        return autoRegister3rdAndLogin(userName, password);
    }

    //自动登录和注册
    private String autoRegister3rdAndLogin(String userName, String password) {
        //如果第三方账号登录过.直接登录，此时用户名有前缀了，不用担心重复
        if (checkUserExists(userName)) {
            return login(userName, password);
        }
        //如果是第一次登录，则先注册,再登录
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(password);
        userInfo.setCreateDate(new Date());
        register(userInfo);
        return login(userName, password);
    }
}