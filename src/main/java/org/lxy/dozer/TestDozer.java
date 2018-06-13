package org.lxy.dozer;

import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.junit.Test;

@Slf4j
public class TestDozer {

    @Test
    public void test() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        TestEntity testEntity = new TestEntity();
        testEntity.setName("testEntity");
        TestItemEntity itemEntity = new TestItemEntity();
        itemEntity.setItemName("itemName");
        testEntity.setItemEntity(itemEntity);
        TestVo testVo = dozerBeanMapper.map(testEntity, TestVo.class);
        log.info("testVo:{}", testVo);
    }
}
