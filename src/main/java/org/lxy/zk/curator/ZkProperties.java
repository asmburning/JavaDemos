package org.lxy.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public interface ZkProperties {

    String ZK_CONNECT_STRING = "127.0.0.1:2181";

    int SESSION_TIMEOUT = 3000;

    String PATH = "/nodeTest";
    String PATH_LOCK = "/lock";
    String PATH_EPHEMERAL = "/EPHEMERAL";

    String CHILD_PATH_1 = "/nodeTest/test-1";
    String CHILD_PATH_2 = "/nodeTest/test-2";

    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
    //2 通过工厂创建连接
    CuratorFramework cf = CuratorFrameworkFactory.builder()
            .connectString(ZK_CONNECT_STRING)
            .sessionTimeoutMs(SESSION_TIMEOUT)
            .retryPolicy(retryPolicy)
            .build();
}
