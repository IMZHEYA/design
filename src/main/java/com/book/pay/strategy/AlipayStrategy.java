package com.book.pay.strategy;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.book.pojo.Order;
import com.book.utils.Constants;

//支付宝支付策略类
public class AlipayStrategy implements PayStrategyInterface {
    @Override
    public String pay(Order order) {
        //创建客户端
        AlipayClient alipayClient = new DefaultAlipayClient(Constants.ALIPAY_GATEWAY,Constants.APP_ID,Constants.APP_PRIVATE_KEY,"JSON","UTF-8",Constants.ALIPAY_PUBLIC_KEY,Constants.SIGN_TYPE);
        //设置请求参数
        AlipayTradePagePayRequest payRequest = new AlipayTradePagePayRequest();
        payRequest.setReturnUrl(Constants.CALLBACK_URL);
        payRequest.setBizContent("{\"out_trade_no\":\"" + order.getOrderId() + "\","
                + "\"total_amount\":\"" + order.getPrice() + "\","
                + "\"subject\":\"" + "伟山育琪" + "\","
                + "\"body\":\"" + "商品描述" + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = null;
        try {
            result = alipayClient.pageExecute(payRequest,"GET").getBody();
            return result;
        } catch (AlipayApiException e) {
            throw new UnsupportedOperationException("Alipay failed" + e);
        }
    }
}
