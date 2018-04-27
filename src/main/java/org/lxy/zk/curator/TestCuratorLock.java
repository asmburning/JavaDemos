package org.lxy.zk.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

@Slf4j
public class TestCuratorLock {


    @Test
    public void test() throws Exception {
        doTestInterProcessMutex(1000);
        Thread.sleep(20000);
    }

    @Test
    public void test21() throws Exception {
        doTestInterProcessMutex(20000);
        Thread.sleep(20000);
    }

    public void doTestInterProcessMutex(long lockTime) throws Exception{
        CuratorFramework cf = ZkProperties.cf;
        //3 开启连接
        cf.start();

        //4 分布式锁
        final InterProcessMutex lock = new InterProcessMutex(cf, ZkProperties.PATH_LOCK);

        new Thread(() -> {
            try {
                //加锁
                lock.acquire();
                //-------------业务处理开始
                log.info("acquire success");
                Thread.sleep(lockTime);
                //-------------业务处理结束
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    //释放
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "t").start();
    }


    @Test
    public void test2() {
        CuratorFramework curator = ZkProperties.cf;
        curator.start();
        try {
            String a = curator.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(ZkProperties.PATH_EPHEMERAL, "abc".getBytes());
            log.info("--------------------" + a);
        } catch (Exception e) {
            log.error("", e);
        }
        curator.close();
    }

    @Test
    public void test3() {
        CuratorFramework curator = ZkProperties.cf;
        curator.start();
        try {
            String a = curator.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(ZkProperties.PATH_EPHEMERAL, "abc".getBytes());
            log.info("--------------------" + a);
        } catch (Exception e) {
            log.error("", e);
        }
        curator.close();
    }
}
