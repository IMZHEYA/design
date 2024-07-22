package com.book.service;

import com.book.dutychain.AbstractBusinessHandler;
import com.book.dutychain.CityHandler;
import com.book.dutychain.builder.HandlerEnum;
import com.book.pojo.BusinessLaunch;
import com.book.pojo.UserInfo;
import com.book.repo.BusinessLaunchRepository;
import com.book.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserRepository userRepository;
    //查询业务投放数据
    @Autowired
    private BusinessLaunchRepository businessLaunchRepository;
    //注入duty.chain
    @Value("${duty.chain}")
    private String handlerType;
    //记录当前的handlerType的配置，判断duty.chain的配置是否有修改
    private String currentHandlerType;
    //记录当前的责任链头节点，如果配置没有修改，下次直接返回即可
    private AbstractBusinessHandler currentHandler;

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

    public List<BusinessLaunch> filterBusinessLaunch(String city,String sex,String product)  {
        List<BusinessLaunch> launchList = businessLaunchRepository.findAll();
        return builderChain().processHandler(launchList,city,sex,product);
    }

    //组装责任链条并返回责任链条首节点


    private AbstractBusinessHandler builderChain()  {
        //如果没有配置，直接返回 null
        if(handlerType == null){
            return null;
        }
        //如果是第一次配置，将hanlerType 记录下来
        if(currentHandlerType == null){
            this.currentHandlerType = this.handlerType;
        }
        //配置未修改且currentHandler 不为null ，直接返回
        if(this.handlerType.equals(currentHandlerType) && this.currentHandler != null){
            return this.currentHandler;
        }else{

            System.out.println("配置有修改或首次初始化，组装责任链条！");
            synchronized (this){
                //创建哑结点，随意找一个类型创建即可
                AbstractBusinessHandler dummyHeadHandler = new CityHandler();
                //创建前置结点，初始赋值为哑结点
                AbstractBusinessHandler preHandler = dummyHeadHandler;
                //将duty.chain的配置用逗号分割为List类型，并通过HandlerEnum创建责任类，并配置责任链条
                List<String> handlerTypeList = Arrays.asList(handlerType.split(","));
                for(String hanlerType : handlerTypeList){
                    AbstractBusinessHandler handler = null;
                    try {
                        handler = (AbstractBusinessHandler) Class.forName(HandlerEnum.valueOf(hanlerType).getValue()).newInstance();
                    } catch (Exception e) {
                        throw new UnsupportedOperationException(e);
                    }
                    preHandler.nextHandler = handler;
                    preHandler = handler;
                }
                //重新赋值新的责任链头节点
                this.currentHandler = dummyHeadHandler.nextHandler;
                //重新赋值修改后的配置
                this.currentHandlerType = this.handlerType;
                //返回责任链头结点
                return currentHandler;
            }
        }

    }
}
