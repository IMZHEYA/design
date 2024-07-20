package com.book.pay.facade;

import com.book.pay.strategy.AlipayStrategy;
import com.book.pay.strategy.WechatStrategy;
import com.book.pay.strategy.context.PayContext;
import com.book.pojo.Order;
import org.springframework.stereotype.Component;

@Component
public class PayFacade {
    public String pay(Order order,Integer payType){
        switch (payType){
            //支付宝支付类型
            case 1 :
                AlipayStrategy alipayStrategy = new AlipayStrategy();
                PayContext alipayContext = new PayContext(alipayStrategy);
                return alipayContext.execute(order);
            //微信支付类型
            case 2 :
                WechatStrategy wechatStrategy = new WechatStrategy();
                PayContext wechatContext = new PayContext(wechatStrategy);
                return wechatContext.execute(order);
            default:
                throw new UnsupportedOperationException("paytype not supported");
        }
    }
}
