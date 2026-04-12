package com.metanet.myddareungi.domain.dashboard;

import com.metanet.myddareungi.domain.dashboard.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
class DashboardCachePerformanceTest {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private CacheManager cacheManager;

    private static final int INNER_WIDTH = 42; // 박스 안쪽 너비 고정

    @BeforeEach
    void clearCache() {
        cacheManager.getCacheNames().forEach(name -> {
            var cache = cacheManager.getCache(name);
            if (cache != null) cache.clear();
        });
    }

    @Test
    @DisplayName("[캐시 성능 비교] KPI 요약")
    void compareKpiPerformance() {
        comparePerformance("KPI 요약", () -> dashboardService.getKpi());
    }

    @Test
    @DisplayName("[캐시 성능 비교] 월별 요약")
    void compareMonthlySummaryPerformance() {
        comparePerformance("월별 요약", () -> dashboardService.getMonthlySummary());
    }

    @Test
    @DisplayName("[캐시 성능 비교] 요일별 요약")
    void compareWeekdaySummaryPerformance() {
        comparePerformance("요일별 요약", () -> dashboardService.getWeekDaySummary());
    }

    @Test
    @DisplayName("[캐시 성능 비교] 연령대별 요약")
    void compareAgeGroupSummaryPerformance() {
        comparePerformance("연령대별 요약", () -> dashboardService.getAgeGroupSummary());
    }

    @Test
    @DisplayName("[캐시 성능 비교] 자치구별 요약")
    void compareDistrictSummaryPerformance() {
        comparePerformance("자치구별 요약", () -> dashboardService.getDistrictSummary());
    }

    @Test
    @DisplayName("[캐시 성능 비교] 이용권 유형별 요약")
    void compareRentTypeSummaryPerformance() {
        comparePerformance("이용권 유형별 요약", () -> dashboardService.getRentTypeSummary());
    }

    private void comparePerformance(String label, Runnable callable) {
        long s1 = System.nanoTime();
        callable.run();
        long noCacheNs = System.nanoTime() - s1;

        long s2 = System.nanoTime();
        callable.run();
        long cachedNs = System.nanoTime() - s2;

        double noCacheMs = noCacheNs / 1_000_000.0;
        double cachedMs = cachedNs / 1_000_000.0;
        double diffMs = noCacheMs - cachedMs;
        double ratio = cachedNs > 0 ? (double) noCacheNs / cachedNs : 0;

        System.out.printf(
                "[%s] 1차(DB): %.3f ms | 2차(Cache): %.3f ms | 단축: %.3f ms | 속도향상: %.2f배%n",
                label, noCacheMs, cachedMs, diffMs, ratio
        );
    }
}