package com.book.service.decorator;

import com.book.pojo.Order;
import com.book.pojo.Products;
import com.book.repo.ProductsRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceDecorator extends AbstractOrderServiceDecorator {
    //apollo 配置中心的消息超时时间
    @Value("${delay.service.time}")
    private String delayServiceTime;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    protected void updateScoreAndSendRedPaper(String productId, int serviceLevel, float price) {
        switch (serviceLevel){
            case 0:
                //根据价格的百分之一更新积分
                int score = Math.round(price) / 100;
                System.out.println("正常处理，为用户更新积分！ score = " + score );
                //根据商品属性发放红包
                Products product = productsRepository.findByProductId(productId);
                if(product != null && product.getSendRedBag() == 1){
                    System.out.println("正常处理，为用户发放红包！" + productId);
                    break;
                }
            case 1:
                MessageProperties properties = new MessageProperties();
                //设置消息过期时间
                properties.setExpiration(delayServiceTime);
                Message msg = new Message(productId.getBytes(),properties);
                //向正常队列中发送消息
                rabbitTemplate.send("normalExchange","myRkey",msg);
                System.out.println("延迟处理，时间=" + delayServiceTime);
                break;
            case 2:
                System.out.println("暂停服务！");
                break;
            default:
                throw new UnsupportedOperationException("不支持的服务级别！");
        }
    }

    //将pay方法与updateScoreAndSendRedPaper 方法进行逻辑结合
    public Order decoratorPay(String orderId,int serviceLevel, float price){
        Order order = super.pay(orderId);
        try{
            this.updateScoreAndSendRedPaper(order.getProductId(), serviceLevel,price);

        }catch (Exception e){
            //重试机制，此处积分更新不能影响支付主流程
        }
        return order;
    }
}
