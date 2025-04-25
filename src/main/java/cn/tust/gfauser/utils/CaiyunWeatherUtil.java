package cn.tust.gfauser.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaiyunWeatherUtil {

    private static final String BASE_URL = "https://api.caiyunapp.com/v2.6/";
    private final String apiKey;

    // 构造函数，传入API密钥
    public CaiyunWeatherUtil(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 获取小时级别天气数据
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 返回JSON格式的小时级别天气数据
     */
    public String getHourlyWeather(double longitude, double latitude) {
        String url = buildUrl(longitude, latitude);
        return sendRequest(url);
    }

    /**
     * 解析小时级别天气数据，并将其整理为每个属性一个列表的JSON格式
     *
     * @param jsonResponse JSON格式的天气数据
     * @return 整理后的JSON数据
     */
    public Map<String, List<Object>> parseHourlyWeatherToJson(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (!jsonObject.get("status").getAsString().equals("ok")) {
            throw new RuntimeException("API请求失败: " + jsonResponse);
        }

        JsonObject resultObject = jsonObject.getAsJsonObject("result");
        JsonObject hourly = resultObject.getAsJsonObject("hourly");

        // 初始化结果Map
        Map<String, List<Object>> result = new HashMap<>();
        result.put("datetime", new ArrayList<>());
        result.put("temperature", new ArrayList<>());
        result.put("apparent_temperature", new ArrayList<>());
        result.put("wind_speed", new ArrayList<>());
        result.put("wind_direction", new ArrayList<>());
        result.put("humidity", new ArrayList<>());
        result.put("skycon", new ArrayList<>());
        result.put("skycon_zh", new ArrayList<>());

        // 解析温度
        JsonArray temperatureArray = hourly.getAsJsonArray("temperature");
        for (int i = 0; i < temperatureArray.size(); i++) {
            JsonObject tempObj = temperatureArray.get(i).getAsJsonObject();
            result.get("datetime").add(tempObj.get("datetime").getAsString());
            result.get("temperature").add(tempObj.get("value").getAsDouble());
        }

        // 解析体感温度
        JsonArray apparentTemperatureArray = hourly.getAsJsonArray("apparent_temperature");
        for (int i = 0; i < apparentTemperatureArray.size(); i++) {
            JsonObject apparentTempObj = apparentTemperatureArray.get(i).getAsJsonObject();
            result.get("apparent_temperature").add(apparentTempObj.get("value").getAsDouble());
        }

        // 解析风速和风向
        JsonArray windArray = hourly.getAsJsonArray("wind");
        for (int i = 0; i < windArray.size(); i++) {
            JsonObject windObj = windArray.get(i).getAsJsonObject();
            result.get("wind_speed").add(windObj.get("speed").getAsDouble());
            result.get("wind_direction").add(windObj.get("direction").getAsInt());
        }

        // 解析湿度
        JsonArray humidityArray = hourly.getAsJsonArray("humidity");
        for (int i = 0; i < humidityArray.size(); i++) {
            JsonObject humidityObj = humidityArray.get(i).getAsJsonObject();
            result.get("humidity").add(humidityObj.get("value").getAsDouble());
        }

        // 解析天气现象
        JsonArray skyconArray = hourly.getAsJsonArray("skycon");
        for (int i = 0; i < skyconArray.size(); i++) {
            JsonObject skyconObj = skyconArray.get(i).getAsJsonObject();
            String skycon = skyconObj.get("value").getAsString();
            result.get("skycon").add(skycon);
            result.get("skycon_zh").add(convertSkyconToChinese(skycon)); // 转换为中文描述
        }

        // 解析预警信息
        if (resultObject.has("alert")) {
            JsonObject alertObject = resultObject.getAsJsonObject("alert");
            result.put("alert_status", new ArrayList<>());
            result.put("alert_content", new ArrayList<>());
            result.put("alert_adcodes", new ArrayList<>());

            // 解析预警状态
            result.get("alert_status").add(alertObject.get("status").getAsString());

            // 解析预警内容
            JsonArray alertContentArray = alertObject.getAsJsonArray("content");
            List<Map<String, Object>> alertContentList = new ArrayList<>();
            for (int i = 0; i < alertContentArray.size(); i++) {
                JsonObject alertContentObj = alertContentArray.get(i).getAsJsonObject();
                Map<String, Object> alertContentMap = new HashMap<>();
                alertContentMap.put("province", alertContentObj.get("province").getAsString());
                alertContentMap.put("status", alertContentObj.get("status").getAsString());
                alertContentMap.put("code", alertContentObj.get("code").getAsString());
                alertContentMap.put("description", alertContentObj.get("description").getAsString());
                alertContentMap.put("regionId", alertContentObj.get("regionId").getAsString());
                alertContentMap.put("county", alertContentObj.get("county").getAsString());
                alertContentMap.put("pubtimestamp", alertContentObj.get("pubtimestamp").getAsLong());
                alertContentMap.put("latlon", gson.fromJson(alertContentObj.get("latlon"), List.class));
                alertContentMap.put("city", alertContentObj.get("city").getAsString());
                alertContentMap.put("alertId", alertContentObj.get("alertId").getAsString());
                alertContentMap.put("title", alertContentObj.get("title").getAsString());
                alertContentMap.put("adcode", alertContentObj.get("adcode").getAsString());
                alertContentMap.put("source", alertContentObj.get("source").getAsString());
                alertContentMap.put("location", alertContentObj.get("location").getAsString());
                alertContentMap.put("request_status", alertContentObj.get("request_status").getAsString());
                alertContentList.add(alertContentMap);
            }
            result.get("alert_content").add(alertContentList);

            // 解析预警区域代码
            JsonArray adcodesArray = alertObject.getAsJsonArray("adcodes");
            List<Map<String, Object>> adcodesList = new ArrayList<>();
            for (int i = 0; i < adcodesArray.size(); i++) {
                JsonObject adcodeObj = adcodesArray.get(i).getAsJsonObject();
                Map<String, Object> adcodeMap = new HashMap<>();
                adcodeMap.put("adcode", adcodeObj.get("adcode").getAsInt());
                adcodeMap.put("name", adcodeObj.get("name").getAsString());
                adcodesList.add(adcodeMap);
            }
            result.get("alert_adcodes").add(adcodesList);
        }

        return result;
    }
    /**
     * 将天气现象代码转换为中文描述
     *
     * @param skycon 天气现象代码
     * @return 中文描述
     */
    private String convertSkyconToChinese(String skycon) {
        switch (skycon) {
            case "CLEAR_DAY":
                return "晴（白天）";
            case "CLEAR_NIGHT":
                return "晴（夜间）";
            case "PARTLY_CLOUDY_DAY":
                return "多云（白天）";
            case "PARTLY_CLOUDY_NIGHT":
                return "多云（夜间）";
            case "CLOUDY":
                return "阴";
            case "LIGHT_HAZE":
                return "轻度雾霾";
            case "MODERATE_HAZE":
                return "中度雾霾";
            case "HEAVY_HAZE":
                return "重度雾霾";
            case "LIGHT_RAIN":
                return "小雨";
            case "MODERATE_RAIN":
                return "中雨";
            case "HEAVY_RAIN":
                return "大雨";
            case "STORM_RAIN":
                return "暴雨";
            case "FOG":
                return "雾";
            case "LIGHT_SNOW":
                return "小雪";
            case "MODERATE_SNOW":
                return "中雪";
            case "HEAVY_SNOW":
                return "大雪";
            case "STORM_SNOW":
                return "暴雪";
            case "DUST":
                return "浮尘";
            case "SAND":
                return "沙尘";
            case "WIND":
                return "大风";
            default:
                return "未知";
        }
    }
    /**
     * 构建API请求URL
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 完整的API请求URL
     */
    private String buildUrl(double longitude, double latitude) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + apiKey + "/" + longitude + "," + latitude + "/hourly.json").newBuilder();
        urlBuilder.addQueryParameter("lang", "zh_CN");
        urlBuilder.addQueryParameter("unit", "metric");
        urlBuilder.addQueryParameter("alert", "true");
        return urlBuilder.build().toString();
    }

    /**
     * 发送HTTP请求
     *
     * @param url 请求URL
     * @return 返回响应内容
     */
    private String sendRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("请求失败: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("请求异常: " + e.getMessage(), e);
        }
    }
}