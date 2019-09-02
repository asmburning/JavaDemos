package org.lxy.utils.jaxb;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by liuxinyi on 2016/6/12.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"service", "serviceVersion", "partner", "entrustPartner"})
@Getter
@Setter
public class CommonRequest implements Serializable {
    @XmlElement(name = "service")
    private String service;
    @XmlElement(name = "service_version")
    private String serviceVersion;
    @XmlElement(name = "partner")
    private String partner;
    @XmlElement(name = "entrust_partner")
    private String entrustPartner;

}
