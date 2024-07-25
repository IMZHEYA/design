package com.book.service;

import com.book.ordermanagement.command.OrderCommand;
import com.book.ordermanagement.command.invoker.OrderCommandInvoker;
import com.book.ordermanagement.state.OrderState;
import com.book.ordermanagement.state.OrderStateChangeAction;
import com.book.pay.facade.PayFacade;
import com.book.pojo.Order;
import com.book.service.inter.OrderServiceInterface;
import com.book.transaction.colleague.AbstractCustomer;
import com.book.transaction.colleague.Buyer;
import com.book.transaction.colleague.Payer;
import com.book.transaction.mediator.Mediator;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OrderService implements OrderServiceInterface {
    //注入状态机
    @Autowired
    private StateMachine<OrderState, OrderStateChangeAction> orderStateMachine;

    //注入RedisPersister 存取工具，持久化状态机
    @Autowired
    private StateMachinePersister<OrderState, OrderStateChangeAction, String> stateMachineRedisPersister;


    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private OrderCommand orderCommand;

    @Autowired
    private PayFacade payFacade;

    @Autowired
    private Mediator mediator;

    public Order createOrder(String productId) {
        String orderId = "OID" + productId;
        Order order = Order.builder()
                .orderId(orderId)
                .productId(productId)
                .orderState(OrderState.ORDER_WAIT_PAY)
                .build();
        redisCommonProcessor.set(order.getOrderId(), order, 900);
        //命令模式融入
        OrderCommandInvoker invoker = new OrderCommandInvoker();
        invoker.invoke(orderCommand,order);
        return order;
    }

    //订单支付：有雷
    public Order pay(String orderId) {
        //从Redis中获取订单
        Order order = (Order) redisCommonProcessor.get(orderId);
        //包装订单状态变更Message,并附带订单操作PAY_ORDER
        Message message = MessageBuilder
                .withPayload(OrderStateChangeAction.PAY_ORDER)
                .setHeader("order",order)
                .build();
        //将Message传递给 Spring 状态机
        if(changeStateAction(message,order)){
            return order;
        }
        return null;
    }


    public Order send(String orderId) {
        //从Redis中获取订单
        Order order = (Order) redisCommonProcessor.get(orderId);
        //包装订单状态变更Message,并附带订单操作PAY_ORDER
        Message message = MessageBuilder
                .withPayload(OrderStateChangeAction.SEND_ORDER)
                .setHeader("order",order)
                .build();
        //将Message传递给 Spring 状态机
        if(changeStateAction(message,order)){
            return order;
        }
        return null;
    }

    public Order receive(String orderId) {
        //从Redis中获取订单
        Order order = (Order) redisCommonProcessor.get(orderId);
        //包装订单状态变更Message,并附带订单操作PAY_ORDER
        Message message = MessageBuilder
                .withPayload(OrderStateChangeAction.RECEIVE_ORDER)
                .setHeader("order",order)
                .build();
        //将Message传递给 Spring 状态机
        if(changeStateAction(message,order)){
            return order;
        }
        return null;
    }
    //状态机的相关操作
    private boolean changeStateAction(Message<OrderStateChangeAction> message,Order order){

        //启动状态机
        orderStateMachine.start();

        //从Redis中读取状态机，缓存的key为orderId + "STATE",自定义
        try {
            stateMachineRedisPersister.restore(orderStateMachine,order.getOrderId() + "STATE");
            //将Message发送给OrderStateListener
            boolean res = orderStateMachine.sendEvent(message);
            //将更改完订单状态的 状态机 存储到Redis 缓存
            stateMachineRedisPersister.persist(orderStateMachine,order.getOrderId() + "STATE");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            orderStateMachine.stop();
        }
       return false;
    }
    //支付调用逻辑
    public String getPayUrl(String orderId, Float price, Integer payType) {
        Order order = (Order) redisCommonProcessor.get(orderId);
        order.setPrice(price);
        return payFacade.pay(order,payType);
    }

    public void friendPay(String sourceCustomer, String orderId, String targetCustomer, String payResult, String role) {

        Buyer buyer = new Buyer(orderId,mediator,sourceCustomer);
        Payer payer = new Payer(orderId,mediator,sourceCustomer);
        HashMap<String, AbstractCustomer> map = new HashMap<>();
        map.put("buyer",buyer);
        map.put("payer",payer);
        mediator.customerInstance.put(orderId,map);
        if(role.equals("B")){
            buyer.messageTransfer(orderId,targetCustomer,payResult);
        } else if (role.equals("P")) {
            payer.messageTransfer(orderId,targetCustomer,payResult);
        }
    }
}
