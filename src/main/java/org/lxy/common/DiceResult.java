package org.lxy.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Eric.Liu on 2016/11/22.
 */
@Getter
@Setter
@ToString
public class DiceResult {
    private int start;
    private int end;

    public DiceResult(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
