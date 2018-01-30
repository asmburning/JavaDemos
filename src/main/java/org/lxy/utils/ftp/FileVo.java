package org.lxy.utils.ftp;

import lombok.*;

/**
 * Created by Eric.Liu on 2016/11/10.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileVo {
    private String localFilePath;
    private String remoteFilePath;
    private String localName;
    private String remoteName;
}
