package com.book.items.vistor;

import com.book.items.composite.AbstractProductItem;

//vistor抽象访问者
//泛型T进行接口定义，提高代码扩展性
public interface ItemVisitor<T> {
    //定义公共的 vistor 方法供子类实现
    T vistor(AbstractProductItem productItem);

}
