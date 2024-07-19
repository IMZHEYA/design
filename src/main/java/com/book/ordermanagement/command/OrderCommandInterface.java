package com.book.ordermanagement.command;

import com.book.pojo.Order;

public interface OrderCommandInterface {
    //执行命令
    void execute(Order order);

}
