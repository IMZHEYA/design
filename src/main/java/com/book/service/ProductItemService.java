package com.book.service;

import com.book.items.composite.AbstractProductItem;
import com.book.items.composite.ProductComposite;
import com.book.pojo.ProductItem;
import com.book.repo.ProductItemRepository;
import com.book.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//主要负责查询的逻辑
@Service
public class ProductItemService {
    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private ProductItemRepository productItemRepository;

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
}