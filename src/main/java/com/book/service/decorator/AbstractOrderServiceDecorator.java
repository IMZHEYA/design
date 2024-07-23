package com.book.service.decorator;

import com.book.pojo.Order;
import com.book.service.inter.OrderServiceInterface;
//实现 OrderServiceInterface
public abstract class AbstractOrderServiceDecorator implements OrderServiceInterface {
    //关联OrderServiceInterface
    private OrderServiceInterface orderServiceInterface;
    //set初始化
    public void setOrderServiceInterface(OrderServiceInterface orderServiceInterface){
        this.orderServiceInterface = orderServiceInterface;
    }

    @Override
    public Order createOrder(String productId) {
        return this.orderServiceInterface.createOrder(productId);
    }

    @Override
    public Order pay(String orderId) {
        return this.orderServiceInterface.pay(orderId);
    }

    @Override
    public Order send(String orderId) {
        return this.orderServiceInterface.send(orderId);
    }

    @Override
    public Order receive(String orderId) {
        return this.orderServiceInterface.receive(orderId);
    }

    @Override
    public String getPayUrl(String orderId, Float price, Integer payType) {
        return this.orderServiceInterface.getPayUrl(orderId,price,payType);
    }
    //定义新的方法，根据userId,productId更新用户积分，发放红包
    protected abstract void updateScoreAndSendRedPaper(String productId,int serviceLevel,float price);

}
