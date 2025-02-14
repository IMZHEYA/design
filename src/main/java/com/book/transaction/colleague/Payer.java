package com.book.transaction.colleague;

import com.book.transaction.mediator.AbstractMediator;

public class Payer extends AbstractCustomer {
    public Payer(String orderId, AbstractMediator mediator, String customerName) {
        super(orderId, mediator, customerName);
    }

    @Override
    public void messageTransfer(String orderId, String targetCustomer, String payResult) {
        super.mediator.messageTransfer(orderId,targetCustomer,this,payResult);
    }
}
