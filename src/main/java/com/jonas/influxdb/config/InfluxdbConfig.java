package com.jonas.influxdb.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * InfluxdbConfig
 *
 * @author shenjy
 * @time 2023/12/7 16:50
 */
@Getter
@Configuration
public class InfluxdbConfig {
    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Value("${influxdb.token}")
    private String token;

    @Value("${influxdb.org}")
    private String org;

    @Bean
    public InfluxDBClient influxDB() {
        return InfluxDBClientFactory.create(url, token.toCharArray());
    }
}
