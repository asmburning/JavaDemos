package org.lxy.utils.pdf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业交易表
 * 只有在 SAVED 和 DENY 状态下,操作者可以修改交易信息
 */
@Data
public class Trade {
    private String id;

    private String orgCode; // 机构编码

    private Date createTime;

    private String operatorId;// 经办人id

    private Date operateTime;// 申请时间

    private String auditorId; // 审核人id

    private Date auditTime; // 审核时间

    private Date updateTime;

    private String tradeMessage;// 申办信息

    private String auditMessage; // 审核信息

    private String buyFundCode; // 购买基金代码

    private String buyFundName; // 购买基金名称

    private Integer buyFundRisk;// 购买基金风险等级

    private Integer orgRisk; // 机构风险等级

    private boolean riskOver = false;  // 交易风险是否超过机构风险

    private boolean currency; // 是否货币基金; 申购的基金 认购的基金 以及转换的目标基金 是不是货币基金

    private int payType;// 支付方式

    private BigDecimal buyTotal;// 购买总金额

    private String buyFeeType;

    private String ransomFundCode;// 赎回基金名称

    private String ransomFundName;

    private String ransomFeeType;

    private String mainStatus; // 状态

    /**
     * 主交易类型
     * 申购 对应 TradeApply
     * 认购 对应 TradeClaim
     * 赎回 对应 TradeRansom
     * 赎转申购 先 TradeRansom,后 TradeApply
     * 赎转认购 先 TradeRansom,后 TradeClaim , 巨额赎回认购时
     * 转换 对应 TradeTransfer
     *
     *
     */
    private String mainTradeType; //

    // 巨额赎回交易时 需要记录更多信息
    private boolean huge;//是否巨额赎回转换

    private BigDecimal ransomApplyShare;// 申请赎回的份额

    private BigDecimal ransomedShare;// 赎回成功的份额

    private BigDecimal boughtShare;// 购买成功份额

    private String remark;

    private String proofFileName; // 汇款凭证文件名

    private String proofOssKey; // 汇款凭证ossKey

    private boolean exported; // 是否导出到Excel

    private Date exportTime;// 导出时间

    private boolean matched = false; // 是否匹配成功过

    private Date matchTime;// 导出时间

    private boolean audited = false;

    private String searchString;

    private boolean payed = false; // 支付标记

    private boolean allPayed = false;//全部支付标记位

    private Date payTime;  // 支付时间

    private BigDecimal payTotal; // 支付金额

    // since 2017-05-27
    private String hugeRansomType; // 巨额赎回方式

    private String bonusType; // 分红方式

    private String riskMailBid;
}
