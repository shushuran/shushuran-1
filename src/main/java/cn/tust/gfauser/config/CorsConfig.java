package cn.tust.gfauser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // 当前跨域请求最大有效时长。这里默认1天（86400秒）
    private static final long MAX_AGE = 86400;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*"); // 允许所有域
        corsConfiguration.addAllowedHeader("*"); // 允许所有请求头
        corsConfiguration.addAllowedMethod("*"); // 允许的方法
        corsConfiguration.setAllowCredentials(true); // 允许携带认证信息
        corsConfiguration.setMaxAge(MAX_AGE); // 缓存预检请求的结果（秒）

        source.registerCorsConfiguration("/**", corsConfiguration); // 对所有路径应用 CORS 配置
        return new CorsFilter(source);
    }
}