package com.book.dutychain.builder;

public enum HandlerEnum {
    //业务投放的城市
    city("com.book.dutychain.CityHandler"),
    sex("com.book.dutychain.SexHandler"),
    product("com.book.dutychain.ProductsHandler");
    String value = "";
    HandlerEnum(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }

}
