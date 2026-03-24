package com.example.bai5.utils;

import java.util.DoubleSummaryStatistics;
import java.util.List;

public final class PerformanceMonitorUtil {

    private PerformanceMonitorUtil() {
    }

    public static DoubleSummaryStatistics summarize(List<Long> responseTimesMs) {
        return responseTimesMs.stream().mapToDouble(Long::doubleValue).summaryStatistics();
    }

    public static void logSummary(String endpointLabel, DoubleSummaryStatistics stats, int totalRuns) {
        System.out.printf("[SLA-MONITOR] %s -> runs=%d, min=%.2fms, avg=%.2fms, max=%.2fms%n",
                endpointLabel,
                totalRuns,
                stats.getMin(),
                stats.getAverage(),
                stats.getMax());
    }
}
