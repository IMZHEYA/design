package com.book.dutychain;

import com.book.pojo.BusinessLaunch;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CityHandler extends AbstractBusinessHandler {
    @Override
    public List<BusinessLaunch> processHandler(List<BusinessLaunch> launchList, String targetCity, String targetSex, String targetProduct) {
        if(launchList.isEmpty()){
            return launchList;
        }
        launchList = launchList.stream().filter(launch -> {
            String city = launch.getTargetCity();
            if(StringUtils.isEmpty(city)){
                return true;
            }
            List<String> cityList = Arrays.asList(city.split(","));
            return cityList.contains(targetCity);
        }).collect(Collectors.toList());

        if(hasNextHandler()){
            return nextHandler.processHandler(launchList,targetCity,targetSex,targetProduct);
        }
        return launchList;
    }
}
