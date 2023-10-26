package org.denys.hudymov.model;

import lombok.Builder;

@Builder
public record RevenueDto(
        Integer year,
        Integer month,
        String monthlyRevenue,
        String percentGrowth
) {
}
