package com.book.items.vistor;

import com.book.items.composite.AbstractProductItem;
import com.book.items.composite.ProductComposite;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddItemVisitor<T> implements ItemVisitor<AbstractProductItem> {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Override
    public AbstractProductItem vistor(AbstractProductItem productItem) {
        //从redis中获取到树形结构
        ProductComposite currentItem = (ProductComposite)redisCommonProcessor.get("items");
        //需要新增的商品类目
        ProductComposite addItem = (ProductComposite) productItem;
        //如果新增节点的父节点为当前节点，则直接添加
        if(addItem.getPid() == currentItem.getId()){
            currentItem.addProductItem(addItem);
            return currentItem;
        }
        //否则，通过addChild方法进行递归寻找新增类目的插入点
        addChild(addItem,currentItem);
        return currentItem;
    }

    private void addChild(ProductComposite addItem, ProductComposite currentItem) {
        for(AbstractProductItem abstractItem : currentItem.getChild()){
            ProductComposite item = (ProductComposite) abstractItem;
            if(item.getId() == addItem.getPid()){
                item.addProductItem(addItem);
                break;
            }else{
                addChild(addItem,item);
            }
        }
    }

}
