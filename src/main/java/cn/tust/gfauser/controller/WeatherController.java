package cn.tust.gfauser.controller;

import cn.tust.gfauser.utils.CaiyunWeatherUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final CaiyunWeatherUtil weatherUtil;

    // 构造函数注入工具类实例
    public WeatherController() {
        String apiKey = "BgUVrwfUDxkdWDri"; // 替换为你的API密钥
        this.weatherUtil = new CaiyunWeatherUtil(apiKey);
    }

    /**
     * 获取小时级别天气数据，并将其整理为每个属性一个列表的JSON格式
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 整理后的JSON数据
     */
    @GetMapping("/hourly")
    public Map<String, List<Object>> getHourlyWeather(
            @RequestParam double longitude,
            @RequestParam double latitude) {
        // 获取小时级别天气数据
        String jsonResponse = weatherUtil.getHourlyWeather(longitude, latitude);
        System.out.println("API响应: " + jsonResponse);

        // 解析并整理小时级别天气数据
        return weatherUtil.parseHourlyWeatherToJson(jsonResponse);
    }

}