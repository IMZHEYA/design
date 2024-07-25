package com.book.transaction.colleague;

import com.book.transaction.mediator.AbstractMediator;

public abstract class AbstractCustomer {
    //关联中介者
    public AbstractMediator mediator;

    public String orderId;

    public String customerName;

    AbstractCustomer(String orderId,AbstractMediator mediator,String customerName){
        this.mediator = mediator;
        this.orderId = orderId;
        this.customerName = customerName;
    }
    public String getCustomerName(){
        return this.customerName;
    }

    //定义与中介者的信息交互方法，供子类实现
    public abstract void messageTransfer(String orderId,String targetCustomer,String payResult);
}
