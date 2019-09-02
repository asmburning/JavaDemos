package org.lxy.common;

import lombok.Getter;


@Getter
public enum ApiCode {

  OK("200", "OK");

  String code;
  String desc;

  ApiCode(final String code, final String desc) {
    this.code = code;
    this.desc = desc;
  }

}
