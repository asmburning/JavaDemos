package org.lxy.effective.ch6.item37;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class EnumMapTest {

    @Test
    public void test() {
        Transition transition = Transition.from(Phase.GAS, Phase.LIQUID);
        log.info(transition.name());
    }
}
