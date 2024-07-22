package com.book.dutychain;

import com.book.pojo.BusinessLaunch;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductHandler extends AbstractBusinessHandler {
    @Override
    public List<BusinessLaunch> processHandler(List<BusinessLaunch> launchList, String targetCity, String targetSex, String targetProduct) {
        if(launchList.isEmpty()){
            return launchList;
        }
        launchList = launchList.stream().filter(launch -> {
            String product = launch.getTargetProduct();
            if(StringUtils.isEmpty(product)){
                return true;
            }
            List<String> productList = Arrays.asList(product.split(","));
            return productList.contains(targetCity);
        }).collect(Collectors.toList());

        if(hasNextHandler()){
            return nextHandler.processHandler(launchList,targetCity,targetSex,targetProduct);
        }
        return launchList;
    }
}
