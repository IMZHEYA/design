package com.book.items.vistor;

import com.book.items.composite.AbstractProductItem;
import com.book.items.composite.ProductComposite;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DelItemVisitor<T> implements ItemVisitor<AbstractProductItem> {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Override
    public AbstractProductItem vistor(AbstractProductItem productItem) {
        //从redis中获取到树形结构
        ProductComposite currentItem = (ProductComposite)redisCommonProcessor.get("items");
        //需要删除的商品类目
        ProductComposite delItem = (ProductComposite) productItem;
        if(delItem.getId() == currentItem.getId()){
            throw new UnsupportedOperationException("根节点不能删");
        }
        //如果删除节点的父节点为当前节点，则直接删除
        if(delItem.getPid() == currentItem.getId()){
            currentItem.delProductChild(delItem);
            return currentItem;
        }
        //否则，通过addChild方法进行递归寻找新增类目的插入点
        delChild(delItem,currentItem);
        return currentItem;
    }

    private void delChild(ProductComposite delItem, ProductComposite currentItem) {
        for(AbstractProductItem abstractItem : currentItem.getChild()){
            ProductComposite item = (ProductComposite) abstractItem;
            if(item.getId() == delItem.getPid()){
                item.delProductChild(delItem);
                break;
            }else{
                delChild(delItem,item);
            }
        }
    }
}
