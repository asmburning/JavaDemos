package org.lxy.utils.pdf;

import lombok.Data;

import java.util.Date;

/**
 * 企业信息表
 */
@Data
public class Organization {
    private String id;// id

    private String code; //企业编号

    private String hsNo; // 恒生系统机构号

    private String name; //企业简称

    private String fullName; //企业全称

    private String entrustType;//委托方式

    private String certType;//证件类型

    private String certNo;//证件号码

    private String accountType;//账户类型

    private String certValidTime;//证件有效期

    private String certInspectValidTime;//证件年检有效期

    private String postCode;//邮政编码

    private String telephone; //联系电话

    private String mobile;// 手机号

    private String postAddress;//通讯地址

    private String faxNo;//传真号码

    private String email;//邮件地址

    private String cityCode;//城市代码

    private String bonusType;//分红方式 红利再投 现金红利

    private String billSendFrequency;//对账单发送频次
    //  1不寄送 2按月寄送 3按季度寄送 4半年寄送 5一年寄送
    private String billSendMethod;//对账单发送方式
    // 1邮寄 2Email
    private String extraEntrustType;// 其他委托方式

    private String legalName; //法人姓名

    private String legalCertType;// 法人证件类型

    private String legalCertNo;//法人证件号码

    private String legalCertValidDate;//法人证件有效期

    private String agentCode;//经纪人代码

    private String agentName;//经纪人名称

    private String agentType;//经纪人类型

    private String subCenter;//分中心

    private String organizationType; //企业性质

    private String industryType;//行业类型

    private String registerFund;//注册资本(万元)

    private String hsOpenDate;

    private String registerAddress;//注册地址

    private String productName; //产品名称

    private int risk; // 风险等级

    private Date riskUpdateDate;

    private Date createTime; //创建时间

    private Date updateTime; //修改时间

    private String hsTradeAcc; // 恒生 tradeAcc

    // since 0.2
    private String investType;//投资者类型

    private String proInvestLevel; // 专业投资者级别

    private Date proValidDate;// 专业投资者有效期 专业投资者中 A,B,C长期有效 D和E可以设置有效期
}
