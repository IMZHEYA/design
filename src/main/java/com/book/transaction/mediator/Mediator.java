package com.book.transaction.mediator;

import com.book.transaction.colleague.AbstractCustomer;
import com.book.transaction.colleague.Buyer;
import com.book.transaction.colleague.Payer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class Mediator extends AbstractMediator {
    private AbstractCustomer buyer;
    private AbstractCustomer payer;
    public void setBuyer(Buyer buyer){
        this.buyer = buyer;
    }
    public void setPayer(Payer payer){
        this.payer = payer;
    }
    public static Map<String, Map<String,AbstractCustomer>> customerInstance = new ConcurrentHashMap<>();
    @Override
    public void messageTransfer(String orderId, String targetCustomer, AbstractCustomer customer, String payResult) {

        if(customer instanceof Buyer){
            AbstractCustomer buyer = customerInstance.get(orderId).get("buyer");
            System.out.println("朋友代付：" + buyer.getCustomerName() + "转发orderId " + orderId + "到用户" + targetCustomer);
        } else if (customer instanceof  Payer) {
            AbstractCustomer payer = customerInstance.get(orderId).get("payer");
            System.out.println("代付完成：" + payer.getCustomerName() + "完成OrderId" + orderId + "的支付。通知" + targetCustomer + ",支付结果：" + payResult);
            customerInstance.remove(orderId);
        }
    }
}
