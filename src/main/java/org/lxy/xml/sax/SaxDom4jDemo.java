package org.lxy.xml.sax;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SaxDom4jDemo {

    @Test
    public void test() throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new InputSource(new FileReader("D:/user.xml")));
        Element root = document.getRootElement();//获取根元素
        List<Element> childElements = root.elements();//获取当前元素下的全部子元素
        List<User> users = new ArrayList<>();
        for (Element element : childElements) {//循环输出全部book的相关信息
            String name = element.getName();//获取当前元素名
            String text = element.getText();//获取当前元素值
            log.info("name:{},text:{}", name, text);
            if (StringUtils.equals("user", name)) {
                User user = new User();
                user.setId(element.attribute("id").getValue());
                Element nameEle = element.element("name");//根据元素名获取子元素
                Element passwordEle = element.element("password");
                user.setName(nameEle.getText());
                user.setPassword(passwordEle.getText());
                users.add(user);
            }
        }
        log.info("users:{}", users);
    }
}
