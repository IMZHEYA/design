package com.book.ordermanagement.statemachine;

import com.book.ordermanagement.state.OrderState;
import com.book.ordermanagement.state.OrderStateChangeAction;
import org.hibernate.annotations.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "OrderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderStateChangeAction> {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
   //覆写configure方法，进行状态机的初始化操作
    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderStateChangeAction> states) throws Exception {
        //设置订单创建成功后的初始化状态为待支付 ORDER_WAIT_PAY
        states.withStates().initial(OrderState.ORDER_WAIT_PAY)
                //将订单状态类 OrderState 中所有的状态，加载配置到状态机中
                .states(EnumSet.allOf(OrderState.class));
    }

    //配置订单状态的转换流程

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderStateChangeAction> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderState.ORDER_WAIT_PAY)
                .target(OrderState.ORDER_WAIT_SEND)
                .event(OrderStateChangeAction.PAY_ORDER)
                .and()
                .withExternal().source(OrderState.ORDER_WAIT_SEND)
                .target(OrderState.ORDER_WAIT_RECEIVE)
                .event(OrderStateChangeAction.SEND_ORDER)
                .and()
                .withExternal().source(OrderState.ORDER_WAIT_RECEIVE)
                .target(OrderState.ORDER_FINISH)
                .event(OrderStateChangeAction.RECEIVE_ORDER);
    }

    //初始化RedisStateMachinePersister
    @Bean(name = "stateMachineRedisPersister")
    public RedisStateMachinePersister<OrderState,OrderStateChangeAction> getRedisPersister(){
        RedisStateMachineContextRepository<OrderState, OrderStateChangeAction> repository = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        RepositoryStateMachinePersist<OrderState, OrderStateChangeAction> persist = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(persist);
    }

}
