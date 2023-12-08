package com.jonas.influxdb.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.influxdb.query.dsl.Flux;
import com.influxdb.query.dsl.functions.restriction.Restrictions;
import com.jonas.influxdb.config.InfluxdbConfig;
import com.jonas.influxdb.entity.HostCpuUsage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * InfluxdbService
 *
 * @author shenjy
 * @time 2023/12/7 16:52
 */
@Slf4j
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

    // flux-dsl
    // https://github.com/influxdata/influxdb-client-java/tree/master/flux-dsl
    public List<HostCpuUsage> query() {
        Restrictions restrictions = Restrictions.and(
                Restrictions.measurement().equal("host_cpu_usage"),
                Restrictions.value().exists()
        );

        Flux flux = Flux
                .from(influxdbConfig.getBucket())
                .range(-10L, ChronoUnit.HOURS)
                .filter(restrictions)
                .pivot()
                    .withRowKey(new String[]{"_time"})
                    .withColumnKey(new String[]{"_field"})
                    .withValueColumn("_value");
        QueryApi queryApi = influxdbClient.getQueryApi();
        log.info("flux query : {}", flux);
        return queryApi.query(flux.toString(), influxdbConfig.getOrg(), HostCpuUsage.class);
    }
}
