package com.metanet.myddareungi.domain.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingFileDto {
    private Long fileId;
    private String fileName;
    private String fileType;
    private String uploadDate;
    private String status;
    private String writer;
    private String fileSize;
}
