package org.lxy.jca;

import lombok.Data;

/**
 *
 * @Since 2017/12/21 17:00
 * @Auther XinyiLiu
 */
@Data
public class EccBase64KeyPair {

  private String base64Public;
  private String base64Private;
  private String keyFactoryAlgorithm;
}
