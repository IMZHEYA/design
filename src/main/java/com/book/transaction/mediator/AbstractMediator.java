package com.book.transaction.mediator;

import com.book.transaction.colleague.AbstractCustomer;

public abstract class AbstractMediator {
    public abstract void messageTransfer(String orderId, String targetCustomer, AbstractCustomer customer, String payResult);
}
