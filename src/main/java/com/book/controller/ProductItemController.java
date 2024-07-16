package com.book.controller;

import com.book.items.composite.ProductComposite;
import com.book.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductItemController {
    @Autowired
    private ProductItemService productItemService;

    //功能一：前端获取商品类目信息
    @PostMapping("/fetchAllItems")
    public ProductComposite fetchAllItems(){
        return productItemService.fetchAllItems();
    }
}
