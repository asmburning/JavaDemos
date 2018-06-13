package org.lxy.dozer;

import lombok.Data;
import org.dozer.Mapping;

@Data
public class TestVo {
    private String name;
    @Mapping("itemEntity")
    private TestItemVo itemVo;
}
