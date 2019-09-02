package org.lxy.utils.pdf;

import lombok.Getter;

/**
 * 交易状态
 */
@Getter
public enum TradeMainTypeEnum {

    APPLY_BUY("APPLY_BUY", "申购"),
    CLAIM_BUY("CLAIM_BUY", "认购"),
    RANSOM("RANSOM", "赎回"),
    TRANSFER("TRANSFER", "转换"),
    RANSOM_APPLY("RANSOM_APPLY", "赎回转申购"),
    RANSOM_CLAIM("RANSOM_CLAIM", "赎回转认购"),;

    TradeMainTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private String type;
    private String desc;
}
