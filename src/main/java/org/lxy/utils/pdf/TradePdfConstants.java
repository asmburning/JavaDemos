package org.lxy.utils.pdf;


import java.util.ArrayList;
import java.util.List;

public class TradePdfConstants {

    public static final String pdfTitle = "开放式基金交易类业务申请信息";

    public static final String pdfSignName = "上海凯石财富基金销售有限公司";
    public static final String pdfSignDate = "日期：";

    public static List<String> tips = new ArrayList<>();

    static {
        tips.add(" 1.请贵机构交易付款前仔细阅读《基金合同》、《招募说明书》或其他法律文件，完全了解产品风险与收益特征，");
        tips.add(" 自主决策、自担风险；");
        tips.add(" 2.本凭证仅表示我公司初步接收了贵单位的交易申请，交易申请成功受理尚需交易资金及时到账且满足其他基金业务规则；");
        tips.add(" 3.贵机构交易资金应当于交易日14:50前划入我公司指定收款账户，否则交易申请将按照受理失败处理或者作为下一交易日交易申请；");
        tips.add(" 4.若贵机构上传银行付款回单等凭证，应当于当交易日14:50前完成系统上传，具体付款时间以银行实际支付时间为准，但如果资金实际到账时间不符合基金业务规则，我公司有权拒绝受理交易申请；");
        tips.add(" 5.若贵机构未上传银行付款回单等凭证，具体资金支付时间以我公司查询到账时间为准，基金交易所属交易日期为实际到账时间所属交易日；");
        tips.add(" 6.我公司已在民生银行开立销售监管账户，用于接受贵机构交易付款，收款账户 ：601480381，户名：上海凯石财富基金销售有限公司，开户行：民生银行上海分行营业部，贵机构可登录中国证券投资基金业协会官方网站核实上述账户信息。");
    }

    public static final String warningTitle="风险不匹配警示函";

    public static final String warningContent="经核实，贵机构申请购买的产品或服务风险等级为%s，贵机构当前的风险承受能力为%s，" +
            "不属于最低风险承受能力的普通投资者，不存在违反准入性要求的情况。根据中国证券监督管理委员会【第130号令】《证券期货投资者适当性管理办法》 的适当性匹配原则，" +
            "该产品或者服务高于贵机构的风险承受能力。凯石财富特此向您警示：购买该产品或服务，可能导致贵机构承担超出自身承受能力的损失以及不利后果。";
    public static final String warningContent2="请贵机构认真考虑相应风险，审慎决定购买该产品或服务。";

    public static final String confirmTitle="风险不匹配确认书";

    public static final String confirmContent1="本机构已认真阅读凯石财富出具的《风险不匹配警示函》，对于本机构申请购买产品或服务风险等级高于本机构风险承受能力的情况已知悉，并且已充分了解该产品或服务的风险特征和可能的不利后果。";
    public static final String confirmContent2="经本机构审慎考虑，仍坚持申请购买该产品或服务，并自愿承担由此可能产生的一切不利后果和损失。";
    public static final String confirmContent3="凯石财富在销售过程中，不存在直接或间接主动向本机构推介该产品或服务的行为。";

}
