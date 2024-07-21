package com.book.pay.strategy.context;

import com.book.pojo.Order;

public abstract class AbstractPayContext {
    public abstract String execute(Order order);
}
