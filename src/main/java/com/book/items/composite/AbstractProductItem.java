package com.book.items.composite;

public abstract class AbstractProductItem {
    //增加商品类目
    public void addProductItem(AbstractProductItem item){
        throw new UnsupportedOperationException("Not Support child add!");
    }

    //移除商品类目
    public void delProductChild(AbstractProductItem item){
        throw new UnsupportedOperationException("Not Support child remove!");
    }
}
