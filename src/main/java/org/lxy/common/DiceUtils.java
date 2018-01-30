package org.lxy.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric.Liu on 2016/11/22.
 */
@Slf4j
public class DiceUtils {
    /**
     * 数据分片工具 , 下标从0开始
     *
     * @param total     数据总数
     * @param diceLimit 每个分片最多接受数据量
     * @return
     */
    public static List<DiceResult> diceByTotal(int total, int diceLimit) {
        return diceByTotal(total, diceLimit, 0);
    }

    /**
     * 数据分片工具 , 下标从(0+offset)开始
     *
     * @param total     数据总数
     * @param diceLimit 每个分片最多接受数据量
     * @param offset    下标偏移量
     * @return
     */
    public static List<DiceResult> diceByTotal(int total, int diceLimit, int offset) {
        List<DiceResult> results = new ArrayList<>();
        if (total < 1 || diceLimit < 1) {
            return results;
        } else if (total < diceLimit) {
            results.add(new DiceResult(0 + offset, total - 1 + offset));
        }
        int diceCount = (total - 1) / diceLimit + 1;
        int start = offset;
        int end = start;
        for (int i = 1; i < diceCount; i++) {
            end = start + diceLimit - 1;
            results.add(new DiceResult(start, end));
            start = end + 1;
        }
        end = start + total - (diceCount - 1) * diceLimit - 1;
        results.add(new DiceResult(start, end));
        return results;
    }

    /**
     * @param start     开始
     * @param end       结束
     * @param diceLimit 每个分片最多接受数据量
     * @return 分片结果
     */
    public static List<DiceResult> diceByStartEnd(int start, int end, int diceLimit) {
        return diceByTotal(end - start, diceLimit, start);
    }

    @Test
    public void test() {
        List<DiceResult> diceResults = diceByTotal(23, 10);
        log.info("diceResults : {} ", diceResults);
    }
}
