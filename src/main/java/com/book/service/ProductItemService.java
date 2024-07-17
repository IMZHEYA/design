package com.book.service;

import com.book.items.composite.AbstractProductItem;
import com.book.items.composite.ProductComposite;
import com.book.items.vistor.AddItemVisitor;
import com.book.items.vistor.DelItemVisitor;
import com.book.pojo.ProductItem;
import com.book.repo.ProductItemRepository;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//主要负责查询的逻辑
@Service
@Transactional // 事务
public class ProductItemService {
    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private AddItemVisitor addItemVisitor;

    @Autowired
    private DelItemVisitor delItemVisitor;
    //获取商品类目信息
    public ProductComposite fetchAllItems() {
        //先查询redis缓存，如果不为null,直接返回即可
        Object cacheItems = redisCommonProcessor.get("items");
        if (cacheItems != null) {
            return (ProductComposite) cacheItems;
        }

        //如果redis缓存为null则查询DB，调用findAll方法获取所有商品类目
        List<ProductItem> fetchDbItems = productItemRepository.findAll();

        //将DB中的商品类目信息拼接成组合模式的树形结构
        ProductComposite items = generateProductTree(fetchDbItems);
        if (items == null) {
            throw new UnsupportedOperationException("Product items should not be empty in DB !");
        }
        //将商品类目信息设置到redis缓存中，下次查询可直接通过Redis缓存获取到
        redisCommonProcessor.set("items", items);
        return items;
    }

    //将DB中的商品类目信息组装成树形机构：组合模式中的ProductComposite
    private ProductComposite generateProductTree(List<ProductItem> fetchDbItems) {
        ArrayList<ProductComposite> composites = new ArrayList<>(fetchDbItems.size());
        fetchDbItems.forEach(dbItem ->
                composites.add(ProductComposite.builder()
                        .id(dbItem.getId())
                        .name(dbItem.getName())
                        .pid(dbItem.getPid())
                        .build()));
        Map<Integer, List<ProductComposite>> groupingList = composites.stream().collect(Collectors.groupingBy(ProductComposite::getPid));
        composites.stream().forEach(item -> {
            List<ProductComposite> list = groupingList.get(item.getId());
            item.setChild(list == null ? new ArrayList<>() : list.stream().map(x -> (AbstractProductItem) x).collect(Collectors.toList()));
        });
        ProductComposite composite = composites.size() == 0 ? null : composites.get(0);
        return composite;
    }

    //添加商品类目
    public ProductComposite addItems(ProductItem item){
        //先更新数据库
        productItemRepository.addItem(item.getName(), item.getPid());
        //访问者模式访问树形数据结构，并添加新的商品类目
        ProductComposite addItem = ProductComposite.builder()
                .id(productItemRepository.findByNameAndPid(item.getName(), item.getPid()).getId())
                .name(item.getName())
                .pid(item.getPid())
                .child(new ArrayList<>())
                .build();
        AbstractProductItem updatedItems = addItemVisitor.vistor(addItem);

        //更新Redis缓存，此处可以做重试机制，如果重试不成功，可人工介入
        redisCommonProcessor.set("items",updatedItems);
        return (ProductComposite) updatedItems;
     }



    //删除商品类目
    public ProductComposite delItems(ProductItem item){
        //先更新数据库
        productItemRepository.delItem(item.getId());
        //访问者模式访问树形数据结构，并添加新的商品类目
        ProductComposite delItem = ProductComposite.builder()
                .id(item.getId())
                .name(item.getName())
                .pid(item.getPid())
                .build();
        AbstractProductItem updatedItems = delItemVisitor.vistor(delItem);

        //更新Redis缓存，此处可以做重试机制，如果重试不成功，可人工介入
        redisCommonProcessor.set("items",updatedItems);
        return (ProductComposite) updatedItems;
    }
}