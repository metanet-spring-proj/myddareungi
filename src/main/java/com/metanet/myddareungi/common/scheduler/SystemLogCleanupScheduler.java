package com.metanet.myddareungi.common.scheduler;

import com.metanet.myddareungi.common.log.repository.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SystemLogCleanupScheduler {

    private final SystemLogRepository systemLogRepository;

    @Value("${log.cleanup.interval-days:30}")
    private int intervalDays;

    @Value("${log.cleanup.interval-minutes:0}")
    private int intervalMinutes;

    @Scheduled(cron = "${log.cleanup.cron}")
    public void deleteOldLogs() {
        log.debug("[Scheduler] 오래된 시스템 로그 삭제 시작 (days={}, minutes={})", intervalDays, intervalMinutes);
        try {
            int deletedCount = systemLogRepository.deleteOldLogs(intervalDays, intervalMinutes);
            log.debug("[Scheduler] 삭제 완료 - {}건 삭제됨", deletedCount);
        } catch (Exception e) {
            log.error("[Scheduler] 로그 삭제 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}