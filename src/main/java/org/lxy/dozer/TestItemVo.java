package org.lxy.dozer;

import lombok.Data;
import org.dozer.Mapping;

@Data
public class TestItemVo {
    @Mapping("itemName")
    private String itemVoName;
}
