package com.metanet.myddareungi.domain.admin.service;

import com.metanet.myddareungi.domain.admin.dto.AdminDashboardDto;

public interface IAdminService {
    AdminDashboardDto getDashboardData(long userId, int page, int size);

}
