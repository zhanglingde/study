package com.ling.zookeeper.config;

/**
 * 当zookeeper中的配置被更改后，应用或服务触发回调，修改项目中的配置
 *
 * @author zhangling 2020/10/27 14:24
 */
// 这个class是未来最关心的地方
public class MyConf {

    private String conf;

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }
}
