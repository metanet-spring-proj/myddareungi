package com.metanet.myddareungi.common.log.repository;

import com.metanet.myddareungi.common.log.model.SystemLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemLogRepository {

    void insertLog(SystemLog systemLog);

    int deleteOldLogs();
}