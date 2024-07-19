package com.book.ordermanagement.command.invoker;

import com.book.ordermanagement.command.OrderCommandInterface;
import com.book.pojo.Order;

public class OrderCommandInvoker {
    public void invoke(OrderCommandInterface command, Order order){
        //调用命令角色的 execute 方法
        command.execute(order);
    }
}
