package com.metanet.myddareungi.domain.admin.model;

import lombok.Data;

@Data
public class Admin {
    private Long userId;
    private String loginId;
    private String userName;
    private String email;
    private String role;
}
