package com.jonas.influxdb;

import com.jonas.influxdb.entity.HostCpuUsage;
import com.jonas.influxdb.service.InfluxdbService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private InfluxdbService influxdbService;

    @Test
    public void testWrite() {
        String measurement = "host_cpu_usage";

        Map<String, String> tags = new HashMap<>();
        tags.put("host_name", "host2");
        tags.put("cpu_core", "core0");

        Map<String, Object> fields = new HashMap<>();
        fields.put("cpu_usage", 0.22);
        fields.put("cpu_idle", 0.56);
        influxdbService.write(measurement, tags, fields);
    }

    @Test
    public void testWriteObject() {
        HostCpuUsage hostCpuUsage = new HostCpuUsage();
        hostCpuUsage.setHostName("host2");
        hostCpuUsage.setCpuCore("core1");
        hostCpuUsage.setCpuUsage(0.33);
        hostCpuUsage.setCpuIdle(0.55);
        influxdbService.write(hostCpuUsage);
    }
}
