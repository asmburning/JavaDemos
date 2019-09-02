package org.lxy.utils.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * Created by liuxinyi on 2016/6/12.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"applyNo", "idType", "idNo", "qunarUserId", "loanNo"})
@XmlRootElement(name = "req")
@Data
public class PaymentQueryRequest extends CommonRequest {
    @XmlElement(name = "apply_no")
    private String applyNo; // 是交易流水号 流水号规则:WDIPQ + [ 合 作 方 标 识 ] +YYYYMMDDHHMMSS + 10位固定长度随机数
    @XmlElement(name = "id_type")
    private String idType; // 是 证件类型
    @XmlElement(name = "id_no")
    private String idNo; // 是 证件号码
    @XmlElement(name = "qunar_user_id")
    private String qunarUserId; // 是 合作方系统中的用户唯一标识
    @XmlElement(name = "loan_no")
    private String loanNo; // 是 借据号

}
