package com.example.mail_tracker.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRangeRequest {
    private Date startDate;
    private Date endDate;
}
