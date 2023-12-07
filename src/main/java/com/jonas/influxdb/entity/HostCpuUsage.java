package com.jonas.influxdb.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;

/**
 * 数据实体结构
 *
 * @author shenjy
 * @time 2023/12/7 18:05
 */
@Data
@Measurement(name = "host_cpu_usage")
public class HostCpuUsage {
    // tags
    @Column(tag = true)
    private String hostName;
    @Column(tag = true)
    private String cpuCore;

    // fields
    @Column
    private Double cpuUsage;
    @Column
    private Double cpuIdle;

    @Column(timestamp = true)
    private Instant time;
}
