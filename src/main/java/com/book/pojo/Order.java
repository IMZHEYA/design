package com.book.pojo;

import com.book.ordermanagement.state.OrderState;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
/**
 * 订单对象类
 */
public class Order {
    private String orderId;
    private String productId;
    private OrderState orderState;
    private Float price;
}
