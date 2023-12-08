package com.jonas.influxdb.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * 数据实体结构
 *
 * @author shenjy
 * @time 2023/12/7 18:05
 */
@Getter
@Setter
@ToString
@Measurement(name = "host_cpu_usage")
public class HostCpuUsage {
    // tags
    @Column(tag = true)
    private String hostName;
    @Column(tag = true)
    private String cpuCore;

    // fields
    @Column
    private double cpuUsage;
    @Column
    private double cpuIdle;

    @Column(timestamp = true)
    private Instant time;
}
