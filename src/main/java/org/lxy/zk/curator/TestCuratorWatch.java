package org.lxy.zk.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.junit.Test;

import static org.lxy.zk.curator.ZkProperties.*;

/**
 * only for zookeeper 3.5.X +
 */
@Slf4j
public class TestCuratorWatch {

    @Test
    public void testNodeCacheWatcher() throws Exception {
        CuratorFramework cf = ZkProperties.cf;
        //3 建立连接
        cf.start();

        //4 建立一个cache缓存
        final NodeCache cache = new NodeCache(cf, PATH, false);
        cache.start(true);
        cache.getListenable().addListener(() -> {
                    System.out.println("路径为：" + cache.getCurrentData().getPath());
                    System.out.println("数据为：" + new String(cache.getCurrentData().getData()));
                    System.out.println("状态为：" + cache.getCurrentData().getStat());
                    System.out.println("---------------------------------------");
                }
        );

        if (cf.checkExists().forPath(PATH) == null) {
            log.info("path not exists");
            Thread.sleep(1000);
            cf.create().forPath(PATH, "123".getBytes());
        }

        Thread.sleep(1000);
        cf.setData().forPath(PATH, "456".getBytes());

        Thread.sleep(1000);
        cf.delete().forPath(PATH);

        Thread.sleep(20000);
    }

    @Test
    public void testChildrenCacheWatcher() throws Exception {
        CuratorFramework cf = ZkProperties.cf;
        //3 建立连接
        cf.start();
        //4 建立一个PathChildrenCache缓存,第三个参数为是否接受节点数据内容 如果为false则不接受
        PathChildrenCache cache = new PathChildrenCache(cf, PATH, true);
        //5 在初始化的时候就进行缓存监听
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((curatorFramework, event) -> {
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("新增了子节点 :" + event.getData().getPath());
                            break;
                        case CHILD_UPDATED:
                            System.out.println("修改了子节点 :" + event.getData().getPath());
                            break;
                        case CHILD_REMOVED:
                            System.out.println("删除了子节点 :" + event.getData().getPath());
                            break;
                        default:
                            break;
                    }
                }
        );

        //创建本身节点不发生变化
        cf.create().forPath(PATH, "init".getBytes());

        //添加子节点
        Thread.sleep(1000);
        cf.create().forPath(CHILD_PATH_1, "c1内容".getBytes());
        Thread.sleep(1000);
        cf.create().forPath(CHILD_PATH_2, "c2内容".getBytes());

        //修改子节点
        Thread.sleep(1000);
        cf.setData().forPath(CHILD_PATH_1, "c1更新内容".getBytes());

        //删除子节点
        Thread.sleep(1000);
        cf.delete().forPath(CHILD_PATH_2);

        //删除本身节点
        Thread.sleep(1000);
        cf.delete().deletingChildrenIfNeeded().forPath(PATH);

        Thread.sleep(20000);
    }
}
