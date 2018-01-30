package org.lxy.utils.jaxb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Eric.Liu on 2016/11/11.
 */
@ToString
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"userName", "userEmail"})
@XmlRootElement(name = "user")
public class SingleBean implements Serializable {

    @XmlElement(name = "user_name")
    private String userName;

    @XmlElement(name = "user_email")
    private String userEmail;


}
