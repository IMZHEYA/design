package com.book.bridge.abst.factory;

import com.book.bridge.abst.AbstractRegisterLoginComponent;
import com.book.bridge.abst.RegisterLoginComponent;
import com.book.bridge.function.RegisterLoginByDefault;
import com.book.bridge.function.RegisterLoginFuncInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegisterLoginComponentFactory {
    //缓存左路AbstractRegisterLoginComponent，根据不同的登录方式进行缓存
    public static Map<String, AbstractRegisterLoginComponent> componentMap = new ConcurrentHashMap<>();

    //缓存不同类型的实现类（右路），如RegisterLoginByDefault
    public static Map<String, RegisterLoginFuncInterface> funcMap = new ConcurrentHashMap<>();

    //根据不同的类型，获取AbstractRegisterLoginComponent
    public static AbstractRegisterLoginComponent getComponent(String type) {
        AbstractRegisterLoginComponent component = componentMap.get(type);
        if(component == null){
            //并发情况下，汲取双重检查锁机制的设计，如果componentMap没有，则进行创建
            synchronized (componentMap){
                component = componentMap.get(type);
                if(component == null){
                    component = new RegisterLoginComponent(funcMap.get(type));
                    componentMap.put(type,component);
                }
            }
        }
        return component;
    }

}
