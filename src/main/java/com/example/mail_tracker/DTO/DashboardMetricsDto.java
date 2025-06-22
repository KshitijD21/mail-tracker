package com.example.mail_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardMetricsDto {

    private int totalEmailsSent;
    private int totalOpens;
    private int totalUniqueRecipients;
    private double openRate;
}
