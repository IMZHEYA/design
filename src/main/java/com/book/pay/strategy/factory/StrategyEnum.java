package com.book.pay.strategy.factory;

public enum StrategyEnum {
    alipay("com.book.pay.strategy.AlipayStrategy"),
    wechat("com.book.pay.strategy.WechatStrategy");
    String value = "";
    StrategyEnum(String value){
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
