### Docker 安装

    docker run --name influxdb -v /Users/shenjiayun/Documents/workspace/docker/influxdb/data:/var/lib/influxdb2 -p 8086:8086 influxdb:2.7.4

### 控制台

<http://localhost:8086/>

用户名密码：`root/12345678`

### 参考资料

[Install InfluxDB](https://docs.influxdata.com/influxdb/v2/install/?t=Docker)
