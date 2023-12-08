package com.jonas.influxdb;

import com.jonas.influxdb.entity.HostCpuUsage;
import com.jonas.influxdb.service.InfluxdbService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    @Test
    public void testWriteObjects() {
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            HostCpuUsage hostCpuUsage = new HostCpuUsage();
            hostCpuUsage.setHostName("host3");
            hostCpuUsage.setCpuCore("core3");
            hostCpuUsage.setCpuUsage(random.nextDouble());
            hostCpuUsage.setCpuIdle(random.nextDouble());
            influxdbService.write(hostCpuUsage);

            long randomNumber = random.nextInt((5000 + 1) - 500) + 500;
            try {
                Thread.sleep(randomNumber);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    void testQuery() {
        List<HostCpuUsage> cpuUsages = influxdbService.query();
        System.out.println(cpuUsages);
    }
}
