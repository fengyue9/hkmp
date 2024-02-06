package com.ruoyi.web.controller.weather;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 天气控制器
 *
 * @author hongrongjian
 * @date 2023/10/17
 */
@RestController
public class WeatherController {

    /**
     * 获取天气
     *
     * @return {@code AjaxResult }
     * @throws UnsupportedEncodingException
     * @author hongrongjian
     * @date 2023/10/17
     */
    @GetMapping("/getWeatherByLocalIP")
    public AjaxResult getWeather() throws UnsupportedEncodingException {
        AjaxResult result = AjaxResult.success();
        //获取当前ip所在城市名
        String localCityName = GetLocationAndIP.getLocalCityName();
        //调用天气API
        String encodeCity = URLEncoder.encode(localCityName, "UTF-8");
        System.out.println(encodeCity);
        String url = "http://apis.juhe.cn/simpleWeather/query?city=" + encodeCity + "&key=81fe33a6077267b2e4ae2967af47eeb7";
        String weatherInfo = HttpUtils.sendGet(url);
        result.put("msg", weatherInfo);
        return result;
    }

}