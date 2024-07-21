package com.book.pay.strategy.factory;

import com.book.pay.strategy.PayStrategyInterface;
import com.book.pay.strategy.context.PayContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class PayContextFactory<T> extends AbstractPayContextFactory<PayContext> {
    //创建 Map 数据结构作为缓存存储 PayContext
    private static final Map<String,PayContext> payContexts = new ConcurrentHashMap<>();
    @Override
    public PayContext getContext(Integer payType) {
        //根据 payType定位枚举类
        StrategyEnum strategyEnum =
                payType == 1 ? StrategyEnum.alipay :
                payType == 2 ? StrategyEnum.wechat :
                null;
        if(strategyEnum == null){
            throw new UnsupportedOperationException("payType not support!");
        }
        //尝试从 Map 中获取 PayContext
        PayContext context = payContexts.get(strategyEnum.name());
        //第一次钓调用 context为null
        if(context == null){
            //通过反射，创建具体策略类
            try {
                PayStrategyInterface payStrategy = (PayStrategyInterface) Class.forName(strategyEnum.getValue()).newInstance();
                //将具体策略类作为入参，创建 payContext 类
                PayContext payContext = new PayContext(payStrategy);
                //将payContext类存储Map缓存，下次可直接使用
                payContexts.put(strategyEnum.name(), payContext);
            } catch (Exception e) {
                throw new UnsupportedOperationException("get payStrategy failed!" + e);
            }
        }
        return payContexts.get(strategyEnum.name());
    }
}
