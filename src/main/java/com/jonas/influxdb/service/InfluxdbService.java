package com.jonas.influxdb.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.jonas.influxdb.config.InfluxdbConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Map;

/**
 * InfluxdbService
 *
 * @author shenjy
 * @time 2023/12/7 16:52
 */
@Service
@RequiredArgsConstructor
public class InfluxdbService {

    private final InfluxDBClient influxdbClient;
    private final InfluxdbConfig influxdbConfig;

    /**
     * 插入数据
     *
     * @param measurement 表
     * @param tags        标签
     * @param fields      字段
     */
    public void write(String measurement, Map<String, String> tags, Map<String, Object> fields) {
        if (StringUtils.isBlank(measurement) || CollectionUtils.isEmpty(tags) || CollectionUtils.isEmpty(fields)) {
            return;
        }
        Point point = Point
                .measurement(measurement)
                .addTags(tags)
                .addFields(fields)
                .time(Instant.now(), WritePrecision.NS);

        WriteApiBlocking writeApi = influxdbClient.getWriteApiBlocking();
        writeApi.writePoint(influxdbConfig.getBucket(), influxdbConfig.getOrg(), point);
    }

    public <M> void write(M obj) {
        WriteApiBlocking writeApi = influxdbClient.getWriteApiBlocking();
        writeApi.writeMeasurement(influxdbConfig.getBucket(), influxdbConfig.getOrg(), WritePrecision.NS, obj);
    }
}
